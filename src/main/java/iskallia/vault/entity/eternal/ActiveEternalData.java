package iskallia.vault.entity.eternal;

import com.mojang.datafixers.util.Either;
import iskallia.vault.config.EternalAuraConfig;
import iskallia.vault.entity.EternalEntity;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.ActiveEternalMessage;
import iskallia.vault.util.SkinProfile;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nullable;
import java.util.*;

@Mod.EventBusSubscriber
public class ActiveEternalData {
    private static final Integer ETERNAL_TIMEOUT = Integer.valueOf(160);
    private static final ActiveEternalData INSTANCE = new ActiveEternalData();
    private final Map<UUID, Set<ActiveEternal>> eternals = new HashMap<>();


    public static ActiveEternalData getInstance() {
        return INSTANCE;
    }

    public void updateEternal(EternalEntity eternal) {
        Either<UUID, ServerPlayerEntity> owner = eternal.getOwner();
        if (owner.left().isPresent()) {
            return;
        }
        ServerPlayerEntity sPlayer = owner.right().get();
        if (!sPlayer.getCommandSenderWorld().dimension().equals(eternal.getCommandSenderWorld().dimension())) {
            return;
        }

        UUID ownerId = sPlayer.getUUID();
        boolean update = false;

        ActiveEternal active = getActive(ownerId, eternal);
        if (active == null) {
            active = ActiveEternal.create(eternal);
            ((Set<ActiveEternal>) this.eternals.computeIfAbsent(ownerId, id -> new LinkedHashSet())).add(active);
            update = true;
        }
        active.timeout = ETERNAL_TIMEOUT.intValue();

        float current = active.health;
        float healthToSet = eternal.getHealth();
        if (healthToSet <= 0.0F || Math.abs(current - healthToSet) >= 0.3F) {
            active.health = healthToSet;
            update = true;
        }
        if (!Objects.equals(active.abilityName, eternal.getProvidedAura())) {
            active.abilityName = eternal.getProvidedAura();
            update = true;
        }
        if (!Objects.equals(active.eternalName, eternal.getSkinName())) {
            active.eternalName = eternal.getSkinName();
            update = true;
        }
        if (update) {
            syncActives(ownerId, this.eternals.getOrDefault(ownerId, Collections.emptySet()));
        }
    }

    @Nullable
    private ActiveEternal getActive(UUID ownerId, EternalEntity eternal) {
        UUID eternalId = eternal.getEternalId();

        Set<ActiveEternal> activeEternals = this.eternals.computeIfAbsent(ownerId, id -> new LinkedHashSet());
        for (ActiveEternal activeEternal : activeEternals) {
            if (activeEternal.eternalId.equals(eternalId)) {
                return activeEternal;
            }
        }
        return null;
    }

    public boolean isEternalActive(UUID eternalId) {
        for (Set<ActiveEternal> activeEternals : this.eternals.values()) {
            for (ActiveEternal activeEternal : activeEternals) {
                if (activeEternal.eternalId.equals(eternalId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        INSTANCE.eternals.forEach((playerId, activeEternals) -> {
            boolean removedAny = activeEternals.removeIf((ActiveEternalData.ActiveEternal eternal)->{
                return false;
            });
            if (removedAny) {
                INSTANCE.syncActives(playerId, activeEternals);
            }
        });
    }


    @SubscribeEvent
    public static void onChangeDim(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity)) {
            return;
        }
        UUID playerId = event.getEntity().getUUID();
        if (INSTANCE.eternals.containsKey(playerId)) {
            Set<ActiveEternal> eternals = INSTANCE.eternals.remove(playerId);
            if (eternals != null && !eternals.isEmpty()) {
                INSTANCE.syncActives((ServerPlayerEntity) event.getEntity(), Collections.emptySet());
            }
        }
    }

    private void syncActives(UUID playerId, Set<ActiveEternal> eternals) {
        MinecraftServer srv = (MinecraftServer) LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        if (srv == null) {
            return;
        }
        ServerPlayerEntity sPlayer = srv.getPlayerList().getPlayer(playerId);
        if (sPlayer == null) {
            return;
        }
        syncActives(sPlayer, eternals);
    }

    private void syncActives(ServerPlayerEntity sPlayer, Set<ActiveEternal> eternals) {
        ModNetwork.CHANNEL.sendTo(new ActiveEternalMessage(eternals), sPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }


    public static class ActiveEternal {
        private final UUID eternalId;
        private final boolean ancient;
        private String eternalName;
        private String abilityName;
        private float health;
        private int timeout = ActiveEternalData.ETERNAL_TIMEOUT.intValue();
        private SkinProfile skinUtil = null;

        private ActiveEternal(UUID eternalId, String eternalName, String abilityName, boolean ancient, float health) {
            this.eternalId = eternalId;
            this.eternalName = eternalName;
            this.abilityName = abilityName;
            this.ancient = ancient;
            this.health = health;
        }

        public static ActiveEternal create(EternalEntity eternal) {
            return new ActiveEternal(eternal.getEternalId(), eternal.getSkinName(), eternal.getProvidedAura(), eternal.isAncient(), eternal.getHealth());
        }

        public static ActiveEternal read(PacketBuffer buffer) {
            return new ActiveEternal(buffer.readUUID(), buffer.readUtf(32767),
                    buffer.readBoolean() ? buffer.readUtf(32767) : null, buffer
                    .readBoolean(), buffer.readFloat());
        }

        public void write(PacketBuffer buffer) {
            buffer.writeUUID(this.eternalId);
            buffer.writeUtf(this.eternalName, 32767);
            buffer.writeBoolean((this.abilityName != null));
            if (this.abilityName != null) {
                buffer.writeUtf(this.abilityName, 32767);
            }
            buffer.writeBoolean(this.ancient);
            buffer.writeFloat(this.health);
        }

        public String getAbilityName() {
            return this.abilityName;
        }

        public Optional<EternalAuraConfig.AuraConfig> getAbilityConfig() {
            if (getAbilityName() == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(ModConfigs.ETERNAL_AURAS.getByName(getAbilityName()));
        }

        public boolean isAncient() {
            return this.ancient;
        }

        public float getHealth() {
            return this.health;
        }

        @OnlyIn(Dist.CLIENT)
        public void updateFrom(ActiveEternal activeEternal) {
            this.health = activeEternal.health;
            this.abilityName = activeEternal.abilityName;
            if (!this.eternalName.equals(activeEternal.eternalName)) {
                this.eternalName = activeEternal.eternalName;
                if (this.skinUtil != null) {
                    this.skinUtil.updateSkin(this.eternalName);
                }
            }
        }

        @OnlyIn(Dist.CLIENT)
        public SkinProfile getSkin() {
            if (this.skinUtil == null) {
                this.skinUtil = new SkinProfile();
                this.skinUtil.updateSkin(this.eternalName);
            }
            return this.skinUtil;
        }


        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ActiveEternal that = (ActiveEternal) o;
            return Objects.equals(this.eternalId, that.eternalId);
        }


        public int hashCode() {
            return Objects.hash(new Object[]{this.eternalId});
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\eternal\ActiveEternalData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */