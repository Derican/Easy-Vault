package iskallia.vault.world.vault.logic.objective.architect.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.world.vault.logic.objective.architect.processor.BossSpawnPieceProcessor;
import iskallia.vault.world.vault.logic.objective.architect.processor.ExitPortalPieceProcessor;
import iskallia.vault.world.vault.logic.objective.architect.processor.VaultPieceProcessor;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class BossExitModifier extends VoteModifier {
    @Expose
    private final float portalChance;

    public BossExitModifier(String name, String description, int voteLockDurationChangeSeconds, float portalChance) {
        super(name, description, voteLockDurationChangeSeconds);
        this.portalChance = portalChance;
    }

    public boolean generatePortal(VaultRaid vault) {
        return ((Boolean) vault.getProperties().getBase(VaultRaid.IDENTIFIER)
                .map(id -> {
                    Random r = new Random(id.getMostSignificantBits() ^ id.getLeastSignificantBits());
                    return Boolean.valueOf((r.nextFloat() < this.portalChance));
                }).orElse(Boolean.valueOf(false))).booleanValue();
    }


    @Nullable
    public JigsawPiece getSpecialRoom(ArchitectObjective objective, VaultRaid vault) {
        if (generatePortal(vault)) {
            return VaultJigsawHelper.getArchitectRoom();
        }
        return super.getSpecialRoom(objective, vault);
    }


    @Nullable
    public VaultPieceProcessor getPostProcessor(ArchitectObjective objective, VaultRaid vault) {
        if (generatePortal(vault)) {
            return (VaultPieceProcessor) new ExitPortalPieceProcessor(objective);
        }
        return (VaultPieceProcessor) new BossSpawnPieceProcessor(objective);
    }


    public void onApply(ArchitectObjective objective, VaultRaid vault, ServerWorld world) {
        super.onApply(objective, vault, world);
        objective.setVotingLocked();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\modifier\BossExitModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */