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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnvilEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onVaultAnvil(AnvilUpdateEvent event) {
        World world = event.getPlayer().getCommandSenderWorld();

        Item repairItem = event.getRight().getItem();
        if (repairItem == ModItems.MAGNETITE || repairItem == ModItems.REPAIR_CORE || repairItem == ModItems.REPAIR_CORE_T2) {
            return;
        }


        if (world.dimension() == Vault.VAULT_KEY) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onCombineVaultGear(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof iskallia.vault.item.gear.VaultGear && event.getRight().getItem() instanceof iskallia.vault.item.gear.VaultGear) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack equipment = event.getLeft();
        ItemStack enchantedBook = event.getRight();

        if (equipment.getItem() == Items.ENCHANTED_BOOK)
            return;
        if (enchantedBook.getItem() != Items.ENCHANTED_BOOK)
            return;
        ItemStack upgradedEquipment = equipment.copy();

        Map<Enchantment, Integer> equipmentEnchantments = OverlevelEnchantHelper.getEnchantments(equipment);
        Map<Enchantment, Integer> bookEnchantments = OverlevelEnchantHelper.getEnchantments(enchantedBook);
        int overlevels = OverlevelEnchantHelper.getOverlevels(enchantedBook);

        if (overlevels == -1)
            return;
        Map<Enchantment, Integer> enchantmentsToApply = new HashMap<>(equipmentEnchantments);

        for (Enchantment bookEnchantment : bookEnchantments.keySet()) {
            if (!equipmentEnchantments.containsKey(bookEnchantment))
                continue;
            int currentLevel = ((Integer) equipmentEnchantments.getOrDefault(bookEnchantment, Integer.valueOf(0))).intValue();
            int bookLevel = ((Integer) bookEnchantments.get(bookEnchantment)).intValue();
            int nextLevel = (currentLevel == bookLevel) ? (currentLevel + 1) : Math.max(currentLevel, bookLevel);
            enchantmentsToApply.put(bookEnchantment, Integer.valueOf(nextLevel));
        }

        EnchantmentHelper.setEnchantments(enchantmentsToApply, upgradedEquipment);

        if (upgradedEquipment.equals(equipment, true)) {
            event.setCanceled(true);
        } else {
            EnchantedBookEntry bookTier = ModConfigs.OVERLEVEL_ENCHANT.getTier(overlevels);
            event.setOutput(upgradedEquipment);
            event.setCost((bookTier == null) ? 1 : bookTier.getLevelNeeded());
        }
    }

    @SubscribeEvent
    public static void onApplyPaxelCharm(AnvilUpdateEvent event) {
        ItemStack paxelStack = event.getLeft();
        ItemStack charmStack = event.getRight();

        if (paxelStack.getItem() != ModItems.VAULT_PAXEL)
            return;
        if (charmStack.getItem() != ModItems.PAXEL_CHARM)
            return;
        if (PaxelEnhancements.getEnhancement(paxelStack) != null) {
            return;
        }
        ItemStack enhancedStack = paxelStack.copy();
        PaxelEnhancements.markShouldEnhance(enhancedStack);

        event.setCost(5);
        event.setOutput(enhancedStack);
    }

    @SubscribeEvent
    public static void onApplySeal(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof ItemVaultCrystalSeal) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
            if (!data.getModifiers().isEmpty() || data.getSelectedObjective() != null) {
                return;
            }

            if (event.getRight().getItem() == ModItems.CRYSTAL_SEAL_ANCIENTS && data.getType() == CrystalData.Type.COOP) {
                return;
            }

            VaultRaid.init();
            ResourceLocation objectiveKey = ((ItemVaultCrystalSeal) event.getRight().getItem()).getObjectiveId();
            VaultObjective objective = VaultObjective.getObjective(objectiveKey);
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
    public static void onApplyCake(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() == Items.CAKE) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
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
    public static void onApplyRaffleSeal(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof ItemVaultRaffleSeal) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
            if (!data.getModifiers().isEmpty() || data.getSelectedObjective() != null) {
                return;
            }
            String playerName = ItemVaultRaffleSeal.getPlayerName(event.getRight());
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
    public static void onApplyCatalyst(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof VaultCatalystItem) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
            if (!data.canModifyWithCrafting()) {
                return;
            }

            List<String> modifiers = VaultCatalystItem.getCrystalCombinationModifiers(event.getRight(), event.getLeft());
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
    public static void onApplyRune(AnvilUpdateEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
            return;
        }
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof VaultRuneItem) {
            ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.getPlayer();
            int level = PlayerVaultStatsData.get(sPlayer.getLevel()).getVaultStats((PlayerEntity) sPlayer).getVaultLevel();
            int minLevel = ((Integer) ModConfigs.VAULT_RUNE.getMinimumLevel(event.getRight().getItem()).orElse(Integer.valueOf(0))).intValue();
            if (level < minLevel) {
                return;
            }

            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
            if (!data.canModifyWithCrafting()) {
                return;
            }
            VaultRuneItem runeItem = (VaultRuneItem) event.getRight().getItem();
            if (!data.canAddRoom(runeItem.getRoomName())) {
                return;
            }

            int amount = event.getRight().getCount();
            for (int i = 0; i < amount; i++) {
                data.addGuaranteedRoom(runeItem.getRoomName());
            }

            event.setOutput(output);
            event.setCost(amount * 4);
            event.setMaterialCost(amount);
        }
    }

    @SubscribeEvent
    public static void onCraftInhibitor(AnvilUpdateEvent event) {
        if (!(event.getLeft().getItem() instanceof VaultCatalystItem))
            return;
        if (event.getRight().getItem() != ModItems.PERFECT_ECHO_GEM)
            return;
        ItemStack catalyst = event.getLeft().copy();

        ItemStack inhibitor = new ItemStack((IItemProvider) ModItems.VAULT_INHIBITOR);
        List<ModifierRollResult> modifiers = VaultCatalystItem.getModifierRolls(catalyst).orElse(Collections.emptyList());
        VaultInhibitorItem.setModifierRolls(inhibitor, modifiers);

        event.setOutput(inhibitor);
        event.setCost(1);
        event.setMaterialCost(1);
    }

    @SubscribeEvent
    public static void onApplyPainiteStar(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() == ModItems.PAINITE_STAR) {
            ItemStack output = event.getLeft().copy();
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
    public static void onApplyInhibitor(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() instanceof VaultInhibitorItem) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
            if (!data.canModifyWithCrafting()) {
                return;
            }

            List<CrystalData.Modifier> crystalModifiers = data.getModifiers();
            List<String> inhibitorModifiers = VaultInhibitorItem.getCrystalCombinationModifiers(event.getRight(), event.getLeft());
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
    public static void onApplyEchoGemToCrystal(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() == ModItems.ECHO_GEM) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);

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
    public static void onApplyEchoCrystal(AnvilUpdateEvent event) {
        if (!(event.getLeft().getItem() instanceof VaultCrystalItem))
            return;
        if (!(event.getRight().getItem() instanceof VaultCrystalItem))
            return;
        ItemStack output = event.getLeft().copy();
        if (output.getOrCreateTag().getBoolean("Cloned")) {
            return;
        }
        CrystalData crystalData = VaultCrystalItem.getData(output);
        if (crystalData.getEchoData().getEchoCount() != 0)
            return;
        if (!crystalData.canBeModified())
            return;
        ItemStack echoCrystal = event.getRight().copy();
        CrystalData echoCrystalData = VaultCrystalItem.getData(echoCrystal);
        if (echoCrystalData.getEchoData().getEchoCount() <= 0)
            return;
        boolean success = (MathUtilities.randomFloat(0.0F, 1.0F) < echoCrystalData.getEchoData().getCloneSuccessRate());
        VaultCrystalItem.markForClone(output, success);

        event.setCost(1);
        event.setMaterialCost(1);
        event.setOutput(output);
    }


    @SubscribeEvent
    public static void onRepairDeny(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof iskallia.vault.item.paxel.VaultPaxelItem && event.getRight().getItem() instanceof iskallia.vault.item.paxel.VaultPaxelItem) {
            event.setCanceled(true);
        }
        if (event.getLeft().getItem() instanceof iskallia.vault.item.VaultMagnetItem && event.getRight().getItem() instanceof iskallia.vault.item.VaultMagnetItem) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void onApplySoulFlame(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof VaultCrystalItem && event.getRight().getItem() == ModItems.SOUL_FLAME) {
            ItemStack output = event.getLeft().copy();
            CrystalData data = VaultCrystalItem.getData(output);
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
    public static void onApplyPog(AnvilUpdateEvent event) {
        if (event.getRight().getItem() != ModItems.OMEGA_POG)
            return;
        ResourceLocation name = event.getLeft().getItem().getRegistryName();

        if (name.equals(ModBlocks.VAULT_ARTIFACT.getRegistryName())) {
            event.setOutput(new ItemStack((IItemProvider) ModItems.UNIDENTIFIED_ARTIFACT));
            event.setMaterialCost(1);
            event.setCost(1);
        }
    }

    @SubscribeEvent
    public static void onApplyMending(AnvilUpdateEvent event) {
        ItemStack out = event.getOutput();
        if (!(out.getItem() instanceof net.minecraft.item.ShieldItem)) {
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
    public static void onRepairMagnet(AnvilUpdateEvent event) {
        if (!(event.getLeft().getItem() instanceof iskallia.vault.item.VaultMagnetItem))
            return;
        if (event.getRight().getItem() != ModItems.MAGNETITE)
            return;
        if (event.getLeft().getDamageValue() == 0 || event.getLeft().getOrCreateTag().getInt("TotalRepairs") >= 30) {
            event.setCanceled(true);

            return;
        }
        ItemStack magnet = event.getLeft();
        ItemStack magnetite = event.getRight();
        ItemStack output = magnet.copy();

        CompoundNBT nbt = output.getOrCreateTag();
        if (!nbt.contains("TotalRepairs")) {
            nbt.putInt("TotalRepairs", 0);
            output.setTag(nbt);
        }

        int damage = magnet.getDamageValue();
        int repairAmount = (int) Math.ceil(magnet.getMaxDamage() * 0.1D);
        int newDamage = Math.max(0, damage - magnetite.getCount() * repairAmount);
        int materialCost = (int) Math.ceil((damage - newDamage) / repairAmount);
        event.setMaterialCost(materialCost);
        event.setCost(materialCost);

        nbt.putInt("TotalRepairs", (int) Math.ceil((materialCost + nbt.getInt("TotalRepairs"))));
        output.setTag(nbt);
        output.setDamageValue(newDamage);

        event.setOutput(output);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\event\AnvilEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */