//
// Decompiled by Procyon v0.6.0
//

package iskallia.vault.world.vault.logic.objective.raid;

import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraft.util.math.AxisAlignedBB;
import iskallia.vault.world.gen.decorator.BreadcrumbFeature;
import com.google.common.collect.Lists;
import iskallia.vault.world.vault.gen.piece.VaultPiece;
import iskallia.vault.world.vault.modifier.LootableModifier;
import iskallia.vault.world.vault.logic.objective.raid.modifier.BlockPlacementModifier;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.world.vault.modifier.CatalystChanceModifier;
import iskallia.vault.world.vault.logic.objective.raid.modifier.FloatingItemModifier;
import net.minecraft.util.IItemProvider;
import iskallia.vault.init.ModItems;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.gen.structure.VaultJigsawHelper;
import net.minecraft.util.Direction;
import iskallia.vault.world.vault.gen.piece.VaultRaidRoom;
import iskallia.vault.util.MathUtilities;
import iskallia.vault.world.vault.modifier.InventoryRestoreModifier;
import net.minecraft.tileentity.TileEntity;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.block.entity.VaultRaidControllerTileEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import iskallia.vault.init.ModNetwork;

import java.util.List;

import iskallia.vault.network.message.VaultGoalMessage;

import java.util.ArrayList;

import iskallia.vault.util.PlayerFilter;

import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Map;
import java.util.Iterator;
import java.util.function.BiConsumer;

import iskallia.vault.world.vault.logic.objective.raid.modifier.ModifierDoublingModifier;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;

import net.minecraft.loot.LootTable;

import java.util.function.Function;

import iskallia.vault.world.vault.gen.VaultGenerator;

import java.util.function.Supplier;
import javax.annotation.Nonnull;

import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.Vault;
import iskallia.vault.world.vault.logic.task.VaultTask;
import net.minecraft.util.ResourceLocation;
import iskallia.vault.world.vault.logic.objective.raid.modifier.RaidModifier;

import java.util.LinkedHashMap;

import net.minecraftforge.fml.common.Mod;
import iskallia.vault.world.vault.logic.objective.VaultObjective;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RaidChallengeObjective extends VaultObjective {
    private final LinkedHashMap<RaidModifier, Float> modifierValues;
    private int completedRaids;
    private int targetRaids;
    private boolean started;
    private double damageTaken;
    private ResourceLocation roomPool;
    private ResourceLocation tunnelPool;

    public RaidChallengeObjective(final ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
        this.modifierValues = new LinkedHashMap<RaidModifier, Float>();
        this.completedRaids = 0;
        this.targetRaids = -1;
        this.started = false;
        this.damageTaken = 0.0;
        this.roomPool = Vault.id("raid/rooms");
        this.tunnelPool = Vault.id("vault/tunnels");
    }

    public void setRoomPool(final ResourceLocation roomPool) {
        this.roomPool = roomPool;
    }

    public void setTunnelPool(final ResourceLocation tunnelPool) {
        this.tunnelPool = tunnelPool;
    }

    @Nonnull
    @Override
    public BlockState getObjectiveRelevantBlock(final VaultRaid vault, final ServerWorld world, final BlockPos pos) {
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
        return new StringTextComponent("Raid").withStyle(TextFormatting.RED);
    }

    @Nullable
    @Override
    public ITextComponent getObjectiveTargetDescription(final int amount) {
        return (amount < 0) ? null : new StringTextComponent("Raids to complete: ").append(new StringTextComponent(String.valueOf(amount)).withStyle(TextFormatting.RED));
    }

    @Override
    public void setObjectiveTargetCount(final int amount) {
        this.targetRaids = amount;
    }

    @Override
    public ITextComponent getVaultName() {
        return new StringTextComponent("Vault Raid");
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
        return Collections.unmodifiableMap(this.modifierValues);
    }

    public <T extends RaidModifier> Map<T, Float> getModifiersOfType(Class<T> modifierClass) {
        return (Map<T, Float>) this.modifierValues.entrySet().stream()
                .filter(modifierTpl -> modifierClass.isAssignableFrom(modifierTpl.getKey().getClass()))
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
            final VaultGoalMessage pkt = VaultGoalMessage.raidChallenge(wave, totalWaves, mobs, totalMobs, startDelay, this.completedRaids, this.targetRaids, positives, negatives);
            ModNetwork.CHANNEL.sendTo((Object) pkt, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
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
        });
    }

    public void onRaidFinish(final VaultRaid vault, final ServerWorld world, final ActiveRaid raid, final BlockPos controller) {
        ++this.completedRaids;
        if (this.targetRaids >= 0 && this.completedRaids >= this.targetRaids) {
            this.setCompleted();
            return;
        }
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
                    VaultJigsawHelper.expandVault(vault, world, raidRoom, direction, VaultJigsawHelper.getRandomPiece(this.roomPool), VaultJigsawHelper.getRandomPiece(this.tunnelPool));
                }
            }
        }
        raidRoom.setRaidFinished();
        this.getAllModifiers().forEach((modifier1, value) -> modifier1.onVaultRaidFinish(vault, world, controller, raid, value));
        final AxisAlignedBB raidBoundingBox = raid.getRaidBoundingBox();
        final FloatingItemModifier catalystPlacement = new FloatingItemModifier("", 4, new WeightedList<SingleItemEntry>().add(new SingleItemEntry(ModItems.VAULT_CATALYST_FRAGMENT), 1), "");
        vault.getActiveModifiersFor(PlayerFilter.any(), CatalystChanceModifier.class).forEach(modifier2 -> catalystPlacement.onVaultRaidFinish(vault, world, controller, raid, 1.0f));
        final BlockPlacementModifier orePlacement = new BlockPlacementModifier("", ModBlocks.UNKNOWN_ORE, 12, "");
        vault.getActiveModifiersFor(PlayerFilter.any(), LootableModifier.class).forEach(modifier3 -> orePlacement.onVaultRaidFinish(vault, world, controller, raid, modifier3.getAverageMultiplier()));
        if (!vault.getProperties().exists(VaultRaid.PARENT)) {
            BreadcrumbFeature.generateVaultBreadcrumb(vault, world, Lists.newArrayList(new VaultPiece[]{raidRoom}));
        }
    }

    @Override
    public boolean isCompleted() {
        return super.isCompleted();
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(final LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayerEntity) {
            final ServerPlayerEntity sPlayer = (ServerPlayerEntity) event.getEntityLiving();
            final VaultRaid vault = VaultRaidData.get(sPlayer.getLevel()).getActiveFor(sPlayer);
            if (vault != null) {
                vault.getActiveObjective(RaidChallengeObjective.class).ifPresent(objective -> objective.addDamageTaken(event.getAmount()));
            }
        }
    }

    public void addDamageTaken(final double damageTaken) {
        this.damageTaken = MathHelper.clamp(this.damageTaken + damageTaken, 0.0, 3000.0);
    }

    public double getDamageTaken() {
        return this.damageTaken;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT tag = super.serializeNBT();
        final ListNBT modifiers = new ListNBT();
        this.modifierValues.forEach((modifier, value) -> {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putString("name", modifier.getName());
            nbt.putFloat("value", value);
            modifiers.add(nbt);
            return;
        });
        tag.put("raidModifiers", modifiers);
        tag.putInt("completedRaids", this.completedRaids);
        tag.putDouble("damageTaken", this.damageTaken);
        if (this.targetRaids >= 0) {
            tag.putInt("targetRaids", this.targetRaids);
        }
        tag.putBoolean("started", this.started);
        tag.putString("roomPool", this.roomPool.toString());
        tag.putString("tunnelPool", this.tunnelPool.toString());
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
        this.damageTaken = nbt.getDouble("damageTaken");
        if (nbt.contains("targetRaids", 3)) {
            this.targetRaids = nbt.getInt("targetRaids");
        }
        this.started = nbt.getBoolean("started");
        if (nbt.contains("roomPool", 8)) {
            this.roomPool = new ResourceLocation(nbt.getString("roomPool"));
        }
        if (nbt.contains("tunnelPool", 8)) {
            this.tunnelPool = new ResourceLocation(nbt.getString("tunnelPool"));
        }
    }
}
