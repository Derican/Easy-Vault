package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BreakableTalent extends PlayerTalent {
    @Expose
    private final float damagePreventionChance;

    public BreakableTalent(int cost, float damagePreventionChance, float damageAsDurabilityMultiplier) {
        super(cost);
        this.damagePreventionChance = damagePreventionChance;
        this.damageAsDurabilityMultiplier = damageAsDurabilityMultiplier;
    }

    @Expose
    private final float damageAsDurabilityMultiplier;

    @SubscribeEvent
    public static void onPlayerDamage(LivingHurtEvent event) {
        if ((event.getEntityLiving()).level.isClientSide)
            return;
        if (!(event.getEntityLiving() instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntityLiving();

        int armorPieces = 0;
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            if (slotType.getType() != EquipmentSlotType.Group.HAND) {


                ItemStack stack = player.getItemBySlot(slotType);
                if (!stack.isEmpty() && stack.isDamageableItem())
                    armorPieces++;
            }
        }
        if (armorPieces <= 0) {
            return;
        }

        float durabilityDamageMultiplier = 1.0F;
        float preventionChance = 0.0F;
        TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity) player);
        for (BreakableTalent talent : talents.getTalents(BreakableTalent.class)) {
            preventionChance += talent.damagePreventionChance;
            durabilityDamageMultiplier += talent.damageAsDurabilityMultiplier;
        }
        if (preventionChance <= 0.0F || rand.nextFloat() >= preventionChance) {
            return;
        }


        float dmgAmount = event.getAmount();
        float postArmorAmount = dmgAmount / 4.0F * (4 - armorPieces);

        float armorDmgHit = dmgAmount / 4.0F * durabilityDamageMultiplier;

        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            if (slotType.getType() != EquipmentSlotType.Group.HAND) {


                ItemStack stack = player.getItemBySlot(slotType);
                if (!stack.isEmpty() && stack.isDamageableItem()) {
                    stack.hurtAndBreak(MathHelper.ceil(armorDmgHit), (LivingEntity) player, brokenStack -> player.broadcastBreakEvent(slotType));
                }
            }
        }
        player.getCommandSenderWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_GOLEM_DAMAGE, SoundCategory.MASTER, 0.5F, 1.0F);


        event.setAmount(postArmorAmount);
        if (armorPieces >= 4)
            event.setAmount(0.0F);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\BreakableTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */