package iskallia.vault.util.calc;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.set.CarapaceSet;
import iskallia.vault.skill.set.PlayerSet;
import iskallia.vault.skill.set.SetNode;
import iskallia.vault.skill.set.SetTree;
import iskallia.vault.skill.talent.TalentGroup;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.type.AbsorptionTalent;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.data.PlayerSetsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class AbsorptionHelper {
    public static float getMaxAbsorption(PlayerEntity player) {
        if (((Boolean) MiscUtils.getTalent(player, (TalentGroup) ModConfigs.TALENTS.BARBARIC).map(TalentNode::isLearned).orElse(Boolean.valueOf(false))).booleanValue()) {
            return 0.0F;
        }

        float limit = 12.0F;
        float maxHealthPerc = 0.0F;

        maxHealthPerc += ((Float) MiscUtils.getTalent(player, ModConfigs.TALENTS.BARRIER)
                .map(TalentNode::getTalent)
                .map(AbsorptionTalent::getIncreasedAbsorptionLimit)
                .orElse(Float.valueOf(0.0F))).floatValue();

        if (PlayerSet.isActive(VaultGear.Set.CARAPACE, (LivingEntity) player)) {
            SetTree sets = PlayerSetsData.get((ServerWorld) player.level).getSets(player);

            for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
                if (!(node.getSet() instanceof CarapaceSet))
                    continue;
                CarapaceSet set = (CarapaceSet) node.getSet();
                maxHealthPerc += set.getAbsorptionPercent();
            }
        }

        limit += maxHealthPerc * player.getMaxHealth();
        return limit;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START ||
                !event.side.isServer() || event.player.tickCount % 10 != 0) {
            return;
        }


        PlayerEntity player = event.player;
        float absorption = player.getAbsorptionAmount();

        if (absorption > 0.0F && absorption > getMaxAbsorption(player))
            player.setAbsorptionAmount(getMaxAbsorption(player));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\calc\AbsorptionHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */