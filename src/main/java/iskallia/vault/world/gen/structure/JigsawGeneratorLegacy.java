package iskallia.vault.world.gen.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import iskallia.vault.Vault;
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
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.*;

public class JigsawGeneratorLegacy {
    public static void addPieces(final DynamicRegistries p_242837_0_, final VillageConfig p_242837_1_, final JigsawManager.IPieceFactory p_242837_2_, final ChunkGenerator p_242837_3_, final TemplateManager p_242837_4_, final BlockPos p_242837_5_, final List<? super AbstractVillagePiece> p_242837_6_, final Random p_242837_7_, final boolean p_242837_8_, final boolean p_242837_9_) {
        Structure.bootstrap();
        final MutableRegistry<JigsawPattern> mutableregistry = p_242837_0_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        final Rotation rotation = Rotation.getRandom(p_242837_7_);
        final JigsawPattern jigsawpattern = p_242837_1_.startPool().get();
        final JigsawPiece jigsawpiece = jigsawpattern.getRandomTemplate(p_242837_7_);
        final AbstractVillagePiece abstractvillagepiece = p_242837_2_.create(p_242837_4_, jigsawpiece, p_242837_5_, jigsawpiece.getGroundLevelDelta(), rotation, jigsawpiece.getBoundingBox(p_242837_4_, p_242837_5_, rotation));
        final MutableBoundingBox mutableboundingbox = abstractvillagepiece.getBoundingBox();
        final int i = (mutableboundingbox.x1 + mutableboundingbox.x0) / 2;
        final int j = (mutableboundingbox.z1 + mutableboundingbox.z0) / 2;
        int k;
        if (p_242837_9_) {
            k = p_242837_5_.getY() + p_242837_3_.getFirstFreeHeight(i, j, Heightmap.Type.WORLD_SURFACE_WG);
        } else {
            k = p_242837_5_.getY();
        }
        final int l = mutableboundingbox.y0 + abstractvillagepiece.getGroundLevelDelta();
        abstractvillagepiece.move(0, k - l, 0);
        p_242837_6_.add(abstractvillagepiece);
        if (p_242837_1_.maxDepth() > 0) {
            final int maxRange = 1073741823;
            final AxisAlignedBB axisalignedbb = new AxisAlignedBB(i - maxRange, k - maxRange, j - maxRange, i + maxRange + 1, k + maxRange + 1, j + maxRange + 1);
            final Assembler jigsawmanager$assembler = new Assembler(mutableregistry, p_242837_1_.maxDepth(), p_242837_2_, p_242837_3_, p_242837_4_, p_242837_6_, p_242837_7_);
            jigsawmanager$assembler.availablePieces.addLast(new Entry(abstractvillagepiece, new MutableObject(VoxelShapes.join(VoxelShapes.create(axisalignedbb), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox)), IBooleanFunction.ONLY_FIRST)), k + maxRange, 0));
            while (!jigsawmanager$assembler.availablePieces.isEmpty()) {
                final Entry jigsawmanager$entry = jigsawmanager$assembler.availablePieces.removeFirst();
                jigsawmanager$assembler.tryPlacingChildren(jigsawmanager$entry.villagePiece, jigsawmanager$entry.free, jigsawmanager$entry.boundsTop, jigsawmanager$entry.depth, p_242837_8_);
            }
        }
    }

    static final class Assembler {
        private final Registry<JigsawPattern> pools;
        private final int maxDepth;
        private final JigsawManager.IPieceFactory pieceFactory;
        private final ChunkGenerator chunkGenerator;
        private final TemplateManager templateManager;
        private final List<? super AbstractVillagePiece> structurePieces;
        private final Random rand;
        private final Deque<Entry> availablePieces;

        private Assembler(final Registry<JigsawPattern> p_i242005_1_, final int p_i242005_2_, final JigsawManager.IPieceFactory p_i242005_3_, final ChunkGenerator p_i242005_4_, final TemplateManager p_i242005_5_, final List<? super AbstractVillagePiece> p_i242005_6_, final Random p_i242005_7_) {
            this.availablePieces = Queues.newArrayDeque();
            this.pools = p_i242005_1_;
            this.maxDepth = p_i242005_2_;
            this.pieceFactory = p_i242005_3_;
            this.chunkGenerator = p_i242005_4_;
            this.templateManager = p_i242005_5_;
            this.structurePieces = p_i242005_6_;
            this.rand = p_i242005_7_;
        }

        private void tryPlacingChildren(final AbstractVillagePiece p_236831_1_, final MutableObject<VoxelShape> p_236831_2_, final int p_236831_3_, final int currentDepth, final boolean p_236831_5_) {
            final JigsawPiece jigsawpiece = p_236831_1_.getElement();
            final BlockPos blockpos = p_236831_1_.getPosition();
            final Rotation rotation = p_236831_1_.getRotation();
            final JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour = jigsawpiece.getProjection();
            final boolean flag = jigsawpattern$placementbehaviour == JigsawPattern.PlacementBehaviour.RIGID;
            final MutableObject<VoxelShape> mutableobject = (MutableObject<VoxelShape>) new MutableObject();
            final MutableBoundingBox mutableboundingbox = p_236831_1_.getBoundingBox();
            final int i = mutableboundingbox.y0;
            while (true) {
            Label_0086:
                for (final Template.BlockInfo template$blockinfo : jigsawpiece.getShuffledJigsawBlocks(this.templateManager, blockpos, rotation, this.rand)) {
                    final Direction direction = JigsawBlock.getFrontFacing(template$blockinfo.state);
                    final BlockPos blockpos2 = template$blockinfo.pos;
                    final BlockPos blockpos3 = blockpos2.relative(direction);
                    final int j = blockpos2.getY() - i;
                    int k = -1;
                    final ResourceLocation resourcelocation = new ResourceLocation(template$blockinfo.nbt.getString("pool"));
                    final Optional<JigsawPattern> mainJigsawPattern = this.pools.getOptional(resourcelocation);
                    if (mainJigsawPattern.isPresent() && (mainJigsawPattern.get().size() != 0 || Objects.equals(resourcelocation, JigsawPatternRegistry.EMPTY.location()))) {
                        final ResourceLocation resourcelocation2 = mainJigsawPattern.get().getFallback();
                        final Optional<JigsawPattern> fallbackJigsawPattern = this.pools.getOptional(resourcelocation2);
                        if (fallbackJigsawPattern.isPresent() && (fallbackJigsawPattern.get().size() != 0 || Objects.equals(resourcelocation2, JigsawPatternRegistry.EMPTY.location()))) {
                            final boolean flag2 = mutableboundingbox.isInside(blockpos3);
                            MutableObject<VoxelShape> mutableobject2;
                            int l;
                            if (flag2) {
                                mutableobject2 = mutableobject;
                                l = i;
                                if (mutableobject.getValue() == null) {
                                    mutableobject.setValue(VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox)));
                                }
                            } else {
                                mutableobject2 = p_236831_2_;
                                l = p_236831_3_;
                            }
                            final List<JigsawPiece> list = Lists.newArrayList();
                            if (currentDepth != this.maxDepth) {
                                list.addAll(mainJigsawPattern.get().getShuffledTemplates(this.rand));
                                list.addAll(fallbackJigsawPattern.get().getShuffledTemplates(this.rand));
                            } else {
                                list.addAll(fallbackJigsawPattern.get().getShuffledTemplates(this.rand));
                            }
                            for (final JigsawPiece jigsawpiece2 : list) {
                                if (jigsawpiece2 == EmptyJigsawPiece.INSTANCE) {
                                    break;
                                }
                                for (final Rotation rotation2 : Rotation.getShuffled(this.rand)) {
                                    final List<Template.BlockInfo> list2 = jigsawpiece2.getShuffledJigsawBlocks(this.templateManager, BlockPos.ZERO, rotation2, this.rand);
                                    final MutableBoundingBox mutableboundingbox2 = jigsawpiece2.getBoundingBox(this.templateManager, BlockPos.ZERO, rotation2);
                                    int i2;
                                    if (p_236831_5_ && mutableboundingbox2.getYSpan() <= 16) {
                                        i2 = list2.stream().mapToInt(p_242841_2_ -> {
                                            if (!mutableboundingbox2.isInside(p_242841_2_.pos.relative(JigsawBlock.getFrontFacing(p_242841_2_.state)))) {
                                                return 0;
                                            } else {
                                                final ResourceLocation resourcelocation3 = new ResourceLocation(p_242841_2_.nbt.getString("pool"));
                                                final Optional<JigsawPattern> optional2 = this.pools.getOptional(resourcelocation3);
                                                final Optional<JigsawPattern> optional3 = optional2.flatMap(p_242843_1_ -> this.pools.getOptional(p_242843_1_.getFallback()));
                                                final int k4 = optional2.map(p_242842_1_ -> p_242842_1_.getMaxSize(this.templateManager)).orElse(0);
                                                final int l4 = optional3.map(p_242840_1_ -> p_242840_1_.getMaxSize(this.templateManager)).orElse(0);
                                                return Math.max(k4, l4);
                                            }
                                        }).max().orElse(0);
                                    } else {
                                        i2 = 0;
                                    }
                                    for (final Template.BlockInfo template$blockinfo2 : list2) {
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
                                            } else {
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
                                            if (VoxelShapes.joinIsNotEmpty(mutableobject2.getValue(), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox4).deflate(0.25)), IBooleanFunction.ONLY_SECOND)) {
                                                continue;
                                            }
                                            mutableobject2.setValue(VoxelShapes.joinUnoptimized(mutableobject2.getValue(), VoxelShapes.create(AxisAlignedBB.of(mutableboundingbox4)), IBooleanFunction.ONLY_FIRST));
                                            final int j4 = p_236831_1_.getGroundLevelDelta();
                                            int l3;
                                            if (flag3) {
                                                l3 = j4 - l2;
                                            } else {
                                                l3 = jigsawpiece2.getGroundLevelDelta();
                                            }
                                            final AbstractVillagePiece abstractvillagepiece = this.pieceFactory.create(this.templateManager, jigsawpiece2, blockpos6, l3, rotation2, mutableboundingbox4);
                                            int i4;
                                            if (flag) {
                                                i4 = i + j;
                                            } else if (flag3) {
                                                i4 = i3 + k2;
                                            } else {
                                                if (k == -1) {
                                                    k = this.chunkGenerator.getFirstFreeHeight(blockpos2.getX(), blockpos2.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
                                                }
                                                i4 = k + l2 / 2;
                                            }
                                            p_236831_1_.addJunction(new JigsawJunction(blockpos3.getX(), i4 - j + j4, blockpos3.getZ(), l2, jigsawpattern$placementbehaviour2));
                                            abstractvillagepiece.addJunction(new JigsawJunction(blockpos2.getX(), i4 - k2 + l3, blockpos2.getZ(), -l2, jigsawpattern$placementbehaviour));
                                            if (abstractvillagepiece.getBoundingBox().y0 <= 0 || abstractvillagepiece.getBoundingBox().y1 >= 256) {
                                                continue Label_0086;
                                            }
                                            this.structurePieces.add(abstractvillagepiece);
                                            if (currentDepth + 1 <= this.maxDepth) {
                                                this.availablePieces.addLast(new Entry(abstractvillagepiece, mutableobject2, l, currentDepth + 1));
                                                continue Label_0086;
                                            }
                                            continue Label_0086;
                                        }
                                    }
                                }
                            }
                        } else {
                            Vault.LOGGER.warn("Empty or none existent fallback pool: {}", resourcelocation2);
                        }
                    } else {
                        Vault.LOGGER.warn("Empty or none existent pool: {}", resourcelocation);
                    }
                }
                break;
            }
        }
    }

    static final class Entry {
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
