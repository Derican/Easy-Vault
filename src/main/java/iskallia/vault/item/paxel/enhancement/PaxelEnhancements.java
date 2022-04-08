package iskallia.vault.item.paxel.enhancement;

import iskallia.vault.Vault;
import iskallia.vault.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;


public class PaxelEnhancements {
    public static Map<ResourceLocation, PaxelEnhancement> REGISTRY = new HashMap<>();


    public static DurabilityEnhancement FRAGILE = register("fragile", new DurabilityEnhancement(-3000));


    public static DurabilityEnhancement STURDY = register("sturdy", new DurabilityEnhancement(2000));


    public static PaxelEnhancement DESTRUCTIVE = register("destructive", new DestructiveEnhancement());


    public static PaxelEnhancement HAMMER = register("hammer", new HammerEnhancement());


    public static PaxelEnhancement AUTO_SMELT = register("auto_smelt", new AutoSmeltEnhancement());


    public static PaxelEnhancement FORTUNATE = register("fortunate", new FortuneEnhancement(1));


    public static PaxelEnhancement RUSH = register("rush", new EffectEnhancement(Effects.DIG_SPEED, 1));


    public static PaxelEnhancement RUSH_II = register("rush_2", new EffectEnhancement(Effects.DIG_SPEED, 2));


    public static PaxelEnhancement SPEEDY = register("speedy", new EffectEnhancement(Effects.MOVEMENT_SPEED, 1));

    public static final String ID_TAG = "Id";

    private static <T extends PaxelEnhancement> T register(String name, T enhancement) {
        return register(Vault.id(name), enhancement);
    }

    public static final String ENHANCEMENT_TAG = "Enhancement";
    public static final String SHOULD_ENHANCE_TAG = "ShouldEnhance";

    private static <T extends PaxelEnhancement> T register(ResourceLocation resourceLocation, T enhancement) {
        enhancement.setResourceLocation(resourceLocation);
        REGISTRY.put(resourceLocation, (PaxelEnhancement) enhancement);
        return enhancement;
    }


    public static void enhance(ItemStack itemStack, PaxelEnhancement enhancement) {
        CompoundNBT nbt = itemStack.getOrCreateTag();
        nbt.put("Enhancement", (INBT) enhancement.serializeNBT());
        nbt.putBoolean("ShouldEnhance", false);
    }

    @Nullable
    public static PaxelEnhancement getEnhancement(ItemStack itemStack) {
        if (itemStack.getItem() != ModItems.VAULT_PAXEL) {
            return null;
        }
        CompoundNBT nbt = itemStack.getOrCreateTag();

        if (!nbt.contains("Enhancement", 10)) {
            return null;
        }
        CompoundNBT enhancementNBT = nbt.getCompound("Enhancement");
        String sId = enhancementNBT.getString("Id");

        if (sId.isEmpty()) {
            return null;
        }
        return REGISTRY.get(new ResourceLocation(sId));
    }

    public static void markShouldEnhance(ItemStack itemStack) {
        CompoundNBT nbt = itemStack.getOrCreateTag();
        nbt.putBoolean("ShouldEnhance", true);
    }

    public static boolean shouldEnhance(ItemStack itemStack) {
        CompoundNBT nbt = itemStack.getOrCreateTag();
        return (nbt.getBoolean("ShouldEnhance") &&
                !nbt.contains("Enhancement", 10));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\paxel\enhancement\PaxelEnhancements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */