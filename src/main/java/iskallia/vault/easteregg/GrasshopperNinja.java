package iskallia.vault.easteregg;

import iskallia.vault.Vault;
import iskallia.vault.attribute.IntegerAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModModels;
import iskallia.vault.item.gear.VaultArmorItem;
import iskallia.vault.skill.set.PlayerSet;
import iskallia.vault.util.AdvancementHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class GrasshopperNinja {
    public static void achieve(ServerPlayerEntity playerEntity) {
        AdvancementHelper.grantCriterion(playerEntity, Vault.id("main/grasshopper_ninja"), "hopped");
    }

    public static boolean isGrasshopperShape(PlayerEntity playerEntity) {
        return PlayerSet.allMatch((LivingEntity) playerEntity, (slotType, stack) -> {
            if (!(stack.getItem() instanceof VaultArmorItem))
                return false;
            Integer gearSpecialModel = (Integer) ((IntegerAttribute) ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(stack, Integer.valueOf(-1))).getValue(stack);
            int gearColor = ((VaultArmorItem) stack.getItem()).getColor(stack);
            return (gearSpecialModel.intValue() == ModModels.SpecialGearModel.FAIRY_SET.modelForSlot(slotType).getId() && isGrasshopperGreen(gearColor));
        });
    }


    public static boolean isGrasshopperGreen(int color) {
        float grasshopperGreenR = 0.58431375F;
        float grasshopperGreenG = 0.7607843F;
        float grasshopperGreenB = 0.40784314F;

        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        float dr = red - grasshopperGreenR;
        float dg = green - grasshopperGreenG;
        float db = blue - grasshopperGreenB;

        float distance = (float) (Math.sqrt((dr * dr + dg * dg + db * db)) / 1.73205080757D);
        return (distance < 0.35D);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\easteregg\GrasshopperNinja.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */