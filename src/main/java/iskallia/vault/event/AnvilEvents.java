package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.config.entry.EnchantedBookEntry;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.*;
import iskallia.vault.item.catalyst.ModifierRollResult;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.item.paxel.VaultPaxelItem;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.util.OverlevelEnchantHelper;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnvilEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onVaultAnvil(final AnvilUpdateEvent event) {
        final World world = event.getPlayer().getCommandSenderWorld();
        final Item repairItem = event.getRight().getItem();
        if (repairItem == ModItems.MAGNETITE || repairItem == ModItems.REPAIR_CORE || repairItem == ModItems.REPAIR_CORE_T2) {
            return;
        }
        if (world.dimension() == Vault.VAULT_KEY) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onCombineVaultGear(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultGear && event.getRight().getItem() instanceof VaultGear) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(final AnvilUpdateEvent event) {
        final ItemStack equipment = event.getLeft();
        final ItemStack enchantedBook = event.getRight();
        if (equipment.getItem() == Items.ENCHANTED_BOOK) {
            return;
        }
        if (enchantedBook.getItem() != Items.ENCHANTED_BOOK) {
            return;
        }
        final ItemStack upgradedEquipment = equipment.copy();
        final Map<Enchantment, Integer> equipmentEnchantments = OverlevelEnchantHelper.getEnchantments(equipment);
        final Map<Enchantment, Integer> bookEnchantments = OverlevelEnchantHelper.getEnchantments(enchantedBook);
        final int overlevels = OverlevelEnchantHelper.getOverlevels(enchantedBook);
        if (overlevels == -1) {
            return;
        }
        final Map<Enchantment, Integer> enchantmentsToApply = new HashMap<Enchantment, Integer>(equipmentEnchantments);
        for (final Enchantment bookEnchantment : bookEnchantments.keySet()) {
            if (!equipmentEnchantments.containsKey(bookEnchantment)) {
                continue;
            }
            final int currentLevel = equipmentEnchantments.getOrDefault(bookEnchantment, 0);
            final int bookLevel = bookEnchantments.get(bookEnchantment);
            final int nextLevel = (currentLevel == bookLevel) ? (currentLevel + 1) : Math.max(currentLevel, bookLevel);
            enchantmentsToApply.put(bookEnchantment, nextLevel);
        }
        EnchantmentHelper.setEnchantments(enchantmentsToApply, upgradedEquipment);
        if (upgradedEquipment.equals(equipment, true)) {
            event.setCanceled(true);
        } else {
            final EnchantedBookEntry bookTier = ModConfigs.OVERLEVEL_ENCHANT.getTier(overlevels);
            event.setOutput(upgradedEquipment);
            event.setCost((bookTier == null) ? 1 : bookTier.getLevelNeeded());
        }
    }

    @SubscribeEvent
    public static void onApplyPaxelCharm(final AnvilUpdateEvent event) {
        final ItemStack paxelStack = event.getLeft();
        final ItemStack charmStack = event.getRight();
        if (paxelStack.getItem() != ModItems.VAULT_PAXEL) {
            return;
        }
        if (charmStack.getItem() != ModItems.PAXEL_CHARM) {
            return;
        }
        if (PaxelEnhancements.getEnhancement(paxelStack) != null) {
            return;
        }
        final ItemStack enhancedStack = paxelStack.copy();
        PaxelEnhancements.markShouldEnhance(enhancedStack);
        event.setCost(5);
        event.setOutput(enhancedStack);
    }

    @SubscribeEvent
    public static void onApplySeal(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof ItemVaultCrystalSeal) {
            final ItemStack output = event.getLeft().copy();
            final CrystalData data = VaultCrystalItem.getData(output);
            if (!data.getModifiers().isEmpty() || data.getSelectedObjective() != null) {
                return;
            }
            VaultRaid.init();
            final ResourceLocation objectiveKey = ((ItemVaultCrystalSeal) event.getRight().getItem()).getObjectiveId();
            final VaultObjective objective = VaultObjective.getObjective(objectiveKey);
            if (objective != null) {
                data.setSelectedObjective(objectiveKey);
                VaultCrystalItem.setRandomSeed(output);
                event.setOutput(output);
                event.setMaterialCost(1);
                event.setCost(8);
            }
        }
    }

    @SubscribeEvent
    public static void onApplyCake(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() == Items.CAKE) {
            final ItemStack output = event.getLeft().copy();
            final CrystalData data = VaultCrystalItem.getData(output);
            if (!data.getModifiers().isEmpty() || (data.getSelectedObjective() != null && data.getType() == CrystalData.Type.COOP)) {
                return;
            }
            VaultRaid.init();
            data.setSelectedObjective(Vault.id("cake_hunt"));
            VaultCrystalItem.setRandomSeed(output);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(8);
        }
    }

    @SubscribeEvent
    public static void onApplyRaffleSeal(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof ItemVaultRaffleSeal) {
            final ItemStack output = event.getLeft().copy();
            final CrystalData data = VaultCrystalItem.getData(output);
            if (!data.getModifiers().isEmpty() || data.getSelectedObjective() != null) {
                return;
            }
            final String playerName = ItemVaultRaffleSeal.getPlayerName(event.getRight());
            if (playerName.isEmpty()) {
                return;
            }
            data.setPlayerBossName(playerName);
            event.setOutput(output);
            event.setMaterialCost(1);
            event.setCost(8);
        }
    }

    @SubscribeEvent
    public static void onApplyCatalyst(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof VaultCatalystItem) {
            final ItemStack output = event.getLeft().copy();
            final CrystalData data = VaultCrystalItem.getData(output);
            if (!data.canModifyWithCrafting()) {
                return;
            }
            final List<String> modifiers = VaultCatalystItem.getCrystalCombinationModifiers(event.getRight(), event.getLeft());
            if (modifiers == null || modifiers.isEmpty()) {
                return;
            }
            modifiers.forEach(modifier -> data.addCatalystModifier(modifier, true, CrystalData.Modifier.Operation.ADD));
            event.setOutput(output);
            event.setCost(modifiers.size() * 4);
            event.setMaterialCost(1);
        }
    }

    @SubscribeEvent
    public static void onApplyRune(final AnvilUpdateEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
            return;
        }
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof VaultRuneItem) {
            final ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.getPlayer();
            final int level = PlayerVaultStatsData.get(sPlayer.getLevel()).getVaultStats(sPlayer).getVaultLevel();
            final int minLevel = ModConfigs.VAULT_RUNE.getMinimumLevel(event.getRight().getItem()).orElse(0);
            if (level < minLevel) {
                return;
            }
            final ItemStack output = event.getLeft().copy();
            final CrystalData data = VaultCrystalItem.getData(output);
            if (!data.canModifyWithCrafting()) {
                return;
            }
            final VaultRuneItem runeItem = (VaultRuneItem) event.getRight().getItem();
            if (!data.canAddRoom(runeItem.getRoomName())) {
                return;
            }
            final int amount = event.getRight().getCount();
            for (int i = 0; i < amount; ++i) {
                data.addGuaranteedRoom(runeItem.getRoomName());
            }
            event.setOutput(output);
            event.setCost(amount * 4);
            event.setMaterialCost(amount);
        }
    }

    @SubscribeEvent
    public static void onCraftInhibitor(final AnvilUpdateEvent event) {
        if (!(event.getLeft().getItem() instanceof VaultCatalystItem)) {
            return;
        }
        if (event.getRight().getItem() != ModItems.PERFECT_ECHO_GEM) {
            return;
        }
        final ItemStack catalyst = event.getLeft().copy();
        final ItemStack inhibitor = new ItemStack(ModItems.VAULT_INHIBITOR);
        final List<ModifierRollResult> modifiers = VaultCatalystItem.getModifierRolls(catalyst).orElse(Collections.emptyList());
        VaultInhibitorItem.setModifierRolls(inhibitor, modifiers);
        event.setOutput(inhibitor);
        event.setCost(1);
        event.setMaterialCost(1);
    }

    @SubscribeEvent
    public static void onApplyPainiteStar(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() == ModItems.PAINITE_STAR) {
            final ItemStack output = event.getLeft().copy();
            if (!VaultCrystalItem.getData(output).canBeModified()) {
                return;
            }
            VaultCrystalItem.setRandomSeed(output);
            event.setOutput(output);
            event.setCost(2);
            event.setMaterialCost(1);
        }
    }

    @SubscribeEvent
    public static void onApplyInhibitor(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof VaultInhibitorItem) {
            final ItemStack output = event.getLeft().copy();
            final CrystalData data = VaultCrystalItem.getData(output);
            if (!data.canModifyWithCrafting()) {
                return;
            }
            final List<CrystalData.Modifier> crystalModifiers = data.getModifiers();
            final List<String> inhibitorModifiers = VaultInhibitorItem.getCrystalCombinationModifiers(event.getRight(), event.getLeft());
            if (crystalModifiers.isEmpty() || inhibitorModifiers == null || inhibitorModifiers.isEmpty()) {
                return;
            }
            inhibitorModifiers.forEach(modifier -> data.removeCatalystModifier(modifier, true, CrystalData.Modifier.Operation.ADD));
            VaultCrystalItem.markAttemptExhaust(output);
            VaultCrystalItem.setRandomSeed(output);
            event.setOutput(output);
            event.setCost(inhibitorModifiers.size() * 8);
            event.setMaterialCost(1);
        }
    }

    @SubscribeEvent
    public static void onApplyEchoGemToCrystal(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() == ModItems.ECHO_GEM) {
            final ItemStack output = event.getLeft().copy();
            final CrystalData data = VaultCrystalItem.getData(output);
            if (data.getEchoData().getEchoCount() == 0) {
                data.addEchoGems(1);
                data.setModifiable(false);
                event.setMaterialCost(1);
            } else {
                VaultCrystalItem.markAttemptApplyEcho(output, event.getRight().getCount());
                event.setMaterialCost(event.getRight().getCount());
            }
            event.setCost(1);
            event.setOutput(output);
        }
    }

    @SubscribeEvent
    public static void onApplyEchoCrystal(final AnvilUpdateEvent event) {
        if (!(event.getLeft().getItem() instanceof VaultCrystalItem)) {
            return;
        }
        if (!(event.getRight().getItem() instanceof VaultCrystalItem)) {
            return;
        }
        final ItemStack output = event.getLeft().copy();
        if (output.getOrCreateTag().getBoolean("Cloned")) {
            return;
        }
        final CrystalData crystalData = VaultCrystalItem.getData(output);
        if (crystalData.getEchoData().getEchoCount() != 0) {
            return;
        }
        if (!crystalData.canBeModified()) {
            return;
        }
        final ItemStack echoCrystal = event.getRight().copy();
        final CrystalData echoCrystalData = VaultCrystalItem.getData(echoCrystal);
        if (echoCrystalData.getEchoData().getEchoCount() <= 0) {
            return;
        }
        final boolean success = MathUtilities.randomFloat(0.0f, 1.0f) < echoCrystalData.getEchoData().getCloneSuccessRate();
        VaultCrystalItem.markForClone(output, success);
        event.setCost(1);
        event.setMaterialCost(1);
        event.setOutput(output);
    }

    @SubscribeEvent
    public static void onRepairDeny(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultPaxelItem && event.getRight().getItem() instanceof VaultPaxelItem) {
            event.setCanceled(true);
        }
        if (event.getLeft().getItem() instanceof VaultMagnetItem && event.getRight().getItem() instanceof VaultMagnetItem) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onApplySoulFlame(final AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() == ModItems.SOUL_FLAME) {
            final ItemStack output = event.getLeft().copy();
            final CrystalData data = VaultCrystalItem.getData(output);
            if (data.getType() == CrystalData.Type.FINAL_LOBBY) {
                return;
            }
            if (!data.getModifiers().isEmpty()) {
                return;
            }
            if (!data.canAddModifier("Afterlife", CrystalData.Modifier.Operation.ADD)) {
                return;
            }
            if (data.addCatalystModifier("Afterlife", false, CrystalData.Modifier.Operation.ADD)) {
                event.setOutput(output);
                event.setMaterialCost(1);
                event.setCost(10);
            }
        }
    }

    @SubscribeEvent
    public static void onApplyPog(final AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.OMEGA_POG) {
            return;
        }
        final ResourceLocation name = event.getLeft().getItem().getRegistryName();
        if (name.equals(ModBlocks.VAULT_ARTIFACT.getRegistryName())) {
            event.setOutput(new ItemStack(ModItems.UNIDENTIFIED_ARTIFACT));
            event.setMaterialCost(1);
            event.setCost(1);
        }
    }

    @SubscribeEvent
    public static void onApplyMending(final AnvilUpdateEvent event) {
        final ItemStack out = event.getOutput();
        if (!(out.getItem() instanceof ShieldItem)) {
            return;
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, out) > 0) {
            event.setCanceled(true);
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.THORNS, out) > 0) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRepairMagnet(final AnvilUpdateEvent event) {
        if (!(event.getLeft().getItem() instanceof VaultMagnetItem)) {
            return;
        }
        if (event.getRight().getItem() != ModItems.MAGNETITE) {
            return;
        }
        if (event.getLeft().getDamageValue() == 0 || event.getLeft().getOrCreateTag().getInt("TotalRepairs") >= 30) {
            event.setCanceled(true);
            return;
        }
        final ItemStack magnet = event.getLeft();
        final ItemStack magnetite = event.getRight();
        final ItemStack output = magnet.copy();
        final CompoundNBT nbt = output.getOrCreateTag();
        if (!nbt.contains("TotalRepairs")) {
            nbt.putInt("TotalRepairs", 0);
            output.setTag(nbt);
        }
        final int damage = magnet.getDamageValue();
        final int repairAmount = (int) Math.ceil(magnet.getMaxDamage() * 0.1);
        final int newDamage = Math.max(0, damage - magnetite.getCount() * repairAmount);
        final int materialCost = (int) Math.ceil((damage - newDamage) / (double) repairAmount);
        event.setMaterialCost(materialCost);
        event.setCost(materialCost);
        nbt.putInt("TotalRepairs", (int) Math.ceil(materialCost + nbt.getInt("TotalRepairs")));
        output.setTag(nbt);
        output.setDamageValue(newDamage);
        event.setOutput(output);
    }
}
