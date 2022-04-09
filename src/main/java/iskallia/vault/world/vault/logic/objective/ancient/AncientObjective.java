// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.ancient;

import iskallia.vault.block.CryoChamberBlock;
import iskallia.vault.block.VaultCrateBlock;
import iskallia.vault.block.entity.AncientCryoChamberTileEntity;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.ScheduledItemDropData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.layout.DenseSquareRoomLayout;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
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
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

@Mod.EventBusSubscriber
public class AncientObjective extends VaultObjective
{
    public static final int MAX_ANCIENTS = 4;
    private int generatedIdentifier;
    private final List<String> generatedEternals;
    private final List<String> foundEternals;
    private final Set<BlockPos> placedEternals;
    
    public AncientObjective(final ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
        this.generatedIdentifier = 0;
        this.generatedEternals = new ArrayList<String>();
        this.foundEternals = new ArrayList<String>();
        this.placedEternals = new HashSet<BlockPos>();
    }
    
    @Override
    public void initialize(final MinecraftServer srv, final VaultRaid vault) {
        vault.getProperties().getBase(VaultRaid.HOST).ifPresent(id -> {
            final List<String> ancients = AncientEternalArchive.getAncients(srv, id);
            for (int i = 0; i < Math.min(ancients.size(), 4); ++i) {
                this.generatedEternals.add((String)ancients.get(i));
            }
        });
    }
    
    @Nonnull
    @Override
    public BlockState getObjectiveRelevantBlock() {
        return ModBlocks.CRYO_CHAMBER.defaultBlockState().setValue(CryoChamberBlock.CHAMBER_STATE, CryoChamberBlock.ChamberState.RUSTY);
    }
    
    @Override
    public void postProcessObjectiveRelevantBlock(final ServerWorld world, final BlockPos pos) {
        if (this.generatedIdentifier >= this.generatedEternals.size()) {
            return;
        }
        final TileEntity te = world.getBlockEntity(pos);
        if (te instanceof AncientCryoChamberTileEntity) {
            world.setBlock(pos.above(), (ModBlocks.CRYO_CHAMBER.defaultBlockState().setValue(CryoChamberBlock.HALF, DoubleBlockHalf.UPPER)).setValue(CryoChamberBlock.CHAMBER_STATE, CryoChamberBlock.ChamberState.RUSTY), 3);
            final String name = this.generatedEternals.get(this.generatedIdentifier);
            final AncientCryoChamberTileEntity cryoChamber = (AncientCryoChamberTileEntity)te;
            cryoChamber.setEternalName(name);
            this.placedEternals.add(pos);
            ++this.generatedIdentifier;
        }
    }
    
    @Override
    public int modifyObjectiveCount(final int objectives) {
        return this.generatedEternals.size();
    }
    
    @Override
    public void notifyBail(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        if (!this.foundEternals.isEmpty()) {
            this.setCompleted();
            player.runIfPresent(world.getServer(), sPlayer -> {
                ScheduledItemDropData.get(world).addDrop((PlayerEntity)sPlayer, this.getRewardCrate(sPlayer, vault));
                final IFormattableTextComponent ct = (IFormattableTextComponent)new StringTextComponent("");
                ct.append((ITextComponent)sPlayer.getDisplayName().copy().setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168))));
                ct.append(" rescued ");
                for (int i = 0; i < this.foundEternals.size(); ++i) {
                    if (i != 0) {
                        ct.append(", ");
                    }
                    ct.append((ITextComponent)new StringTextComponent((String)this.foundEternals.get(i)).withStyle(TextFormatting.GOLD));
                }
                ct.append("!");
                MiscUtils.broadcast((ITextComponent)ct);
            });
        }
    }
    
    private ItemStack getRewardCrate(final ServerPlayerEntity sPlayer, final VaultRaid vault) {
        final ServerWorld world = sPlayer.getLevel();
        final BlockPos pos = sPlayer.blockPosition();
        final LootContext.Builder builder = new LootContext.Builder(world).withRandom(world.random).withParameter(LootParameters.THIS_ENTITY, sPlayer).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf((Vector3i)pos)).withLuck(sPlayer.getLuck());
        final LootContext ctx = builder.create(LootParameterSets.CHEST);
        final NonNullList<ItemStack> stacks = this.createLoot(world, vault, ctx);
        final ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE, stacks);
        this.crates.add(new Crate((List<ItemStack>)stacks));
        return crate;
    }
    
    @Nullable
    @Override
    public LootTable getRewardLootTable(final VaultRaid vault, final Function<ResourceLocation, LootTable> tblResolver) {
        final int level = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        final LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
        return (config != null) ? tblResolver.apply(config.getBossCrate()) : LootTable.EMPTY;
    }
    
    @Override
    protected void addSpecialLoot(final ServerWorld world, final VaultRaid vault, final LootContext context, final NonNullList<ItemStack> stacks) {
        super.addSpecialLoot(world, vault, context, stacks);
        final int level = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        final LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
        final LootTable ancientTable = world.getServer().getLootTables().get(config.getAncientEternalBonusBox());
        for (final String eternalName : this.foundEternals) {
            final NonNullList<ItemStack> ancientLoot = NonNullList.create();
            ancientLoot.addAll((Collection)ancientTable.getRandomItems(context));
            Collections.shuffle((List<?>)ancientLoot);
            final ItemStack box = new ItemStack((IItemProvider)Items.BLACK_SHULKER_BOX);
            ItemStackHelper.saveAllItems(box.getOrCreateTagElement("BlockEntityTag"), (NonNullList)ancientLoot);
            box.setHoverName((ITextComponent)new StringTextComponent(eternalName).withStyle(TextFormatting.GOLD));
            stacks.add(box);
        }
    }
    
    @Override
    public ITextComponent getVaultName() {
        return (ITextComponent)new StringTextComponent("Ancient Vault").withStyle(TextFormatting.DARK_AQUA);
    }
    
    @Override
    public ITextComponent getObjectiveDisplayName() {
        return (ITextComponent)new StringTextComponent("Ancient Vault").withStyle(TextFormatting.DARK_AQUA);
    }
    
    @Nullable
    @Override
    public VaultRoomLayoutGenerator getCustomLayout() {
        return new DenseSquareRoomLayout(19);
    }
    
    @Override
    public int getVaultTimerStart(final int vaultTime) {
        return 18000;
    }
    
    @Override
    public boolean preventsEatingExtensionFruit(final MinecraftServer srv, final VaultRaid vault) {
        return true;
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onBreak(final BlockEvent.BreakEvent event) {
        if (event.getWorld().isClientSide() || !event.getState().is((Block)ModBlocks.CRYO_CHAMBER)) {
            return;
        }
        if (!(event.getWorld() instanceof ServerWorld)) {
            return;
        }
        final ServerWorld sWorld = (ServerWorld)event.getWorld();
        final TileEntity te = CryoChamberBlock.getCryoChamberTileEntity((World)sWorld, event.getPos(), event.getState());
        if (!(te instanceof AncientCryoChamberTileEntity)) {
            return;
        }
        final AncientCryoChamberTileEntity ancientCryoChamber = (AncientCryoChamberTileEntity)te;
        final VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, event.getPos());
        if (vault != null) {
            vault.getActiveObjective(AncientObjective.class).ifPresent(objective -> {
                if (objective.placedEternals.contains(te.getBlockPos())) {
                    objective.foundEternals.add(ancientCryoChamber.getEternalName());
                }
            });
        }
    }
    
    @Override
    public void tick(final VaultRaid vault, final PlayerFilter filter, final ServerWorld world) {
        super.tick(vault, filter, world);
        final MinecraftServer srv = world.getServer();
        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, playerEntity -> {
            final VaultGoalMessage pkt = VaultGoalMessage.ancientsHunt(this.generatedEternals.size(), this.foundEternals.size());
            ModNetwork.CHANNEL.sendTo(pkt, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }));
    }
    
    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        final ListNBT ancients = new ListNBT();
        this.generatedEternals.forEach(ancient -> ancients.add(StringNBT.valueOf(ancient)));
        tag.put("ancients", (INBT)ancients);
        final ListNBT placed = new ListNBT();
        this.placedEternals.forEach(pos -> {
            final CompoundNBT posTag = new CompoundNBT();
            posTag.putInt("x", pos.getX());
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());
            placed.add(posTag);
            return;
        });
        tag.put("placed", (INBT)placed);
        final ListNBT found = new ListNBT();
        this.foundEternals.forEach(name -> found.add(StringNBT.valueOf(name)));
        tag.put("found", (INBT)found);
        tag.putInt("generatedIdentifier", this.generatedIdentifier);
        return tag;
    }
    
    @Override
    public void deserializeNBT(final CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.generatedEternals.clear();
        ListNBT ancients = tag.getList("ancients", 10);
        for (int i = 0; i < ancients.size(); ++i) {
            final CompoundNBT ancientTag = ancients.getCompound(i);
            this.generatedEternals.add(ancientTag.getString("name"));
        }
        ancients = tag.getList("ancients", 8);
        for (int i = 0; i < ancients.size(); ++i) {
            this.generatedEternals.add(ancients.getString(i));
        }
        this.placedEternals.clear();
        final ListNBT placed = tag.getList("placed", 10);
        for (int j = 0; j < placed.size(); ++j) {
            final CompoundNBT posTag = placed.getCompound(j);
            this.placedEternals.add(new BlockPos(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z")));
        }
        this.foundEternals.clear();
        final ListNBT found = tag.getList("found", 8);
        for (int k = 0; k < found.size(); ++k) {
            this.foundEternals.add(found.getString(k));
        }
        this.generatedIdentifier = tag.getInt("generatedIdentifier");
    }
}
