package iskallia.vault.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.block.VaultCrateBlock;
import iskallia.vault.block.item.LootStatueBlockItem;
import iskallia.vault.block.item.TrophyStatueBlockItem;
import iskallia.vault.config.LootTablesConfig;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.util.StatueType;
import iskallia.vault.util.WeekKey;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.data.generated.ChallengeCrystalArchive;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GiveLootCommand extends Command {
    @Override
    public String getName() {
        return "give_loot";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }

    @Override
    public void build(final LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("raffle_boss_crate").then(Commands.argument("boss_name", (ArgumentType) StringArgumentType.word()).executes(ctx -> this.giveRaffleBossCrate((CommandContext<CommandSource>) ctx, ((CommandSource) ctx.getSource()).getPlayerOrException(), StringArgumentType.getString(ctx, "boss_name")))));
        builder.then(Commands.literal("normal_boss_crate").executes(ctx -> this.giveNormalBossCrate((CommandContext<CommandSource>) ctx, ((CommandSource) ctx.getSource()).getPlayerOrException())));
        builder.then(Commands.literal("raid_reward_crate").executes(ctx -> this.giveRaidRewardCrate((CommandContext<CommandSource>) ctx, ((CommandSource) ctx.getSource()).getPlayerOrException())));
        builder.then(Commands.literal("record_trophy").then(Commands.argument("year", (ArgumentType) IntegerArgumentType.integer()).then(Commands.argument("week", (ArgumentType) IntegerArgumentType.integer()).executes(this::giveTrophy))));
        builder.then(Commands.literal("record_box").executes(this::giveTrophyBox));
        builder.then(Commands.literal("challenge_crystal").then(Commands.argument("index", (ArgumentType)IntegerArgumentType.integer()).executes(this::giveChallengeCrystal)));
    }

    public int giveChallengeCrystal(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity sPlayer = ((CommandSource)context.getSource()).getPlayerOrException();
        final int index = IntegerArgumentType.getInteger((CommandContext)context, "index");
        sPlayer.addItem(ChallengeCrystalArchive.get(index));
        return 0;
    }

    public int giveTrophyBox(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity sPlayer = ((CommandSource) context.getSource()).getPlayerOrException();
        final ServerWorld sWorld = sPlayer.getLevel();
        final LootContext.Builder builder = new LootContext.Builder(sWorld).withRandom(sWorld.random).withLuck(sPlayer.getLuck());
        final int playerLevel = PlayerVaultStatsData.get(sWorld).getVaultStats(sPlayer.getUUID()).getVaultLevel();
        final LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(playerLevel);
        final LootTable bossBonusTbl = sPlayer.getServer().getLootTables().get(config.getScavengerCrate());
        final NonNullList<ItemStack> quickBossLoot = NonNullList.create();
        quickBossLoot.addAll((Collection) bossBonusTbl.getRandomItems(builder.create(LootParameterSets.EMPTY)));
        Collections.shuffle((List<?>) quickBossLoot);
        final ItemStack box = new ItemStack((IItemProvider) Items.WHITE_SHULKER_BOX);
        box.getOrCreateTag().put("BlockEntityTag", (INBT) new CompoundNBT());
        ItemStackHelper.saveAllItems(box.getOrCreateTag().getCompound("BlockEntityTag"), (NonNullList) quickBossLoot);
        sPlayer.addItem(box);
        sPlayer.sendMessage((ITextComponent) new StringTextComponent("Generated Recordbox for Vault level " + playerLevel), Util.NIL_UUID);
        return 0;
    }

    public int giveTrophy(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity sPlayer = ((CommandSource) context.getSource()).getPlayerOrException();
        final int year = IntegerArgumentType.getInteger((CommandContext) context, "year");
        final int week = IntegerArgumentType.getInteger((CommandContext) context, "week");
        final ItemStack statue = TrophyStatueBlockItem.getTrophy(sPlayer.getLevel(), WeekKey.of(year, week));
        if (!statue.isEmpty()) {
            sPlayer.addItem(statue);
        } else {
            sPlayer.sendMessage((ITextComponent) new StringTextComponent("No record set!"), Util.NIL_UUID);
        }
        return 0;
    }

    private int giveRaidRewardCrate(final CommandContext<CommandSource> ctx, final ServerPlayerEntity player) {
        EntityHelper.giveItem((PlayerEntity) player, VaultRaidData.generateRaidRewardCrate());
        return 0;
    }

    public int giveNormalBossCrate(final CommandContext<CommandSource> context, final ServerPlayerEntity player) {
        final ServerWorld world = player.getLevel();
        final LootContext.Builder builder = new LootContext.Builder(world).withRandom(world.random).withLuck(player.getLuck());
        final LootContext ctx = builder.create(LootParameterSets.EMPTY);
        final int level = PlayerVaultStatsData.get(world).getVaultStats((PlayerEntity) player).getVaultLevel();
        final NonNullList<ItemStack> stacks = NonNullList.create();
        stacks.addAll((Collection) world.getServer().getLootTables().get(ModConfigs.LOOT_TABLES.getForLevel(level).getBossCrate()).getRandomItems(ctx));
        final ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE, stacks);
        EntityHelper.giveItem((PlayerEntity) player, crate);
        return 0;
    }

    public int giveRaffleBossCrate(final CommandContext<CommandSource> context, final ServerPlayerEntity player, final String bossName) {
        final ServerWorld world = player.getLevel();
        final LootContext.Builder builder = new LootContext.Builder(world).withRandom(world.random).withLuck(player.getLuck());
        final LootContext ctx = builder.create(LootParameterSets.EMPTY);
        final NonNullList<ItemStack> stacks = NonNullList.create();
        stacks.add(LootStatueBlockItem.getStatueBlockItem(bossName, StatueType.VAULT_BOSS));
        final int level = PlayerVaultStatsData.get(world).getVaultStats((PlayerEntity) player).getVaultLevel();
        final List<ItemStack> items = world.getServer().getLootTables().get(ModConfigs.LOOT_TABLES.getForLevel(level).getBossCrate()).getRandomItems(ctx);
        stacks.addAll((Collection) items);
        final ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE, stacks);
        EntityHelper.giveItem((PlayerEntity) player, crate);
        return 0;
    }
}
