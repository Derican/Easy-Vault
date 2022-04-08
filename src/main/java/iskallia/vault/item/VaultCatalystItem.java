package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.catalyst.CompoundModifierOutcome;
import iskallia.vault.item.catalyst.ModifierRollResult;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.util.CodecUtils;
import iskallia.vault.util.MiscUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class VaultCatalystItem extends Item {
    private static final Random rand = new Random();

    public VaultCatalystItem(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(1));
        setRegistryName(id);
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        getModifierRolls(stack).ifPresent(result -> {
            if (!result.isEmpty()) {
                String modifierDesc = String.format("Adds Modifier%s:", new Object[]{(result.size() <= 1) ? "" : "s"});
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
        if (world instanceof net.minecraft.world.server.ServerWorld) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);

                    ItemStack catalyst = stack.copy();
                    catalyst.setCount(1);
                    MiscUtils.giveItem(player, catalyst);
                }
            }
        }

        List<ModifierRollResult> results = getModifierRolls(stack).orElse(Collections.emptyList());
        if (results.isEmpty()) {
            setModifierRolls(stack, createRandomModifiers());
        }
        getSeed(stack);
    }

    public static ItemStack createRandom() {
        ItemStack stack = new ItemStack((IItemProvider) ModItems.VAULT_CATALYST);
        setModifierRolls(stack, createRandomModifiers());
        return stack;
    }

    private static List<ModifierRollResult> createRandomModifiers() {
        CompoundModifierOutcome randomOutcome = ModConfigs.VAULT_CRYSTAL_CATALYST.getModifiers();
        if (randomOutcome == null) {
            return Collections.emptyList();
        }
        return (List<ModifierRollResult>) randomOutcome.getRolls()
                .stream()
                .map(outcome -> outcome.resolve(rand))
                .collect(Collectors.toList());
    }

    public static long getSeed(ItemStack stack) {
        if (!(stack.getItem() instanceof VaultCatalystItem)) {
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

        List<String> newModifiers = new ArrayList<>();
        for (ModifierRollResult modifierRoll : rolls) {


            List<String> usedModifiers = (List<String>) data.getModifiers().stream().map(CrystalData.Modifier::getModifierName).collect(Collectors.toList());
            String availableModifier = modifierRoll.getModifier(rand, modifierStr ->
                    (usedModifiers.contains(modifierStr) || newModifiers.contains(modifierStr)));


            if (availableModifier == null) {
                return null;
            }

            if (!data.addCatalystModifier(availableModifier, true, CrystalData.Modifier.Operation.ADD)) {

                return null;
            }
            newModifiers.add(availableModifier);
        }
        return newModifiers;
    }

    public static Optional<List<ModifierRollResult>> getModifierRolls(ItemStack stack) {
        if (!(stack.getItem() instanceof VaultCatalystItem)) {
            return Optional.empty();
        }

        CompoundNBT tag = stack.getOrCreateTag();
        return CodecUtils.readNBT(ModifierRollResult.CODEC.listOf(), (INBT) tag.getList("modifiers", 10));
    }

    public static void setModifierRolls(ItemStack stack, List<ModifierRollResult> result) {
        if (!(stack.getItem() instanceof VaultCatalystItem)) {
            return;
        }

        CompoundNBT tag = stack.getOrCreateTag();
        CodecUtils.writeNBT(ModifierRollResult.CODEC.listOf(), result, nbt -> tag.put("modifiers", nbt));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\VaultCatalystItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */