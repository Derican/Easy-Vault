package iskallia.vault.item.paxel.enhancement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public abstract class PaxelEnhancement implements INBTSerializable<CompoundNBT> {
    private static final Map<UUID, Integer> PLAYER_HELD_SLOT;
    private static final Map<UUID, ItemStack> PLAYER_HELD_STACK;
    protected ResourceLocation resourceLocation;

    @SubscribeEvent
    public static void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        final UUID playerUUID = player.getUUID();
        final int currentHeldSlotIndex = player.inventory.selected;
        final ItemStack currentStack = (ItemStack) player.inventory.items.get(currentHeldSlotIndex);
        PaxelEnhancement.PLAYER_HELD_SLOT.put(playerUUID, currentHeldSlotIndex);
        PaxelEnhancement.PLAYER_HELD_STACK.put(playerUUID, currentStack);
        final PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(currentStack);
        if (enhancement != null) {
            enhancement.onEnhancementActivated(player, currentStack);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent event) {
        final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        final UUID playerUUID = player.getUUID();
        final int currentHeldSlotIndex = player.inventory.selected;
        final ItemStack currentStack = (ItemStack) player.inventory.items.get(currentHeldSlotIndex);
        PaxelEnhancement.PLAYER_HELD_SLOT.remove(playerUUID);
        PaxelEnhancement.PLAYER_HELD_STACK.remove(playerUUID);
        final PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(currentStack);
        if (enhancement != null) {
            enhancement.onEnhancementDeactivated(player, currentStack);
        }
    }

    @SubscribeEvent
    public static void onInventoryTick(final TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) {
            return;
        }
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        final UUID playerUUID = player.getUUID();
        final int currentHeldSlotIndex = player.inventory.selected;
        final int previousHeldSlotIndex = PaxelEnhancement.PLAYER_HELD_SLOT.computeIfAbsent(playerUUID, uuid -> currentHeldSlotIndex);
        final ItemStack currentStack = (ItemStack) player.inventory.items.get(currentHeldSlotIndex);
        final PaxelEnhancement currentEnhancement = PaxelEnhancements.getEnhancement(currentStack);
        final ItemStack prevStack = PaxelEnhancement.PLAYER_HELD_STACK.computeIfAbsent(playerUUID, uuid -> ((ItemStack) player.inventory.items.get(previousHeldSlotIndex)).copy());
        final PaxelEnhancement prevEnhancement = PaxelEnhancements.getEnhancement(prevStack);
        if (currentHeldSlotIndex != previousHeldSlotIndex || !ItemStack.matches(currentStack, prevStack)) {
            PaxelEnhancement.PLAYER_HELD_SLOT.put(playerUUID, currentHeldSlotIndex);
            PaxelEnhancement.PLAYER_HELD_STACK.put(playerUUID, currentStack.copy());
            if (prevEnhancement != null) {
                prevEnhancement.onEnhancementDeactivated(player, prevStack);
            }
            if (currentEnhancement != null) {
                currentEnhancement.onEnhancementActivated(player, currentStack);
            }
        }
        if (currentEnhancement != null) {
            currentEnhancement.heldTick(player, currentStack, currentHeldSlotIndex);
        }
    }

    public IFormattableTextComponent getName() {
        return (IFormattableTextComponent) new TranslationTextComponent(String.format("paxel_enhancement.%s.%s", this.resourceLocation.getNamespace(), this.resourceLocation.getPath()));
    }

    public IFormattableTextComponent getDescription() {
        return (IFormattableTextComponent) new TranslationTextComponent(String.format("paxel_enhancement.%s.%s.desc", this.resourceLocation.getNamespace(), this.resourceLocation.getPath()));
    }

    public abstract Color getColor();

    public void setResourceLocation(final ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public void heldTick(final ServerPlayerEntity player, final ItemStack paxelStack, final int slotIndex) {
    }

    public void onEnhancementActivated(final ServerPlayerEntity player, final ItemStack paxelStack) {
    }

    public void onEnhancementDeactivated(final ServerPlayerEntity player, final ItemStack paxelStack) {
    }

    public void inventoryTick(final ItemStack itemStack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", this.resourceLocation.toString());
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        this.resourceLocation = new ResourceLocation(nbt.getString("Id"));
    }

    static {
        PLAYER_HELD_SLOT = new HashMap<UUID, Integer>();
        PLAYER_HELD_STACK = new HashMap<UUID, ItemStack>();
    }
}
