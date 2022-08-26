package iskallia.vault.world.vault.gen;

import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.config.VaultSizeConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModFeatures;
import iskallia.vault.init.ModStructures;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.gen.FragmentedJigsawGenerator;
import iskallia.vault.world.gen.PortalPlacer;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.layout.DiamondRoomLayout;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutRegistry;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class FragmentedVaultGenerator extends VaultGenerator {
    public static final int REGION_SIZE = 8192;
    private VaultRoomLayoutGenerator layoutGenerator;
    private VaultSizeConfig.SizeLayout layout;

    public FragmentedVaultGenerator(final ResourceLocation id) {
        super(id);
    }
    public FragmentedVaultGenerator setLayout(final VaultSizeConfig.SizeLayout layout) {
        this.layout = layout;
        return this;
    }

    @Nonnull
    protected VaultRoomLayoutGenerator provideLayoutGenerator(final VaultSizeConfig.SizeLayout layout) {
        VaultRoomLayoutGenerator generator = VaultRoomLayoutRegistry.getLayoutGenerator(layout.getLayout());
        if (generator == null) {
            generator = new DiamondRoomLayout();
        }
        generator.setSize(layout.getSize());
        return generator;
    }

    @Override
    public boolean generate(final ServerWorld world, final VaultRaid vault, final BlockPos.Mutable pos) {
        final MutableBoundingBox vaultBox = this.generateBoundingBox(vault, pos.immutable());
        pos.move(Direction.EAST, 8192);
        final boolean raffle = vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(false);
        final int level = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        final boolean generatesTreasureRooms = vault.getProperties().getBase(VaultRaid.CRYSTAL_DATA).map(CrystalData::canGenerateTreasureRooms).orElse(true);
        final VaultSizeConfig.SizeLayout layout = (this.layout != null) ? this.layout : ModConfigs.VAULT_SIZE.getLayout(level, raffle);
        if (this.layoutGenerator == null) {
            this.layoutGenerator = vault.getAllObjectives().stream().findFirst().map(VaultObjective::getCustomLayout).orElse(this.provideLayoutGenerator(layout));
        }
        final VaultRoomLayoutGenerator.Layout vaultLayout = this.layoutGenerator.generateLayout();
        this.setGuaranteedRooms(vaultLayout, vault);
        VaultRoomLevelRestrictions.addGenerationPreventions(vaultLayout, level);
        this.startChunk = new ChunkPos(new BlockPos(vaultBox.getCenter()));
        final FragmentedJigsawGenerator gen = new FragmentedJigsawGenerator(vaultBox, this.startChunk.getWorldPosition().offset(0, 19, 0), generatesTreasureRooms, this.layoutGenerator, vaultLayout);
        final StructureStart<?> start = ModFeatures.VAULT_FEATURE.generate(gen, world.registryAccess(), world.getChunkSource().generator, world.getStructureManager(), 0, world.getSeed());
        gen.getGeneratedPieces().stream().flatMap(piece -> VaultPiece.of(piece).stream()).forEach(this.pieces::add);
        this.removeRandomObjectivePieces(vault, gen, layout.getObjectiveRoomRatio());
        world.getChunk(this.startChunk.x, this.startChunk.z, ChunkStatus.EMPTY, true).setStartForFeature(ModStructures.VAULT_STAR, start);
        this.tick(world, vault);
        return (!vault.getProperties().exists(VaultRaid.START_POS) || !vault.getProperties().exists(VaultRaid.START_FACING)) && this.findStartPosition(world, vault, this.startChunk, () -> new PortalPlacer((pos1, random, facing) -> ModBlocks.VAULT_PORTAL.defaultBlockState().setValue(VaultPortalBlock.AXIS, facing.getAxis()), (pos1, random, facing) -> Blocks.BLACKSTONE.defaultBlockState()));
    }

    private void setGuaranteedRooms(final VaultRoomLayoutGenerator.Layout vaultLayout, final VaultRaid vault) {
        final CrystalData data = vault.getProperties().getBaseOrDefault(VaultRaid.CRYSTAL_DATA, CrystalData.EMPTY);
        final Collection<VaultRoomLayoutGenerator.Room> rooms = vaultLayout.getRooms();
        List<String> roomKeys = data.getGuaranteedRoomFilters();
        if (roomKeys.size() > rooms.size()) {
            roomKeys = roomKeys.subList(0, rooms.size());
        }
        final Set<Vector3i> usedRooms = new HashSet<Vector3i>();
        roomKeys.forEach(roomKey -> {
            if (VaultRoomNames.getName(roomKey) != null) {
                VaultRoomLayoutGenerator.Room room;
                do {
                    room = MiscUtils.getRandomEntry(vaultLayout.getRooms(), FragmentedVaultGenerator.rand);
                } while (room == null || usedRooms.contains(room.getRoomPosition()));
                usedRooms.add(room.getRoomPosition());
                room.andFilter(key -> key.getPath().contains(roomKey));
            }
        });
    }

    private void removeRandomObjectivePieces(final VaultRaid vault, final FragmentedJigsawGenerator generator, final float objectiveRatio) {
        final List<StructurePiece> obeliskPieces = generator.getGeneratedPieces().stream().filter(this::isObjectivePiece).collect(Collectors.toList());
        Collections.shuffle(obeliskPieces);
        final int maxObjectives = MathHelper.floor(obeliskPieces.size() / objectiveRatio);
        int objectiveCount = vault.getAllObjectives().stream().findFirst().map(objective -> objective.modifyObjectiveCount(maxObjectives)).orElse(maxObjectives);
        final int requiredCount = vault.getProperties().getBaseOrDefault(VaultRaid.CRYSTAL_DATA, CrystalData.EMPTY).getTargetObjectiveCount();
        if (requiredCount != -1) {
            objectiveCount = vault.getAllObjectives().stream().findFirst().map(objective -> objective.modifyMinimumObjectiveCount(maxObjectives, requiredCount)).orElse(objectiveCount);
        }
        for (int i = objectiveCount; i < obeliskPieces.size(); ++i) {
            generator.removePiece(obeliskPieces.get(i));
        }
    }

    private MutableBoundingBox generateBoundingBox(final VaultRaid vault, final BlockPos pos) {
        final MutableBoundingBox box = vault.getProperties().getBase(VaultRaid.BOUNDING_BOX).orElseGet(() -> {
            final BlockPos max = pos.offset(8192, 0, 8192);
            return new MutableBoundingBox(pos.getX(), 0, pos.getZ(), max.getX(), 256, max.getZ());
        });
        vault.getProperties().create(VaultRaid.BOUNDING_BOX, box);
        return box;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        if (this.layoutGenerator != null) {
            tag.put("Layout", VaultRoomLayoutRegistry.serialize(this.layoutGenerator));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("Layout", 10)) {
            final VaultRoomLayoutGenerator layout = VaultRoomLayoutRegistry.deserialize(nbt.getCompound("Layout"));
            if (layout != null) {
                this.layoutGenerator = layout;
            }
        }
    }
}
