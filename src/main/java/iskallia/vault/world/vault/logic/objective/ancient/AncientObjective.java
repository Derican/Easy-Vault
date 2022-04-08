package iskallia.vault.world.vault.logic.objective.ancient;

import iskallia.vault.block.CryoChamberBlock;
import iskallia.vault.block.entity.AncientCryoChamberTileEntity;
import iskallia.vault.block.entity.CryoChamberTileEntity;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.ScheduledItemDropData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.Property;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@EventBusSubscriber
public class AncientObjective extends VaultObjective {
    private int generatedIdentifier = 0;
    public static final int MAX_ANCIENTS = 4;
    private final List<String> generatedEternals = new ArrayList<>();

    private final List<String> foundEternals = new ArrayList<>();
    private final Set<BlockPos> placedEternals = new HashSet<>();

    public AncientObjective(ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
    }


    public void initialize(MinecraftServer srv, VaultRaid vault) {
        vault.getProperties().getBase(VaultRaid.HOST).ifPresent(id -> {
            List<String> ancients = AncientEternalArchive.getAncients(srv, id);
            for (int i = 0; i < Math.min(ancients.size(), 4); i++) {
                this.generatedEternals.add(ancients.get(i));
            }
        });
    }


    @Nonnull
    public BlockState getObjectiveRelevantBlock() {
        return (BlockState) ModBlocks.CRYO_CHAMBER.defaultBlockState().setValue((Property) CryoChamberBlock.CHAMBER_STATE, (Comparable) CryoChamberBlock.ChamberState.RUSTY);
    }


