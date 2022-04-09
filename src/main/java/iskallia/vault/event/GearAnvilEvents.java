// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.event;

import com.mojang.datafixers.util.Pair;
import iskallia.vault.attribute.VAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ArtisanScrollItem;
import iskallia.vault.item.FlawedRubyItem;
import iskallia.vault.item.VoidOrbItem;
import iskallia.vault.item.gear.IdolItem;
import iskallia.vault.item.gear.VaultArmorItem;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.item.gear.VaultGearHelper;
import iskallia.vault.item.gear.applicable.VaultPlateItem;
import iskallia.vault.item.gear.applicable.VaultRepairCoreItem;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.SideOnlyFixer;
import iskallia.vault.util.SidedHelper;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class GearAnvilEvents
{
    @SubscribeEvent
    public static void onApplyT2Charm(final AnvilUpdateEvent event) {
        final ItemStack left = event.getLeft();
        if (left.getItem() == ModItems.ETCHING) {
            return;
        }
        if (left.getItem() instanceof VaultGear && event.getRight().getItem() == ModItems.GEAR_CHARM) {
            if (ModAttributes.GEAR_STATE.getOrDefault(left, VaultGear.State.UNIDENTIFIED).getValue(left) != VaultGear.State.UNIDENTIFIED) {
                return;
            }
            if (!ModAttributes.GEAR_ROLL_TYPE.exists(left)) {
                return;
            }
            if (ModAttributes.GEAR_TIER.getOrDefault(left, 0).getValue(left) >= 1) {
                return;
            }
            final PlayerEntity player = MiscUtils.findPlayerUsingAnvil(left, event.getRight());
            if (player == null) {
                return;
            }
            final int vaultLevel = SidedHelper.getVaultLevel(player);
            if (vaultLevel < 100) {
                return;
            }
            final String pool = ModAttributes.GEAR_ROLL_TYPE.get(left).map(attribute -> attribute.getValue(left)).get();
            final String upgraded = ModConfigs.VAULT_GEAR_UPGRADE.getUpgradedRarity(pool);
            final ItemStack output = left.copy();
            ModAttributes.GEAR_TIER.create(output, 1);
            ModAttributes.GEAR_ROLL_TYPE.create(output, upgraded);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(20);
        }
    }
    
    @SubscribeEvent
    public static void onApplyT3Charm(final AnvilUpdateEvent event) {
        final ItemStack left = event.getLeft();
        if (left.getItem() == ModItems.ETCHING) {
            return;
        }
        if (left.getItem() instanceof VaultGear && event.getRight().getItem() == ModItems.GEAR_CHARM_T3) {
            if (ModAttributes.GEAR_STATE.getOrDefault(left, VaultGear.State.UNIDENTIFIED).getValue(left) != VaultGear.State.UNIDENTIFIED) {
                return;
            }
            if (!ModAttributes.GEAR_ROLL_TYPE.exists(left)) {
                return;
            }
            if (ModAttributes.GEAR_TIER.getOrDefault(left, 0).getValue(left) != 1) {
                return;
            }
            final PlayerEntity player = MiscUtils.findPlayerUsingAnvil(left, event.getRight());
            if (player == null) {
                return;
            }
            final int vaultLevel = SidedHelper.getVaultLevel(player);
            if (vaultLevel < 200) {
                return;
            }
            final String pool = ModAttributes.GEAR_ROLL_TYPE.get(left).map(attribute -> attribute.getValue(left)).get();
            final String upgraded = ModConfigs.VAULT_GEAR_UPGRADE.getUpgradedRarity(pool);
            final ItemStack output = left.copy();
            ModAttributes.GEAR_TIER.create(output, 2);
            ModAttributes.GEAR_ROLL_TYPE.create(output, upgraded);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(20);
        }
    }
    
    @SubscribeEvent
    public static void onApplyBanishedSoul(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof IdolItem && event.getRight().getItem() == ModItems.BANISHED_SOUL && event.getLeft().getDamageValue() == 0) {
            final ItemStack output = event.getLeft().copy();
            if (ModAttributes.IDOL_AUGMENTED.exists(output) || !ModAttributes.GEAR_RANDOM_SEED.exists(output)) {
                return;
            }
            ModAttributes.IDOL_AUGMENTED.create(output, true);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(15);
        }
    }
    
    @SubscribeEvent
    public static void onBreakBanishedSoul(final AnvilRepairEvent event) {
        final ItemStack result = event.getItemResult();
        ItemStack original;
        if (result.isEmpty()) {
            result.setCount(1);
            if (!(result.getItem() instanceof IdolItem)) {
                return;
            }
            final int originalSlot = SideOnlyFixer.getSlotFor(event.getPlayer().inventory, result);
            if (originalSlot == -1) {
                return;
            }
            original = event.getPlayer().inventory.getItem(originalSlot);
        }
        else {
            original = event.getItemResult();
        }
        if (original.getItem() instanceof IdolItem && event.getIngredientInput().getItem() == ModItems.BANISHED_SOUL) {
            final long seed = ModAttributes.GEAR_RANDOM_SEED.getBase(original).orElse(0L);
            final Random r = new Random(seed);
            event.setBreakChance(1.0f);
            if (r.nextFloat() <= 0.33333334f) {
                original.setCount(0);
                event.getPlayer().getCommandSenderWorld().levelEvent(1029, event.getPlayer().blockPosition(), 0);
            }
            else {
                ModAttributes.DURABILITY.getBase(original).ifPresent(value -> ModAttributes.DURABILITY.create(original, value + 3000));
            }
        }
    }
    
    @SubscribeEvent
    public static void onApplyEtching(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultArmorItem && event.getRight().getItem() == ModItems.ETCHING) {
            final ItemStack output = event.getLeft().copy();
            if (ModAttributes.GEAR_SET.exists(output) && ModAttributes.GEAR_SET.getBase(output).orElse(VaultGear.Set.NONE) != VaultGear.Set.NONE) {
                return;
            }
            final VaultGear.Set set = ModAttributes.GEAR_SET.getOrDefault(event.getRight(), VaultGear.Set.NONE).getValue(event.getRight());
            ModAttributes.GEAR_SET.create(output, set);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(25);
        }
    }
    
    @SubscribeEvent
    public static void onApplyRepairCore(final AnvilUpdateEvent event) {
        if (!(event.getRight().getItem() instanceof VaultRepairCoreItem)) {
            return;
        }
        if (event.getLeft().getItem() instanceof VaultGear) {
            final ItemStack output = event.getLeft().copy();
            final ItemStack repairCore = event.getRight();
            final int repairLevel = ((VaultRepairCoreItem)repairCore.getItem()).getVaultGearTier();
            if (ModAttributes.GEAR_TIER.getOrDefault(output, 0).getValue(output) != repairLevel) {
                return;
            }
            final int maxRepairs = ModAttributes.MAX_REPAIRS.getOrDefault(output, -1).getValue(output);
            final int curRepairs = ModAttributes.CURRENT_REPAIRS.getOrDefault(output, 0).getValue(output);
            if (maxRepairs == -1 || curRepairs >= maxRepairs) {
                return;
            }
            ModAttributes.CURRENT_REPAIRS.create(output, curRepairs + 1);
            ModAttributes.DURABILITY.getBase(output).ifPresent(value -> ModAttributes.DURABILITY.create(output, (int)(value * 0.75f)));
            output.setDamageValue(0);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(1);
        }
    }
    
    @SubscribeEvent
    public static void onApplyPlating(final AnvilUpdateEvent event) {
        if (!(event.getRight().getItem() instanceof VaultPlateItem)) {
            return;
        }
        if (event.getLeft().getItem() instanceof VaultGear) {
            final ItemStack output = event.getLeft().copy();
            final ItemStack plate = event.getRight();
            final int plateLevel = ((VaultPlateItem)plate.getItem()).getVaultGearTier();
            if (ModAttributes.GEAR_TIER.getOrDefault(output, 0).getValue(output) != plateLevel) {
                return;
            }
            final int level = ModAttributes.ADD_PLATING.getOrDefault(output, 0).getValue(output);
            final int decrement = Math.min(20 - level, event.getRight().getCount());
            ModAttributes.ADD_PLATING.create(output, level + decrement);
            event.setOutput(output);
            event.setMaterialCost(decrement);
            event.setCost(decrement);
        }
    }
    
    @SubscribeEvent
    public static void onApplyWutaxShard(final AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.WUTAX_SHARD) {
            return;
        }
        if (event.getLeft().getItem() instanceof VaultGear) {
            final ItemStack output = event.getLeft().copy();
            ModAttributes.MIN_VAULT_LEVEL.getValue(output).ifPresent(level -> {
                final int tier = ModAttributes.GEAR_TIER.get(event.getLeft()).map(attribute -> attribute.getValue(event.getLeft())).orElse(0);
                final int tierMinLevel = ModConfigs.VAULT_GEAR.getTierConfig(tier).getMinLevel();
                final int maxAllowedDecrement = Math.max(0, level - tierMinLevel);
                final int decrement = Math.min(maxAllowedDecrement, event.getRight().getCount());
                ModAttributes.MIN_VAULT_LEVEL.create(output, level - decrement);
                event.setOutput(output);
                event.setMaterialCost(decrement);
                event.setCost(decrement);
            });
        }
    }
    
    @SubscribeEvent
    public static void onApplyWutaxCrystal(final AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.WUTAX_CRYSTAL) {
            return;
        }
        if (event.getLeft().getItem() instanceof VaultGear) {
            final ItemStack output = event.getLeft().copy();
            final float level = ModAttributes.GEAR_LEVEL.getOrDefault(output, 0.0f).getValue(output);
            final int max = ModAttributes.GEAR_MAX_LEVEL.getOrDefault(output, 0).getValue(output);
            final int increment = Math.min(max - (int)level, event.getRight().getCount());
            VaultGear.addLevel(output, (float)increment);
            event.setOutput(output);
            event.setMaterialCost(increment);
            event.setCost(increment);
        }
    }
    
    @SubscribeEvent
    public static void onApplyVoidOrb(final AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.VOID_ORB) {
            return;
        }
        if (event.getLeft().getItem() instanceof VaultGear) {
            final ItemStack output = event.getLeft().copy();
            final VaultGear<?> outputItem = (VaultGear<?>)output.getItem();
            final int maxRepairs = ModAttributes.MAX_REPAIRS.getOrDefault(output, -1).getValue(output);
            final int curRepairs = ModAttributes.CURRENT_REPAIRS.getOrDefault(output, 0).getValue(output);
            final float level = ModAttributes.GEAR_LEVEL.getOrDefault(output, 0.0f).getValue(output);
            if (maxRepairs == -1 || curRepairs >= maxRepairs || level == 0.0f) {
                return;
            }
            final int rolls = ModAttributes.GEAR_MODIFIERS_TO_ROLL.getOrDefault(output, 0).getValue(output);
            if (rolls != 0) {
                return;
            }
            final Pair<EquipmentSlotType, VAttribute<?, ?>> predefinedRemoval = VoidOrbItem.getPredefinedRemoval(event.getRight());
            VAttribute<?, ?> foundAttribute = null;
            if (predefinedRemoval != null) {
                if (!outputItem.isIntendedForSlot((EquipmentSlotType)predefinedRemoval.getFirst())) {
                    return;
                }
                final List<VAttribute<?, ?>> attributes = VaultGearHelper.getAliasAttributes(((VAttribute)predefinedRemoval.getSecond()).getId());
                for (final VAttribute<?, ?> attribute : attributes) {
                    if (VaultGearHelper.hasModifier(output, attribute)) {
                        foundAttribute = attribute;
                        break;
                    }
                }
                if (foundAttribute == null) {
                    return;
                }
            }
            if (!VaultGearHelper.hasModifier(output) || !VaultGearHelper.hasUsedLevels(output)) {
                return;
            }
            ModAttributes.GEAR_MODIFIERS_TO_ROLL.create(output, -1);
            if (predefinedRemoval != null) {
                ModAttributes.GUARANTEED_MODIFIER_REMOVAL.create(output, foundAttribute.getId().toString());
            }
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(1);
        }
    }
    
    @SubscribeEvent
    public static void onApplyArtisanScroll(final AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.ARTISAN_SCROLL) {
            return;
        }
        if (event.getLeft().isDamaged()) {
            return;
        }
        if (!(event.getLeft().getItem() instanceof VaultGear)) {
            return;
        }
        final VaultGear<?> gearItem = (VaultGear<?>)event.getLeft().getItem();
        final PlayerEntity playerEntity = event.getPlayer();
        if (playerEntity == null) {
            return;
        }
        final World world = playerEntity.getCommandSenderWorld();
        if (!(world instanceof ServerWorld)) {
            return;
        }
        final ItemStack output = event.getLeft().copy();
        final VaultGear.Rarity rarity = ModAttributes.GEAR_RARITY.get(output).map(attribute -> attribute.getValue(output)).orElse(null);
        if (rarity == null) {
            return;
        }
        final int tier = ModAttributes.GEAR_TIER.getOrDefault(output, 0).getValue(output);
        final Pair<EquipmentSlotType, VAttribute<?, ?>> gearModifier = ArtisanScrollItem.getPredefinedRoll(event.getRight());
        if (ModAttributes.REFORGED.getOrDefault(output, false).getValue(output)) {
            return;
        }
        if (ModAttributes.GEAR_STATE.getOrDefault(output, VaultGear.State.UNIDENTIFIED).getValue(output) == VaultGear.State.UNIDENTIFIED) {
            return;
        }
        if (gearModifier != null && !VaultGearHelper.canRollModifier(output, rarity, tier, (VAttribute<?, ?>)gearModifier.getSecond())) {
            return;
        }
        VaultGearHelper.removeAllAttributes(output);
        ModAttributes.GEAR_STATE.create(output, VaultGear.State.UNIDENTIFIED);
        ModAttributes.REFORGED.create(output, true);
        VAttribute<?, ?> attribute = null;
        if (gearModifier != null) {
            final EquipmentSlotType slotType = (EquipmentSlotType)gearModifier.getFirst();
            attribute = (VAttribute)gearModifier.getSecond();
            if (!gearItem.isIntendedForSlot(slotType)) {
                return;
            }
            ModAttributes.GUARANTEED_MODIFIER.create(output, attribute.getId().toString());
        }
        event.setCost(5);
        event.setMaterialCost(1);
        event.setOutput(output);
    }
    
    @SubscribeEvent
    public static void onCreateVoidOrb(final AnvilUpdateEvent event) {
        if (!(event.getLeft().getItem() instanceof ArtisanScrollItem) || !(event.getRight().getItem() instanceof VoidOrbItem)) {
            return;
        }
        final ItemStack scroll = event.getLeft();
        final ItemStack orb = event.getRight();
        final Pair<EquipmentSlotType, VAttribute<?, ?>> predefinedRoll;
        if ((predefinedRoll = ArtisanScrollItem.getPredefinedRoll(scroll)) == null || VoidOrbItem.getPredefinedRemoval(orb) != null) {
            return;
        }
        final ItemStack output = orb.copy();
        VoidOrbItem.setPredefinedRemoval(output, (EquipmentSlotType)predefinedRoll.getFirst(), (VAttribute<?, ?>)predefinedRoll.getSecond());
        event.setCost(5);
        event.setMaterialCost(1);
        event.setOutput(output);
    }
    
    @SubscribeEvent
    public static void onCreateArtisanScroll(final AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.FABRICATION_JEWEL) {
            return;
        }
        if (event.getLeft().isDamaged()) {
            return;
        }
        if (!(event.getLeft().getItem() instanceof VaultGear)) {
            return;
        }
        final ItemStack input = event.getLeft();
        final PlayerEntity playerEntity = event.getPlayer();
        if (!hasLearnedArtisan(playerEntity)) {
            return;
        }
        if (!ModAttributes.GEAR_RANDOM_SEED.exists(input)) {
            return;
        }
        if (ModAttributes.GEAR_STATE.getOrDefault(input, VaultGear.State.UNIDENTIFIED).getValue(input) == VaultGear.State.UNIDENTIFIED) {
            return;
        }
        if (!VaultGearHelper.hasModifier(input)) {
            return;
        }
        event.setCost(5);
        event.setMaterialCost(1);
        final ItemStack result = new ItemStack((IItemProvider)ModItems.ARTISAN_SCROLL);
        ArtisanScrollItem.setInitialized(result, true);
        event.setOutput(result);
    }
    
    @SubscribeEvent
    public static void onCreateArtisanScroll(final AnvilRepairEvent event) {
        final ItemStack input = event.getItemInput();
        final ItemStack result = event.getItemResult();
        if (input.isDamaged()) {
            return;
        }
        if (!(input.getItem() instanceof VaultGear)) {
            return;
        }
        if (result.getItem() != ModItems.ARTISAN_SCROLL) {
            return;
        }
        if (!VaultGearHelper.hasModifier(input)) {
            return;
        }
        final EquipmentSlotType slotType = ((VaultGear)input.getItem()).getIntendedSlot();
        if (slotType == null) {
            return;
        }
        if (!ModAttributes.GEAR_RANDOM_SEED.exists(input)) {
            return;
        }
        final long seed = ModAttributes.GEAR_RANDOM_SEED.getBase(input).orElse(0L);
        final Random rand = new Random(seed);
        final VAttribute<?, ?> randomModifier = VaultGearHelper.getRandomModifier(input, rand);
        if (randomModifier != null && rand.nextFloat() < ModConfigs.VAULT_GEAR_UTILITIES.getFabricationJewelKeepModifierChance()) {
            ArtisanScrollItem.setPredefinedRoll(result, slotType, randomModifier);
        }
        ArtisanScrollItem.setInitialized(result, true);
    }
    
    @SubscribeEvent
    public static void onApplyArtisanPearl(final AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.FLAWED_RUBY) {
            return;
        }
        final PlayerEntity playerEntity = event.getPlayer();
        if (hasLearnedArtisan(playerEntity) || hasLearnedTreasureHunter(playerEntity)) {
            if (ModAttributes.IMBUED.getOrDefault(event.getLeft(), false).getValue(event.getLeft())) {
                return;
            }
            if (event.getLeft().getItem() instanceof VaultGear) {
                final ItemStack output = event.getLeft().copy();
                FlawedRubyItem.markApplied(output);
                event.setOutput(output);
                event.setMaterialCost(1);
                event.setCost(1);
            }
        }
    }
    
    private static boolean hasLearnedArtisan(final PlayerEntity player) {
        if (player == null) {
            return false;
        }
        final World world = player.getCommandSenderWorld();
        if (!(world instanceof ServerWorld)) {
            return false;
        }
        final ServerWorld serverWorld = (ServerWorld)world;
        final TalentTree talents = PlayerTalentsData.get(serverWorld).getTalents(player);
        return talents.hasLearnedNode(ModConfigs.TALENTS.ARTISAN);
    }
    
    private static boolean hasLearnedTreasureHunter(final PlayerEntity player) {
        if (player == null) {
            return false;
        }
        final World world = player.getCommandSenderWorld();
        if (!(world instanceof ServerWorld)) {
            return false;
        }
        final ServerWorld serverWorld = (ServerWorld)world;
        final TalentTree talents = PlayerTalentsData.get(serverWorld).getTalents(player);
        return talents.hasLearnedNode(ModConfigs.TALENTS.TREASURE_HUNTER);
    }
}
