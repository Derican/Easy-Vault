// 
// Decompiled by Procyon v0.6.0
// 

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
public class ActiveEternalData
{
    private static final Integer ETERNAL_TIMEOUT;
    private static final ActiveEternalData INSTANCE;
    private final Map<UUID, Set<ActiveEternal>> eternals;
    
    private ActiveEternalData() {
        this.eternals = new HashMap<UUID, Set<ActiveEternal>>();
    }
    
    public static ActiveEternalData getInstance() {
        return ActiveEternalData.INSTANCE;
    }
    
    public void updateEternal(final EternalEntity eternal) {
        final Either<UUID, ServerPlayerEntity> owner = eternal.getOwner();
        if (owner.left().isPresent()) {
            return;
        }
        final ServerPlayerEntity sPlayer = owner.right().get();
        if (!sPlayer.getCommandSenderWorld().dimension().equals(eternal.getCommandSenderWorld().dimension())) {
            return;
        }
        final UUID ownerId = sPlayer.getUUID();
        boolean update = false;
        ActiveEternal active = this.getActive(ownerId, eternal);
        if (active == null) {
            active = ActiveEternal.create(eternal);
            this.eternals.computeIfAbsent(ownerId, id -> new LinkedHashSet()).add(active);
            update = true;
        }
        active.timeout = ActiveEternalData.ETERNAL_TIMEOUT;
        final float current = active.health;
        final float healthToSet = eternal.getHealth();
        if (healthToSet <= 0.0f || Math.abs(current - healthToSet) >= 0.3f) {
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
            this.syncActives(ownerId, this.eternals.getOrDefault(ownerId, Collections.emptySet()));
        }
    }
    
    @Nullable
    private ActiveEternal getActive(final UUID ownerId, final EternalEntity eternal) {
        final UUID eternalId = eternal.getEternalId();
        final Set<ActiveEternal> activeEternals = this.eternals.computeIfAbsent(ownerId, id -> new LinkedHashSet());
        for (final ActiveEternal activeEternal : activeEternals) {
            if (activeEternal.eternalId.equals(eternalId)) {
                return activeEternal;
            }
        }
        return null;
    }
    
    public boolean isEternalActive(final UUID eternalId) {
        for (final Set<ActiveEternal> activeEternals : this.eternals.values()) {
            for (final ActiveEternal activeEternal : activeEternals) {
                if (activeEternal.eternalId.equals(eternalId)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @SubscribeEvent
    public static void onTick(final TickEvent.ServerTickEvent event) {
        ActiveEternalData.INSTANCE.eternals.forEach((playerId, activeEternals) -> {
            final boolean removedAny = activeEternals.removeIf(activeEternal -> {
                activeEternal.timeout--;
                return activeEternal.timeout <= 0;
            });
            if (removedAny) {
                ActiveEternalData.INSTANCE.syncActives(playerId, activeEternals);
            }
        });
    }
    
    @SubscribeEvent
    public static void onChangeDim(final EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity)) {
            return;
        }
        final UUID playerId = event.getEntity().getUUID();
        if (ActiveEternalData.INSTANCE.eternals.containsKey(playerId)) {
            final Set<ActiveEternal> eternals = ActiveEternalData.INSTANCE.eternals.remove(playerId);
            if (eternals != null && !eternals.isEmpty()) {
                ActiveEternalData.INSTANCE.syncActives((ServerPlayerEntity)event.getEntity(), Collections.emptySet());
            }
        }
    }
    
    private void syncActives(final UUID playerId, final Set<ActiveEternal> eternals) {
        final MinecraftServer srv = (MinecraftServer)LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        if (srv == null) {
            return;
        }
        final ServerPlayerEntity sPlayer = srv.getPlayerList().getPlayer(playerId);
        if (sPlayer == null) {
            return;
        }
        this.syncActives(sPlayer, eternals);
    }
    
    private void syncActives(final ServerPlayerEntity sPlayer, final Set<ActiveEternal> eternals) {
        ModNetwork.CHANNEL.sendTo(new ActiveEternalMessage(eternals), sPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    
    static {
        ETERNAL_TIMEOUT = 160;
        INSTANCE = new ActiveEternalData();
    }
    
    public static class ActiveEternal
    {
        private final UUID eternalId;
        private final boolean ancient;
        private String eternalName;
        private String abilityName;
        private float health;
        private int timeout;
        private SkinProfile skinUtil;
        
        private ActiveEternal(final UUID eternalId, final String eternalName, final String abilityName, final boolean ancient, final float health) {
            this.timeout = ActiveEternalData.ETERNAL_TIMEOUT;
            this.skinUtil = null;
            this.eternalId = eternalId;
            this.eternalName = eternalName;
            this.abilityName = abilityName;
            this.ancient = ancient;
            this.health = health;
        }
        
        public static ActiveEternal create(final EternalEntity eternal) {
            return new ActiveEternal(eternal.getEternalId(), eternal.getSkinName(), eternal.getProvidedAura(), eternal.isAncient(), eternal.getHealth());
        }
        
        public static ActiveEternal read(final PacketBuffer buffer) {
            return new ActiveEternal(buffer.readUUID(), buffer.readUtf(32767), buffer.readBoolean() ? buffer.readUtf(32767) : null, buffer.readBoolean(), buffer.readFloat());
        }
        
        public void write(final PacketBuffer buffer) {
            buffer.writeUUID(this.eternalId);
            buffer.writeUtf(this.eternalName, 32767);
            buffer.writeBoolean(this.abilityName != null);
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
            if (this.getAbilityName() == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(ModConfigs.ETERNAL_AURAS.getByName(this.getAbilityName()));
        }
        
        public boolean isAncient() {
            return this.ancient;
        }
        
        public float getHealth() {
            return this.health;
        }
        
        @OnlyIn(Dist.CLIENT)
        public void updateFrom(final ActiveEternal activeEternal) {
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
                (this.skinUtil = new SkinProfile()).updateSkin(this.eternalName);
            }
            return this.skinUtil;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final ActiveEternal that = (ActiveEternal)o;
            return Objects.equals(this.eternalId, that.eternalId);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.eternalId);
        }
    }
}
