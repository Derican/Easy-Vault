package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CriticalStrikeTalent
        extends PlayerTalent {
    public CriticalStrikeTalent(int cost, float chance) {
        super(cost);
        this.chance = chance;
    }

    @Expose
    private final float chance;

    public float getChance() {
        return this.chance;
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        if ((event.getEntity()).level.isClientSide())
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);

        for (CriticalStrikeTalent criticalStrikeTalent : talents.getTalents(CriticalStrikeTalent.class)) {
            if (player.level.random.nextFloat() < criticalStrikeTalent.getChance()) {
                if (event.getDamageModifier() < 1.5F) {
                    event.setDamageModifier(1.5F);
                }
                event.setResult(Event.Result.ALLOW);
                return;
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\CriticalStrikeTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */