package iskallia.vault.world.vault.gen;

import iskallia.vault.config.VaultSizeConfig;
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
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FragmentedVaultGenerator extends VaultGenerator {
    public static final int REGION_SIZE = 8192;

    public FragmentedVaultGenerator(ResourceLocation id) {
        super(id);
    }

    private VaultRoomLayoutGenerator layoutGenerator;

    @Nonnull
    protected VaultRoomLayoutGenerator provideLayoutGenerator(VaultSizeConfig.SizeLayout layout) {
        DiamondRoomLayout diamondRoomLayout;
        VaultRoomLayoutGenerator generator = VaultRoomLayoutRegistry.getLayoutGenerator(layout.getLayout());
        if (generator == null) {
            diamondRoomLayout = new DiamondRoomLayout();
        }
        diamondRoomLayout.setSize(layout.getSize());
        return (VaultRoomLayoutGenerator) diamondRoomLayout;
    }


    public boolean generate(ServerWorld world, VaultRaid vault, BlockPos.Mutable pos) {
        MutableBoundingBox vaultBox = generateBoundingBox(vault, pos.immutable());
        pos.move(Direction.EAST, 8192);

        boolean raffle = ((Boolean) vault.getProperties().getBase(VaultRaid.IS_RAFFLE).orElse(Boolean.valueOf(false))).booleanValue();
        int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();

        boolean generatesTreasureRooms = ((Boolean) vault.getProperties().getBase(VaultRaid.CRYSTAL_DATA).map(CrystalData::canGenerateTreasureRooms).orElse(Boolean.valueOf(true))).booleanValue();
        VaultSizeConfig.SizeLayout layout = ModConfigs.VAULT_SIZE.getLayout(level, raffle);

        if (this.layoutGenerator == null) {
            this


                    .layoutGenerator = vault.getAllObjectives().stream().findFirst().map(VaultObjective::getCustomLayout).orElse(provideLayoutGenerator(layout));
        }
        VaultRoomLayoutGenerator.Layout vaultLayout = this.layoutGenerator.generateLayout();
        setGuaranteedRooms(vaultLayout, vault);
        VaultRoomLevelRestrictions.addGenerationPreventions(vaultLayout, level);

        this.startChunk = new ChunkPos(new BlockPos(vaultBox.getCenter()));
        FragmentedJigsawGenerator gen = new FragmentedJigsawGenerator(vaultBox, this.startChunk.getWorldPosition().offset(0, 19, 0), generatesTreasureRooms, (JigsawPoolProvider) this.layoutGenerator, vaultLayout);


        StructureStart<?> start = ModFeatures.VAULT_FEATURE.generate((VaultJigsawGenerator) gen, world.registryAccess(),
                (world.getChunkSource()).generator, world.getStructureManager(), 0, world.getSeed());

        gen.getGeneratedPieces().stream().flatMap(piece -> VaultPiece.of(piece).stream()).forEach(this.pieces::add);
        removeRandomObjectivePieces(vault, gen, layout.getObjectiveRoomRatio());

        world.getChunk(this.startChunk.x, this.startChunk.z, ChunkStatus.EMPTY, true).setStartForFeature(ModStructures.VAULT_STAR, start);
        tick(world, vault);

        if (!vault.getProperties().exists(VaultRaid.START_POS) || !vault.getProperties().exists(VaultRaid.START_FACING)) {
            return findStartPosition(world, vault, this.startChunk, () -> new PortalPlacer((), ()));
        }


        return false;
    }

    private void setGuaranteedRooms(VaultRoomLayoutGenerator.Layout vaultLayout, VaultRaid vault) {
        CrystalData data = (CrystalData) vault.getProperties().getBaseOrDefault(VaultRaid.CRYSTAL_DATA, CrystalData.EMPTY);

        Collection<VaultRoomLayoutGenerator.Room> rooms = vaultLayout.getRooms();
        List<String> roomKeys = data.getGuaranteedRoomFilters();
        if (roomKeys.size() > rooms.size()) {
            roomKeys = roomKeys.subList(0, rooms.size());
        }

        Set<Vector3i> usedRooms = new HashSet<>();
        roomKeys.forEach(roomKey -> {
            if (VaultRoomNames.getName(roomKey) == null) {
                return;
            }
            while (true) {
                VaultRoomLayoutGenerator.Room room = (VaultRoomLayoutGenerator.Room) MiscUtils.getRandomEntry(vaultLayout.getRooms(), rand);
                if (room != null && !usedRooms.contains(room.getRoomPosition())) {
                    usedRooms.add(room.getRoomPosition());
                    room.andFilter(());
                    return;
                }
            }
        });
    }

    private void removeRandomObjectivePieces(VaultRaid vault, FragmentedJigsawGenerator generator, float objectiveRatio) {
        List<StructurePiece> obeliskPieces = (List<StructurePiece>) generator.getGeneratedPieces().stream().filter(this::isObjectivePiece).collect(Collectors.toList());
        Collections.shuffle(obeliskPieces);

        int maxObjectives = MathHelper.floor(obeliskPieces.size() / objectiveRatio);


        int objectiveCount = ((Integer) vault.getAllObjectives().stream().findFirst().map(objective -> Integer.valueOf(objective.modifyObjectiveCount(maxObjectives))).orElse(Integer.valueOf(maxObjectives))).intValue();


        int requiredCount = ((CrystalData) vault.getProperties().getBaseOrDefault(VaultRaid.CRYSTAL_DATA, CrystalData.EMPTY)).getTargetObjectiveCount();
        if (requiredCount != -1) {


            objectiveCount = ((Integer) vault.getAllObjectives().stream().findFirst().map(objective -> Integer.valueOf(objective.modifyMinimumObjectiveCount(maxObjectives, requiredCount))).orElse(Integer.valueOf(objectiveCount))).intValue();
        }

        for (int i = objectiveCount; i < obeliskPieces.size(); i++) {
            generator.removePiece(obeliskPieces.get(i));
        }
    }


    private MutableBoundingBox generateBoundingBox(VaultRaid vault, BlockPos pos) {
        MutableBoundingBox box = vault.getProperties().getBase(VaultRaid.BOUNDING_BOX).orElseGet(() -> {
            BlockPos max = pos.offset(8192, 0, 8192);
            return new MutableBoundingBox(pos.getX(), 0, pos.getZ(), max.getX(), 256, max.getZ());
        });
        vault.getProperties().create(VaultRaid.BOUNDING_BOX, box);
        return box;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        if (this.layoutGenerator != null) {
            tag.put("Layout", (INBT) VaultRoomLayoutRegistry.serialize(this.layoutGenerator));
        }
        return tag;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("Layout", 10)) {
            VaultRoomLayoutGenerator layout = VaultRoomLayoutRegistry.deserialize(nbt.getCompound("Layout"));
            if (layout != null)
                this.layoutGenerator = layout;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\gen\FragmentedVaultGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */