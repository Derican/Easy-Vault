package iskallia.vault.easteregg;

import iskallia.vault.Vault;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModModels;
import iskallia.vault.item.gear.VaultArmorItem;
import iskallia.vault.skill.set.PlayerSet;
import iskallia.vault.util.AdvancementHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class GrasshopperNinja {
    public static void achieve(final ServerPlayerEntity playerEntity) {
        AdvancementHelper.grantCriterion(playerEntity, Vault.id("main/grasshopper_ninja"), "hopped");
    }

    public static boolean isGrasshopperShape(final PlayerEntity playerEntity) {
        return PlayerSet.allMatch((LivingEntity) playerEntity, (slotType, stack) -> {
            if (!(stack.getItem() instanceof VaultArmorItem)) {
                return false;
            } else {
                final Integer gearSpecialModel = ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(stack, -1).getValue(stack);
                final int gearColor = ((VaultArmorItem) stack.getItem()).getColor(stack);
                return gearSpecialModel == ModModels.SpecialGearModel.FAIRY_SET.modelForSlot(slotType).getId() && isGrasshopperGreen(gearColor);
            }
        });
    }

    public static boolean isGrasshopperGreen(final int color) {
        final float grasshopperGreenR = 0.58431375f;
        final float grasshopperGreenG = 0.7607843f;
        final float grasshopperGreenB = 0.40784314f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final float dr = red - grasshopperGreenR;
        final float dg = green - grasshopperGreenG;
        final float db = blue - grasshopperGreenB;
        final float distance = (float) (Math.sqrt(dr * dr + dg * dg + db * db) / 1.73205080757);
        return distance < 0.35;
    }
}
