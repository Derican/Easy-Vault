// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type.archetype;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.PlayerDamageHelper;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class FrenzyTalent extends ArchetypeTalent
{
    private static final Map<UUID, PlayerDamageHelper.DamageMultiplier> multiplierMap;
    @Expose
    protected float threshold;
    @Expose
    protected float damageMultiplier;
    
    public FrenzyTalent(final int cost, final float threshold, final float damageMultiplier) {
        super(cost);
        this.threshold = threshold;
        this.damageMultiplier = damageMultiplier;
    }
    
    public float getThreshold() {
        return this.threshold;
    }
    
    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }
    
    @SubscribeEvent
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity sPlayer = (ServerPlayerEntity)event.player;
        final TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity)sPlayer);
        final float healthPart = sPlayer.getHealth() / sPlayer.getMaxHealth();
        boolean fulfillsFrenzyConditions = false;
        float damageMultiplier = 1.0f;
        for (final TalentNode<?> talentNode : talents.getLearnedNodes(FrenzyTalent.class)) {
            if (healthPart <= ((FrenzyTalent)talentNode.getTalent()).getThreshold()) {
                fulfillsFrenzyConditions = true;
                damageMultiplier = ((FrenzyTalent)talentNode.getTalent()).getDamageMultiplier();
                break;
            }
        }
        if (fulfillsFrenzyConditions && ArchetypeTalent.isEnabled((World)sPlayer.getLevel())) {
            PlayerDamageHelper.DamageMultiplier existing = FrenzyTalent.multiplierMap.get(sPlayer.getUUID());
            if (existing != null) {
                if (existing.getMultiplier() == damageMultiplier) {
                    existing.refreshDuration(sPlayer.getServer());
                }
                else {
                    PlayerDamageHelper.removeMultiplier(sPlayer, existing);
                    existing = null;
                }
            }
            if (existing == null) {
                existing = PlayerDamageHelper.applyMultiplier(sPlayer, damageMultiplier, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY);
                FrenzyTalent.multiplierMap.put(sPlayer.getUUID(), existing);
            }
        }
        else {
            removeExistingDamageBuff(sPlayer);
        }
    }
    
    private static void removeExistingDamageBuff(final ServerPlayerEntity player) {
        final PlayerDamageHelper.DamageMultiplier existing = FrenzyTalent.multiplierMap.get(player.getUUID());
        if (existing != null) {
            PlayerDamageHelper.removeMultiplier(player, existing);
        }
    }
    
    static {
        multiplierMap = new HashMap<UUID, PlayerDamageHelper.DamageMultiplier>();
    }
}
