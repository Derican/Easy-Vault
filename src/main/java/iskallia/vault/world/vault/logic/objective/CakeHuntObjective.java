// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective;

import iskallia.vault.block.VaultCrateBlock;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.config.VaultModifiersConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.gen.decorator.BreadcrumbFeature;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.layout.SingularVaultRoomLayout;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.modifier.*;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.player.VaultRunner;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class CakeHuntObjective extends VaultObjective {
    private int maxCakeCount;
    private int cakeCount;

    public CakeHuntObjective(final ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
        this.maxCakeCount = 22 + CakeHuntObjective.rand.nextInt(9);
        this.cakeCount = 0;
    }

    public void expandVault(final ServerWorld world, final BlockPos cakePos, final VaultRaid vault) {
        vault.getGenerator().getPiecesAt(cakePos, VaultRoom.class).stream().findAny().ifPresent(room -> vault.getProperties().getBase(VaultRaid.START_FACING).ifPresent(vaultDir -> {
            ++this.cakeCount;
            if (this.cakeCount < this.maxCakeCount) {
                this.addRandomModifier(vault, world);
                final List<VaultPiece> generatedPieces = VaultJigsawHelper.expandVault(vault, world, (VaultRoom) room, vaultDir.getClockWise());
                generatedPieces.stream().filter(piece -> piece instanceof VaultRoom).findFirst().ifPresent(newRoomPiece -> this.ensureCakeIsPresent(world, (VaultRoom) newRoomPiece));
                BreadcrumbFeature.generateVaultBreadcrumb(vault, world, generatedPieces);
            }
        }));
    }

    private void addRandomModifier(final VaultRaid vault, final ServerWorld sWorld) {
        if (sWorld.getRandom().nextFloat() < 0.25) {
            return;
        }
        final int level = vault.getProperties().getValue(VaultRaid.LEVEL);
        final Set<VaultModifier> modifiers = ModConfigs.VAULT_MODIFIERS.getRandom(CakeHuntObjective.rand, level, VaultModifiersConfig.ModifierPoolType.DEFAULT, null);
        modifiers.removeIf(mod -> mod instanceof NoExitModifier);
        modifiers.removeIf(mod -> mod instanceof TimerModifier);
        modifiers.removeIf(mod -> mod instanceof InventoryRestoreModifier);
        if (sWorld.getRandom().nextFloat() < 0.65f) {
            modifiers.removeIf(mod -> mod instanceof ArtifactChanceModifier);
        }
        final List<VaultModifier> modifierList = new ArrayList<VaultModifier>(modifiers);
        Collections.shuffle(modifierList);
        final VaultModifier modifier = MiscUtils.getRandomEntry(modifierList, CakeHuntObjective.rand);
        if (modifier != null) {
            final ITextComponent ct = (ITextComponent) new StringTextComponent("Added ").withStyle(TextFormatting.GRAY).append(modifier.getNameComponent());
            vault.getModifiers().addPermanentModifier(modifier);
            vault.getPlayers().forEach(vPlayer -> {
                modifier.apply(vault, vPlayer, sWorld, sWorld.getRandom());
                vPlayer.runIfPresent(sWorld.getServer(), sPlayer -> sPlayer.sendMessage(ct, Util.NIL_UUID));
            });
        }
    }

    private void spawnRewards(final ServerWorld world, final VaultRaid vault) {
        final VaultPlayer rewardPlayer = vault.getProperties().getBase(VaultRaid.HOST).flatMap(vault::getPlayer).filter(vPlayer -> vPlayer instanceof VaultRunner).orElseGet(() -> vault.getPlayers().stream().filter(vPlayer -> vPlayer instanceof VaultRunner).findAny().orElse(null));
        if (rewardPlayer == null) {
            return;
        }
        rewardPlayer.runIfPresent(world.getServer(), sPlayer -> {
            final BlockPos pos = sPlayer.blockPosition();
            final LootContext.Builder builder = new LootContext.Builder(world).withRandom(world.random).withParameter(LootParameters.THIS_ENTITY, sPlayer).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf((Vector3i) pos)).withLuck(sPlayer.getLuck());
            final LootContext ctx = builder.create(LootParameterSets.CHEST);
            this.dropRewardCrate(world, vault, pos, ctx);
            for (int i = 1; i < vault.getPlayers().size(); ++i) {
                if (CakeHuntObjective.rand.nextFloat() < 0.5f) {
                    this.dropRewardCrate(world, vault, pos, ctx);
                }
            }
            final IFormattableTextComponent msgContainer = new StringTextComponent("").withStyle(TextFormatting.WHITE);
            final IFormattableTextComponent playerName = sPlayer.getDisplayName().copy();
            playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
            MiscUtils.broadcast((ITextComponent) msgContainer.append((ITextComponent) playerName).append(" finished a Cake Hunt!"));
        });
    }

    private void dropRewardCrate(final ServerWorld world, final VaultRaid vault, final BlockPos pos, final LootContext context) {
        final NonNullList<ItemStack> stacks = this.createLoot(world, vault, context);
        final ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE_CAKE, stacks);
        final ItemEntity item = new ItemEntity((World) world, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), crate);
        item.setDefaultPickUpDelay();
        world.addFreshEntity((Entity) item);
        this.crates.add(new Crate((List<ItemStack>) stacks));
    }

    @Override
    protected void addSpecialLoot(final ServerWorld world, final VaultRaid vault, final LootContext context, final NonNullList<ItemStack> stacks) {
        super.addSpecialLoot(world, vault, context, stacks);
        for (int amt = Math.max(CakeHuntObjective.rand.nextInt(this.maxCakeCount), CakeHuntObjective.rand.nextInt(this.maxCakeCount)), i = 0; i < amt; ++i) {
            stacks.add(new ItemStack((IItemProvider) Items.CAKE));
        }
        if (world.getRandom().nextFloat() < 0.25f) {
            stacks.add(new ItemStack((IItemProvider) ModItems.ARMOR_CRATE_CAKE));
        }
    }

    @Override
    public void tick(final VaultRaid vault, final PlayerFilter filter, final ServerWorld world) {
        super.tick(vault, filter, world);
        final MinecraftServer srv = world.getServer();
        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, playerEntity -> {
            final VaultGoalMessage pkt = VaultGoalMessage.cakeHunt(this.maxCakeCount, this.cakeCount);
            ModNetwork.CHANNEL.sendTo(pkt, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }));
        vault.getGenerator().getPieces(VaultRoom.class).forEach(room -> this.ensureCakeIsPresent(world, (VaultRoom) room));
        if (this.cakeCount >= this.maxCakeCount) {
            this.setCompleted();
            this.spawnRewards(world, vault);
        } else if (world.getGameTime() % 300L == 0L) {
            vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, playerEntity -> vault.getGenerator().getPiecesAt(playerEntity.blockPosition(), VaultRoom.class).stream().findFirst().ifPresent((room) -> {
                if (!((VaultRoom) room).isCakeEaten()) {
                    final BlockPos cakePos = ((VaultRoom) room).getCakePos();
                    if (cakePos != null) {
                        final int bDst = (int) MathHelper.sqrt(playerEntity.blockPosition().distSqr((Vector3i) cakePos));
                        ;
                        final ITextComponent dist = new StringTextComponent("Distance to cake: " + bDst + "m").withStyle(TextFormatting.GRAY);
                        playerEntity.displayClientMessage(dist, true);
                    }
                }
            })));
        }
    }

    private void ensureCakeIsPresent(final ServerWorld world, final VaultRoom room) {
        if (room.isCakeEaten()) {
            return;
        }
        final MutableBoundingBox roomBox = room.getBoundingBox();
        if (room.getCakePos() == null) {
            for (int xx = roomBox.x0; xx <= roomBox.x1; ++xx) {
                for (int yy = roomBox.y0; yy <= roomBox.y1; ++yy) {
                    for (int zz = roomBox.z0; zz <= roomBox.z1; ++zz) {
                        final BlockPos pos = new BlockPos(xx, yy, zz);
                        final BlockState state = world.getBlockState(pos);
                        if (state.getBlock() instanceof CakeBlock) {
                            world.removeBlock(pos, false);
                        }
                    }
                }
            }
            BlockPos at;
            do {
                at = MiscUtils.getRandomPos(roomBox, CakeHuntObjective.rand);
            } while (!world.isEmptyBlock(at) || !world.getBlockState(at.below()).isFaceSturdy((IBlockReader) world, at, Direction.UP));
            world.setBlock(at, Blocks.CAKE.defaultBlockState(), 2);
            room.setCakePos(at);
        } else {
            for (int xx = roomBox.x0; xx <= roomBox.x1; ++xx) {
                for (int yy = roomBox.y0; yy <= roomBox.y1; ++yy) {
                    for (int zz = roomBox.z0; zz <= roomBox.z1; ++zz) {
                        final BlockPos pos = new BlockPos(xx, yy, zz);
                        final BlockState state = world.getBlockState(pos);
                        if (state.getBlock() instanceof CakeBlock) {
                            return;
                        }
                    }
                }
            }
            BlockPos at;
            do {
                at = MiscUtils.getRandomPos(roomBox, CakeHuntObjective.rand);
            } while (!world.isEmptyBlock(at) || !world.getBlockState(at.below()).isFaceSturdy((IBlockReader) world, at, Direction.UP));
            world.setBlock(at, Blocks.CAKE.defaultBlockState(), 2);
            room.setCakePos(at);
        }
    }

    @Nonnull
    @Override
    public BlockState getObjectiveRelevantBlock() {
        return Blocks.AIR.defaultBlockState();
    }

    @Nullable
    @Override
    public LootTable getRewardLootTable(final VaultRaid vault, final Function<ResourceLocation, LootTable> tblResolver) {
        final int level = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
        final LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(level);
        return (config != null) ? tblResolver.apply(config.getBossCrate()) : LootTable.EMPTY;
    }

    @Override
    public ITextComponent getObjectiveDisplayName() {
        return (ITextComponent) new StringTextComponent("Cake Hunt").withStyle(TextFormatting.DARK_PURPLE);
    }

    @Override
    public void setObjectiveTargetCount(final int amount) {
        this.maxCakeCount = amount;
    }

    @Nullable
    @Override
    public ITextComponent getObjectiveTargetDescription(final int amount) {
        return (ITextComponent) new StringTextComponent("Cakes needed to be found: ").append((ITextComponent) new StringTextComponent(String.valueOf(amount)).withStyle(TextFormatting.DARK_PURPLE));
    }

    @Nullable
    @Override
    public VaultRoomLayoutGenerator getCustomLayout() {
        return new SingularVaultRoomLayout();
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        tag.putInt("maxCakeCount", this.maxCakeCount);
        tag.putInt("cakeCount", this.cakeCount);
        return tag;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.maxCakeCount = nbt.getInt("maxCakeCount");
        this.cakeCount = nbt.getInt("cakeCount");
    }
}
