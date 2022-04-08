package iskallia.vault.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import iskallia.vault.world.data.GlobalDifficultyData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class DifficultyButton
        extends Button {
    private final String text;
    private final String key;
    GlobalDifficultyData.Difficulty difficulty = GlobalDifficultyData.Difficulty.STANDARD;

    public DifficultyButton(String text, String key, int x, int y, int width, int height, ITextComponent label, Button.IPressable onPress) {
        super(x, y, width, height, label, onPress);
        this.text = text;
        this.key = key;
    }

    public void getNextOption() {
        this.difficulty = this.difficulty.getNext();
        setMessage((ITextComponent) new StringTextComponent(this.text + ": " + this.difficulty.toString()));
    }

    public String getKey() {
        return this.key.replace(" ", "");
    }

    public GlobalDifficultyData.Difficulty getCurrentOption() {
        return this.difficulty;
    }


    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
        List<StringTextComponent> tooltips = new ArrayList<>();
        tooltips.add(new StringTextComponent(this.text));
        GuiUtils.drawHoveringText(matrixStack, tooltips, mouseX, mouseY, this.width + mouseX, this.height + mouseY, -1, (Minecraft.getInstance()).font);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\widget\DifficultyButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */