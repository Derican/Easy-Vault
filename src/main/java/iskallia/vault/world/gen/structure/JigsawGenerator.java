// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.gen.structure;

import com.google.common.collect.Queues;
import iskallia.vault.Vault;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.gen.VaultJigsawGenerator;
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
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.jigsaw.*;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.*;

public class JigsawGenerator implements VaultJigsawGenerator
{
    private final MutableBoundingBox box;
    private final BlockPos startPos;
    private final int depth;
    private List<StructurePiece> pieceList;
    
    public JigsawGenerator(final MutableBoundingBox box, final BlockPos pos, final int depth) {
        this.pieceList = new ArrayList<StructurePiece>();
        this.box = box;
        this.startPos = pos;
        this.depth = depth;
    }
    
    @Override
    public BlockPos getStartPos() {
        return this.startPos;
    }
    
    @Override
    public MutableBoundingBox getStructureBox() {
        return this.box;
    }
    
    @Override
    public int getSize() {
        return this.depth;
    }
    
    @Override
    public List<StructurePiece> getGeneratedPieces() {
        return this.pieceList;
    }
    
    public void setPieceList(final List<StructurePiece> pieceList) {
        this.pieceList = pieceList;
    }
    
    public static Builder builder(final MutableBoundingBox box, final BlockPos pos) {
        return new Builder(box, pos);
    }
    
    @Override
    public void generate(final DynamicRegistries registries, final VillageConfig config, final JigsawManager.IPieceFactory pieceFactory, final ChunkGenerator gen, final TemplateManager manager, final List<StructurePiece> pieceList, final Random random, final boolean flag1, final boolean flag2) {
        Structure.bootstrap();
        final MutableRegistry<JigsawPattern> registry = (MutableRegistry<JigsawPattern>)registries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        final Rotation rotation = Rotation.getRandom(random);
        final JigsawPattern pattern = config.startPool().get();
        final JigsawPiece startJigsaw = pattern.getRandomTemplate(random);
        final AbstractVillagePiece startPiece = pieceFactory.create(manager, startJigsaw, this.getStartPos(), startJigsaw.getGroundLevelDelta(), rotation, startJigsaw.getBoundingBox(manager, this.getStartPos(), rotation));
        final MutableBoundingBox startBox = startPiece.getBoundingBox();
        final int centerX = (startBox.x1 + startBox.x0) / 2;
        final int centerZ = (startBox.z1 + startBox.z0) / 2;
        int centerY;
        if (flag2) {
            centerY = this.getStartPos().getY() + gen.getFirstFreeHeight(centerX, centerZ, Heightmap.Type.WORLD_SURFACE_WG);
        }
        else {
            centerY = this.getStartPos().getY();
        }
        final int offset = startBox.y0 + startPiece.getGroundLevelDelta();
        startPiece.move(0, centerY - offset, 0);
        pieceList.add((StructurePiece)startPiece);
        final int depth = (this.getSize() == -1) ? config.maxDepth() : this.getSize();
        if (depth > 0) {
            final AxisAlignedBB boundingBox = new AxisAlignedBB((double)this.getStructureBox().x0, (double)this.getStructureBox().y0, (double)this.getStructureBox().z0, (double)this.getStructureBox().x1, (double)this.getStructureBox().y1, (double)this.getStructureBox().z1);
            final MutableObject<VoxelShape> mutableBox = (MutableObject<VoxelShape>)new MutableObject(VoxelShapes.join(VoxelShapes.create(boundingBox), VoxelShapes.create(AxisAlignedBB.of(startBox)), IBooleanFunction.ONLY_FIRST));
            final Assembler assembler = new Assembler((Registry)registry, depth, pieceFactory, gen, manager, (List)pieceList, random);
            assembler.availablePieces.addLast(new Entry(startPiece, (MutableObject)mutableBox, this.getStructureBox().y1, 0));
            while (!assembler.availablePieces.isEmpty()) {
                final Entry entry = assembler.availablePieces.removeFirst();
                assembler.generate(entry.villagePiece, entry.free, entry.boundsTop, entry.depth, flag1);
            }
        }
        this.pieceList = pieceList;
    }
    
    public static class Builder
    {
        private final MutableBoundingBox box;
        private final BlockPos startPos;
        private int depth;
        
        protected Builder(final MutableBoundingBox box, final BlockPos startPos) {
            this.depth = -1;
            this.box = box;
            this.startPos = startPos;
        }
        
        public Builder setDepth(final int depth) {
            this.depth = depth;
            return this;
        }
        
        public JigsawGenerator build() {
            return new JigsawGenerator(this.box, this.startPos, this.depth);
        }
    }
    
    static final class Assembler
    {
        private final Registry<JigsawPattern> registry;
        private final int maxDepth;
        private final JigsawManager.IPieceFactory pieceFactory;
        private final ChunkGenerator chunkGenerator;
        private final TemplateManager templateManager;
        private final List<? super AbstractVillagePiece> structurePieces;
        private final Random rand;
        private final Deque<Entry> availablePieces;
        
        private Assembler(final Registry<JigsawPattern> registry, final int maxDepth, final JigsawManager.IPieceFactory pieceFactory, final ChunkGenerator chunkGenerator, final TemplateManager templateManager, final List<? super AbstractVillagePiece> structurePieces, final Random rand) {
            this.availablePieces = Queues.newArrayDeque();
            this.registry = registry;
            this.maxDepth = maxDepth;
            this.pieceFactory = pieceFactory;
            this.chunkGenerator = chunkGenerator;
            this.templateManager = templateManager;
            this.structurePieces = structurePieces;
            this.rand = rand;
        }
        
        private void generate(final AbstractVillagePiece piece, final MutableObject<VoxelShape> shape, final int p_236831_3_, final int currentDepth, final boolean p_236831_5_) {
            final JigsawPiece jigsawpiece = piece.getElement();
            final BlockPos blockpos = piece.getPosition();
            final Rotation rotation = piece.getRotation();
            final JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour = jigsawpiece.getProjection();
            final boolean flag = jigsawpattern$placementbehaviour == JigsawPattern.PlacementBehaviour.RIGID;
            final MutableObject<VoxelShape> mutableobject = (MutableObject<VoxelShape>)new MutableObject();
            final MutableBoundingBox mutableboundingbox = piece.getBoundingBox();
            final int i = mutableboundingbox.y0;
        Label_0086:
            while (true) {
                for (final Template.BlockInfo template$blockinfo : jigsawpiece.getShuffledJigsawBlocks(this.templateManager, blockpos, rotation, this.rand)) {
                    final Direction direction = JigsawBlock.getFrontFacing(template$blockinfo.state);
                    final BlockPos blockpos2 = template$blockinfo.pos;
                    final BlockPos blockpos3 = blockpos2.relative(direction);
                    final int j = blockpos2.getY() - i;
                    int k = -1;
                    final ResourceLocation resourcelocation = new ResourceLocation(template$blockinfo.nbt.getString("pool"));
                    final Optional<JigsawPattern> mainJigsawPattern = this.registry.getOptional(resourcelocation);
                    if (mainJigsawPattern.isPresent() && (mainJigsawPattern.get().size() != 0 || Objects.equals(resourcelocation, JigsawPatternRegistry.EMPTY.location()))) {
                        final ResourceLocation resourcelocation2 = mainJigsawPattern.get().getFallback();
                        final Optional<JigsawPattern> fallbackJigsawPattern = this.registry.getOptional(resourcelocation2);
                        if (fallbackJigsawPattern.isPresent() && (fallbackJigsawPattern.get().size() != 0 || Objects.equals(resourcelocation2, JigsawPatternRegistry.EMPTY.location()))) {
                            final boolean flag2 = mutableboundingbox.isInside((Vector3i)blockpos3);
                            MutableObject<VoxelShape> mutableobject2;
                            int l;
                            if (flag2) {
                                mutableobject2 = mutableobject;
                                l = i;
                                if (mutableobject.getValue() == null) {
                                    mutableobject.setValue(VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox)));
                                }
                            }
                            else {
                                mutableobject2 = shape;
                                l = p_236831_3_;
                            }
                            final WeightedList<JigsawPiece> weightedPieces = new WeightedList<JigsawPiece>();
                            if (currentDepth != this.maxDepth) {
                                mainJigsawPattern.get().rawTemplates.forEach(weightedPiece -> weightedPieces.add(weightedPiece.getFirst(), (int)weightedPiece.getSecond()));
                                fallbackJigsawPattern.get().rawTemplates.forEach(weightedPiece -> weightedPieces.add(weightedPiece.getFirst(), (int)weightedPiece.getSecond()));
                            }
                            else {
                                fallbackJigsawPattern.get().rawTemplates.forEach(weightedPiece -> weightedPieces.add(weightedPiece.getFirst(), (int)weightedPiece.getSecond()));
                            }
                            while (!weightedPieces.isEmpty()) {
                                final JigsawPiece jigsawpiece2 = weightedPieces.removeRandom(this.rand);
                                if (jigsawpiece2 == null) {
                                    break;
                                }
                                if (jigsawpiece2 == EmptyJigsawPiece.INSTANCE) {
                                    break;
                                }
                                for (final Rotation rotation2 : Rotation.getShuffled(this.rand)) {
                                    final List<Template.BlockInfo> list1 = jigsawpiece2.getShuffledJigsawBlocks(this.templateManager, BlockPos.ZERO, rotation2, this.rand);
                                    final MutableBoundingBox mutableboundingbox2 = jigsawpiece2.getBoundingBox(this.templateManager, BlockPos.ZERO, rotation2);
                                    int i2;
                                    if (p_236831_5_ && mutableboundingbox2.getYSpan() <= 16) {
                                        i2 = list1.stream().mapToInt(p_242841_2_ -> {
                                            if (!mutableboundingbox2.isInside((Vector3i)p_242841_2_.pos.relative(JigsawBlock.getFrontFacing(p_242841_2_.state)))) {
                                                return 0;
                                            }
                                            else {
                                                final ResourceLocation resourcelocation3 = new ResourceLocation(p_242841_2_.nbt.getString("pool"));
                                                final Optional<JigsawPattern> optional2 = this.registry.getOptional(resourcelocation3);
                                                final Optional<JigsawPattern> optional3 = optional2.flatMap(p_242843_1_ -> this.registry.getOptional(p_242843_1_.getFallback()));
                                                final int k4 = optional2.map(p_242842_1_ -> p_242842_1_.getMaxSize(this.templateManager)).orElse(0);
                                                final int l4 = optional3.map(p_242840_1_ -> p_242840_1_.getMaxSize(this.templateManager)).orElse(0);
                                                return Math.max(k4, l4);
                                            }
                                        }).max().orElse(0);
                                    }
                                    else {
                                        i2 = 0;
                                    }
                                    for (final Template.BlockInfo template$blockinfo2 : list1) {
                                        if (JigsawBlock.canAttach(template$blockinfo, template$blockinfo2)) {
                                            final BlockPos blockpos4 = template$blockinfo2.pos;
                                            final BlockPos blockpos5 = new BlockPos(blockpos3.getX() - blockpos4.getX(), blockpos3.getY() - blockpos4.getY(), blockpos3.getZ() - blockpos4.getZ());
                                            final MutableBoundingBox mutableboundingbox3 = jigsawpiece2.getBoundingBox(this.templateManager, blockpos5, rotation2);
                                            final int j2 = mutableboundingbox3.y0;
                                            final JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour2 = jigsawpiece2.getProjection();
                                            final boolean flag3 = jigsawpattern$placementbehaviour2 == JigsawPattern.PlacementBehaviour.RIGID;
                                            final int k2 = blockpos4.getY();
                                            final int l2 = j - k2 + JigsawBlock.getFrontFacing(template$blockinfo.state).getStepY();
                                            int i3;
                                            if (flag && flag3) {
                                                i3 = i + l2;
                                            }
                                            else {
                                                if (k == -1) {
                                                    k = this.chunkGenerator.getFirstFreeHeight(blockpos2.getX(), blockpos2.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
                                                }
                                                i3 = k - k2;
                                            }
                                            final int j3 = i3 - j2;
                                            final MutableBoundingBox mutableboundingbox4 = mutableboundingbox3.moved(0, j3, 0);
                                            final BlockPos blockpos6 = blockpos5.offset(0, j3, 0);
                                            if (i2 > 0) {
                                                final int k3 = Math.max(i2 + 1, mutableboundingbox4.y1 - mutableboundingbox4.y0);
                                                mutableboundingbox4.y1 = mutableboundingbox4.y0 + k3;
                                            }
                                            if (VoxelShapes.joinIsNotEmpty((VoxelShape)mutableobject2.getValue(), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox4).deflate(0.25)), IBooleanFunction.ONLY_SECOND)) {
                                                continue;
                                            }
                                            mutableobject2.setValue(VoxelShapes.joinUnoptimized((VoxelShape)mutableobject2.getValue(), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox4)), IBooleanFunction.ONLY_FIRST));
                                            final int j4 = piece.getGroundLevelDelta();
                                            int l3;
                                            if (flag3) {
                                                l3 = j4 - l2;
                                            }
                                            else {
                                                l3 = jigsawpiece2.getGroundLevelDelta();
                                            }
                                            final AbstractVillagePiece abstractvillagepiece = this.pieceFactory.create(this.templateManager, jigsawpiece2, blockpos6, l3, rotation2, mutableboundingbox4);
                                            int i4;
                                            if (flag) {
                                                i4 = i + j;
                                            }
                                            else if (flag3) {
                                                i4 = i3 + k2;
                                            }
                                            else {
                                                if (k == -1) {
                                                    k = this.chunkGenerator.getFirstFreeHeight(blockpos2.getX(), blockpos2.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
                                                }
                                                i4 = k + l2 / 2;
                                            }
                                            piece.addJunction(new JigsawJunction(blockpos3.getX(), i4 - j + j4, blockpos3.getZ(), l2, jigsawpattern$placementbehaviour2));
                                            abstractvillagepiece.addJunction(new JigsawJunction(blockpos2.getX(), i4 - k2 + l3, blockpos2.getZ(), -l2, jigsawpattern$placementbehaviour));
                                            if (abstractvillagepiece.getBoundingBox().y0 <= 0 || abstractvillagepiece.getBoundingBox().y1 >= 256) {
                                                continue Label_0086;
                                            }
                                            this.structurePieces.add(abstractvillagepiece);
                                            if (currentDepth + 1 <= this.maxDepth) {
                                                this.availablePieces.addLast(new Entry(abstractvillagepiece, (MutableObject)mutableobject2, l, currentDepth + 1));
                                                continue Label_0086;
                                            }
                                            continue Label_0086;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            Vault.LOGGER.warn("Empty or none existent fallback pool: {}", resourcelocation2);
                        }
                    }
                    else {
                        Vault.LOGGER.warn("Empty or none existent pool: {}", resourcelocation);
                    }
                }
                break;
            }
        }
    }
    
    static final class Entry
    {
        private final AbstractVillagePiece villagePiece;
        private final MutableObject<VoxelShape> free;
        private final int boundsTop;
        private final int depth;
        
        private Entry(final AbstractVillagePiece p_i232042_1_, final MutableObject<VoxelShape> p_i232042_2_, final int p_i232042_3_, final int p_i232042_4_) {
            this.villagePiece = p_i232042_1_;
            this.free = p_i232042_2_;
            this.boundsTop = p_i232042_3_;
            this.depth = p_i232042_4_;
        }
    }
}
