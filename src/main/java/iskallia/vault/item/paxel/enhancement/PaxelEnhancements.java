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
    public static Map<ResourceLocation, PaxelEnhancement> REGISTRY;
    public static DurabilityEnhancement FRAGILE;
    public static DurabilityEnhancement STURDY;
    public static PaxelEnhancement DESTRUCTIVE;
    public static PaxelEnhancement HAMMER;
    public static PaxelEnhancement AUTO_SMELT;
    public static PaxelEnhancement FORTUNATE;
    public static PaxelEnhancement RUSH;
    public static PaxelEnhancement RUSH_II;
    public static PaxelEnhancement SPEEDY;
    public static final String ID_TAG = "Id";
    public static final String ENHANCEMENT_TAG = "Enhancement";
    public static final String SHOULD_ENHANCE_TAG = "ShouldEnhance";

    private static <T extends PaxelEnhancement> T register(final String name, final T enhancement) {
        return register(Vault.id(name), enhancement);
    }

    private static <T extends PaxelEnhancement> T register(final ResourceLocation resourceLocation, final T enhancement) {
        enhancement.setResourceLocation(resourceLocation);
        PaxelEnhancements.REGISTRY.put(resourceLocation, enhancement);
        return enhancement;
    }

    public static void enhance(final ItemStack itemStack, final PaxelEnhancement enhancement) {
        final CompoundNBT nbt = itemStack.getOrCreateTag();
        nbt.put("Enhancement", enhancement.serializeNBT());
        nbt.putBoolean("ShouldEnhance", false);
    }

    @Nullable
    public static PaxelEnhancement getEnhancement(final ItemStack itemStack) {
        if (itemStack.getItem() != ModItems.VAULT_PAXEL) {
            return null;
        }
        final CompoundNBT nbt = itemStack.getOrCreateTag();
        if (!nbt.contains("Enhancement", 10)) {
            return null;
        }
        final CompoundNBT enhancementNBT = nbt.getCompound("Enhancement");
        final String sId = enhancementNBT.getString("Id");
        if (sId.isEmpty()) {
            return null;
        }
        return PaxelEnhancements.REGISTRY.get(new ResourceLocation(sId));
    }

    public static void markShouldEnhance(final ItemStack itemStack) {
        final CompoundNBT nbt = itemStack.getOrCreateTag();
        nbt.putBoolean("ShouldEnhance", true);
    }

    public static boolean shouldEnhance(final ItemStack itemStack) {
        final CompoundNBT nbt = itemStack.getOrCreateTag();
        return nbt.getBoolean("ShouldEnhance") && !nbt.contains("Enhancement", 10);
    }

    static {
        PaxelEnhancements.REGISTRY = new HashMap<ResourceLocation, PaxelEnhancement>();
        PaxelEnhancements.FRAGILE = register("fragile", new DurabilityEnhancement(-3000));
        PaxelEnhancements.STURDY = register("sturdy", new DurabilityEnhancement(2000));
        PaxelEnhancements.DESTRUCTIVE = register("destructive", new DestructiveEnhancement());
        PaxelEnhancements.HAMMER = register("hammer", new HammerEnhancement());
        PaxelEnhancements.AUTO_SMELT = register("auto_smelt", new AutoSmeltEnhancement());
        PaxelEnhancements.FORTUNATE = register("fortunate", new FortuneEnhancement(1));
        PaxelEnhancements.RUSH = register("rush", new EffectEnhancement(Effects.DIG_SPEED, 1));
        PaxelEnhancements.RUSH_II = register("rush_2", new EffectEnhancement(Effects.DIG_SPEED, 2));
        PaxelEnhancements.SPEEDY = register("speedy", new EffectEnhancement(Effects.MOVEMENT_SPEED, 1));
    }
}
