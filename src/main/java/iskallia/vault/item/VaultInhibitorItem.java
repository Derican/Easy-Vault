package iskallia.vault.item;

import iskallia.vault.item.catalyst.ModifierRollResult;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.util.CodecUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class VaultInhibitorItem extends Item {
    private static final Random rand = new Random();

    public VaultInhibitorItem(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(1));
        setRegistryName(id);
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        getModifierRolls(stack).ifPresent(result -> {
            if (!result.isEmpty()) {
                String modifierDesc = String.format("Removes Modifier%s:", new Object[]{(result.size() <= 1) ? "" : "s"});
                tooltip.add(StringTextComponent.EMPTY);
                tooltip.add((new StringTextComponent(modifierDesc)).withStyle(TextFormatting.GOLD));
                for (ModifierRollResult outcome : result) {
                    tooltip.addAll(outcome.getTooltipDescription("- ", true));
                }
            }
        });
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isClientSide()) {
            return;
        }

        List<ModifierRollResult> results = getModifierRolls(stack).orElse(Collections.emptyList());
        if (results.isEmpty()) ;


        getSeed(stack);
    }

    public static long getSeed(ItemStack stack) {
        if (!(stack.getItem() instanceof VaultInhibitorItem)) {
            return 0L;
        }
        CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.contains("Seed", 4)) {
            nbt.putLong("Seed", rand.nextLong());
        }

        return nbt.getLong("Seed");
    }

    @Nullable
    public static List<String> getCrystalCombinationModifiers(ItemStack catalyst, ItemStack crystal) {
        CrystalData data = VaultCrystalItem.getData(crystal.copy());
        if (!data.canModifyWithCrafting()) {
            return null;
        }

        Optional<List<ModifierRollResult>> rollsOpt = getModifierRolls(catalyst);
        if (!rollsOpt.isPresent()) {
            return null;
        }
        List<ModifierRollResult> rolls = rollsOpt.get();

        long seed = VaultCrystalItem.getSeed(crystal) ^ getSeed(catalyst);
        Random rand = new Random(seed);
        for (int i = 0; i < rand.nextInt(32); i++) {
            rand.nextLong();
        }

        List<String> removeModifiers = new ArrayList<>();
        for (ModifierRollResult modifierRoll : rolls) {


            List<String> usedModifiers = (List<String>) data.getModifiers().stream().map(CrystalData.Modifier::getModifierName).collect(Collectors.toList());
            String availableModifier = modifierRoll.getModifier(rand, modifierStr -> (!usedModifiers.contains(modifierStr) && !removeModifiers.contains(modifierStr)));


            if (availableModifier == null) {
                return null;
            }

            if (!data.removeCatalystModifier(availableModifier, true, CrystalData.Modifier.Operation.ADD)) {

                return null;
            }
            removeModifiers.add(availableModifier);
        }
        return removeModifiers;
    }

    public static Optional<List<ModifierRollResult>> getModifierRolls(ItemStack stack) {
        if (!(stack.getItem() instanceof VaultInhibitorItem)) {
            return Optional.empty();
        }

        CompoundNBT tag = stack.getOrCreateTag();
        return CodecUtils.readNBT(ModifierRollResult.CODEC.listOf(), (INBT) tag.getList("modifiers", 10));
    }

    public static void setModifierRolls(ItemStack stack, List<ModifierRollResult> result) {
        if (!(stack.getItem() instanceof VaultInhibitorItem)) {
            return;
        }

        CompoundNBT tag = stack.getOrCreateTag();
        CodecUtils.writeNBT(ModifierRollResult.CODEC.listOf(), result, nbt -> tag.put("modifiers", nbt));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\VaultInhibitorItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */