package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.attribute.IntegerAttribute;
import iskallia.vault.block.entity.VaultChestTileEntity;
import iskallia.vault.entity.EternalEntity;
import iskallia.vault.entity.FighterEntity;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.network.message.FighterSizeMessage;
import iskallia.vault.skill.set.*;
import iskallia.vault.util.*;
import iskallia.vault.world.data.PlayerSetsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.VaultCharmData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvents {
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        if (target.level.isClientSide)
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        if (target instanceof FighterEntity)
            ModNetwork.CHANNEL.sendTo(new FighterSizeMessage(target, ((FighterEntity) target).sizeMultiplier), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        if (target instanceof EternalEntity)
            ModNetwork.CHANNEL.sendTo(new FighterSizeMessage(target, ((EternalEntity) target).sizeMultiplier), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        PlayerEntity attacker = event.getPlayer();
        if (attacker.level.isClientSide()) {
            return;
        }

        int level = PlayerVaultStatsData.get((ServerWorld) attacker.level).getVaultStats(attacker).getVaultLevel();
        ItemStack stack = attacker.getMainHandItem();

        if (ModAttributes.MIN_VAULT_LEVEL.exists(stack) && level < ((Integer) ((IntegerAttribute) ModAttributes.MIN_VAULT_LEVEL.get(stack).get()).getValue(stack)).intValue()) {
            event.setCanceled(true);

            return;
        }
        if (event.getTarget() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) event.getTarget();
            EntityHelper.getNearby((IWorld) attacker.level, (Vector3i) attacker.blockPosition(), 9.0F, EternalEntity.class)
                    .forEach(eternal -> eternal.setTarget(target));
        }
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingHurtEvent event) {
        Entity target = event.getEntity();
        if (!(target instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) target;
        if (player.getLevel().dimension() != Vault.VAULT_KEY)
            return;
        VaultRaid active = VaultRaidData.get(player.getLevel()).getActiveFor(player);
        if (active != null && active.isFinished()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick2(TickEvent.PlayerTickEvent event) {
        if (event.player.hasEffect(Effects.FIRE_RESISTANCE)) {
            event.player.clearFire();
        }

        if (event.player.getCommandSenderWorld().isClientSide() || !(event.player instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getType().equals(EquipmentSlotType.Group.ARMOR)) {


                ItemStack stack = player.getItemBySlot(slot);
                int level = PlayerVaultStatsData.get((ServerWorld) event.player.level).getVaultStats((PlayerEntity) player).getVaultLevel();

                if (ModAttributes.MIN_VAULT_LEVEL.exists(stack) && level < ((Integer) ((IntegerAttribute) ModAttributes.MIN_VAULT_LEVEL
                        .get(stack).get()).getValue(stack)).intValue()) {
                    player.drop(stack.copy(), false, false);
                    stack.setCount(0);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onApplyPlayerSets(TickEvent.PlayerTickEvent event) {
        if (event.player.getCommandSenderWorld().isClientSide() || !(event.player instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        SetTree sets = PlayerSetsData.get(player.getLevel()).getSets((PlayerEntity) player);


        if (PlayerSet.isActive(VaultGear.Set.DRAGON, (LivingEntity) player) &&
                !PlayerDamageHelper.getMultiplier(player, DragonSet.MULTIPLIER_ID).isPresent()) {
            float multiplier = 1.0F;

            for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
                if (!(node.getSet() instanceof DragonSet))
                    continue;
                DragonSet set = (DragonSet) node.getSet();
                multiplier += set.getDamageMultiplier();
            }

            PlayerDamageHelper.applyMultiplier(DragonSet.MULTIPLIER_ID, (ServerPlayerEntity) event.player, multiplier, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY, false);
        } else if (!PlayerSet.isActive(VaultGear.Set.DRAGON, (LivingEntity) player)) {
            PlayerDamageHelper.removeMultiplier(player, DragonSet.MULTIPLIER_ID);
        }


        if (PlayerSet.isActive(VaultGear.Set.DREAM, (LivingEntity) player) &&
                !PlayerDamageHelper.getMultiplier(player, DreamSet.MULTIPLIER_ID).isPresent()) {
            float multiplier = 1.0F;

            for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
                if (node.getSet() instanceof DreamSet) {
                    DreamSet set = (DreamSet) node.getSet();
                    multiplier += set.getIncreasedDamage();
                }
            }

            PlayerDamageHelper.applyMultiplier(DreamSet.MULTIPLIER_ID, (ServerPlayerEntity) event.player, multiplier, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY, false);
        } else if (!PlayerSet.isActive(VaultGear.Set.DREAM, (LivingEntity) player)) {
            PlayerDamageHelper.removeMultiplier(player, DreamSet.MULTIPLIER_ID);
        }


        if (PlayerSet.isActive(VaultGear.Set.DRYAD, (LivingEntity) player)) {
            float health = 0.0F;

            for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
                if (!(node.getSet() instanceof DryadSet))
                    continue;
                DryadSet set = (DryadSet) node.getSet();
                health += set.getExtraHealth();
            }


            player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(new AttributeModifier(DryadSet.HEALTH_MODIFIER_ID, "Dryad Bonus Health", health, AttributeModifier.Operation.ADDITION));
        } else {

            player.getAttribute(Attributes.MAX_HEALTH).removeModifier(DryadSet.HEALTH_MODIFIER_ID);
        }


        if (PlayerSet.isActive(VaultGear.Set.BLOOD, (LivingEntity) player) &&
                !PlayerDamageHelper.getMultiplier(player, BloodSet.MULTIPLIER_ID).isPresent()) {
            float multiplier = 0.0F;

            for (SetNode<?> node : (Iterable<SetNode<?>>) sets.getNodes()) {
                if (!(node.getSet() instanceof BloodSet))
                    continue;
                BloodSet set = (BloodSet) node.getSet();
                multiplier += set.getDamageMultiplier();
            }

            PlayerDamageHelper.applyMultiplier(BloodSet.MULTIPLIER_ID, (ServerPlayerEntity) event.player, multiplier, PlayerDamageHelper.Operation.ADDITIVE_MULTIPLY);
        } else if (!PlayerSet.isActive(VaultGear.Set.BLOOD, (LivingEntity) player)) {
            PlayerDamageHelper.removeMultiplier(player, BloodSet.MULTIPLIER_ID);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getWorld().isClientSide() || !(event.getWorld() instanceof ServerWorld)) {
            return;
        }

        TileEntity tile = event.getWorld().getBlockEntity(event.getPos());
        if (tile instanceof LockableLootTileEntity) {
            if (tile instanceof VaultChestTileEntity) {
                ((VaultChestTileEntity) tile).generateChestLoot(event.getPlayer(), true);
            } else {
                ((LockableLootTileEntity) tile).unpackLootTable(event.getPlayer());
            }
        }
        if (tile instanceof VaultChestTileEntity) {
            Random rand = event.getWorld().getRandom();
            VaultRarity rarity = ((VaultChestTileEntity) tile).getRarity();
            if (rarity == VaultRarity.EPIC) {
                event.getWorld().playSound(null, event.getPos(), ModSounds.VAULT_CHEST_EPIC_OPEN, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.1F + 0.9F);
            } else if (rarity == VaultRarity.OMEGA) {
                event.getWorld().playSound(null, event.getPos(), ModSounds.VAULT_CHEST_OMEGA_OPEN, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.1F + 0.9F);
            } else if (rarity == VaultRarity.RARE) {
                event.getWorld().playSound(null, event.getPos(), ModSounds.VAULT_CHEST_RARE_OPEN, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    @SubscribeEvent
    public static void onCraftVaultgear(PlayerEvent.ItemCraftedEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.getCommandSenderWorld().isClientSide()) {
            return;
        }
        ItemStack crafted = event.getCrafting();

        if (!(crafted.getItem() instanceof VaultGear)) {
            return;
        }


        if (crafted.getItem() instanceof iskallia.vault.item.gear.EtchingItem) {
            return;
        }

        int slot = SideOnlyFixer.getSlotFor(player.inventory, crafted);
        if (slot != -1) {
            ModAttributes.GEAR_CRAFTED_BY.create(player.inventory.getItem(slot), player.getName().getString());
        }
        ModAttributes.GEAR_CRAFTED_BY.create(crafted, player.getName().getString());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemTooltip(ItemTooltipEvent event) {
        if ((Minecraft.getInstance()).player != null && (Minecraft.getInstance()).player.isCreative()) {
            return;
        }
        for (int i = 0; i < event.getToolTip().size(); i++) {
            ITextComponent txt = event.getToolTip().get(i);
            if (txt.getString().contains("the_vault:idol")) {
                event.getToolTip().set(i, (new StringTextComponent("the_vault:idol")).setStyle(txt.getStyle()));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerEnterVault(PlayerEvent.PlayerChangedDimensionEvent event) {
        PlayerEntity player = event.getPlayer();

        if (event.getTo() == Vault.VAULT_KEY && player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            AdvancementHelper.grantCriterion(serverPlayer, Vault.id("main/root"), "entered_vault");
            AdvancementHelper.grantCriterion(serverPlayer, Vault.id("armors/root"), "entered_vault");
        }
    }

    @SubscribeEvent
    public static void onVaultCharmUse(EntityItemPickupEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ItemEntity itemEntity = event.getItem();
        ItemStack stack = itemEntity.getItem();
        if (stack.isEmpty()) {
            return;
        }
        ServerWorld world = player.getLevel();

        if (world.dimension() != Vault.VAULT_KEY)
            return;
        if (!hasVaultCharm(player.inventory))
            return;
        List<ResourceLocation> whitelist = VaultCharmData.get(world).getWhitelistedItems(player);
        if (whitelist.contains(stack.getItem().getRegistryName())) {
            event.setCanceled(true);
            itemEntity.remove();

            world.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (world.random

                    .nextFloat() - world.random.nextFloat()) * 1.4F + 2.0F);
        }
    }

    private static boolean hasVaultCharm(PlayerInventory inventory) {
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty() &&
                    stack.getItem() == ModItems.VAULT_CHARM) return true;
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\event\PlayerEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */