package iskallia.vault.client.gui.component;

import net.minecraft.item.ItemStack;

import java.awt.*;

public class StatueOptionSlot {
    private final int posX;
    private final int posY;
    private final int width;
    private final int height;
    private final ItemStack stack;

    public StatueOptionSlot(final int posX, final int posY, final int width, final int height, final ItemStack stack) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.stack = stack;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public boolean contains(final double mouseX, final double mouseY) {
        return new Rectangle(this.posX, this.posY, this.width, this.height).contains(mouseX, mouseY);
    }

    @Override
    public String toString() {
        return "StatueOptionSlot{posX=" + this.posX + ", posY=" + this.posY + ", stack=" + this.stack.getHoverName().getString() + '}';
    }
}
