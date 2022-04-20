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
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class VaultInhibitorItem extends Item {
    private static final Random rand;

    public VaultInhibitorItem(final ItemGroup group, final ResourceLocation id) {
        super(new Item.Properties().tab(group).stacksTo(1));
        this.setRegistryName(id);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        getModifierRolls(stack).ifPresent(result -> {
            if (!result.isEmpty()) {
                final String modifierDesc = String.format("Removes Modifier%s:", (result.size() <= 1) ? "" : "s");
                tooltip.add(StringTextComponent.EMPTY);
                tooltip.add(new StringTextComponent(modifierDesc).withStyle(TextFormatting.GOLD));

                final Iterator<ModifierRollResult> iterator = result.iterator();
                while (iterator.hasNext()) {
                    final ModifierRollResult outcome = iterator.next();
                    tooltip.addAll(outcome.getTooltipDescription("- ", true));
                }
            }
        });
    }

    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        if (world.isClientSide()) {
            return;
        }
        final List<ModifierRollResult> results = getModifierRolls(stack).orElse(Collections.emptyList());
        if (results.isEmpty()) {
        }
        getSeed(stack);
    }

    public static long getSeed(final ItemStack stack) {
        if (!(stack.getItem() instanceof VaultInhibitorItem)) {
            return 0L;
        }
        final CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.contains("Seed", 4)) {
            nbt.putLong("Seed", VaultInhibitorItem.rand.nextLong());
        }
        return nbt.getLong("Seed");
    }

    @Nullable
    public static List<String> getCrystalCombinationModifiers(final ItemStack catalyst, final ItemStack crystal) {
        final CrystalData data = VaultCrystalItem.getData(crystal.copy());
        if (!data.canModifyWithCrafting()) {
            return null;
        }
        final Optional<List<ModifierRollResult>> rollsOpt = getModifierRolls(catalyst);
        if (!rollsOpt.isPresent()) {
            return null;
        }
        final List<ModifierRollResult> rolls = rollsOpt.get();
        final long seed = VaultCrystalItem.getSeed(crystal) ^ getSeed(catalyst);
        final Random rand = new Random(seed);
        for (int i = 0; i < rand.nextInt(32); ++i) {
            rand.nextLong();
        }
        final List<String> removeModifiers = new ArrayList<String>();
        for (final ModifierRollResult modifierRoll : rolls) {
            final List<String> usedModifiers = data.getModifiers().stream().map(CrystalData.Modifier::getModifierName).collect(Collectors.toList());
            final String availableModifier = modifierRoll.getModifier(rand, modifierStr -> !usedModifiers.contains(modifierStr) && !removeModifiers.contains(modifierStr));
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

    public static Optional<List<ModifierRollResult>> getModifierRolls(final ItemStack stack) {
        if (!(stack.getItem() instanceof VaultInhibitorItem)) {
            return Optional.empty();
        }
        final CompoundNBT tag = stack.getOrCreateTag();
        return CodecUtils.readNBT((com.mojang.serialization.Codec<List<ModifierRollResult>>) ModifierRollResult.CODEC.listOf(), (INBT) tag.getList("modifiers", 10));
    }

    public static void setModifierRolls(final ItemStack stack, final List<ModifierRollResult> result) {
        if (!(stack.getItem() instanceof VaultInhibitorItem)) {
            return;
        }
        final CompoundNBT tag = stack.getOrCreateTag();
        CodecUtils.writeNBT((com.mojang.serialization.Codec<List<ModifierRollResult>>) ModifierRollResult.CODEC.listOf(), result, nbt -> tag.put("modifiers", nbt));
    }

    static {
        rand = new Random();
    }
}