    public void postProcessObjectiveRelevantBlock(ServerWorld world, BlockPos pos) {
        if (this.generatedIdentifier >= this.generatedEternals.size()) {
            return;
        }

        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof AncientCryoChamberTileEntity) {
            world.setBlock(pos.above(), (BlockState) ((BlockState) ModBlocks.CRYO_CHAMBER.defaultBlockState()
                    .setValue((Property) CryoChamberBlock.HALF, (Comparable) DoubleBlockHalf.UPPER))
                    .setValue((Property) CryoChamberBlock.CHAMBER_STATE, (Comparable) CryoChamberBlock.ChamberState.RUSTY), 3);

            String name = this.generatedEternals.get(this.generatedIdentifier);

            AncientCryoChamberTileEntity cryoChamber = (AncientCryoChamberTileEntity) te;
            cryoChamber.setEternalName(name);

            this.placedEternals.add(pos);
            this.generatedIdentifier++;
        }
    }


    public int modifyObjectiveCount(int objectives) {
        return this.generatedEternals.size();
    }


    public void notifyBail(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        if (!this.foundEternals.isEmpty()) {
            setCompleted();

            player.runIfPresent(world.getServer(), sPlayer -> {
                ScheduledItemDropData.get(world).addDrop((PlayerEntity) sPlayer, getRewardCrate(sPlayer, vault));
                StringTextComponent stringTextComponent = new StringTextComponent("");
                stringTextComponent.append((ITextComponent) sPlayer.getDisplayName().copy().setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168))));
                stringTextComponent.append(" rescued ");
                for (int i = 0; i < this.foundEternals.size(); i++) {
                    if (i != 0) {
                        stringTextComponent.append(", ");
                    }
                    stringTextComponent.append((ITextComponent) (new StringTextComponent(this.foundEternals.get(i))).withStyle(TextFormatting.GOLD));
                }
                stringTextComponent.append("!");
                MiscUtils.broadcast((ITextComponent) stringTextComponent);
            });
        }
    }


    private ItemStack getRewardCrate(ServerPlayerEntity sPlayer, VaultRaid vault) {
        ServerWorld world = sPlayer.getLevel();
        BlockPos pos = sPlayer.blockPosition();


        LootContext.Builder builder = (new LootContext.Builder(world)).withRandom(world.random).withParameter(LootParameters.THIS_ENTITY, sPlayer).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf((Vector3i) pos)).withLuck(sPlayer.getLuck());
        LootContext ctx = builder.create(LootParameterSets.CHEST);

        NonNullList<ItemStack> stacks = createLoot(world, vault, ctx);
        ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE, stacks);

        this.crates.add(new VaultObjective.Crate((List) stacks));
        return crate;
    }


    @Nullable
    public LootTable getRewardLootTable(VaultRaid vault, Function<ResourceLocation, LootTable> tblResolver) {
        int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
        return (config != null) ? tblResolver.apply(config.getBossCrate()) : LootTable.EMPTY;
    }


    protected void addSpecialLoot(ServerWorld world, VaultRaid vault, LootContext context, NonNullList<ItemStack> stacks) {
        super.addSpecialLoot(world, vault, context, stacks);

        int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
        LootTable ancientTable = world.getServer().getLootTables().get(config.getAncientEternalBonusBox());

        for (String eternalName : this.foundEternals) {
            NonNullList<ItemStack> ancientLoot = NonNullList.create();
            ancientLoot.addAll(ancientTable.getRandomItems(context));
            Collections.shuffle((List<?>) ancientLoot);

            ItemStack box = new ItemStack((IItemProvider) Items.BLACK_SHULKER_BOX);
            ItemStackHelper.saveAllItems(box.getOrCreateTagElement("BlockEntityTag"), ancientLoot);
            box.setHoverName((ITextComponent) (new StringTextComponent(eternalName)).withStyle(TextFormatting.GOLD));

            stacks.add(box);
        }
    }


    public ITextComponent getVaultName() {
        return (ITextComponent) (new StringTextComponent("Ancient Vault")).withStyle(TextFormatting.DARK_AQUA);
    }


    public ITextComponent getObjectiveDisplayName() {
        return (ITextComponent) (new StringTextComponent("Ancient Vault")).withStyle(TextFormatting.DARK_AQUA);
    }


    @Nullable
    public VaultRoomLayoutGenerator getCustomLayout() {
        return (VaultRoomLayoutGenerator) new DenseSquareRoomLayout(19);
    }


    public int getVaultTimerStart(int vaultTime) {
        return 18000;
    }


    public boolean preventsEatingExtensionFruit(MinecraftServer srv, VaultRaid vault) {
        return true;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (event.getWorld().isClientSide() || !event.getState().is((Block) ModBlocks.CRYO_CHAMBER)) {
            return;
        }
        if (!(event.getWorld() instanceof ServerWorld)) {
            return;
        }
        ServerWorld sWorld = (ServerWorld) event.getWorld();
        CryoChamberTileEntity cryoChamberTileEntity = CryoChamberBlock.getCryoChamberTileEntity((World) sWorld, event.getPos(), event.getState());
        if (!(cryoChamberTileEntity instanceof AncientCryoChamberTileEntity)) {
            return;
        }
        AncientCryoChamberTileEntity ancientCryoChamber = (AncientCryoChamberTileEntity) cryoChamberTileEntity;

        VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, event.getPos());
        if (vault != null) {
            vault.getActiveObjective(AncientObjective.class).ifPresent(objective -> {
                if (objective.placedEternals.contains(te.getBlockPos())) {
                    objective.foundEternals.add(ancientCryoChamber.getEternalName());
                }
            });
        }
    }


    public void tick(VaultRaid vault, PlayerFilter filter, ServerWorld world) {
        super.tick(vault, filter, world);
        MinecraftServer srv = world.getServer();

        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, ()));
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();

        ListNBT ancients = new ListNBT();
        this.generatedEternals.forEach(ancient -> ancients.add(StringNBT.valueOf(ancient)));
        tag.put("ancients", (INBT) ancients);

        ListNBT placed = new ListNBT();
        this.placedEternals.forEach(pos -> {
            CompoundNBT posTag = new CompoundNBT();
            posTag.putInt("x", pos.getX());
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());
            placed.add(posTag);
        });
        tag.put("placed", (INBT) placed);

        ListNBT found = new ListNBT();
        this.foundEternals.forEach(name -> found.add(StringNBT.valueOf(name)));
        tag.put("found", (INBT) found);

        tag.putInt("generatedIdentifier", this.generatedIdentifier);

        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
        super.deserializeNBT(tag);

        this.generatedEternals.clear();
        ListNBT ancients = tag.getList("ancients", 10);
        int i;
        for (i = 0; i < ancients.size(); i++) {
            CompoundNBT ancientTag = ancients.getCompound(i);
            this.generatedEternals.add(ancientTag.getString("name"));
        }
        ancients = tag.getList("ancients", 8);
        for (i = 0; i < ancients.size(); i++) {
            this.generatedEternals.add(ancients.getString(i));
        }

        this.placedEternals.clear();
        ListNBT placed = tag.getList("placed", 10);
        for (int j = 0; j < placed.size(); j++) {
            CompoundNBT posTag = placed.getCompound(j);
            this.placedEternals.add(new BlockPos(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z")));
        }
        this.foundEternals.clear();
        ListNBT found = tag.getList("found", 8);
        for (int k = 0; k < found.size(); k++) {
            this.foundEternals.add(found.getString(k));
        }
        this.generatedIdentifier = tag.getInt("generatedIdentifier");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\ancient\AncientObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */