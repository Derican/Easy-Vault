package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerFavourData;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ExperiencedTalent extends PlayerTalent {
    @Expose
    private final float increasedExpPercentage;

    public ExperiencedTalent(int cost, float increasedExpPercentage) {
        super(cost);
        this.increasedExpPercentage = increasedExpPercentage;
    }

    public float getIncreasedExpPercentage() {
        return this.increasedExpPercentage;
    }

    @SubscribeEvent
    public static void onOrbPickup(PlayerXpEvent.PickupXp event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        ExperienceOrbEntity orb = event.getOrb();
        float increase = 0.0F;

        for (ExperiencedTalent talent : talents.getTalents(ExperiencedTalent.class)) {
            increase += talent.getIncreasedExpPercentage();
        }
        int favour = PlayerFavourData.get(player.getLevel()).getFavour(player.getUUID(), PlayerFavourData.VaultGodType.OMNISCIENT);
        if (favour >= 4) {
            increase += favour * 0.2F;
        } else if (favour <= -4) {
            increase -= Math.min(Math.abs(favour), 8) * 0.0625F;
        }

        orb.value = (int) (orb.value * (1.0F + increase));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\ExperiencedTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */