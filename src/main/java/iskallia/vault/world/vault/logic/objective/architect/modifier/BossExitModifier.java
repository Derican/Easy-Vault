// 
// Decompiled by Procyon v0.6.0
// 

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

public class BossExitModifier extends VoteModifier
{
    @Expose
    private final float portalChance;
    
    public BossExitModifier(final String name, final String description, final int voteLockDurationChangeSeconds, final float portalChance) {
        super(name, description, voteLockDurationChangeSeconds);
        this.portalChance = portalChance;
    }
    
    public boolean generatePortal(final VaultRaid vault) {
        return vault.getProperties().getBase(VaultRaid.IDENTIFIER).map(id -> {
            final Random r = new Random(id.getMostSignificantBits() ^ id.getLeastSignificantBits());
            return r.nextFloat() < this.portalChance;
        }).orElse(false);
    }
    
    @Nullable
    @Override
    public JigsawPiece getSpecialRoom(final ArchitectObjective objective, final VaultRaid vault) {
        if (this.generatePortal(vault)) {
            return VaultJigsawHelper.getArchitectRoom();
        }
        return super.getSpecialRoom(objective, vault);
    }
    
    @Nullable
    @Override
    public VaultPieceProcessor getPostProcessor(final ArchitectObjective objective, final VaultRaid vault) {
        if (this.generatePortal(vault)) {
            return new ExitPortalPieceProcessor(objective);
        }
        return new BossSpawnPieceProcessor(objective);
    }
    
    @Override
    public void onApply(final ArchitectObjective objective, final VaultRaid vault, final ServerWorld world) {
        super.onApply(objective, vault, world);
        objective.setVotingLocked();
    }
}
