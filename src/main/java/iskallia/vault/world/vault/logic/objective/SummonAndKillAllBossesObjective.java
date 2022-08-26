package iskallia.vault.world.vault.logic.objective;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Optional;

import iskallia.vault.world.vault.VaultUtils;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import iskallia.vault.network.message.BossMusicMessage;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.VaultGoalMessage;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.config.LootTablesConfig;
import net.minecraft.loot.LootTable;

import java.util.function.Function;
import javax.annotation.Nonnull;

import iskallia.vault.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.ITextComponent;

import java.util.Set;

import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import iskallia.vault.util.MiscUtils;

import java.util.List;
import java.util.Collections;
import java.util.Collection;

import iskallia.vault.world.vault.modifier.VaultModifier;

import java.util.ArrayList;

import iskallia.vault.config.VaultModifiersConfig;
import iskallia.vault.init.ModConfigs;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.VaultRaid;

import javax.annotation.Nullable;

import iskallia.vault.world.vault.gen.layout.SquareRoomLayout;
import iskallia.vault.world.vault.gen.layout.VaultRoomLayoutGenerator;
import iskallia.vault.Vault;
import iskallia.vault.world.vault.logic.task.VaultTask;
import net.minecraft.nbt.StringNBT;

import java.util.UUID;

import iskallia.vault.nbt.VListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "the_vault", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SummonAndKillAllBossesObjective extends VaultObjective {
    protected int progressCount;
    protected int bossesCount;
    protected int targetCount;
    private ResourceLocation roomPool;
    private ResourceLocation tunnelPool;
    protected VListNBT<UUID, StringNBT> bosses;

    public SummonAndKillAllBossesObjective(final ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
        this.targetCount = 10;
        this.roomPool = Vault.id("raid/rooms");
        this.tunnelPool = Vault.id("vault/tunnels");
        this.bosses = VListNBT.ofUUID();
    }

    public boolean allObelisksClicked() {
        return this.progressCount >= this.targetCount;
    }

    public boolean allBossesDefeated() {
        return this.bossesCount >= this.targetCount;
    }

    public void addObelisk() {
        ++this.progressCount;
    }

    public void setRoomPool(final ResourceLocation roomPool) {
        this.roomPool = roomPool;
    }

    public void setTunnelPool(final ResourceLocation tunnelPool) {
        this.tunnelPool = tunnelPool;
    }

    @Nullable
    @Override
    public VaultRoomLayoutGenerator getCustomLayout() {
        final SquareRoomLayout layout = new SquareRoomLayout();
        layout.setRoomId(this.roomPool);
        layout.setTunnelId(this.tunnelPool);
        return layout;
    }

    public void completeBoss(final VaultRaid vault, final ServerWorld world, final UUID uuid) {
        if (!this.bosses.remove(uuid)) {
            return;
        }
        ++this.bossesCount;
        if (this.bossesCount >= this.targetCount) {
            return;
        }
        final int level = vault.getProperties().getValue(VaultRaid.LEVEL);
        final Set<VaultModifier> modifiers = ModConfigs.VAULT_MODIFIERS.getRandom(SummonAndKillAllBossesObjective.rand, level, VaultModifiersConfig.ModifierPoolType.FINAL_TENOS_ADDS, this.getId());
        final List<VaultModifier> modifierList = new ArrayList<VaultModifier>(modifiers);
        Collections.shuffle(modifierList);
        final VaultModifier modifier = MiscUtils.getRandomEntry(modifierList, SummonAndKillAllBossesObjective.rand);
        if (modifier != null) {
            final ITextComponent ct = new StringTextComponent("Added ").withStyle(TextFormatting.GRAY).append(modifier.getNameComponent());
            vault.getModifiers().addPermanentModifier(modifier);
            vault.getPlayers().forEach(vPlayer -> {
                modifier.apply(vault, vPlayer, world, world.getRandom());
                vPlayer.runIfPresent(world.getServer(), sPlayer -> sPlayer.sendMessage(ct, Util.NIL_UUID));
            });
        }
    }

    public void addBoss(final LivingEntity boss) {
        this.bosses.add(boss.getUUID());
    }

    @Override
    public void setObjectiveTargetCount(final int amount) {
        this.targetCount = amount;
    }

    @Nullable
    @Override
    public ITextComponent getObjectiveTargetDescription(final int amount) {
        return new StringTextComponent("Find Obelisks: ").append(new StringTextComponent(String.valueOf(amount)).withStyle(TextFormatting.GOLD));
    }

    @Nonnull
    @Override
    public BlockState getObjectiveRelevantBlock(final VaultRaid vault, final ServerWorld world, final BlockPos pos) {
        return ModBlocks.OBELISK.defaultBlockState();
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
        return new StringTextComponent("Kill all Bosses").withStyle(TextFormatting.GOLD);
    }

    @Override
    public void tick(final VaultRaid vault, final PlayerFilter filter, final ServerWorld world) {
        super.tick(vault, filter, world);
        if (this.isCompleted()) {
            return;
        }
        vault.getPlayers().stream().filter(vPlayer -> filter.test(vPlayer.getPlayerId())).forEach(vPlayer -> vPlayer.runIfPresent(world.getServer(), playerEntity -> {
            final VaultGoalMessage pkt = this.allObelisksClicked() ? VaultGoalMessage.killBossGoal() : VaultGoalMessage.obeliskGoal(this.progressCount, this.targetCount);
            ModNetwork.CHANNEL.sendTo((Object) pkt, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }));
        if (this.allBossesDefeated()) {
            this.setCompleted();
        }
    }

    @Override
    public void complete(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        super.complete(vault, player, world);
        player.sendIfPresent(world.getServer(), new BossMusicMessage(false));
        player.sendIfPresent(world.getServer(), VaultGoalMessage.clear());
    }

    @Override
    public void complete(final VaultRaid vault, final ServerWorld world) {
        super.complete(vault, world);
        vault.getPlayers().forEach(player -> {
            player.sendIfPresent(world.getServer(), new BossMusicMessage(false));
            player.sendIfPresent(world.getServer(), VaultGoalMessage.clear());
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBossDeath(final LivingDeathEvent event) {
        if (event.getEntity().level.isClientSide()) {
            return;
        }
        final ServerWorld world = (ServerWorld) event.getEntity().level;
        final VaultRaid vault = VaultRaidData.get(world).getAt(world, event.getEntity().blockPosition());
        if (!VaultUtils.inVault(vault, event.getEntity())) {
            return;
        }
        final List<SummonAndKillAllBossesObjective> matchingObjectives = vault.getPlayers().stream().map(player -> player.getActiveObjective(SummonAndKillAllBossesObjective.class)).filter(Optional::isPresent).map(Optional::get).filter(o -> !o.isCompleted()).collect(Collectors.toList());
        if (matchingObjectives.isEmpty()) {
            vault.getActiveObjective(SummonAndKillAllBossesObjective.class).ifPresent(objective -> objective.completeBoss(vault, world, event.getEntity().getUUID()));
        } else {
            matchingObjectives.forEach(objective -> objective.completeBoss(vault, world, event.getEntity().getUUID()));
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("ProgressCount", this.progressCount);
        nbt.putInt("TargetCount", this.targetCount);
        nbt.putInt("BossesCount", this.bossesCount);
        nbt.put("Bosses", this.bosses.serializeNBT());
        nbt.putString("roomPool", this.roomPool.toString());
        nbt.putString("tunnelPool", this.tunnelPool.toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.progressCount = nbt.getInt("ProgressCount");
        this.targetCount = nbt.getInt("TargetCount");
        this.bossesCount = nbt.getInt("BossesCount");
        this.bosses.deserializeNBT(nbt.getList("Bosses", 9));
        if (nbt.contains("roomPool", 8)) {
            this.roomPool = new ResourceLocation(nbt.getString("roomPool"));
        }
        if (nbt.contains("tunnelPool", 8)) {
            this.tunnelPool = new ResourceLocation(nbt.getString("tunnelPool"));
        }
    }
}
