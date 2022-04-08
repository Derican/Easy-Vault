package iskallia.vault.world.vault.logic.objective;

import iskallia.vault.block.VaultCrateBlock;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.config.VaultModifiersConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.modifier.VaultModifier;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class CakeHuntObjective extends VaultObjective {
    private int maxCakeCount = 22 + rand.nextInt(9);
    private int cakeCount = 0;

    public CakeHuntObjective(ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
    }

    public void expandVault(ServerWorld world, BlockPos cakePos, VaultRaid vault) {
        vault.getGenerator().getPiecesAt(cakePos, VaultRoom.class).stream().findAny().ifPresent(room -> vault.getProperties().getBase(VaultRaid.START_FACING).ifPresent(()));
    }


    private void addRandomModifier(VaultRaid vault, ServerWorld sWorld) {
        if (sWorld.getRandom().nextFloat() < 0.25D) {
            return;
        }
        int level = ((Integer) vault.getProperties().getValue(VaultRaid.LEVEL)).intValue();
        Set<VaultModifier> modifiers = ModConfigs.VAULT_MODIFIERS.getRandom(rand, level, VaultModifiersConfig.ModifierPoolType.DEFAULT, null);
        modifiers.removeIf(mod -> mod instanceof iskallia.vault.world.vault.modifier.NoExitModifier);
        modifiers.removeIf(mod -> mod instanceof iskallia.vault.world.vault.modifier.TimerModifier);
        modifiers.removeIf(mod -> mod instanceof iskallia.vault.world.vault.modifier.InventoryRestoreModifier);
        if (sWorld.getRandom().nextFloat() < 0.65F) {
            modifiers.removeIf(mod -> mod instanceof iskallia.vault.world.vault.modifier.ArtifactChanceModifier);
        }
        List<VaultModifier> modifierList = new ArrayList<>(modifiers);
        Collections.shuffle(modifierList);
        VaultModifier modifier = (VaultModifier) MiscUtils.getRandomEntry(modifierList, rand);
        if (modifier != null) {
            IFormattableTextComponent iFormattableTextComponent = (new StringTextComponent("Added ")).withStyle(TextFormatting.GRAY).append(modifier.getNameComponent());
            vault.getModifiers().addPermanentModifier(modifier);
            vault.getPlayers().forEach(vPlayer -> {
                modifier.apply(vault, vPlayer, sWorld, sWorld.getRandom());
                vPlayer.runIfPresent(sWorld.getServer(), ());
            });
        }
    }


    private void spawnRewards(ServerWorld world, VaultRaid vault) {
        VaultPlayer rewardPlayer = vault.getProperties().getBase(VaultRaid.HOST).flatMap(vault::getPlayer).filter(vPlayer -> vPlayer instanceof iskallia.vault.world.vault.player.VaultRunner).orElseGet(() -> (VaultPlayer) vault.getPlayers().stream().filter(()).findAny().orElse(null));


        if (rewardPlayer == null) {
            return;
        }
        rewardPlayer.runIfPresent(world.getServer(), sPlayer -> {
            BlockPos pos = sPlayer.blockPosition();
            LootContext.Builder builder = (new LootContext.Builder(world)).withRandom(world.random).withParameter(LootParameters.THIS_ENTITY, sPlayer).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf((Vector3i) pos)).withLuck(sPlayer.getLuck());
            LootContext ctx = builder.create(LootParameterSets.CHEST);
            dropRewardCrate(world, vault, pos, ctx);
            for (int i = 1; i < vault.getPlayers().size(); i++) {
                if (rand.nextFloat() < 0.5F) {
                    dropRewardCrate(world, vault, pos, ctx);
                }
            }
            IFormattableTextComponent msgContainer = (new StringTextComponent("")).withStyle(TextFormatting.WHITE);
            IFormattableTextComponent playerName = sPlayer.getDisplayName().copy();
            playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
            MiscUtils.broadcast((ITextComponent) msgContainer.append((ITextComponent) playerName).append(" finished a Cake Hunt!"));
        });
    }


    private void dropRewardCrate(ServerWorld world, VaultRaid vault, BlockPos pos, LootContext context) {
        NonNullList<ItemStack> stacks = createLoot(world, vault, context);

        ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE_CAKE, stacks);
        ItemEntity item = new ItemEntity((World) world, pos.getX(), pos.getY(), pos.getZ(), crate);
        item.setDefaultPickUpDelay();
        world.addFreshEntity((Entity) item);

        this.crates.add(new VaultObjective.Crate((List<ItemStack>) stacks));
    }


    protected void addSpecialLoot(ServerWorld world, VaultRaid vault, LootContext context, NonNullList<ItemStack> stacks) {
        super.addSpecialLoot(world, vault, context, stacks);

        int amt = Math.max(rand.nextInt(this.maxCakeCount), rand.nextInt(this.maxCakeCount));
        for (int i = 0; i < amt; i++) {
            stacks.add(new ItemStack((IItemProvider) Items.CAKE));
        }
        if (world.getRandom().nextFloat() < 0.25F) {
            stacks.add(new ItemStack((IItemProvider) ModItems.ARMOR_CRATE_CAKE));
        }
    }


    public void tick(VaultRaid vault, PlayerFilter filter, ServerWorld world) {
        super.tick(vault, filter, world);
        MinecraftServer srv = world.getServer();

        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, ()));


        vault.getGenerator().getPieces(VaultRoom.class).forEach(room -> ensureCakeIsPresent(world, room));

        if (this.cakeCount >= this.maxCakeCount) {
            setCompleted();
            spawnRewards(world, vault);
        } else if (world.getGameTime() % 300L == 0L) {
            vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, ()));
        }
    }


    private void ensureCakeIsPresent(ServerWorld world, VaultRoom room) {
        if (room.isCakeEaten()) {
            return;
        }

        MutableBoundingBox roomBox = room.getBoundingBox();
        if (room.getCakePos() == null) {
            BlockPos at;
            for (int xx = roomBox.x0; xx <= roomBox.x1; xx++) {
                for (int yy = roomBox.y0; yy <= roomBox.y1; yy++) {
                    for (int zz = roomBox.z0; zz <= roomBox.z1; zz++) {
                        BlockPos pos = new BlockPos(xx, yy, zz);
                        BlockState state = world.getBlockState(pos);
                        if (state.getBlock() instanceof net.minecraft.block.CakeBlock) {
                            world.removeBlock(pos, false);
                        }
                    }
                }
            }


            do {
                at = MiscUtils.getRandomPos(roomBox, rand);
            } while (!world.isEmptyBlock(at) || !world.getBlockState(at.below()).isFaceSturdy((IBlockReader) world, at, Direction.UP));

            world.setBlock(at, Blocks.CAKE.defaultBlockState(), 2);
            room.setCakePos(at);
        } else {
            BlockPos at;
            for (int xx = roomBox.x0; xx <= roomBox.x1; xx++) {
                for (int yy = roomBox.y0; yy <= roomBox.y1; yy++) {
                    for (int zz = roomBox.z0; zz <= roomBox.z1; zz++) {
                        BlockPos pos = new BlockPos(xx, yy, zz);
                        BlockState state = world.getBlockState(pos);
                        if (state.getBlock() instanceof net.minecraft.block.CakeBlock) {
                            return;
                        }
                    }
                }
            }


            do {
                at = MiscUtils.getRandomPos(roomBox, rand);
            } while (!world.isEmptyBlock(at) || !world.getBlockState(at.below()).isFaceSturdy((IBlockReader) world, at, Direction.UP));

            world.setBlock(at, Blocks.CAKE.defaultBlockState(), 2);
            room.setCakePos(at);
        }
    }


    @Nonnull
    public BlockState getObjectiveRelevantBlock() {
        return Blocks.AIR.defaultBlockState();
    }


    @Nullable
    public LootTable getRewardLootTable(VaultRaid vault, Function<ResourceLocation, LootTable> tblResolver) {
        int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
        return (config != null) ? tblResolver.apply(config.getBossCrate()) : LootTable.EMPTY;
    }


    public ITextComponent getObjectiveDisplayName() {
        return (ITextComponent) (new StringTextComponent("Cake Hunt")).withStyle(TextFormatting.DARK_PURPLE);
    }


    public void setObjectiveTargetCount(int amount) {
        this.maxCakeCount = amount;
    }


    @Nullable
    public ITextComponent getObjectiveTargetDescription(int amount) {
        return (ITextComponent) (new StringTextComponent("Cakes needed to be found: "))
                .append((ITextComponent) (new StringTextComponent(String.valueOf(amount))).withStyle(TextFormatting.DARK_PURPLE));
    }


    @Nullable
    public VaultRoomLayoutGenerator getCustomLayout() {
        return (VaultRoomLayoutGenerator) new SingularVaultRoomLayout();
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putInt("maxCakeCount", this.maxCakeCount);
        tag.putInt("cakeCount", this.cakeCount);
        return tag;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.maxCakeCount = nbt.getInt("maxCakeCount");
        this.cakeCount = nbt.getInt("cakeCount");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\CakeHuntObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */