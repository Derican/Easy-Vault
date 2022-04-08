package iskallia.vault.world.gen.structure;

import iskallia.vault.Vault;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.block.JigsawBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.jigsaw.EmptyJigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.*;
import java.util.function.Predicate;

public class JigsawPieceResolver {
    private static final Object templateLoadLock = new Object();
    private static final Random rand = new Random();

    private final JigsawPiece piece;

    private final BlockPos pos;
    private Rotation pieceRotation = Rotation.NONE;
    private Predicate<ResourceLocation> filter = key -> true;
    private final List<AxisAlignedBB> additionalStructureBoxes = new ArrayList<>();

    private JigsawPieceResolver(JigsawPiece piece, BlockPos pos) {
        this.piece = piece;
        this.pos = pos;
    }

    public static JigsawPieceResolver newResolver(JigsawPiece piece, BlockPos pos) {
        return new JigsawPieceResolver(piece, pos);
    }

    public JigsawPieceResolver withRotation(Rotation rotation) {
        this.pieceRotation = rotation;
        return this;
    }

    public JigsawPieceResolver andJigsawFilter(Predicate<ResourceLocation> filter) {
        this.filter = filter.and(filter);
        return this;
    }

    public JigsawPieceResolver addStructureBox(AxisAlignedBB boundingBox) {
        this.additionalStructureBoxes.add(boundingBox);
        return this;
    }


    public List<AbstractVillagePiece> resolveJigsawPieces(TemplateManager templateManager, Registry<JigsawPattern> jigsawPatternRegistry) {
        AbstractVillagePiece beginningPiece = new AbstractVillagePiece(templateManager, this.piece, this.pos, this.piece.getGroundLevelDelta(), this.pieceRotation, this.piece.getBoundingBox(templateManager, this.pos, this.pieceRotation));
        MutableBoundingBox pieceBox = beginningPiece.getBoundingBox();
        int centerY = this.pos.getY();
        int offset = pieceBox.y0 + this.piece.getGroundLevelDelta();
        beginningPiece.move(0, centerY - offset, 0);

        VoxelShape generationShape = VoxelShapes.create(AxisAlignedBB.of(pieceBox).inflate(15.0D));
        for (AxisAlignedBB additionalBoxes : this.additionalStructureBoxes) {
            generationShape = VoxelShapes.join(generationShape, VoxelShapes.create(additionalBoxes), IBooleanFunction.ONLY_FIRST);
        }
        MutableObject<VoxelShape> generationBoxRef = new MutableObject(generationShape);

        List<AbstractVillagePiece> resolvedPieces = new ArrayList<>();
        resolvedPieces.add(beginningPiece);

        List<Entry> generationEntries = new ArrayList<>();
        generationEntries.add(new Entry(beginningPiece, generationBoxRef));
        while (!generationEntries.isEmpty()) {
            Entry generationEntry = generationEntries.remove(0);
            calculatePieces(resolvedPieces, generationEntries, generationEntry.villagePiece, generationEntry.generationBox, templateManager, jigsawPatternRegistry);
        }

        return resolvedPieces;
    }


    private void calculatePieces(List<AbstractVillagePiece> resolvedPieces, List<Entry> generationEntries, AbstractVillagePiece piece, MutableObject<VoxelShape> generationBox, TemplateManager templateMgr, Registry<JigsawPattern> jigsawPatternRegistry) {
        List<Template.BlockInfo> thisPieceBlocks;
        JigsawPiece jigsawpiece = piece.getElement();
        BlockPos pos = piece.getPosition();
        Rotation rotation = piece.getRotation();
        MutableBoundingBox pieceBox = piece.getBoundingBox();
        MutableObject<VoxelShape> thisPieceGenerationBox = new MutableObject();
        int minY = pieceBox.y0;


        synchronized (templateLoadLock) {
            thisPieceBlocks = jigsawpiece.getShuffledJigsawBlocks(templateMgr, pos, rotation, rand);
        }


        label71:
        for (Template.BlockInfo blockInfo : thisPieceBlocks) {
            MutableObject<VoxelShape> nextGenerationBox;
            Direction connectingDirection = JigsawBlock.getFrontFacing(blockInfo.state);
            BlockPos jigsawConnectorPos = blockInfo.pos;
            BlockPos expectedConnectionPos = jigsawConnectorPos.relative(connectingDirection);

            int jigsawYPos = jigsawConnectorPos.getY() - minY;

            ResourceLocation connectorPool = new ResourceLocation(blockInfo.nbt.getString("pool"));
            Optional<JigsawPattern> mainJigsawPattern = jigsawPatternRegistry.getOptional(connectorPool);


            if (!mainJigsawPattern.isPresent() || (((JigsawPattern) mainJigsawPattern.get()).size() == 0 && !Objects.equals(connectorPool, JigsawPatternRegistry.EMPTY.location()))) {
                Vault.LOGGER.warn("Empty or none existent pool: {}", connectorPool);

                continue;
            }
            ResourceLocation fallbackConnectorPool = ((JigsawPattern) mainJigsawPattern.get()).getFallback();
            Optional<JigsawPattern> fallbackJigsawPattern = jigsawPatternRegistry.getOptional(fallbackConnectorPool);
            if (!fallbackJigsawPattern.isPresent() || (((JigsawPattern) fallbackJigsawPattern.get()).size() == 0 && !Objects.equals(fallbackConnectorPool, JigsawPatternRegistry.EMPTY.location()))) {
                Vault.LOGGER.warn("Empty or none existent fallback pool: {}", fallbackConnectorPool);

                continue;
            }

            if (pieceBox.isInside((Vector3i) expectedConnectionPos)) {
                nextGenerationBox = thisPieceGenerationBox;
                if (thisPieceGenerationBox.getValue() == null) {
                    thisPieceGenerationBox.setValue(VoxelShapes.create(AxisAlignedBB.of(pieceBox)));
                }
            } else {
                nextGenerationBox = generationBox;
            }

            WeightedList<JigsawPiece> weightedPieces = new WeightedList();
            if (this.filter.test(connectorPool)) {
                ((JigsawPattern) mainJigsawPattern.get()).rawTemplates.forEach(weightedPiece -> weightedPieces.add(weightedPiece.getFirst(), ((Integer) weightedPiece.getSecond()).intValue()));
            }


            if (this.filter.test(fallbackConnectorPool)) {
                ((JigsawPattern) fallbackJigsawPattern.get()).rawTemplates.forEach(weightedPiece -> weightedPieces.add(weightedPiece.getFirst(), ((Integer) weightedPiece.getSecond()).intValue()));
            }


            while (!weightedPieces.isEmpty()) {
                JigsawPiece nextPiece = (JigsawPiece) weightedPieces.removeRandom(rand);
                if (nextPiece == null || nextPiece == EmptyJigsawPiece.INSTANCE) {
                    break;
                }

                for (Rotation nextPieceRotation : Rotation.getShuffled(rand)) {
                    List<Template.BlockInfo> nextPieceBlocks;
                    synchronized (templateLoadLock) {
                        nextPieceBlocks = nextPiece.getShuffledJigsawBlocks(templateMgr, BlockPos.ZERO, nextPieceRotation, rand);
                    }

                    for (Template.BlockInfo nextPieceBlockInfo : nextPieceBlocks) {
                        if (!JigsawBlock.canAttach(blockInfo, nextPieceBlockInfo)) {
                            continue;
                        }

                        BlockPos nextPiecePos = nextPieceBlockInfo.pos;


                        BlockPos pieceDiff = new BlockPos(expectedConnectionPos.getX() - nextPiecePos.getX(), expectedConnectionPos.getY() - nextPiecePos.getY(), expectedConnectionPos.getZ() - nextPiecePos.getZ());
                        MutableBoundingBox nextPieceBox = nextPiece.getBoundingBox(templateMgr, pieceDiff, nextPieceRotation);
                        boolean isNextPieceRigid = (nextPiece.getProjection() == JigsawPattern.PlacementBehaviour.RIGID);

                        int nextY = nextPiecePos.getY();
                        int l1 = jigsawYPos - nextY + JigsawBlock.getFrontFacing(nextPieceBlockInfo.state).getStepY();

                        if (VaultPiece.shouldIgnoreCollision(nextPiece) || !VoxelShapes.joinIsNotEmpty((VoxelShape) nextGenerationBox.getValue(), VoxelShapes.create(AxisAlignedBB.of(nextPieceBox).deflate(0.25D)), IBooleanFunction.ONLY_SECOND)) {
                            nextGenerationBox.setValue(VoxelShapes.joinUnoptimized((VoxelShape) nextGenerationBox.getValue(), VoxelShapes.create(AxisAlignedBB.of(nextPieceBox)), IBooleanFunction.ONLY_FIRST));


                            if (isNextPieceRigid) {
                                int i = piece.getGroundLevelDelta() - l1;
                                continue label71;
                            }
                            int l2 = nextPiece.getGroundLevelDelta();


                            AbstractVillagePiece nextPieceVillagePiece = new AbstractVillagePiece(templateMgr, nextPiece, pieceDiff, l2, nextPieceRotation, nextPieceBox);
                            resolvedPieces.add(nextPieceVillagePiece);
                            generationEntries.add(new Entry(nextPieceVillagePiece, nextGenerationBox));
                        }
                    }
                }
            }
        }
    }


    static final class Entry {
        private final AbstractVillagePiece villagePiece;
        private final MutableObject<VoxelShape> generationBox;

        private Entry(AbstractVillagePiece piece, MutableObject<VoxelShape> generationBox) {
            this.villagePiece = piece;
            this.generationBox = generationBox;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\structure\JigsawPieceResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */