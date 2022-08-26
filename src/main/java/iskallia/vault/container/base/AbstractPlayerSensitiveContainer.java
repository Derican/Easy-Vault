package iskallia.vault.container.base;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Set;

public abstract class AbstractPlayerSensitiveContainer extends Container implements PlayerSensitiveContainer {
    private int dragMode;
    private int dragEvent;
    private final Set<Slot> dragSlots;

    protected AbstractPlayerSensitiveContainer(@Nullable final ContainerType<?> type, final int id) {
        super(type, id);
        this.dragMode = -1;
        this.dragSlots = Sets.newHashSet();
    }

    public void setDragMode(final int dragMode) {
        this.dragMode = dragMode;
    }

    public int getDragMode() {
        return this.dragMode;
    }

    public void setDragEvent(final int dragEvent) {
        this.dragEvent = dragEvent;
    }

    public int getDragEvent() {
        return this.dragEvent;
    }

    public Set<Slot> getDragSlots() {
        return this.dragSlots;
    }

    public void resetQuickCraft() {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }

    public ItemStack clicked(final int slotId, final int dragType, final ClickType clickType, final PlayerEntity player) {
        return this.playerSensitiveSlotClick(this, slotId, dragType, clickType, player);
    }
}
