package iskallia.vault.event;

import com.mojang.datafixers.util.Pair;
import iskallia.vault.attribute.*;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ArtisanScrollItem;
import iskallia.vault.item.FlawedRubyItem;
import iskallia.vault.item.VoidOrbItem;
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
public class GearAnvilEvents {
    @SubscribeEvent
    public static void onApplyT2Charm(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        if (left.getItem() == ModItems.ETCHING) {
            return;
        }
        if (left.getItem() instanceof VaultGear && event.getRight().getItem() == ModItems.GEAR_CHARM) {
            if (((EnumAttribute) ModAttributes.GEAR_STATE.getOrDefault(left, VaultGear.State.UNIDENTIFIED)).getValue(left) != VaultGear.State.UNIDENTIFIED) {
                return;
            }
            if (!ModAttributes.GEAR_ROLL_TYPE.exists(left)) {
                return;
            }
            if (((Integer) ((IntegerAttribute) ModAttributes.GEAR_TIER.getOrDefault(left, Integer.valueOf(0))).getValue(left)).intValue() >= 1) {
                return;
            }
            PlayerEntity player = MiscUtils.findPlayerUsingAnvil(left, event.getRight());
            if (player == null) {
                return;
            }
            int vaultLevel = SidedHelper.getVaultLevel(player);
            if (vaultLevel < 100) {
                return;
            }

            String pool = ModAttributes.GEAR_ROLL_TYPE.get(left).map(attribute -> (String) attribute.getValue(left)).get();
            String upgraded = ModConfigs.VAULT_GEAR_UPGRADE.getUpgradedRarity(pool);

            ItemStack output = left.copy();
            ModAttributes.GEAR_TIER.create(output, Integer.valueOf(1));
            ModAttributes.GEAR_ROLL_TYPE.create(output, upgraded);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(20);
        }
    }

    @SubscribeEvent
    public static void onApplyT3Charm(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        if (left.getItem() == ModItems.ETCHING) {
            return;
        }
        if (left.getItem() instanceof VaultGear && event.getRight().getItem() == ModItems.GEAR_CHARM_T3) {
            if (((EnumAttribute) ModAttributes.GEAR_STATE.getOrDefault(left, VaultGear.State.UNIDENTIFIED)).getValue(left) != VaultGear.State.UNIDENTIFIED) {
                return;
            }
            if (!ModAttributes.GEAR_ROLL_TYPE.exists(left)) {
                return;
            }
            if (((Integer) ((IntegerAttribute) ModAttributes.GEAR_TIER.getOrDefault(left, Integer.valueOf(0))).getValue(left)).intValue() != 1) {
                return;
            }
            PlayerEntity player = MiscUtils.findPlayerUsingAnvil(left, event.getRight());
            if (player == null) {
                return;
            }
            int vaultLevel = SidedHelper.getVaultLevel(player);
            if (vaultLevel < 200) {
                return;
            }

            String pool = ModAttributes.GEAR_ROLL_TYPE.get(left).map(attribute -> (String) attribute.getValue(left)).get();
            String upgraded = ModConfigs.VAULT_GEAR_UPGRADE.getUpgradedRarity(pool);

            ItemStack output = left.copy();
            ModAttributes.GEAR_TIER.create(output, Integer.valueOf(2));
            ModAttributes.GEAR_ROLL_TYPE.create(output, upgraded);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(20);
        }
    }

    @SubscribeEvent
    public static void onApplyBanishedSoul(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof iskallia.vault.item.gear.IdolItem && event
                .getRight().getItem() == ModItems.BANISHED_SOUL && event
                .getLeft().getDamageValue() == 0) {
            ItemStack output = event.getLeft().copy();
            if (ModAttributes.IDOL_AUGMENTED.exists(output) || !ModAttributes.GEAR_RANDOM_SEED.exists(output)) {
                return;
            }
            ModAttributes.IDOL_AUGMENTED.create(output, Boolean.valueOf(true));
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(15);
        }
    }


    @SubscribeEvent
    public static void onBreakBanishedSoul(AnvilRepairEvent event) {
        ItemStack original, result = event.getItemResult();
        if (result.isEmpty()) {
            result.setCount(1);
            if (!(result.getItem() instanceof iskallia.vault.item.gear.IdolItem)) {
                return;
            }
            int originalSlot = SideOnlyFixer.getSlotFor((event.getPlayer()).inventory, result);
            if (originalSlot != -1) {
                original = (event.getPlayer()).inventory.getItem(originalSlot);
            } else {

                return;
            }
        } else {
            original = event.getItemResult();
        }
        if (original.getItem() instanceof iskallia.vault.item.gear.IdolItem && event.getIngredientInput().getItem() == ModItems.BANISHED_SOUL) {
            long seed = ((Long) ModAttributes.GEAR_RANDOM_SEED.getBase(original).orElse(Long.valueOf(0L))).longValue();
            Random r = new Random(seed);

            event.setBreakChance(1.0F);
            if (r.nextFloat() <= 0.33333334F) {
                original.setCount(0);
                event.getPlayer().getCommandSenderWorld().levelEvent(1029, event.getPlayer().blockPosition(), 0);
            } else {
                ModAttributes.DURABILITY.getBase(original).ifPresent(value -> ModAttributes.DURABILITY.create(original, Integer.valueOf(value.intValue() + 3000)));
            }
        }
    }


    @SubscribeEvent
    public static void onApplyEtching(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof iskallia.vault.item.gear.VaultArmorItem && event.getRight().getItem() == ModItems.ETCHING) {
            ItemStack output = event.getLeft().copy();
            if (ModAttributes.GEAR_SET.exists(output) && ModAttributes.GEAR_SET.getBase(output).orElse(VaultGear.Set.NONE) != VaultGear.Set.NONE) {
                return;
            }
            VaultGear.Set set = (VaultGear.Set) ((EnumAttribute) ModAttributes.GEAR_SET.getOrDefault(event.getRight(), VaultGear.Set.NONE)).getValue(event.getRight());
            ModAttributes.GEAR_SET.create(output, set);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(25);
        }
    }

    @SubscribeEvent
    public static void onApplyRepairCore(AnvilUpdateEvent event) {
        if (!(event.getRight().getItem() instanceof VaultRepairCoreItem))
            return;
        if (event.getLeft().getItem() instanceof VaultGear) {
            ItemStack output = event.getLeft().copy();
            ItemStack repairCore = event.getRight();
            int repairLevel = ((VaultRepairCoreItem) repairCore.getItem()).getVaultGearTier();

            if (((Integer) ((IntegerAttribute) ModAttributes.GEAR_TIER.getOrDefault(output, Integer.valueOf(0))).getValue(output)).intValue() != repairLevel) {
                return;
            }

            int maxRepairs = ((Integer) ((IntegerAttribute) ModAttributes.MAX_REPAIRS.getOrDefault(output, Integer.valueOf(-1))).getValue(output)).intValue();
            int curRepairs = ((Integer) ((IntegerAttribute) ModAttributes.CURRENT_REPAIRS.getOrDefault(output, Integer.valueOf(0))).getValue(output)).intValue();
            if (maxRepairs == -1 || curRepairs >= maxRepairs) {
                return;
            }

            ModAttributes.CURRENT_REPAIRS.create(output, Integer.valueOf(curRepairs + 1));

            ModAttributes.DURABILITY.getBase(output).ifPresent(value -> ModAttributes.DURABILITY.create(output, Integer.valueOf((int) (value.intValue() * 0.75F))));


            output.setDamageValue(0);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(1);
        }
    }

    @SubscribeEvent
    public static void onApplyPlating(AnvilUpdateEvent event) {
        if (!(event.getRight().getItem() instanceof VaultPlateItem))
            return;
        if (event.getLeft().getItem() instanceof VaultGear) {
            ItemStack output = event.getLeft().copy();
            ItemStack plate = event.getRight();
            int plateLevel = ((VaultPlateItem) plate.getItem()).getVaultGearTier();

            if (((Integer) ((IntegerAttribute) ModAttributes.GEAR_TIER.getOrDefault(output, Integer.valueOf(0))).getValue(output)).intValue() != plateLevel) {
                return;
            }

            int level = ((Integer) ((IntegerAttribute) ModAttributes.ADD_PLATING.getOrDefault(output, Integer.valueOf(0))).getValue(output)).intValue();
            int decrement = Math.min(20 - level, event.getRight().getCount());
            ModAttributes.ADD_PLATING.create(output, Integer.valueOf(level + decrement));

            event.setOutput(output);
            event.setMaterialCost(decrement);
            event.setCost(decrement);
        }
    }

    @SubscribeEvent
    public static void onApplyWutaxShard(AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.WUTAX_SHARD)
            return;
        if (event.getLeft().getItem() instanceof VaultGear) {
            ItemStack output = event.getLeft().copy();

            ModAttributes.MIN_VAULT_LEVEL.getValue(output).ifPresent(level -> {
                int tier = ((Integer) ModAttributes.GEAR_TIER.get(event.getLeft()).map(( IntegerAttribute attribute)->{return 0;}).orElse(Integer.valueOf(0))).intValue();
                int tierMinLevel = ModConfigs.VAULT_GEAR.getTierConfig(tier).getMinLevel();
                int maxAllowedDecrement = Math.max(0, level.intValue() - tierMinLevel);
                int decrement = Math.min(maxAllowedDecrement, event.getRight().getCount());
                ModAttributes.MIN_VAULT_LEVEL.create(output, Integer.valueOf(level.intValue() - decrement));
                event.setOutput(output);
                event.setMaterialCost(decrement);
                event.setCost(decrement);
            });
        }
    }


    @SubscribeEvent
    public static void onApplyWutaxCrystal(AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.WUTAX_CRYSTAL)
            return;
        if (event.getLeft().getItem() instanceof VaultGear) {
            ItemStack output = event.getLeft().copy();

            float level = ((Float) ((FloatAttribute) ModAttributes.GEAR_LEVEL.getOrDefault(output, Float.valueOf(0.0F))).getValue(output)).floatValue();
            int max = ((Integer) ((IntegerAttribute) ModAttributes.GEAR_MAX_LEVEL.getOrDefault(output, Integer.valueOf(0))).getValue(output)).intValue();

            int increment = Math.min(max - (int) level, event.getRight().getCount());
            VaultGear.addLevel(output, increment);

            event.setOutput(output);
            event.setMaterialCost(increment);
            event.setCost(increment);
        }
    }

    @SubscribeEvent
    public static void onApplyVoidOrb(AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.VOID_ORB)
            return;
        if (event.getLeft().getItem() instanceof VaultGear) {
            ItemStack output = event.getLeft().copy();
            VaultGear<?> outputItem = (VaultGear) output.getItem();

            int maxRepairs = ((Integer) ((IntegerAttribute) ModAttributes.MAX_REPAIRS.getOrDefault(output, Integer.valueOf(-1))).getValue(output)).intValue();
            int curRepairs = ((Integer) ((IntegerAttribute) ModAttributes.CURRENT_REPAIRS.getOrDefault(output, Integer.valueOf(0))).getValue(output)).intValue();

            float level = ((Float) ((FloatAttribute) ModAttributes.GEAR_LEVEL.getOrDefault(output, Float.valueOf(0.0F))).getValue(output)).floatValue();

            if (maxRepairs == -1 || curRepairs >= maxRepairs || level == 0.0F) {
                return;
            }

            int rolls = ((Integer) ((IntegerAttribute) ModAttributes.GEAR_MODIFIERS_TO_ROLL.getOrDefault(output, Integer.valueOf(0))).getValue(output)).intValue();
            if (rolls != 0) {
                return;
            }

            Pair<EquipmentSlotType, VAttribute<?, ?>> predefinedRemoval = VoidOrbItem.getPredefinedRemoval(event.getRight());
            VAttribute<?, ?> foundAttribute = null;
            if (predefinedRemoval != null) {
                if (!outputItem.isIntendedForSlot((EquipmentSlotType) predefinedRemoval.getFirst())) {
                    return;
                }
                List<VAttribute<?, ?>> attributes = VaultGearHelper.getAliasAttributes(((VAttribute) predefinedRemoval.getSecond()).getId());
                for (VAttribute<?, ?> attribute : attributes) {
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

            ModAttributes.GEAR_MODIFIERS_TO_ROLL.create(output, Integer.valueOf(-1));

            if (predefinedRemoval != null) {
                ModAttributes.GUARANTEED_MODIFIER_REMOVAL.create(output, foundAttribute.getId().toString());
            }

            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(1);
        }
    }

    @SubscribeEvent
    public static void onApplyArtisanScroll(AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.ARTISAN_SCROLL)
            return;
        if (event.getLeft().isDamaged())
            return;
        if (!(event.getLeft().getItem() instanceof VaultGear))
            return;
        VaultGear<?> gearItem = (VaultGear) event.getLeft().getItem();

        PlayerEntity playerEntity = event.getPlayer();
        if (playerEntity == null) {
            return;
        }

        World world = playerEntity.getCommandSenderWorld();
        if (!(world instanceof ServerWorld)) {
            return;
        }

        ItemStack output = event.getLeft().copy();
        VaultGear.Rarity rarity = ModAttributes.GEAR_RARITY.get(output).map(attribute -> (VaultGear.Rarity) attribute.getValue(output)).orElse(null);
        if (rarity == null) {
            return;
        }
        int tier = ((Integer) ((IntegerAttribute) ModAttributes.GEAR_TIER.getOrDefault(output, Integer.valueOf(0))).getValue(output)).intValue();
        Pair<EquipmentSlotType, VAttribute<?, ?>> gearModifier = ArtisanScrollItem.getPredefinedRoll(event.getRight());

        if (((Boolean) ((BooleanAttribute) ModAttributes.REFORGED.getOrDefault(output, Boolean.valueOf(false))).getValue(output)).booleanValue()) {
            return;
        }
        if (((EnumAttribute) ModAttributes.GEAR_STATE.getOrDefault(output, VaultGear.State.UNIDENTIFIED)).getValue(output) == VaultGear.State.UNIDENTIFIED) {
            return;
        }
        if (gearModifier != null && !VaultGearHelper.canRollModifier(output, rarity, tier, (VAttribute) gearModifier.getSecond())) {
            return;
        }

        VaultGearHelper.removeAllAttributes(output);
        ModAttributes.GEAR_STATE.create(output, VaultGear.State.UNIDENTIFIED);
        ModAttributes.REFORGED.create(output, Boolean.valueOf(true));

        if (gearModifier != null) {
            EquipmentSlotType slotType = (EquipmentSlotType) gearModifier.getFirst();
            VAttribute<?, ?> attribute = (VAttribute<?, ?>) gearModifier.getSecond();

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
    public static void onCreateVoidOrb(AnvilUpdateEvent event) {
        if (!(event.getLeft().getItem() instanceof ArtisanScrollItem) || !(event.getRight().getItem() instanceof VoidOrbItem)) {
            return;
        }

        ItemStack scroll = event.getLeft();
        ItemStack orb = event.getRight();
        Pair<EquipmentSlotType, VAttribute<?, ?>> predefinedRoll;
        if ((predefinedRoll = ArtisanScrollItem.getPredefinedRoll(scroll)) == null || VoidOrbItem.getPredefinedRemoval(orb) != null) {
            return;
        }

        ItemStack output = orb.copy();
        VoidOrbItem.setPredefinedRemoval(output, (EquipmentSlotType) predefinedRoll.getFirst(), (VAttribute) predefinedRoll.getSecond());
        event.setCost(5);
        event.setMaterialCost(1);
        event.setOutput(output);
    }

    @SubscribeEvent
    public static void onCreateArtisanScroll(AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.FABRICATION_JEWEL)
            return;
        if (event.getLeft().isDamaged())
            return;
        if (!(event.getLeft().getItem() instanceof VaultGear))
            return;
        ItemStack input = event.getLeft();

        PlayerEntity playerEntity = event.getPlayer();
        if (!hasLearnedArtisan(playerEntity))
            return;
        if (!ModAttributes.GEAR_RANDOM_SEED.exists(input)) {
            return;
        }

        if (((EnumAttribute) ModAttributes.GEAR_STATE.getOrDefault(input, VaultGear.State.UNIDENTIFIED)).getValue(input) == VaultGear.State.UNIDENTIFIED) {
            return;
        }
        if (!VaultGearHelper.hasModifier(input)) {
            return;
        }

        event.setCost(5);
        event.setMaterialCost(1);
        ItemStack result = new ItemStack((IItemProvider) ModItems.ARTISAN_SCROLL);
        ArtisanScrollItem.setInitialized(result, true);
        event.setOutput(result);
    }

    @SubscribeEvent
    public static void onCreateArtisanScroll(AnvilRepairEvent event) {
        ItemStack input = event.getItemInput();
        ItemStack result = event.getItemResult();
        if (input.isDamaged())
            return;
        if (!(input.getItem() instanceof VaultGear))
            return;
        if (result.getItem() != ModItems.ARTISAN_SCROLL)
            return;
        if (!VaultGearHelper.hasModifier(input)) {
            return;
        }
        EquipmentSlotType slotType = ((VaultGear) input.getItem()).getIntendedSlot();
        if (slotType == null) {
            return;
        }
        if (!ModAttributes.GEAR_RANDOM_SEED.exists(input)) {
            return;
        }
        long seed = ((Long) ModAttributes.GEAR_RANDOM_SEED.getBase(input).orElse(Long.valueOf(0L))).longValue();
        Random rand = new Random(seed);
        VAttribute<?, ?> randomModifier = VaultGearHelper.getRandomModifier(input, rand);
        if (randomModifier != null && rand.nextFloat() < ModConfigs.VAULT_GEAR_UTILITIES.getFabricationJewelKeepModifierChance()) {
            ArtisanScrollItem.setPredefinedRoll(result, slotType, randomModifier);
        }
        ArtisanScrollItem.setInitialized(result, true);
    }

    @SubscribeEvent
    public static void onApplyArtisanPearl(AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.FLAWED_RUBY)
            return;
        PlayerEntity playerEntity = event.getPlayer();
        if (hasLearnedArtisan(playerEntity) || hasLearnedTreasureHunter(playerEntity)) {
            if (((Boolean) ((BooleanAttribute) ModAttributes.IMBUED.getOrDefault(event.getLeft(), Boolean.valueOf(false))).getValue(event.getLeft())).booleanValue())
                return;
            if (event.getLeft().getItem() instanceof VaultGear) {
                ItemStack output = event.getLeft().copy();

                FlawedRubyItem.markApplied(output);

                event.setOutput(output);
                event.setMaterialCost(1);
                event.setCost(1);
            }
        }
    }

    private static boolean hasLearnedArtisan(PlayerEntity player) {
        if (player == null) {
            return false;
        }
        World world = player.getCommandSenderWorld();
        if (!(world instanceof ServerWorld)) {
            return false;
        }
        ServerWorld serverWorld = (ServerWorld) world;
        TalentTree talents = PlayerTalentsData.get(serverWorld).getTalents(player);
        return talents.hasLearnedNode(ModConfigs.TALENTS.ARTISAN);
    }

    private static boolean hasLearnedTreasureHunter(PlayerEntity player) {
        if (player == null) {
            return false;
        }
        World world = player.getCommandSenderWorld();
        if (!(world instanceof ServerWorld)) {
            return false;
        }
        ServerWorld serverWorld = (ServerWorld) world;
        TalentTree talents = PlayerTalentsData.get(serverWorld).getTalents(player);
        return talents.hasLearnedNode(ModConfigs.TALENTS.TREASURE_HUNTER);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\event\GearAnvilEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */