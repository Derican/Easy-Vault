// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item;

import iskallia.vault.config.entry.MagnetEntry;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.VectorHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultMagnetItem extends Item
{
    private MagnetType type;
    private static final HashMap<UUID, UUID> pulledItems;
    
    public VaultMagnetItem(final ResourceLocation id, final MagnetType type) {
        super(new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(1));
        this.setRegistryName(id);
        this.type = type;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        if (worldIn == null) {
            return;
        }
        final int totalRepairs = stack.getOrCreateTag().getInt("TotalRepairs");
        tooltip.add((ITextComponent)new StringTextComponent(" "));
        tooltip.add((ITextComponent)new StringTextComponent("Enabled: " + (this.isEnabled(stack) ? (TextFormatting.GREEN + "true") : (TextFormatting.RED + "false"))));
        tooltip.add((ITextComponent)new StringTextComponent("Repairs Remaining: " + this.getColor(30 - totalRepairs) + Math.max(0, 30 - totalRepairs)));
        tooltip.add((ITextComponent)new StringTextComponent(" "));
        super.appendHoverText(stack, worldIn, (List)tooltip, flagIn);
    }
    
    private TextFormatting getColor(final int amount) {
        if (amount < 10) {
            return TextFormatting.RED;
        }
        if (amount < 20) {
            return TextFormatting.YELLOW;
        }
        return TextFormatting.GREEN;
    }
    
    public boolean isFoil(final ItemStack stack) {
        return this.isEnabled(stack);
    }
    
    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        this.setEnabled(stack, !this.isEnabled(stack), false);
        return (ActionResult<ItemStack>)new ActionResult(ActionResultType.SUCCESS, stack);
    }
    
    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        if (world.isClientSide) {
            return;
        }
        final CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.contains("Enabled")) {
            nbt.putBoolean("Enabled", false);
            stack.setTag(nbt);
        }
        if (entity instanceof PlayerEntity && this.isEnabled(stack)) {
            final PlayerEntity player = (PlayerEntity)entity;
            final MagnetType magnetType = ((VaultMagnetItem)stack.getItem()).getType();
            final MagnetEntry settings = ModConfigs.VAULT_UTILITIES.getMagnetSetting(magnetType);
            final boolean instant = settings.shouldPullInstantly();
            final boolean moveItems = settings.shouldPullItems();
            final boolean moveXp = settings.shouldPullExperience();
            final float speed = settings.getSpeed() / 20.0f;
            final float radius = settings.getRadius();
            if (moveItems) {
                final List<ItemEntity> items = world.getEntitiesOfClass((Class)ItemEntity.class, player.getBoundingBox().inflate((double)radius));
                for (final ItemEntity item : items) {
                    if (item.isAlive()) {
                        if (stack.getOrCreateTag().getBoolean("PreventRemoteMovement")) {
                            continue;
                        }
                        if (item.getTags().contains("PreventMagnetMovement")) {
                            continue;
                        }
                        if (!VaultMagnetItem.pulledItems.containsKey(item.getUUID())) {
                            final PlayerEntity closest = this.getClosestPlayerWithMagnet(item, radius);
                            VaultMagnetItem.pulledItems.put(item.getUUID(), (closest == null) ? player.getUUID() : closest.getUUID());
                        }
                        if (!VaultMagnetItem.pulledItems.get(item.getUUID()).equals(player.getUUID())) {
                            continue;
                        }
                        item.setNoPickUpDelay();
                        this.moveItemToPlayer(item, player, speed, instant);
                    }
                }
            }
            if (moveXp) {
                final List<ExperienceOrbEntity> orbs = world.getEntitiesOfClass((Class)ExperienceOrbEntity.class, player.getBoundingBox().inflate((double)radius));
                for (final ExperienceOrbEntity orb : orbs) {
                    this.moveXpToPlayer(orb, player, speed, instant);
                }
            }
        }
    }
    
    private void moveItemToPlayer(final ItemEntity item, final PlayerEntity player, final float speed, final boolean instant) {
        if (instant) {
            item.setPos(player.getX(), player.getY(), player.getZ());
        }
        else {
            final Vector3d target = VectorHelper.getVectorFromPos(player.blockPosition());
            final Vector3d current = VectorHelper.getVectorFromPos(item.blockPosition());
            final Vector3d velocity = VectorHelper.getMovementVelocity(current, target, speed);
            item.push(velocity.x, velocity.y, velocity.z);
            item.hurtMarked = true;
        }
    }
    
    private void moveXpToPlayer(final ExperienceOrbEntity orb, final PlayerEntity player, final float speed, final boolean instant) {
        if (instant) {
            orb.setPos(player.getX(), player.getY(), player.getZ());
        }
        else {
            final Vector3d target = VectorHelper.getVectorFromPos(player.blockPosition());
            final Vector3d current = VectorHelper.getVectorFromPos(orb.blockPosition());
            final Vector3d velocity = VectorHelper.getMovementVelocity(current, target, speed);
            orb.push(velocity.x, velocity.y, velocity.z);
            orb.hurtMarked = true;
        }
    }
    
    public <T extends LivingEntity> int damageItem(final ItemStack stack, final int amount, final T entity, final Consumer<T> onBroken) {
        if (stack.getDamageValue() + amount >= stack.getMaxDamage()) {
            this.setEnabled(stack, false, true);
            return 0;
        }
        return amount;
    }
    
    private void setEnabled(final ItemStack stack, final boolean enabled, final boolean force) {
        if (force) {
            this.setEnabled(stack, enabled);
        }
        else if (stack.getDamageValue() < stack.getMaxDamage() - 1) {
            this.setEnabled(stack, enabled);
        }
    }
    
    private void setEnabled(final ItemStack stack, final boolean enabled) {
        final CompoundNBT tag = stack.getOrCreateTag();
        tag.putBoolean("Enabled", enabled);
        stack.setTag(tag);
    }
    
    private boolean isEnabled(final ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Enabled");
    }
    
    public MagnetType getType() {
        return this.type;
    }
    
    public static boolean isMagnet(final ItemStack stack) {
        return stack.getItem() instanceof VaultMagnetItem;
    }
    
    public boolean showDurabilityBar(final ItemStack stack) {
        return stack.getDamageValue() > 0;
    }
    
    public double getDurabilityForDisplay(final ItemStack stack) {
        return stack.getDamageValue() / (double)this.getMaxDamage(stack);
    }
    
    public int getMaxDamage(final ItemStack stack) {
        if (ModConfigs.VAULT_UTILITIES != null) {
            final MagnetEntry setting = ModConfigs.VAULT_UTILITIES.getMagnetSetting(this.type);
            return setting.getMaxDurability();
        }
        return 0;
    }
    
    public boolean canBeDepleted() {
        return true;
    }
    
    public boolean isValidRepairItem(final ItemStack toRepair, final ItemStack repair) {
        return toRepair.getItem() instanceof VaultMagnetItem && repair.getItem() == ModItems.MAGNETITE;
    }
    
    public boolean isRepairable(final ItemStack stack) {
        return false;
    }
    
    public boolean isEnchantable(final ItemStack stack) {
        return false;
    }
    
    public boolean canApplyAtEnchantingTable(final ItemStack stack, final Enchantment enchantment) {
        return false;
    }
    
    public boolean isBookEnchantable(final ItemStack stack, final ItemStack book) {
        return false;
    }
    
    @SubscribeEvent
    public static void onItemPickup(final PlayerEvent.ItemPickupEvent event) {
        final PlayerEntity player = event.getPlayer();
        final PlayerInventory inventory = player.inventory;
        VaultMagnetItem.pulledItems.remove(event.getOriginalEntity().getUUID());
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            final ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                if (isMagnet(stack) && ((VaultMagnetItem)stack.getItem()).isEnabled(stack)) {
                    stack.hurtAndBreak(1, (LivingEntity)player, onBroken -> {});
                }
                else {
                    final LazyOptional<IItemHandler> itemHandler = (LazyOptional<IItemHandler>)stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                    itemHandler.ifPresent(h -> {
                        for (int j = 0; j < h.getSlots(); ++j) {
                            final ItemStack stackInHandler = h.getStackInSlot(j);
                            if (isMagnet(stackInHandler) && ((VaultMagnetItem)stackInHandler.getItem()).isEnabled(stackInHandler)) {
                                stackInHandler.hurtAndBreak(1, (LivingEntity)player, onBroken -> {});
                            }
                        }
                    });
                }
            }
        }
    }
    
    @Nullable
    private PlayerEntity getClosestPlayerWithMagnet(final ItemEntity item, final double radius) {
        final List<PlayerEntity> players = item.getCommandSenderWorld().getEntitiesOfClass((Class)PlayerEntity.class, item.getBoundingBox().inflate(radius));
        if (players.isEmpty()) {
            return null;
        }
        PlayerEntity closest = players.get(0);
        double distance = radius;
        for (final PlayerEntity player : players) {
            final double temp = player.distanceTo((Entity)item);
            if (temp < distance && this.hasEnabledMagnetInRange(player, radius)) {
                closest = player;
                distance = temp;
            }
        }
        return closest;
    }
    
    private boolean hasEnabledMagnetInRange(final PlayerEntity player, final double radius) {
        final PlayerInventory inventory = player.inventory;
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            final ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                if (isMagnet(stack)) {
                    final VaultMagnetItem magnet = (VaultMagnetItem)stack.getItem();
                    if (magnet.isEnabled(stack)) {
                        final MagnetEntry setting = ModConfigs.VAULT_UTILITIES.getMagnetSetting(magnet.getType());
                        if (setting.getRadius() >= radius) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    static {
        pulledItems = new HashMap<UUID, UUID>();
    }
    
    public enum MagnetType
    {
        WEAK, 
        STRONG, 
        OMEGA;
    }
}
