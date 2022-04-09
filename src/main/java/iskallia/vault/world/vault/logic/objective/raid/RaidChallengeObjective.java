// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.raid;

import com.google.common.collect.Lists;
import iskallia.vault.block.entity.VaultRaidControllerTileEntity;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.gen.decorator.BreadcrumbFeature;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.VaultGenerator;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.gen.piece.VaultRaidRoom;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.logic.objective.raid.modifier.BlockPlacementModifier;
import iskallia.vault.world.vault.logic.objective.raid.modifier.FloatingItemModifier;
import iskallia.vault.world.vault.logic.objective.raid.modifier.ModifierDoublingModifier;
import iskallia.vault.world.vault.logic.objective.raid.modifier.RaidModifier;
import iskallia.vault.world.vault.logic.task.VaultTask;
import iskallia.vault.world.vault.modifier.CatalystChanceModifier;
import iskallia.vault.world.vault.modifier.InventoryRestoreModifier;
import iskallia.vault.world.vault.modifier.LootableModifier;
import iskallia.vault.world.vault.modifier.VaultModifier;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RaidChallengeObjective extends VaultObjective {
    private final LinkedHashMap<RaidModifier, Float> modifierValues;
    private int completedRaids;
    private boolean started;

    public RaidChallengeObjective(final ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
        this.modifierValues = new LinkedHashMap<RaidModifier, Float>();
        this.completedRaids = 0;
        this.started = false;
    }

    @Nonnull
    @Override
    public BlockState getObjectiveRelevantBlock() {
        return Blocks.AIR.defaultBlockState();
    }

    @Nonnull
    @Override
    public Supplier<? extends VaultGenerator> getVaultGenerator() {
        return VaultRaid.RAID_CHALLENGE_GENERATOR;
    }

    @Nullable
    @Override
    public LootTable getRewardLootTable(final VaultRaid vault, final Function<ResourceLocation, LootTable> tblResolver) {
        return null;
    }

    @Override
    public boolean shouldPauseTimer(final MinecraftServer srv, final VaultRaid vault) {
        return !this.started;
    }

    @Override
    public ITextComponent getObjectiveDisplayName() {
        return (ITextComponent) new StringTextComponent("Raid").withStyle(TextFormatting.RED);
    }

    @Override
    public ITextComponent getVaultName() {
        return (ITextComponent) new StringTextComponent("Vault Raid");
    }

    public int getCompletedRaids() {
        return this.completedRaids;
    }

    public void addModifier(final RaidModifier modifier, final float value) {
        if (modifier instanceof ModifierDoublingModifier) {
            this.modifierValues.forEach(this::addModifier);
            return;
        }
        for (final RaidModifier existing : this.modifierValues.keySet()) {
            if (existing.getName().equals(modifier.getName())) {
                final float existingValue = this.modifierValues.get(existing);
                this.modifierValues.put(modifier, existingValue + value);
                return;
            }
        }
        this.modifierValues.put(modifier, value);
    }

    public Map<RaidModifier, Float> getAllModifiers() {
        return Collections.unmodifiableMap((Map<? extends RaidModifier, ? extends Float>) this.modifierValues);
    }

    public <T extends RaidModifier> Map<T, Float> getModifiersOfType(Class<T> modifierClass) {
        return (Map<T, Float>) this.modifierValues.entrySet().stream()
                .filter(modifierTpl -> modifierClass.isAssignableFrom(((RaidModifier) modifierTpl.getKey()).getClass()))
                .map(tpl -> tpl)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public LinkedHashMap<RaidModifier, Float> getModifiers(final boolean positive) {
        final LinkedHashMap<RaidModifier, Float> modifiers = new LinkedHashMap<RaidModifier, Float>();
        this.modifierValues.forEach((modifier, value) -> {
            if ((positive && modifier.isPositive()) || (!positive && !modifier.isPositive())) {
                modifiers.put(modifier, value);
            }
            return;
        });
        return modifiers;
    }

    @Override
    public void tick(final VaultRaid vault, final PlayerFilter filter, final ServerWorld world) {
        super.tick(vault, filter, world);
        final MinecraftServer srv = world.getServer();
        final ActiveRaid raid = vault.getActiveRaid();
        if (raid != null) {
            this.started = true;
        }
        this.sendRaidMessage(vault, filter, srv, raid);
    }

    private void sendRaidMessage(final VaultRaid vault, final PlayerFilter filter, final MinecraftServer srv, @Nullable final ActiveRaid raid) {
        final int wave = (raid == null) ? 0 : raid.getWave();
        final int totalWaves = (raid == null) ? 0 : raid.getTotalWaves();
        final int mobs = (raid == null) ? 0 : raid.getAliveEntities();
        final int totalMobs = (raid == null) ? 0 : raid.getTotalWaveEntities();
        final int startDelay = (raid == null) ? 0 : raid.getStartDelay();
        final List<ITextComponent> positives = new ArrayList<ITextComponent>();
        final List<ITextComponent> negatives = new ArrayList<ITextComponent>();
        this.modifierValues.forEach((modifier, value) -> {
            final ITextComponent display = modifier.getDisplay(value);
            if (modifier.isPositive()) {
                positives.add(display);
            } else {
                negatives.add(display);
            }
            return;
        });
        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(srv, playerEntity -> {
            final VaultGoalMessage pkt = VaultGoalMessage.raidChallenge(wave, totalWaves, mobs, totalMobs, startDelay, this.completedRaids, positives, negatives);
            ModNetwork.CHANNEL.sendTo(pkt, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }));
    }

    public void onRaidStart(final VaultRaid vault, final ServerWorld world, final ActiveRaid raid, final BlockPos controller) {
        final TileEntity te = world.getBlockEntity(controller);
        if (!(te instanceof VaultRaidControllerTileEntity)) {
            return;
        }
        final VaultRaidControllerTileEntity raidController = (VaultRaidControllerTileEntity) te;
        raidController.getRaidModifiers().forEach((modifierName, value) -> {
            final RaidModifier mod = ModConfigs.RAID_MODIFIER_CONFIG.getByName(modifierName);
            if (mod != null) {
                this.addModifier(mod, value);
            }
            return;
        });
        if (ModConfigs.RAID_EVENT_CONFIG.isEnabled() && RaidChallengeObjective.rand.nextFloat() < ModConfigs.RAID_EVENT_CONFIG.getViewerVoteChance()) {
            this.addRandomModifier(vault, world);
        }
    }

    private void addRandomModifier(final VaultRaid vault, final ServerWorld world) {
        VaultModifier vModifier;
        do {
            final String modifierName = ModConfigs.RAID_EVENT_CONFIG.getRandomModifier().getName();
            vModifier = ModConfigs.VAULT_MODIFIERS.getByName(modifierName);
        } while (vModifier == null);
        final VaultModifier fModifier = vModifier;
        final int minutes = ModConfigs.RAID_EVENT_CONFIG.getTemporaryModifierMinutes();
        final ITextComponent ct = (ITextComponent) new StringTextComponent("Added ").withStyle(TextFormatting.GRAY).append(vModifier.getNameComponent()).append((ITextComponent) new StringTextComponent(" for ").withStyle(TextFormatting.GRAY)).append((ITextComponent) new StringTextComponent(minutes + " minutes!").withStyle(TextFormatting.GOLD));
        vault.getModifiers().addTemporaryModifier(vModifier, minutes * 60 * 20);
        vault.getPlayers().forEach(vPlayer -> {
            fModifier.apply(vault, vPlayer, world, world.getRandom());
            vPlayer.runIfPresent(world.getServer(), sPlayer -> sPlayer.sendMessage(ct, Util.NIL_UUID));
        });
    }

    public void onRaidFinish(final VaultRaid vault, final ServerWorld world, final ActiveRaid raid, final BlockPos controller) {
        ++this.completedRaids;
        RaidModifier modifier = null;
        if (this.completedRaids % 10 == 0) {
            modifier = ModConfigs.RAID_MODIFIER_CONFIG.getByName("artifactFragment");
            if (modifier != null) {
                final boolean canGetArtifact = vault.getActiveModifiersFor(PlayerFilter.any(), InventoryRestoreModifier.class).stream().noneMatch(InventoryRestoreModifier::preventsArtifact);
                if (canGetArtifact) {
                    this.addModifier(modifier, MathUtilities.randomFloat(0.02f, 0.05f));
                }
            }
        }
        final VaultRaidRoom raidRoom = (VaultRaidRoom) vault.getGenerator().getPiecesAt(controller, VaultRaidRoom.class).stream().findFirst().orElse(null);
        if (raidRoom == null) {
            return;
        }
        for (final Direction direction : Direction.values()) {
            if (direction.getAxis() != Direction.Axis.Y) {
                if (VaultJigsawHelper.canExpand(vault, raidRoom, direction)) {
                    VaultJigsawHelper.expandVault(vault, world, raidRoom, direction, VaultJigsawHelper.getRaidChallengeRoom());
                }
            }
        }
        raidRoom.setRaidFinished();
        this.getAllModifiers().forEach((modifier1, value) -> modifier1.onVaultRaidFinish(vault, world, controller, raid, value));
        final AxisAlignedBB raidBoundingBox = raid.getRaidBoundingBox();
        final FloatingItemModifier catalystPlacement = new FloatingItemModifier("", 4, new WeightedList<SingleItemEntry>().add(new SingleItemEntry((IItemProvider) ModItems.VAULT_CATALYST_FRAGMENT), 1), "");
        vault.getActiveModifiersFor(PlayerFilter.any(), CatalystChanceModifier.class).forEach(modifier2 -> catalystPlacement.onVaultRaidFinish(vault, world, controller, raid, 1.0f));
        final BlockPlacementModifier orePlacement = new BlockPlacementModifier("", ModBlocks.UNKNOWN_ORE, 12, "");
        vault.getActiveModifiersFor(PlayerFilter.any(), LootableModifier.class).forEach(modifier3 -> orePlacement.onVaultRaidFinish(vault, world, controller, raid, modifier3.getAverageMultiplier()));
        BreadcrumbFeature.generateVaultBreadcrumb(vault, world, Lists.newArrayList(new VaultPiece[]{raidRoom}));
    }

    @Override
    public boolean preventsMobSpawning() {
        return true;
    }

    @Override
    public boolean preventsInfluences() {
        return true;
    }

    @Override
    public boolean preventsNormalMonsterDrops() {
        return true;
    }

    @Override
    public boolean preventsCatalystFragments() {
        return true;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        final ListNBT modifiers = new ListNBT();
        this.modifierValues.forEach((modifier, value) -> {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putString("name", modifier.getName());
            nbt.putFloat("value", (float) value);
            modifiers.add(nbt);
            return;
        });
        tag.put("raidModifiers", (INBT) modifiers);
        tag.putInt("completedRaids", this.completedRaids);
        tag.putBoolean("started", this.started);
        return tag;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        final ListNBT modifiers = nbt.getList("raidModifiers", 10);
        for (int i = 0; i < modifiers.size(); ++i) {
            final CompoundNBT modifierTag = modifiers.getCompound(i);
            final RaidModifier modifier = ModConfigs.RAID_MODIFIER_CONFIG.getByName(modifierTag.getString("name"));
            if (modifier != null) {
                final float val = modifierTag.getFloat("value");
                this.modifierValues.put(modifier, val);
            }
        }
        this.completedRaids = nbt.getInt("completedRaids");
        this.started = nbt.getBoolean("started");
    }
}
