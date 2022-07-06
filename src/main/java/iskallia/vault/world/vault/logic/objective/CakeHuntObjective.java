package iskallia.vault.world.vault.logic.objective;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import iskallia.vault.world.vault.gen.layout.SingularVaultRoomLayout;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;

import javax.annotation.Nullable;

import iskallia.vault.config.LootTablesConfig;
import net.minecraft.loot.LootTable;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.block.Blocks;
import net.minecraft.world.IBlockReader;
import net.minecraft.block.CakeBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.network.NetworkDirection;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.init.ModItems;
import net.minecraft.util.IItemProvider;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.item.ItemEntity;
import iskallia.vault.block.VaultCrateBlock;
import iskallia.vault.init.ModBlocks;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootContext;
import iskallia.vault.world.vault.player.VaultRunner;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.text.ITextComponent;

import java.util.Set;

import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import iskallia.vault.util.MiscUtils;

import java.util.Collections;
import java.util.Collection;

import iskallia.vault.world.vault.modifier.VaultModifier;

import java.util.ArrayList;

import iskallia.vault.world.vault.modifier.ArtifactChanceModifier;
import iskallia.vault.world.vault.modifier.InventoryRestoreModifier;
import iskallia.vault.world.vault.modifier.TimerModifier;
import iskallia.vault.world.vault.modifier.NoExitModifier;
import iskallia.vault.init.ModConfigs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;

import java.util.function.Function;
import java.util.Optional;

import iskallia.vault.world.vault.gen.piece.VaultPiece;

import java.util.List;

import net.minecraft.util.math.vector.Vector3i;
import iskallia.vault.world.gen.decorator.BreadcrumbFeature;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import net.minecraft.util.Direction;
import iskallia.vault.world.vault.gen.layout.SpiralHelper;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.Vault;
import iskallia.vault.world.vault.logic.task.VaultTask;
import net.minecraft.util.ResourceLocation;
import iskallia.vault.config.VaultModifiersConfig;

import java.util.UUID;

public class CakeHuntObjective extends VaultObjective {
    public static UUID PENALTY;
    private int maxCakeCount;
    private int cakeCount;
    private float modifierChance;
    private VaultModifiersConfig.ModifierPoolType poolType;
    private float healthPenalty;
    private ResourceLocation roomPool;
    private ResourceLocation tunnelPool;

    public CakeHuntObjective(final ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
        this.maxCakeCount = 22 + CakeHuntObjective.rand.nextInt(9);
        this.cakeCount = 0;
        this.modifierChance = 0.75f;
        this.poolType = VaultModifiersConfig.ModifierPoolType.DEFAULT;
        this.roomPool = Vault.id("vault/rooms");
        this.tunnelPool = Vault.id("vault/tunnels");
    }

    public void setModifierChance(final float modifierChance) {
        this.modifierChance = modifierChance;
    }

    public void setPoolType(final VaultModifiersConfig.ModifierPoolType poolType) {
        this.poolType = poolType;
    }

    public void setHealthPenalty(final float healthPenalty) {
        this.healthPenalty = healthPenalty;
    }

    public void setRoomPool(final ResourceLocation roomPool) {
        this.roomPool = roomPool;
    }

    public void setTunnelPool(final ResourceLocation tunnelPool) {
        this.tunnelPool = tunnelPool;
    }

    public void expandVault(final ServerWorld world, final ServerPlayerEntity player, final BlockPos cakePos, final VaultRaid vault) {
        vault.getGenerator().getPiecesAt(cakePos, VaultRoom.class).stream().findAny().ifPresent(room -> vault.getProperties().getBase(VaultRaid.START_FACING).ifPresent(vaultDir -> {
            ++this.cakeCount;
            if (this.cakeCount < this.maxCakeCount) {
                this.addRandomModifier(vault, world, player);
                final Vector3i curr = SpiralHelper.getSpiralPosition(this.cakeCount - 1, vaultDir.getClockWise(), vaultDir);
                final Vector3i next = SpiralHelper.getSpiralPosition(this.cakeCount, vaultDir.getClockWise(), vaultDir);
                final Direction direction = Direction.getNearest((float) (next.getX() - curr.getX()), 0.0f, (float) (next.getZ() - curr.getZ()));
                final int level = vault.getProperties().getBaseOrDefault(VaultRaid.LEVEL, 0);
                final List<VaultPiece> generatedPieces = VaultJigsawHelper.expandVault(vault, world, (VaultRoom) room, direction, VaultJigsawHelper.getRandomPiece(this.roomPool, level), VaultJigsawHelper.getRandomPiece(this.tunnelPool, level));
                generatedPieces.stream().filter(piece -> piece instanceof VaultRoom).findFirst().ifPresent(newRoomPiece -> this.ensureCakeIsPresent(world, (VaultRoom) newRoomPiece));
                if (!vault.getProperties().exists(VaultRaid.PARENT)) {
                    BreadcrumbFeature.generateVaultBreadcrumb(vault, world, generatedPieces);
                }
            }
        }));
    }

    private void addRandomModifier(final VaultRaid vault, final ServerWorld sWorld, final ServerPlayerEntity player) {
        if (this.healthPenalty != 0.0f) {
            vault.getPlayers().stream().map(p -> p.getServerPlayer(sWorld.getServer())).filter(Optional::isPresent).map(Optional::get).forEach(other -> {
                final ModifiableAttributeInstance attribute = other.getAttribute(Attributes.MAX_HEALTH);
                if (attribute == null) {
                    return;
                } else {
                    attribute.removeModifier(CakeHuntObjective.PENALTY);
                    final double amount = this.cakeCount * this.healthPenalty;
                    final double amount2 = Math.min(amount, attribute.getValue() - 2.0);
                    final AttributeModifier modifier2 = new AttributeModifier(CakeHuntObjective.PENALTY, "Cake Health Penalty", -amount2, AttributeModifier.Operation.ADDITION);
                    attribute.addTransientModifier(modifier2);
                    return;
                }
            });
        }
        if (sWorld.getRandom().nextFloat() >= this.modifierChance) {
            return;
        }
        final int level = vault.getProperties().getValue(VaultRaid.LEVEL);
        final Set<VaultModifier> modifiers = ModConfigs.VAULT_MODIFIERS.getRandom(CakeHuntObjective.rand, level, this.poolType, null);
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
            final ITextComponent c0 = (ITextComponent) player.getDisplayName().copy().withStyle(TextFormatting.LIGHT_PURPLE);
            final ITextComponent c2 = (ITextComponent) new StringTextComponent(" found a ").withStyle(TextFormatting.GRAY);
            final ITextComponent c3 = (ITextComponent) new StringTextComponent("cake").withStyle(TextFormatting.GREEN);
            final ITextComponent c4 = (ITextComponent) new StringTextComponent(" and added ").withStyle(TextFormatting.GRAY);
            final ITextComponent c5 = modifier.getNameComponent();
            final ITextComponent c6 = (ITextComponent) new StringTextComponent(".").withStyle(TextFormatting.GRAY);
            final ITextComponent ct = (ITextComponent) new StringTextComponent("").append(c0).append(c2).append(c3).append(c4).append(c5).append(c6);
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
            vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, playerEntity -> vault.getGenerator().getPiecesAt(playerEntity.blockPosition(), VaultRoom.class).stream().findFirst().ifPresent(room -> {
                if (!((VaultRoom) room).isCakeEaten()) {
                    final BlockPos cakePos = ((VaultRoom) room).getCakePos();
                    if (cakePos != null) {
                        final int bDst = (int) MathHelper.sqrt(playerEntity.blockPosition().distSqr((Vector3i) cakePos));

                        final StringTextComponent stringTextComponent = new StringTextComponent("Distance to cake: " + bDst + "m");
                        final ITextComponent dist = (ITextComponent) stringTextComponent.withStyle(TextFormatting.GRAY);
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
            } while (!world.isEmptyBlock(at) || !world.getBlockState(at.below()).isFaceSturdy((IBlockReader) world, at, Direction.UP) || !world.isEmptyBlock(at.above()));
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
            } while (!world.isEmptyBlock(at) || !world.getBlockState(at.below()).isFaceSturdy((IBlockReader) world, at, Direction.UP) || !world.isEmptyBlock(at.above()));
            world.setBlock(at, Blocks.CAKE.defaultBlockState(), 2);
            room.setCakePos(at);
        }
    }

    @Nonnull
    @Override
    public BlockState getObjectiveRelevantBlock(final VaultRaid vault, final ServerWorld world, final BlockPos pos) {
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
        final SingularVaultRoomLayout layout = new SingularVaultRoomLayout();
        layout.setRoomId(this.roomPool);
        layout.setTunnelId(this.tunnelPool);
        return layout;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        tag.putInt("maxCakeCount", this.maxCakeCount);
        tag.putInt("cakeCount", this.cakeCount);
        tag.putFloat("modifierChance", this.modifierChance);
        tag.putInt("poolType", this.poolType.ordinal());
        tag.putFloat("healthPenalty", this.healthPenalty);
        tag.putString("roomPool", this.roomPool.toString());
        tag.putString("tunnelPool", this.tunnelPool.toString());
        return tag;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.maxCakeCount = nbt.getInt("maxCakeCount");
        this.cakeCount = nbt.getInt("cakeCount");
        this.modifierChance = nbt.getFloat("modifierChance");
        if (nbt.contains("poolType", 3)) {
            this.poolType = VaultModifiersConfig.ModifierPoolType.values()[nbt.getInt("poolType")];
        }
        this.healthPenalty = nbt.getFloat("healthPenalty");
        if (nbt.contains("roomPool", 8)) {
            this.roomPool = new ResourceLocation(nbt.getString("roomPool"));
        }
        if (nbt.contains("tunnelPool", 8)) {
            this.tunnelPool = new ResourceLocation(nbt.getString("tunnelPool"));
        }
    }

    static {
        CakeHuntObjective.PENALTY = UUID.fromString("5AD3F258-FEE1-4E67-B885-69FD380BB150");
    }
}
