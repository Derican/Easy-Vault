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

public abstract class AbstractPlayerSensitiveContainer
        extends Container
        implements PlayerSensitiveContainer {
    private int dragMode = -1;
    private int dragEvent;
    private final Set<Slot> dragSlots = Sets.newHashSet();

    protected AbstractPlayerSensitiveContainer(@Nullable ContainerType<?> type, int id) {
        super(type, id);
    }


    public void setDragMode(int dragMode) {
        this.dragMode = dragMode;
    }


    public int getDragMode() {
        return this.dragMode;
    }


    public void setDragEvent(int dragEvent) {
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


    public ItemStack clicked(int slotId, int dragType, ClickType clickType, PlayerEntity player) {
        return playerSensitiveSlotClick(this, slotId, dragType, clickType, player);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\base\AbstractPlayerSensitiveContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */