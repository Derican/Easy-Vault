package iskallia.vault.skill.talent.type.archetype;

import com.google.gson.annotations.Expose;
import iskallia.vault.Vault;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.skill.talent.type.EffectTalent;
import iskallia.vault.util.calc.AbsorptionHelper;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class WardTalent extends ArchetypeTalent {
    private static final Map<UUID, Long> lastAttackedTick;
    @Expose
    protected int startRegenAfterCombatSeconds;
    @Expose
    protected EffectTalent fullAbsorptionEffect;
    @Expose
    protected float additionalParryChance;

    public WardTalent(final int cost, final int startRegenAfterCombatSeconds, final EffectTalent fullAbsorptionEffect, final float additionalParryChance) {
        super(cost);
        this.startRegenAfterCombatSeconds = startRegenAfterCombatSeconds;
        this.fullAbsorptionEffect = fullAbsorptionEffect;
        this.additionalParryChance = additionalParryChance;
    }

    public int getStartRegenAfterCombatSeconds() {
        return this.startRegenAfterCombatSeconds;
    }

    @Nullable
    public EffectTalent getFullAbsorptionEffect() {
        return this.fullAbsorptionEffect;
    }

    public float getAdditionalParryChance() {
        return this.additionalParryChance;
    }

    public static boolean isGrantedFullAbsorptionEffect(final ServerWorld world, final PlayerEntity sPlayer) {
        final TalentTree tree = PlayerTalentsData.get(world).getTalents(sPlayer);
        if (tree.hasLearnedNode(ModConfigs.TALENTS.WARD)) {
            final float max = AbsorptionHelper.getMaxAbsorption(sPlayer);
            return sPlayer.getAbsorptionAmount() / max >= 0.9f;
        }
        return false;
    }

    @SubscribeEvent
    public static void onPlayerDamage(final LivingDamageEvent event) {
        final LivingEntity attacked = event.getEntityLiving();
        if (attacked.getCommandSenderWorld().isClientSide() || !(attacked instanceof ServerPlayerEntity)) {
            return;
        }
        WardTalent.lastAttackedTick.put(attacked.getUUID(), attacked.getServer().overworld().getGameTime());
    }

    @SubscribeEvent
    public static void onChangeDim(final EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity)) {
            return;
        }
        if (!event.getDimension().equals(Vault.VAULT_KEY)) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        player.setAbsorptionAmount(0.0f);
    }

    @SubscribeEvent
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !event.side.isServer() || event.player.tickCount % 20 != 0) {
            return;
        }
        if (!(event.player instanceof ServerPlayerEntity)) {
            return;
        }
        if (!ArchetypeTalent.isEnabled(event.player.level)) {
            return;
        }
        final ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.player;
        final UUID playerUUID = sPlayer.getUUID();
        final float maxAbsorption = AbsorptionHelper.getMaxAbsorption((PlayerEntity) sPlayer);
        if (maxAbsorption <= 0.1f) {
            return;
        }
        final TalentTree tree = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);
        final int startSeconds = tree.getLearnedNodes(WardTalent.class).stream().mapToInt(node -> ((WardTalent) node.getTalent()).getStartRegenAfterCombatSeconds()).min().orElse(-1);
        if (startSeconds < 0) {
            return;
        }
        if (WardTalent.lastAttackedTick.containsKey(playerUUID)) {
            final long lastAttacked = WardTalent.lastAttackedTick.get(playerUUID);
            final long current = sPlayer.getServer().overworld().getGameTime();
            if (lastAttacked >= current - startSeconds * 20) {
                return;
            }
        }
        final float absorption = sPlayer.getAbsorptionAmount();
        if (absorption < maxAbsorption) {
            sPlayer.setAbsorptionAmount(Math.min(absorption + 2.0f, maxAbsorption));
        }
    }

    static {
        lastAttackedTick = new HashMap<UUID, Long>();
    }
}
