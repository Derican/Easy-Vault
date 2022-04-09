// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util.calc;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.set.CarapaceSet;
import iskallia.vault.skill.set.PlayerSet;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.type.AbsorptionTalent;
import iskallia.vault.skill.talent.type.archetype.BarbaricTalent;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.data.PlayerSetsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

@Mod.EventBusSubscriber
public class AbsorptionHelper
{
    public static float getMaxAbsorption(final PlayerEntity player) {
        if (MiscUtils.getTalent(player, ModConfigs.TALENTS.BARBARIC).map(TalentNode::isLearned).orElse(false)) {
            return 0.0f;
        }
        float limit = 12.0f;
        float maxHealthPerc = 0.0f;
        maxHealthPerc += MiscUtils.getTalent(player, ModConfigs.TALENTS.BARRIER).map(TalentNode::getTalent).map(AbsorptionTalent::getIncreasedAbsorptionLimit).orElse(0.0f);
        if (PlayerSet.isActive(VaultGear.Set.CARAPACE, (LivingEntity)player)) {
            final SetTree sets = PlayerSetsData.get((ServerWorld)player.level).getSets(player);
            for (final SetNode<?> node : sets.getNodes()) {
                if (!(node.getSet() instanceof CarapaceSet)) {
                    continue;
                }
                final CarapaceSet set = (CarapaceSet)node.getSet();
                maxHealthPerc += set.getAbsorptionPercent();
            }
        }
        limit += maxHealthPerc * player.getMaxHealth();
        return limit;
    }
    
    @SubscribeEvent
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !event.side.isServer() || event.player.tickCount % 10 != 0) {
            return;
        }
        final PlayerEntity player = event.player;
        final float absorption = player.getAbsorptionAmount();
        if (absorption > 0.0f && absorption > getMaxAbsorption(player)) {
            player.setAbsorptionAmount(getMaxAbsorption(player));
        }
    }
}
