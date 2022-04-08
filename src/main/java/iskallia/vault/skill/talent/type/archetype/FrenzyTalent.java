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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@EventBusSubscriber
public class FrenzyTalent
        extends ArchetypeTalent {
    private static final Map<UUID, PlayerDamageHelper.DamageMultiplier> multiplierMap = new HashMap<>();

    @Expose
    protected float threshold;

    public FrenzyTalent(int cost, float threshold, float damageMultiplier) {
        super(cost);
        this.threshold = threshold;
        this.damageMultiplier = damageMultiplier;
    }

    @Expose
    protected float damageMultiplier;

    public float getThreshold() {
        return this.threshold;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.player;
        TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity) sPlayer);
        float healthPart = sPlayer.getHealth() / sPlayer.getMaxHealth();

        boolean fulfillsFrenzyConditions = false;
        float damageMultiplier = 1.0F;
        for (TalentNode<FrenzyTalent> talentNode : (Iterable<TalentNode<FrenzyTalent>>) talents.getLearnedNodes(FrenzyTalent.class)) {
            if (healthPart <= ((FrenzyTalent) talentNode.getTalent()).getThreshold()) {
                fulfillsFrenzyConditions = true;
                damageMultiplier = ((FrenzyTalent) talentNode.getTalent()).getDamageMultiplier();
                break;
            }
        }
        if (fulfillsFrenzyConditions && isEnabled((World) sPlayer.getLevel())) {
            PlayerDamageHelper.DamageMultiplier existing = multiplierMap.get(sPlayer.getUUID());
            if (existing != null) {
                if (existing.getMultiplier() == damageMultiplier) {
                    existing.refreshDuration(sPlayer.getServer());
                } else {
                    PlayerDamageHelper.removeMultiplier(sPlayer, existing);
                    existing = null;
                }
            }
            if (existing == null) {
                existing = PlayerDamageHelper.applyMultiplier(sPlayer, damageMultiplier, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY);
                multiplierMap.put(sPlayer.getUUID(), existing);
            }
        } else {
            removeExistingDamageBuff(sPlayer);
        }
    }

    private static void removeExistingDamageBuff(ServerPlayerEntity player) {
        PlayerDamageHelper.DamageMultiplier existing = multiplierMap.get(player.getUUID());
        if (existing != null)
            PlayerDamageHelper.removeMultiplier(player, existing);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\archetype\FrenzyTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */