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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VaultMagnetItem extends Item {
    private static final HashMap<UUID, UUID> pulledItems = new HashMap<>();
    private MagnetType type;

    public VaultMagnetItem(ResourceLocation id, MagnetType type) {
        super((new Item.Properties()).tab(ModItems.VAULT_MOD_GROUP)
                .stacksTo(1));

        setRegistryName(id);
        this.type = type;
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (worldIn == null)
            return;
        int totalRepairs = stack.getOrCreateTag().getInt("TotalRepairs");
        tooltip.add(new StringTextComponent(" "));
        tooltip.add(new StringTextComponent("Enabled: " + (isEnabled(stack) ? (TextFormatting.GREEN + "true") : (TextFormatting.RED + "false"))));
        tooltip.add(new StringTextComponent("Repairs Remaining: " + getColor(30 - totalRepairs) + Math.max(0, 30 - totalRepairs)));
        tooltip.add(new StringTextComponent(" "));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    private TextFormatting getColor(int amount) {
        if (amount < 10) return TextFormatting.RED;
        if (amount < 20) return TextFormatting.YELLOW;
        return TextFormatting.GREEN;
    }


    public boolean isFoil(ItemStack stack) {
        return isEnabled(stack);
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        setEnabled(stack, !isEnabled(stack), false);

        return new ActionResult(ActionResultType.SUCCESS, stack);
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isClientSide)
            return;
        CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.contains("Enabled")) {
            nbt.putBoolean("Enabled", false);
            stack.setTag(nbt);
        }


        if (entity instanceof PlayerEntity && isEnabled(stack)) {
            PlayerEntity player = (PlayerEntity) entity;
            MagnetType magnetType = ((VaultMagnetItem) stack.getItem()).getType();

            MagnetEntry settings = ModConfigs.VAULT_UTILITIES.getMagnetSetting(magnetType);
            boolean instant = settings.shouldPullInstantly();
            boolean moveItems = settings.shouldPullItems();
            boolean moveXp = settings.shouldPullExperience();
            float speed = settings.getSpeed() / 20.0F;
            float radius = settings.getRadius();

            if (moveItems) {
                List<ItemEntity> items = world.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(radius));
                for (ItemEntity item : items) {
                    if (!item.isAlive() || stack.getOrCreateTag().getBoolean("PreventRemoteMovement") ||
                            item.getTags().contains("PreventMagnetMovement")) {
                        continue;
                    }
                    if (!pulledItems.containsKey(item.getUUID())) {
                        PlayerEntity closest = getClosestPlayerWithMagnet(item, radius);
                        pulledItems.put(item.getUUID(), (closest == null) ? player.getUUID() : closest.getUUID());
                    }


                    if (!((UUID) pulledItems.get(item.getUUID())).equals(player.getUUID()))
                        continue;
                    item.setNoPickUpDelay();
                    moveItemToPlayer(item, player, speed, instant);
                }
            }

            if (moveXp) {
                List<ExperienceOrbEntity> orbs = world.getEntitiesOfClass(ExperienceOrbEntity.class, player.getBoundingBox().inflate(radius));
                for (ExperienceOrbEntity orb : orbs) {
                    moveXpToPlayer(orb, player, speed, instant);
                }
            }
        }
    }

    private void moveItemToPlayer(ItemEntity item, PlayerEntity player, float speed, boolean instant) {
        if (instant) {
            item.setPos(player.getX(), player.getY(), player.getZ());
        } else {
            Vector3d target = VectorHelper.getVectorFromPos(player.blockPosition());
            Vector3d current = VectorHelper.getVectorFromPos(item.blockPosition());

            Vector3d velocity = VectorHelper.getMovementVelocity(current, target, speed);

            item.push(velocity.x, velocity.y, velocity.z);
            item.hurtMarked = true;
        }
    }

    private void moveXpToPlayer(ExperienceOrbEntity orb, PlayerEntity player, float speed, boolean instant) {
        if (instant) {
            orb.setPos(player.getX(), player.getY(), player.getZ());
        } else {
            Vector3d target = VectorHelper.getVectorFromPos(player.blockPosition());
            Vector3d current = VectorHelper.getVectorFromPos(orb.blockPosition());

            Vector3d velocity = VectorHelper.getMovementVelocity(current, target, speed);

            orb.push(velocity.x, velocity.y, velocity.z);
            orb.hurtMarked = true;
        }
    }


    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (stack.getDamageValue() + amount >= stack.getMaxDamage()) {
            setEnabled(stack, false, true);
            return 0;
        }
        return amount;
    }

    private void setEnabled(ItemStack stack, boolean enabled, boolean force) {
        if (force) {
            setEnabled(stack, enabled);
        } else if (stack.getDamageValue() < stack.getMaxDamage() - 1) {
            setEnabled(stack, enabled);
        }

    }


    private void setEnabled(ItemStack stack, boolean enabled) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putBoolean("Enabled", enabled);
        stack.setTag(tag);
    }

    private boolean isEnabled(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Enabled");
    }

    public MagnetType getType() {
        return this.type;
    }

    public static boolean isMagnet(ItemStack stack) {
        return stack.getItem() instanceof VaultMagnetItem;
    }


    public boolean showDurabilityBar(ItemStack stack) {
        return (stack.getDamageValue() > 0);
    }


    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getDamageValue() / getMaxDamage(stack);
    }


    public int getMaxDamage(ItemStack stack) {
        if (ModConfigs.VAULT_UTILITIES != null) {
            MagnetEntry setting = ModConfigs.VAULT_UTILITIES.getMagnetSetting(this.type);
            return setting.getMaxDurability();
        }
        return 0;
    }


    public boolean canBeDepleted() {
        return true;
    }


    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return (toRepair.getItem() instanceof VaultMagnetItem && repair.getItem() == ModItems.MAGNETITE);
    }


    public boolean isRepairable(ItemStack stack) {
        return false;
    }


    public boolean isEnchantable(ItemStack stack) {
        return false;
    }


    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }


    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public enum MagnetType {
        WEAK,
        STRONG,
        OMEGA;
    }

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        PlayerEntity player = event.getPlayer();
        PlayerInventory inventory = player.inventory;


        pulledItems.remove(event.getOriginalEntity().getUUID());

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {

                if (isMagnet(stack) && (
                        (VaultMagnetItem) stack.getItem()).isEnabled(stack)) {
                    stack.hurtAndBreak(1, (LivingEntity) player, onBroken -> {


                    });
                } else {
                    LazyOptional<IItemHandler> itemHandler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                    itemHandler.ifPresent(h -> {
                        for (int j = 0; j < h.getSlots(); j++) {
                            ItemStack stackInHandler = h.getStackInSlot(j);
                            if (isMagnet(stackInHandler) && ((VaultMagnetItem) stackInHandler.getItem()).isEnabled(stackInHandler)) {
                                stackInHandler.hurtAndBreak(1, (LivingEntity) player, ());
                            }
                        }
                    });
                }
            }
        }
    }


    @Nullable
    private PlayerEntity getClosestPlayerWithMagnet(ItemEntity item, double radius) {
        List<PlayerEntity> players = item.getCommandSenderWorld().getEntitiesOfClass(PlayerEntity.class, item.getBoundingBox().inflate(radius));
        if (players.isEmpty()) return null;
        PlayerEntity closest = players.get(0);
        double distance = radius;
        for (PlayerEntity player : players) {
            double temp = player.distanceTo((Entity) item);
            if (temp < distance &&
                    hasEnabledMagnetInRange(player, radius)) {
                closest = player;
                distance = temp;
            }
        }

        return closest;
    }

    private boolean hasEnabledMagnetInRange(PlayerEntity player, double radius) {
        PlayerInventory inventory = player.inventory;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {

                if (isMagnet(stack)) {
                    VaultMagnetItem magnet = (VaultMagnetItem) stack.getItem();
                    if (magnet.isEnabled(stack)) {
                        MagnetEntry setting = ModConfigs.VAULT_UTILITIES.getMagnetSetting(magnet.getType());
                        if (setting.getRadius() >= radius) return true;
                    }
                }
            }
        }
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\VaultMagnetItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */