package iskallia.vault.world.vault.logic.objective.architect.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.world.vault.logic.objective.architect.processor.VaultPieceProcessor;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class VoteModifier {
    @Expose
    private final String name;
    protected static final Random rand = new Random();
    @Expose
    private final String description;
    @Expose
    private final String color = String.valueOf(65535);


    public VoteModifier(String name, String description, int voteLockDurationChangeSeconds) {
        this.name = name;
        this.description = description;
        this.voteLockDurationChangeSeconds = voteLockDurationChangeSeconds;
    }

    @Expose
    private final int voteLockDurationChangeSeconds;

    public String getName() {
        return this.name;
    }

    public String getDescriptionText() {
        return this.description;
    }

    public int getVoteLockDurationChangeSeconds() {
        return this.voteLockDurationChangeSeconds;
    }

    public ITextComponent getDescription() {
        return (ITextComponent) (new StringTextComponent(getDescriptionText())).withStyle(Style.EMPTY.withColor(Color.fromRgb(Integer.parseInt(this.color))));
    }

    @Nullable
    public JigsawPiece getSpecialRoom(ArchitectObjective objective, VaultRaid vault) {
        return null;
    }

    @Nullable
    public VaultPieceProcessor getPostProcessor(ArchitectObjective objective, VaultRaid vault) {
        return null;
    }

    public void onApply(ArchitectObjective objective, VaultRaid vault, ServerWorld world) {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\modifier\VoteModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */