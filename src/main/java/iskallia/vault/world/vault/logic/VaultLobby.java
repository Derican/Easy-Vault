// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic;

import iskallia.vault.util.nbt.NBTHelper;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.INBT;

import java.util.*;
import java.util.function.Consumer;

import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.AxisAlignedBB;
import iskallia.vault.init.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.Item;

import java.util.function.Function;

import net.minecraft.util.text.ChatType;
import iskallia.vault.init.ModItems;
import iskallia.vault.world.data.ScheduledItemDropData;
import iskallia.vault.block.item.VaultChampionTrophyBlockItem;
import net.minecraft.util.math.MathHelper;
import iskallia.vault.block.VaultChampionTrophy;
import iskallia.vault.world.data.FinalVaultData;

import java.util.function.ToIntFunction;

import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import iskallia.vault.block.GodEyeBlock;
import iskallia.vault.world.vault.player.VaultMember;
import iskallia.vault.world.vault.logic.behaviour.VaultBehaviour;
import iskallia.vault.world.vault.player.VaultRunner;
import iskallia.vault.world.vault.logic.objective.raid.RaidChallengeObjective;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.vault.logic.objective.TreasureHuntObjective;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectSummonAndKillBossesObjective;
import iskallia.vault.Vault;
import iskallia.vault.config.VaultModifiersConfig;

import javax.annotation.Nullable;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import iskallia.vault.init.ModNetwork;
import net.minecraft.util.text.TextFormatting;
import iskallia.vault.network.message.EffectMessage;
import iskallia.vault.init.ModSounds;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import net.minecraft.util.ResourceLocation;
import iskallia.vault.util.ServerScheduler;
import net.minecraft.network.IPacket;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.IItemProvider;
import net.minecraft.item.Items;
import net.minecraft.entity.projectile.FireworkRocketEntity;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import iskallia.vault.world.vault.gen.piece.VaultGodEye;
import iskallia.vault.item.crystal.CrystalData;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import iskallia.vault.attribute.UUIDAttribute;
import iskallia.vault.attribute.VAttribute;
import net.minecraft.entity.player.ServerPlayerEntity;
import iskallia.vault.world.data.VaultRaidData;

import java.util.regex.Pattern;

import iskallia.vault.world.vault.gen.piece.VaultPortal;
import iskallia.vault.world.vault.logic.objective.CakeHuntObjective;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.player.VaultPlayer;
import iskallia.vault.world.vault.VaultRaid;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import iskallia.vault.world.data.InventorySnapshotData;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.nbt.StringNBT;
import iskallia.vault.nbt.VListNBT;
import iskallia.vault.world.vault.logic.task.IVaultTask;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class VaultLobby implements INBTSerializable<CompoundNBT>, IVaultTask {
    protected List<Branch> branches;
    protected VListNBT<UUID, StringNBT> players;
    protected Map<PlayerFavourData.VaultGodType, List<Integer>> scores;
    protected UUID bossVaultId;
    public InventorySnapshotData snapshots;

    public VaultLobby() {
        this.branches = new ArrayList<Branch>();
        this.players = VListNBT.ofUUID();
        this.scores = new HashMap<PlayerFavourData.VaultGodType, List<Integer>>();
        this.bossVaultId = null;
        this.snapshots = new InventorySnapshotData("dummy") {
            @Override
            protected boolean shouldSnapshotItem(final PlayerEntity player, final ItemStack stack) {
                return true;
            }

            @Override
            public void createSnapshot(final PlayerEntity player) {
                this.snapshotData.put(player.getUUID(), new Builder(player).setStackFilter(this::shouldSnapshotItem).replaceExisting().createSnapshot());
                this.setDirty();
            }

            @Override
            public boolean restoreSnapshot(final PlayerEntity player) {
                final InventorySnapshot snapshot = this.snapshotData.get(player.getUUID());
                return snapshot != null && snapshot.apply(player);
            }
        };
    }

    public Branch getOrCreate(final UUID portal, final Supplier<Branch> supplier) {
        Optional<Branch> opt = this.branches.stream().filter(g -> g.portalId.equals(portal)).findFirst();
        if (!opt.isPresent()) {
            final Branch branch = supplier.get();
            this.branches.add(branch);
            opt = Optional.of(branch);
        }
        return opt.get();
    }

    public void execute(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        if (!this.players.contains(player.getPlayerId())) {
            this.players.add(player.getPlayerId());
        }
        player.runIfPresent(world.getServer(), sPlayer -> {
            this.tickFinalBoss(vault, world, sPlayer);
            final ModifiableAttributeInstance attribute = sPlayer.getAttribute(Attributes.MAX_HEALTH);
            if (attribute != null) {
                attribute.removeModifier(CakeHuntObjective.PENALTY);
            }
            if (sPlayer.isAlive() && this.snapshots.restoreSnapshot((PlayerEntity) sPlayer)) {
                this.snapshots.removeSnapshot((PlayerEntity) sPlayer);
            }
            final Set<VaultPiece> portals = vault.getGenerator().getPiecesAt(sPlayer.blockPosition(), VaultPortal.class);
            if (!portals.isEmpty()) {
                final VaultPortal portal = (VaultPortal) portals.iterator().next();
                if (!(!this.isInPortal(world, sPlayer))) {
                    final String[] split = portal.getTemplate().getPath().split(Pattern.quote("_"));
                    final PlayerFavourData.VaultGodType type = this.fromColor(split[split.length - 1]);
                    if (type != null) {
                        final Branch branch = this.getOrCreate(portal.getUUID(), () -> new Branch(portal.getUUID(), type));
                        if (branch.vaultId == null || VaultRaidData.get(world).get(branch.vaultId) == null) {
                            final CrystalData data = this.createCrystalData(branch);
                            if (data != null) {
                                final VaultRaid.Builder builder = data.createVault(world, null);
                                VaultRaidData.get(world).startVault(world, builder, v -> {
                                    v.getProperties().create(VaultRaid.LEVEL, 300);
                                    v.getProperties().create(VaultRaid.FORCE_ACTIVE, true);
                                    v.getProperties().create(VaultRaid.PARENT, (UUID) vault.getProperties().getValue(VaultRaid.IDENTIFIER));
                                    branch.vaultId = v.getProperties().getBase(VaultRaid.IDENTIFIER).orElse(null);
                                    this.initialize(branch, v);
                                    return;
                                });
                                sPlayer.setPortalCooldown();
                            }
                        }
                        world.getServer().submit(() -> {
                            if (branch.vaultId != null) {
                                final VaultRaid target = VaultRaidData.get(world).get(branch.vaultId);
                                if (target != null) {
                                    if (VaultRaidData.get(world).getActiveFor(player.getPlayerId()) != VaultRaidData.get(world).get(branch.vaultId)) {
                                        vault.getPlayers().remove(player);
                                        this.snapshots.createSnapshot((PlayerEntity) sPlayer);
                                        this.joinVault(target, sPlayer, world, branch);
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void tickFinalBoss(final VaultRaid vault, final ServerWorld world, final ServerPlayerEntity sPlayer) {
        if (this.bossVaultId == null) {
            final List<VaultPiece> allEyes = new ArrayList<>(vault.getGenerator().getPieces(VaultGodEye.class));
            final List<VaultPiece> filledEyes = allEyes.stream().filter(eye -> ((VaultGodEye) eye).isLit(world)).collect(Collectors.toList());
            if (allEyes.size() != 0 && allEyes.size() == filledEyes.size()) {
                this.bossVaultId = UUID.randomUUID();
                VaultRaid v = null;
                for (final Branch branch : this.branches) {
                    v = VaultRaidData.get(world).get(branch.vaultId);
                    if (v == null) {
                        continue;
                    }
                    for (final VaultPlayer player : v.getPlayers()) {
                        VaultRaid.EXIT_SAFELY.execute(v, player, world);
                    }
                    v.getProperties().create(VaultRaid.FORCE_ACTIVE, false);
                }
                for (final VaultPlayer vPlayer : vault.getPlayers()) {
                    vPlayer.runIfPresent(world.getServer(), playerEntity -> {

                        final FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity((World) world, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), new ItemStack((IItemProvider) Items.FIREWORK_ROCKET));
                        world.addFreshEntity((Entity) fireworkRocketEntity);
                        world.playSound((PlayerEntity) null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1.0f, 1.0f);
                        final StringTextComponent title = new StringTextComponent("The Final Challenge");
                        title.setStyle(Style.EMPTY.withColor(Color.fromRgb(14491166)));
                        final StringTextComponent subtitle = new StringTextComponent("The Gods are watching...");
                        subtitle.setStyle(Style.EMPTY.withColor(Color.fromRgb(14491166)));
                        final STitlePacket titlePacket = new STitlePacket(STitlePacket.Type.TITLE, (ITextComponent) title);
                        final STitlePacket subtitlePacket = new STitlePacket(STitlePacket.Type.SUBTITLE, (ITextComponent) subtitle);
                        playerEntity.connection.send((IPacket) titlePacket);
                        playerEntity.connection.send((IPacket) subtitlePacket);
                        return;
                    });
                }
                ServerScheduler.INSTANCE.schedule(100, () -> this.countdown(vault, world, 15));
                ServerScheduler.INSTANCE.schedule(120, () -> this.countdown(vault, world, 14));
                ServerScheduler.INSTANCE.schedule(140, () -> this.countdown(vault, world, 13));
                ServerScheduler.INSTANCE.schedule(160, () -> this.countdown(vault, world, 12));
                ServerScheduler.INSTANCE.schedule(180, () -> this.countdown(vault, world, 11));
                ServerScheduler.INSTANCE.schedule(200, () -> this.countdown(vault, world, 10));
                ServerScheduler.INSTANCE.schedule(220, () -> this.countdown(vault, world, 9));
                ServerScheduler.INSTANCE.schedule(240, () -> this.countdown(vault, world, 8));
                ServerScheduler.INSTANCE.schedule(260, () -> this.countdown(vault, world, 7));
                ServerScheduler.INSTANCE.schedule(280, () -> this.countdown(vault, world, 6));
                ServerScheduler.INSTANCE.schedule(300, () -> this.countdown(vault, world, 5));
                ServerScheduler.INSTANCE.schedule(320, () -> this.countdown(vault, world, 4));
                ServerScheduler.INSTANCE.schedule(340, () -> this.countdown(vault, world, 3));
                ServerScheduler.INSTANCE.schedule(360, () -> this.countdown(vault, world, 2));
                ServerScheduler.INSTANCE.schedule(380, () -> this.countdown(vault, world, 1));
                ServerScheduler.INSTANCE.schedule(400, () -> {
                    final CrystalData data = new CrystalData();
                    data.setCanGenerateTreasureRooms(false);
                    data.setCanTriggerInfluences(false);
                    data.setType(CrystalData.Type.FINAL_BOSS);
                    data.setSelectedObjective((ResourceLocation) VaultObjective.REGISTRY.inverse().get((Object) VaultRaid.KILL_THE_BOSS));
                    final VaultRaid.Builder builder = data.createVault(world, sPlayer);

                    for (VaultPlayer player2 : vault.getPlayers()) {
                        player2.runIfPresent(world.getServer(), p -> this.snapshots.createSnapshot((PlayerEntity) p));
                    }
                    VaultRaidData.get(world).startVault(world, builder, v1 -> {
                        v1.getProperties().create(VaultRaid.PARENT, (UUID) vault.getProperties().getValue(VaultRaid.IDENTIFIER));
                        this.bossVaultId = v1.getProperties().getBase(VaultRaid.IDENTIFIER).orElse(null);
                        vault.getPlayers().clear();
                        vault.getProperties().create(VaultRaid.LOBBY, this);
                    });
                });
            }
        }
    }

    public void countdown(final VaultRaid vault, final ServerWorld world, final int secondsLeft) {
        final EffectMessage msg = EffectMessage.playSound(ModSounds.TIMER_PANIC_TICK_SFX, SoundCategory.MASTER, 1.0f, 0.4f);
        for (final VaultPlayer vPlayer : vault.getPlayers()) {
            vPlayer.runIfPresent(world.getServer(), player -> {
                final IFormattableTextComponent title = new StringTextComponent(String.valueOf(secondsLeft)).withStyle(TextFormatting.BOLD);
                title.setStyle(Style.EMPTY.withColor(Color.fromRgb(14491166)));
                final STitlePacket titlePacket = new STitlePacket(STitlePacket.Type.TITLE, (ITextComponent) title);
                final STitlePacket subtitlePacket = new STitlePacket(STitlePacket.Type.SUBTITLE, StringTextComponent.EMPTY);
                player.connection.send((IPacket) titlePacket);
                player.connection.send((IPacket) subtitlePacket);
                ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), (Object) msg);
            });
        }
    }

    @Nullable
    public Branch getBranch(final UUID vaultId) {
        for (final Branch branch : this.branches) {
            if (branch.vaultId.equals(vaultId)) {
                return branch;
            }
        }
        return null;
    }

    private PlayerFavourData.VaultGodType fromColor(final String color) {
        if ("green".equals(color)) {
            return PlayerFavourData.VaultGodType.BENEVOLENT;
        }
        if ("blue".equals(color)) {
            return PlayerFavourData.VaultGodType.OMNISCIENT;
        }
        if ("yellow".equals(color)) {
            return PlayerFavourData.VaultGodType.TIMEKEEPER;
        }
        if ("red".equals(color)) {
            return PlayerFavourData.VaultGodType.MALEVOLENCE;
        }
        return null;
    }

    private CrystalData createCrystalData(final Branch branch) {
        CrystalData data = new CrystalData();
        data.setCanGenerateTreasureRooms(false);
        data.setCanTriggerInfluences(false);
        if (branch.type == PlayerFavourData.VaultGodType.BENEVOLENT) {
            data.setType(CrystalData.Type.FINAL_VELARA);
            data.setSelectedObjective((ResourceLocation) VaultObjective.REGISTRY.inverse().get((Object) VaultRaid.CAKE_HUNT));
            data.setTargetObjectiveCount(42);
        } else if (branch.type == PlayerFavourData.VaultGodType.OMNISCIENT) {
            data.setType(CrystalData.Type.FINAL_TENOS);
            data.setSelectedObjective((ResourceLocation) VaultObjective.REGISTRY.inverse().get((Object) VaultRaid.ARCHITECT_KILL_ALL_BOSSES));
        } else if (branch.type == PlayerFavourData.VaultGodType.TIMEKEEPER) {
            data.setType(CrystalData.Type.FINAL_WENDARR);
            data.setSelectedObjective((ResourceLocation) VaultObjective.REGISTRY.inverse().get((Object) VaultRaid.TREASURE_HUNT));
        } else if (branch.type == PlayerFavourData.VaultGodType.MALEVOLENCE) {
            data.setType(CrystalData.Type.FINAL_IDONA);
            data.setSelectedObjective((ResourceLocation) VaultObjective.REGISTRY.inverse().get((Object) VaultRaid.RAID_CHALLENGE));
            data.setTargetObjectiveCount(10);
        } else {
            data = null;
        }
        return data;
    }

    private void initialize(final Branch branch, final VaultRaid vault) {
        if (branch.type == PlayerFavourData.VaultGodType.BENEVOLENT) {
            vault.getActiveObjective(CakeHuntObjective.class).ifPresent(cakeHunt -> {
                cakeHunt.setModifierChance(1.0f);
                cakeHunt.setPoolType(VaultModifiersConfig.ModifierPoolType.FINAL_VELARA_ADDS);
                cakeHunt.setHealthPenalty(2.0f);
                cakeHunt.setRoomPool(Vault.id("final_vault/velara/rooms"));
                cakeHunt.setTunnelPool(Vault.id("final_vault/velara/tunnels"));
                return;
            });
            vault.getProperties().create(VaultRaid.COW_VAULT, true);
            vault.getEvents().add(VaultRaid.REPLACE_WITH_COW);
        } else if (branch.type == PlayerFavourData.VaultGodType.OMNISCIENT) {
            vault.getActiveObjective(ArchitectSummonAndKillBossesObjective.class).ifPresent(killAll -> {
                killAll.setRoomPool(Vault.id("final_vault/tenos/rooms"));
                killAll.setTunnelPool(Vault.id("final_vault/tenos/tunnels"));
            });
        } else if (branch.type == PlayerFavourData.VaultGodType.TIMEKEEPER) {
            vault.getActiveObjective(TreasureHuntObjective.class).ifPresent(treasureHunt -> {
                treasureHunt.setSandPerModifier((this.players.size() > 1) ? ModConfigs.TREASURE_HUNT.mpSandTrigger : ModConfigs.TREASURE_HUNT.spSandTrigger);
                treasureHunt.setRoomPool(Vault.id("final_vault/wendarr/rooms"));
                treasureHunt.setTunnelPool(Vault.id("final_vault/wendarr/tunnels"));
            });
        } else if (branch.type == PlayerFavourData.VaultGodType.MALEVOLENCE) {
            vault.getActiveObjective(RaidChallengeObjective.class).ifPresent(raid -> {
                raid.setRoomPool(Vault.id("final_vault/idona/rooms"));
                raid.setTunnelPool(Vault.id("final_vault/idona/tunnels"));
            });
        }
    }

    private void joinVault(final VaultRaid vault, final ServerPlayerEntity player, final ServerWorld world, final Branch branch) {
        final VaultRunner runner = new VaultRunner(player.getUUID());
        if (branch.type == PlayerFavourData.VaultGodType.BENEVOLENT) {
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_OBJECTIVES_LEFT_GLOBALLY, VaultRaid.EXIT_SAFELY));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_TIME_LEFT, VaultRaid.EXIT_DEATH));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER.and(VaultRaid.IS_DEAD), VaultRaid.EXIT_DEATH_ALL_NO_SAVE));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_FINISHED.negate(), VaultRaid.TICK_SPAWNER.then(VaultRaid.TICK_CHEST_PITY)));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.AFTER_GRACE_PERIOD.and(VaultRaid.IS_FINISHED.negate()), VaultRaid.TICK_INFLUENCES));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER, VaultRaid.PAUSE_IN_ARENA.then(VaultRaid.CHECK_BAIL_FINAL)));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_ACTIVE_RUNNERS_LEFT, VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.EXIT_SAFELY)));
            runner.getProperties().create(VaultRaid.SPAWNER, new VaultSpawner());
            runner.getTimer().start(30000);
        } else if (branch.type == PlayerFavourData.VaultGodType.OMNISCIENT) {
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_OBJECTIVES_LEFT_GLOBALLY, VaultRaid.EXIT_SAFELY));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_TIME_LEFT, VaultRaid.EXIT_DEATH));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER.and(VaultRaid.IS_DEAD), VaultRaid.EXIT_DEATH_ALL_NO_SAVE));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_FINISHED.negate(), VaultRaid.TICK_SPAWNER.then(VaultRaid.TICK_CHEST_PITY)));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.AFTER_GRACE_PERIOD.and(VaultRaid.IS_FINISHED.negate()), VaultRaid.TICK_INFLUENCES));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER, VaultRaid.PAUSE_IN_ARENA.then(VaultRaid.CHECK_BAIL_FINAL)));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_ACTIVE_RUNNERS_LEFT, VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.EXIT_SAFELY)));
            runner.getProperties().create(VaultRaid.SPAWNER, new VaultSpawner());
            runner.getTimer().start(30000);
        } else if (branch.type == PlayerFavourData.VaultGodType.TIMEKEEPER) {
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_OBJECTIVES_LEFT_GLOBALLY, VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.EXIT_SAFELY)));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_TIME_LEFT, VaultRaid.EXIT_DEATH));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER.and(VaultRaid.IS_DEAD), VaultRaid.EXIT_DEATH_ALL_NO_SAVE));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_FINISHED.negate(), VaultRaid.TICK_SPAWNER.then(VaultRaid.TICK_CHEST_PITY)));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.AFTER_GRACE_PERIOD.and(VaultRaid.IS_FINISHED.negate()), VaultRaid.TICK_INFLUENCES));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER, VaultRaid.PAUSE_IN_ARENA.then(VaultRaid.CHECK_BAIL_FINAL)));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_ACTIVE_RUNNERS_LEFT, VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.EXIT_SAFELY)));
            runner.getProperties().create(VaultRaid.SPAWNER, new VaultSpawner());
            runner.getTimer().start(ModConfigs.TREASURE_HUNT.startTicks);
        } else {
            if (branch.type != PlayerFavourData.VaultGodType.MALEVOLENCE) {
                return;
            }
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_OBJECTIVES_LEFT_GLOBALLY, VaultRaid.EXIT_SAFELY));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER.and(VaultRaid.IS_DEAD), VaultRaid.EXIT_DEATH_ALL_NO_SAVE));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_FINISHED.negate(), VaultRaid.TICK_SPAWNER.then(VaultRaid.TICK_CHEST_PITY)));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.AFTER_GRACE_PERIOD.and(VaultRaid.IS_FINISHED.negate()), VaultRaid.TICK_INFLUENCES));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.IS_RUNNER, VaultRaid.PAUSE_IN_ARENA.then(VaultRaid.CHECK_BAIL_FINAL)));
            runner.getBehaviours().add(new VaultBehaviour(VaultRaid.NO_ACTIVE_RUNNERS_LEFT, VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.EXIT_SAFELY)));
            runner.getProperties().create(VaultRaid.SPAWNER, new VaultSpawner());
            runner.getProperties().create(VaultRaid.SHOW_TIMER, false);
        }
        runner.getProperties().create(VaultRaid.LEVEL, 300);
        runner.getTimer().runTime = vault.getPlayers().stream().mapToInt(t -> t.getTimer().runTime).max().orElse(0);
        vault.getPlayers().add(runner);
        vault.getInitializer().execute(vault, runner, world);
    }

    public void exitVault(final VaultRaid vault, final ServerWorld world, final VaultRaid parent, final VaultMember member, final ServerPlayerEntity player, final boolean death) {
        if (vault.getProperties().getValue(VaultRaid.IDENTIFIER).equals(this.bossVaultId)) {
            if (death) {
                if (vault.getPlayers().size() == 1) {
                    final List<VaultPiece> godEyes = new ArrayList<>(parent.getGenerator().getPieces(VaultGodEye.class));
                    final VaultGodEye target = (VaultGodEye) godEyes.get(player.level.getRandom().nextInt(godEyes.size()));
                    final BlockState state = player.getLevel().getBlockState(target.getMin());
                    if (state.hasProperty((Property) GodEyeBlock.LIT)) {
                        player.getLevel().setBlockAndUpdate(target.getMin(), (BlockState) state.setValue((Property) GodEyeBlock.LIT, (Comparable) Boolean.FALSE));
                    }
                    this.bossVaultId = null;
                    parent.getProperties().create(VaultRaid.LOBBY, this);
                }
            } else {
                parent.getPlayers().removeIf(vPlayer -> vPlayer.getPlayerId().equals(player.getUUID()));
                parent.getProperties().create(VaultRaid.FORCE_ACTIVE, false);
                final int score = this.scores.values().stream().map(integers -> integers.stream().mapToInt(Integer::intValue).sum() / integers.size()).mapToInt(Integer::intValue).sum();
                final int id = FinalVaultData.get(world).getTimesCompleted(player.getUUID());
                FinalVaultData.get(world).onCompleted(player.getUUID());
                final ItemStack trophy = VaultChampionTrophyBlockItem.create(player, VaultChampionTrophy.Variant.values()[MathHelper.clamp(id, 0, VaultChampionTrophy.Variant.values().length - 1)]);
                VaultChampionTrophyBlockItem.setScore(trophy, score);
                ScheduledItemDropData.get(player.getLevel()).addDrop((PlayerEntity) player, trophy);
            }
            if (vault.getActiveObjectives().isEmpty()) {
                player.getLevel().getServer().submit(() -> VaultRaid.EXIT_SAFELY.execute(parent, member, player.getLevel()));
            }
            return;
        }
        if (vault.getPlayers().size() != 1 || !vault.getActiveObjectives().isEmpty()) {
            return;
        }
        final Optional<Branch> opt = this.branches.stream().filter(b -> b.vaultId.equals(vault.getProperties().getValue(VaultRaid.IDENTIFIER))).findFirst();
        opt.ifPresent(branch -> {
            Item item = null;
            final PlayerFavourData.VaultGodType type = branch.getType();
            switch (type) {
                case BENEVOLENT: {
                    item = ModItems.VELARA_ESSENCE;
                    break;
                }
                case OMNISCIENT: {
                    item = ModItems.TENOS_ESSENCE;
                    break;
                }
                case TIMEKEEPER: {
                    item = ModItems.WENDARR_ESSENCE;
                    break;
                }
                case MALEVOLENCE: {
                    item = ModItems.IDONA_ESSENCE;
                    break;
                }
            }
            if (item != null) {

                final ItemStack stack = new ItemStack((IItemProvider) item, (this.players.size() > 1) ? 1 : 2);
                player.inventory.add(stack);
                final String vaultName = (new String[]{"Velara's Gluttony", "Tenos' Puzzle", "Wendarr's Passage", "Idona's Wrath"})[type.ordinal()];
                final ITextComponent c0 = (ITextComponent) player.getDisplayName().copy().withStyle(TextFormatting.LIGHT_PURPLE);
                final ITextComponent c2 = (ITextComponent) new StringTextComponent(" completed ").withStyle(TextFormatting.GRAY);
                final ITextComponent c3 = (ITextComponent) new StringTextComponent(vaultName).withStyle(branch.type.getChatColor());
                final ITextComponent c4 = (ITextComponent) new StringTextComponent(" and was awarded ").withStyle(TextFormatting.GRAY);

                final StringTextComponent stringTextComponent = new StringTextComponent(branch.type.getName() + "'s Essence");
                final ITextComponent c5 = (ITextComponent) stringTextComponent.withStyle(branch.type.getChatColor());
                final ITextComponent c6 = (ITextComponent) new StringTextComponent("!").withStyle(TextFormatting.GRAY);
                final ITextComponent message = (ITextComponent) new StringTextComponent("").append(c0).append(c2).append(c3).append(c4).append(c5).append(c6);
                player.getServer().getPlayerList().broadcastMessage(message, ChatType.CHAT, player.getUUID());
                int score2 = 0;
                switch (type) {
                    case BENEVOLENT:
                    case OMNISCIENT: {
                        score2 = vault.getPlayers().stream().filter(vPlayer -> vPlayer.getPlayerId().equals(player.getUUID())).findFirst().map(vPlayer -> vPlayer.getTimer().getTimeLeft()).orElse(0) / 20;
                        break;
                    }
                    case TIMEKEEPER: {
                        score2 = vault.getObjective(TreasureHuntObjective.class).map(TreasureHuntObjective::getAddedSand).orElse(0);
                        break;
                    }
                    case MALEVOLENCE: {
                        final int score3 = vault.getObjective(RaidChallengeObjective.class).map(RaidChallengeObjective::getDamageTaken).map(dmgTaken -> 2000 - (int) (dmgTaken / 100.0)).orElse(0);
                        score2 = Math.max(0, score3);
                        break;
                    }
                }
                this.scores.computeIfAbsent(type, t -> new ArrayList()).add(score2);
                parent.getProperties().create(VaultRaid.LOBBY, this);
            }
        });
    }

    public boolean isInPortal(final ServerWorld world, final ServerPlayerEntity player) {
        final AxisAlignedBB box = player.getBoundingBox();
        final BlockPos min = new BlockPos(box.minX + 0.001, box.minY + 0.001, box.minZ + 0.001);
        final BlockPos max = new BlockPos(box.maxX - 0.001, box.maxY - 0.001, box.maxZ - 0.001);
        final BlockPos.Mutable pos = new BlockPos.Mutable();
        if (!world.hasChunksAt(min, max)) {
            return false;
        }
        if (player.isOnPortalCooldown()) {
            player.setPortalCooldown();
            return false;
        }
        for (int xx = min.getX(); xx <= max.getX(); ++xx) {
            for (int yy = min.getY(); yy <= max.getY(); ++yy) {
                for (int zz = min.getZ(); zz <= max.getZ(); ++zz) {
                    final BlockState state = world.getBlockState((BlockPos) pos.set(xx, yy, zz));
                    if (state.getBlock() == ModBlocks.VAULT_PORTAL) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        final ListNBT branchList = new ListNBT();
        this.branches.stream().map(Branch::serializeNBT).forEach(branchList::add);
        if (this.bossVaultId != null) {
            nbt.putString("BossVaultId", this.bossVaultId.toString());
        }
        nbt.put("Branches", (INBT) branchList);
        nbt.put("Snapshots", (INBT) this.snapshots.serializeNBT());
        nbt.put("Players", (INBT) this.players.serializeNBT());
        final CompoundNBT scores = new CompoundNBT();
        this.scores.forEach((type, scoreList) -> NBTHelper.writeList(scores, type.name(), scoreList, IntNBT.class, IntNBT::valueOf));
        nbt.put("scores", (INBT) scores);
        return nbt;
    }

    public void deserializeNBT(final CompoundNBT nbt) {
        if (nbt.contains("BossVaultId", 8)) {
            this.bossVaultId = UUID.fromString(nbt.getString("BossVaultId"));
        } else {
            this.bossVaultId = null;
        }
        this.players.deserializeNBT(nbt.getList("Players", 8));
        this.branches.clear();
        final ListNBT branchList = nbt.getList("Branches", 10);
        for (int i = 0; i < branchList.size(); ++i) {
            final Branch branch = new Branch();
            branch.deserializeNBT(branchList.getCompound(i));
            this.branches.add(branch);
        }
        this.snapshots.deserializeNBT(nbt.getCompound("Snapshots"));
        this.scores = new HashMap<PlayerFavourData.VaultGodType, List<Integer>>();
        final CompoundNBT scores = nbt.getCompound("scores");
        for (final String key : scores.getAllKeys()) {
            this.scores.put(PlayerFavourData.VaultGodType.valueOf(key), NBTHelper.readList(scores, key, IntNBT.class, IntNBT::getAsInt));
        }
    }

    public static class Branch implements INBTSerializable<CompoundNBT> {
        protected UUID portalId;
        protected UUID vaultId;
        protected PlayerFavourData.VaultGodType type;

        private Branch() {
        }

        public Branch(final UUID portalId, final PlayerFavourData.VaultGodType type) {
            this.portalId = portalId;
            this.type = type;
        }

        public UUID getPortalId() {
            return this.portalId;
        }

        public UUID getVaultId() {
            return this.vaultId;
        }

        public PlayerFavourData.VaultGodType getType() {
            return this.type;
        }

        public CompoundNBT serializeNBT() {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putString("Portal", this.portalId.toString());
            if (this.vaultId != null) {
                nbt.putString("Vault", this.vaultId.toString());
            }
            nbt.putString("Type", this.type.getName());
            return nbt;
        }

        public void deserializeNBT(final CompoundNBT nbt) {
            this.portalId = UUID.fromString(nbt.getString("Portal"));
            this.vaultId = (nbt.contains("Vault", 8) ? UUID.fromString(nbt.getString("Vault")) : null);
            this.type = PlayerFavourData.VaultGodType.fromName(nbt.getString("Type"));
        }
    }
}
