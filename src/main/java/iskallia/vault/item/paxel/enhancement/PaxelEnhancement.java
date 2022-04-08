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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public abstract class PaxelEnhancement
        implements INBTSerializable<CompoundNBT> {
    private static final Map<UUID, Integer> PLAYER_HELD_SLOT = new HashMap<>();
    private static final Map<UUID, ItemStack> PLAYER_HELD_STACK = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        UUID playerUUID = player.getUUID();

        int currentHeldSlotIndex = player.inventory.selected;
        ItemStack currentStack = (ItemStack) player.inventory.items.get(currentHeldSlotIndex);

        PLAYER_HELD_SLOT.put(playerUUID, Integer.valueOf(currentHeldSlotIndex));
        PLAYER_HELD_STACK.put(playerUUID, currentStack);

        PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(currentStack);
        if (enhancement != null) enhancement.onEnhancementActivated(player, currentStack);
    }

    protected ResourceLocation resourceLocation;

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        UUID playerUUID = player.getUUID();

        int currentHeldSlotIndex = player.inventory.selected;
        ItemStack currentStack = (ItemStack) player.inventory.items.get(currentHeldSlotIndex);

        PLAYER_HELD_SLOT.remove(playerUUID);
        PLAYER_HELD_STACK.remove(playerUUID);

        PaxelEnhancement enhancement = PaxelEnhancements.getEnhancement(currentStack);
        if (enhancement != null) enhancement.onEnhancementDeactivated(player, currentStack);
    }

    @SubscribeEvent
    public static void onInventoryTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient())
            return;
        if (event.phase != TickEvent.Phase.END)
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        UUID playerUUID = player.getUUID();

        int currentHeldSlotIndex = player.inventory.selected;
        int previousHeldSlotIndex = ((Integer) PLAYER_HELD_SLOT.computeIfAbsent(playerUUID, uuid -> Integer.valueOf(currentHeldSlotIndex))).intValue();

        ItemStack currentStack = (ItemStack) player.inventory.items.get(currentHeldSlotIndex);
        PaxelEnhancement currentEnhancement = PaxelEnhancements.getEnhancement(currentStack);

        ItemStack prevStack = PLAYER_HELD_STACK.computeIfAbsent(playerUUID, uuid -> ((ItemStack) player.inventory.items.get(previousHeldSlotIndex)).copy());
        PaxelEnhancement prevEnhancement = PaxelEnhancements.getEnhancement(prevStack);

        if (currentHeldSlotIndex != previousHeldSlotIndex || !ItemStack.matches(currentStack, prevStack)) {
            PLAYER_HELD_SLOT.put(playerUUID, Integer.valueOf(currentHeldSlotIndex));
            PLAYER_HELD_STACK.put(playerUUID, currentStack.copy());

            if (prevEnhancement != null) prevEnhancement.onEnhancementDeactivated(player, prevStack);
            if (currentEnhancement != null) currentEnhancement.onEnhancementActivated(player, currentStack);

        }
        if (currentEnhancement != null) {
            currentEnhancement.heldTick(player, currentStack, currentHeldSlotIndex);
        }
    }


    public IFormattableTextComponent getName() {
        return (IFormattableTextComponent) new TranslationTextComponent(String.format("paxel_enhancement.%s.%s", new Object[]{this.resourceLocation
                .getNamespace(), this.resourceLocation
                .getPath()}));
    }

    public IFormattableTextComponent getDescription() {
        return (IFormattableTextComponent) new TranslationTextComponent(String.format("paxel_enhancement.%s.%s.desc", new Object[]{this.resourceLocation
                .getNamespace(), this.resourceLocation
                .getPath()}));
    }


    public void setResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }


    public void heldTick(ServerPlayerEntity player, ItemStack paxelStack, int slotIndex) {
    }

    public void onEnhancementActivated(ServerPlayerEntity player, ItemStack paxelStack) {
    }

    public void onEnhancementDeactivated(ServerPlayerEntity player, ItemStack paxelStack) {
    }

    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Id", this.resourceLocation.toString());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.resourceLocation = new ResourceLocation(nbt.getString("Id"));
    }

    public abstract Color getColor();
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\paxel\enhancement\PaxelEnhancement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */