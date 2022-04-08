package iskallia.vault.world.gen.structure;

import iskallia.vault.util.data.WeightedList;
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
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class JigsawGenerator implements VaultJigsawGenerator {
    private final MutableBoundingBox box;
    private List<StructurePiece> pieceList = new ArrayList<>();
    private final BlockPos startPos;
    private final int depth;

    public JigsawGenerator(MutableBoundingBox box, BlockPos pos, int depth) {
        this.box = box;
        this.startPos = pos;
        this.depth = depth;
    }


    public BlockPos getStartPos() {
        return this.startPos;
    }


    public MutableBoundingBox getStructureBox() {
        return this.box;
    }


    public int getSize() {
        return this.depth;
    }


    public List<StructurePiece> getGeneratedPieces() {
        return this.pieceList;
    }

    public void setPieceList(List<StructurePiece> pieceList) {
        this.pieceList = pieceList;
    }

    public static Builder builder(MutableBoundingBox box, BlockPos pos) {
        return new Builder(box, pos);
    }

    public static class Builder {
        private final MutableBoundingBox box;
        private final BlockPos startPos;
        private int depth = -1;

        protected Builder(MutableBoundingBox box, BlockPos startPos) {
            this.box = box;
            this.startPos = startPos;
        }

        public Builder setDepth(int depth) {
            this.depth = depth;
            return this;
        }

        public JigsawGenerator build() {
            return new JigsawGenerator(this.box, this.startPos, this.depth);
        }
    }


    public void generate(DynamicRegistries registries, VillageConfig config, JigsawManager.IPieceFactory pieceFactory, ChunkGenerator gen, TemplateManager manager, List<StructurePiece> pieceList, Random random, boolean flag1, boolean flag2) {
        int centerY;
        Structure.bootstrap();
        MutableRegistry<JigsawPattern> registry = registries.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Rotation rotation = Rotation.getRandom(random);
        JigsawPattern pattern = config.startPool().get();
        JigsawPiece startJigsaw = pattern.getRandomTemplate(random);
        AbstractVillagePiece startPiece = pieceFactory.create(manager, startJigsaw, getStartPos(), startJigsaw
                .getGroundLevelDelta(), rotation, startJigsaw.getBoundingBox(manager, getStartPos(), rotation));
        MutableBoundingBox startBox = startPiece.getBoundingBox();

        int centerX = (startBox.x1 + startBox.x0) / 2;
        int centerZ = (startBox.z1 + startBox.z0) / 2;


        if (flag2) {
            centerY = getStartPos().getY() + gen.getFirstFreeHeight(centerX, centerZ, Heightmap.Type.WORLD_SURFACE_WG);
        } else {
            centerY = getStartPos().getY();
        }

        int offset = startBox.y0 + startPiece.getGroundLevelDelta();
        startPiece.move(0, centerY - offset, 0);
        pieceList.add(startPiece);

        int depth = (getSize() == -1) ? config.maxDepth() : getSize();

        if (depth > 0) {


            AxisAlignedBB boundingBox = new AxisAlignedBB((getStructureBox()).x0, (getStructureBox()).y0, (getStructureBox()).z0, (getStructureBox()).x1, (getStructureBox()).y1, (getStructureBox()).z1);


            MutableObject<VoxelShape> mutableBox = new MutableObject(VoxelShapes.join(VoxelShapes.create(boundingBox),
                    VoxelShapes.create(AxisAlignedBB.of(startBox)), IBooleanFunction.ONLY_FIRST));

            Assembler assembler = new Assembler((Registry) registry, depth, pieceFactory, gen, manager, pieceList, random);
            assembler.availablePieces.addLast(new Entry(startPiece, mutableBox, (getStructureBox()).y1, 0));

            while (!assembler.availablePieces.isEmpty()) {
                Entry entry = assembler.availablePieces.removeFirst();
                assembler.generate(entry.villagePiece, entry.free, entry.boundsTop, entry.depth, flag1);
            }
        }

        this.pieceList = pieceList;
    }

    static final class Assembler {
        private final Registry<JigsawPattern> registry;
        private final int maxDepth;
        private final JigsawManager.IPieceFactory pieceFactory;
        private final ChunkGenerator chunkGenerator;
        private final TemplateManager templateManager;
        private final List<? super AbstractVillagePiece> structurePieces;
        private final Random rand;
        private final Deque<JigsawGenerator.Entry> availablePieces = Queues.newArrayDeque();


        private Assembler(Registry<JigsawPattern> registry, int maxDepth, JigsawManager.IPieceFactory pieceFactory, ChunkGenerator chunkGenerator, TemplateManager templateManager, List<? super AbstractVillagePiece> structurePieces, Random rand) {
            this.registry = registry;
            this.maxDepth = maxDepth;
            this.pieceFactory = pieceFactory;
            this.chunkGenerator = chunkGenerator;
            this.templateManager = templateManager;
            this.structurePieces = structurePieces;
            this.rand = rand;
        }

        private void generate(AbstractVillagePiece piece, MutableObject<VoxelShape> shape, int p_236831_3_, int currentDepth, boolean p_236831_5_) {
            JigsawPiece jigsawpiece = piece.getElement();
            BlockPos blockpos = piece.getPosition();
            Rotation rotation = piece.getRotation();
            JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour = jigsawpiece.getProjection();
            boolean flag = (jigsawpattern$placementbehaviour == JigsawPattern.PlacementBehaviour.RIGID);
            MutableObject<VoxelShape> mutableobject = new MutableObject();
            MutableBoundingBox mutableboundingbox = piece.getBoundingBox();
            int i = mutableboundingbox.y0;


            for (Template.BlockInfo template$blockinfo : jigsawpiece.getShuffledJigsawBlocks(this.templateManager, blockpos, rotation, this.rand)) {
                Direction direction = JigsawBlock.getFrontFacing(template$blockinfo.state);
                BlockPos blockpos1 = template$blockinfo.pos;
                BlockPos blockpos2 = blockpos1.relative(direction);
                int j = blockpos1.getY() - i;
                int k = -1;
                ResourceLocation resourcelocation = new ResourceLocation(template$blockinfo.nbt.getString("pool"));
                Optional<JigsawPattern> mainJigsawPattern = this.registry.getOptional(resourcelocation);
                if (mainJigsawPattern.isPresent() && (((JigsawPattern) mainJigsawPattern.get()).size() != 0 || Objects.equals(resourcelocation, JigsawPatternRegistry.EMPTY.location()))) {
                    ResourceLocation resourcelocation1 = ((JigsawPattern) mainJigsawPattern.get()).getFallback();
                    Optional<JigsawPattern> fallbackJigsawPattern = this.registry.getOptional(resourcelocation1);
                    if (fallbackJigsawPattern.isPresent() && (((JigsawPattern) fallbackJigsawPattern.get()).size() != 0 || Objects.equals(resourcelocation1, JigsawPatternRegistry.EMPTY.location()))) {
                        MutableObject<VoxelShape> mutableobject1;
                        int l;
                        boolean flag1 = mutableboundingbox.isInside((Vector3i) blockpos2);


                        if (flag1) {
                            mutableobject1 = mutableobject;
                            l = i;
                            if (mutableobject.getValue() == null) {
                                mutableobject.setValue(VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox)));
                            }
                        } else {
                            mutableobject1 = shape;
                            l = p_236831_3_;
                        }

                        WeightedList<JigsawPiece> weightedPieces = new WeightedList();

                        if (currentDepth != this.maxDepth) {
                            ((JigsawPattern) mainJigsawPattern.get()).rawTemplates.forEach(weightedPiece -> weightedPieces.add(weightedPiece.getFirst(), ((Integer) weightedPiece.getSecond()).intValue()));


                            ((JigsawPattern) fallbackJigsawPattern.get()).rawTemplates.forEach(weightedPiece -> weightedPieces.add(weightedPiece.getFirst(), ((Integer) weightedPiece.getSecond()).intValue()));
                        } else {

                            ((JigsawPattern) fallbackJigsawPattern.get()).rawTemplates.forEach(weightedPiece -> weightedPieces.add(weightedPiece.getFirst(), ((Integer) weightedPiece.getSecond()).intValue()));
                        }


                        while (!weightedPieces.isEmpty()) {
                            JigsawPiece jigsawpiece1 = (JigsawPiece) weightedPieces.removeRandom(this.rand);
                            if (jigsawpiece1 == null || jigsawpiece1 == EmptyJigsawPiece.INSTANCE) {
                                break;
                            }

                            for (Rotation rotation1 : Rotation.getShuffled(this.rand)) {
                                int i1;
                                List<Template.BlockInfo> list1 = jigsawpiece1.getShuffledJigsawBlocks(this.templateManager, BlockPos.ZERO, rotation1, this.rand);
                                MutableBoundingBox mutableboundingbox1 = jigsawpiece1.getBoundingBox(this.templateManager, BlockPos.ZERO, rotation1);

                                if (p_236831_5_ && mutableboundingbox1.getYSpan() <= 16) {


                                    i1 = list1.stream().mapToInt(p_242841_2_ -> {
                                        if (!mutableboundingbox1.isInside((Vector3i) p_242841_2_.pos.relative(JigsawBlock.getFrontFacing(p_242841_2_.state))))
                                            return 0;
                                        ResourceLocation resourcelocation2 = new ResourceLocation(p_242841_2_.nbt.getString("pool"));
                                        Optional<JigsawPattern> optional2 = this.registry.getOptional(resourcelocation2);
                                        Optional<JigsawPattern> optional3 = optional2.flatMap(());
                                        int k3 = ((Integer) optional2.<Integer>map(()).orElse(Integer.valueOf(0))).intValue();
                                        int l3 = ((Integer) optional3.<Integer>map(()).orElse(Integer.valueOf(0))).intValue();
                                        return Math.max(k3, l3);
                                    }).max().orElse(0);
                                } else {
                                    i1 = 0;
                                }

                                for (Template.BlockInfo template$blockinfo1 : list1) {
                                    if (JigsawBlock.canAttach(template$blockinfo, template$blockinfo1)) {
                                        int i2;
                                        BlockPos blockpos3 = template$blockinfo1.pos;
                                        BlockPos blockpos4 = new BlockPos(blockpos2.getX() - blockpos3.getX(), blockpos2.getY() - blockpos3.getY(), blockpos2.getZ() - blockpos3.getZ());
                                        MutableBoundingBox mutableboundingbox2 = jigsawpiece1.getBoundingBox(this.templateManager, blockpos4, rotation1);
                                        int j1 = mutableboundingbox2.y0;
                                        JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour1 = jigsawpiece1.getProjection();
                                        boolean flag2 = (jigsawpattern$placementbehaviour1 == JigsawPattern.PlacementBehaviour.RIGID);
                                        int k1 = blockpos3.getY();
                                        int l1 = j - k1 + JigsawBlock.getFrontFacing(template$blockinfo.state).getStepY();

                                        if (flag && flag2) {
                                            i2 = i + l1;
                                        } else {
                                            if (k == -1) {
                                                k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
                                            }

                                            i2 = k - k1;
                                        }

                                        int j2 = i2 - j1;
                                        MutableBoundingBox mutableboundingbox3 = mutableboundingbox2.moved(0, j2, 0);
                                        BlockPos blockpos5 = blockpos4.offset(0, j2, 0);
                                        if (i1 > 0) {
                                            int k2 = Math.max(i1 + 1, mutableboundingbox3.y1 - mutableboundingbox3.y0);
                                            mutableboundingbox3.y1 = mutableboundingbox3.y0 + k2;
                                        }

                                        if (!VoxelShapes.joinIsNotEmpty((VoxelShape) mutableobject1.getValue(), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox3).deflate(0.25D)), IBooleanFunction.ONLY_SECOND)) {
                                            int l2, i3;
                                            mutableobject1.setValue(VoxelShapes.joinUnoptimized((VoxelShape) mutableobject1.getValue(), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox3)), IBooleanFunction.ONLY_FIRST));
                                            int j3 = piece.getGroundLevelDelta();

                                            if (flag2) {
                                                l2 = j3 - l1;
                                            } else {
                                                l2 = jigsawpiece1.getGroundLevelDelta();
                                            }

                                            AbstractVillagePiece abstractvillagepiece = this.pieceFactory.create(this.templateManager, jigsawpiece1, blockpos5, l2, rotation1, mutableboundingbox3);

                                            if (flag) {
                                                i3 = i + j;
                                            } else if (flag2) {
                                                i3 = i2 + k1;
                                            } else {
                                                if (k == -1) {
                                                    k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
                                                }

                                                i3 = k + l1 / 2;
                                            }

                                            piece.addJunction(new JigsawJunction(blockpos2.getX(), i3 - j + j3, blockpos2.getZ(), l1, jigsawpattern$placementbehaviour1));
                                            abstractvillagepiece.addJunction(new JigsawJunction(blockpos1.getX(), i3 - k1 + l2, blockpos1.getZ(), -l1, jigsawpattern$placementbehaviour));

                                            if ((abstractvillagepiece.getBoundingBox()).y0 > 0 && (abstractvillagepiece.getBoundingBox()).y1 < 256) {
                                                this.structurePieces.add(abstractvillagepiece);

                                                if (currentDepth + 1 <= this.maxDepth) {
                                                    this.availablePieces.addLast(new JigsawGenerator.Entry(abstractvillagepiece, mutableobject1, l, currentDepth + 1));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        continue;
                    }
                    Vault.LOGGER.warn("Empty or none existent fallback pool: {}", resourcelocation1);
                    continue;
                }
                Vault.LOGGER.warn("Empty or none existent pool: {}", resourcelocation);
            }
        }
    }


    static final class Entry {
        private final AbstractVillagePiece villagePiece;
        private final MutableObject<VoxelShape> free;
        private final int boundsTop;
        private final int depth;

        private Entry(AbstractVillagePiece p_i232042_1_, MutableObject<VoxelShape> p_i232042_2_, int p_i232042_3_, int p_i232042_4_) {
            this.villagePiece = p_i232042_1_;
            this.free = p_i232042_2_;
            this.boundsTop = p_i232042_3_;
            this.depth = p_i232042_4_;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\structure\JigsawGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */