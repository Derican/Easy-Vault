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

import java.util.Collections;
import java.util.List;


public class GiveLootCommand
        extends Command {
    public String getName() {
        return "give_loot";
    }


    public int getRequiredPermissionLevel() {
        return 2;
    }


    public boolean isDedicatedServerOnly() {
        return false;
    }


    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("raffle_boss_crate")
                .then(Commands.argument("boss_name", (ArgumentType) StringArgumentType.word())
                        .executes(ctx -> giveRaffleBossCrate(ctx, ((CommandSource) ctx.getSource()).getPlayerOrException(), StringArgumentType.getString(ctx, "boss_name")))));

        builder.then(Commands.literal("normal_boss_crate")
                .executes(ctx -> giveNormalBossCrate(ctx, ((CommandSource) ctx.getSource()).getPlayerOrException())));

        builder.then(Commands.literal("raid_reward_crate")
                .executes(ctx -> giveRaidRewardCrate(ctx, ((CommandSource) ctx.getSource()).getPlayerOrException())));

        builder.then(Commands.literal("record_trophy")
                .then(Commands.argument("year", (ArgumentType) IntegerArgumentType.integer())
                        .then(Commands.argument("week", (ArgumentType) IntegerArgumentType.integer())
                                .executes(this::giveTrophy))));

        builder.then(Commands.literal("record_box").executes(this::giveTrophyBox));
    }

    public int giveTrophyBox(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity sPlayer = ((CommandSource) context.getSource()).getPlayerOrException();
        ServerWorld sWorld = sPlayer.getLevel();
        LootContext.Builder builder = (new LootContext.Builder(sWorld)).withRandom(sWorld.random).withLuck(sPlayer.getLuck());
        int playerLevel = PlayerVaultStatsData.get(sWorld).getVaultStats(sPlayer.getUUID()).getVaultLevel();
        LootTablesConfig.Level config = ModConfigs.LOOT_TABLES.getForLevel(playerLevel);

        LootTable bossBonusTbl = sPlayer.getServer().getLootTables().get(config.getScavengerCrate());
        NonNullList<ItemStack> quickBossLoot = NonNullList.create();
        quickBossLoot.addAll(bossBonusTbl.getRandomItems(builder.create(LootParameterSets.EMPTY)));
        Collections.shuffle((List<?>) quickBossLoot);

        ItemStack box = new ItemStack((IItemProvider) Items.WHITE_SHULKER_BOX);
        box.getOrCreateTag().put("BlockEntityTag", (INBT) new CompoundNBT());
        ItemStackHelper.saveAllItems(box.getOrCreateTag().getCompound("BlockEntityTag"), quickBossLoot);

        sPlayer.addItem(box);
        sPlayer.sendMessage((ITextComponent) new StringTextComponent("Generated Recordbox for Vault level " + playerLevel), Util.NIL_UUID);
        return 0;
    }

    public int giveTrophy(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity sPlayer = ((CommandSource) context.getSource()).getPlayerOrException();
        int year = IntegerArgumentType.getInteger(context, "year");
        int week = IntegerArgumentType.getInteger(context, "week");

        ItemStack statue = TrophyStatueBlockItem.getTrophy(sPlayer.getLevel(), WeekKey.of(year, week));
        if (!statue.isEmpty()) {
            sPlayer.addItem(statue);
        } else {
            sPlayer.sendMessage((ITextComponent) new StringTextComponent("No record set!"), Util.NIL_UUID);
        }
        return 0;
    }

    private int giveRaidRewardCrate(CommandContext<CommandSource> ctx, ServerPlayerEntity player) {
        EntityHelper.giveItem((PlayerEntity) player, VaultRaidData.generateRaidRewardCrate());
        return 0;
    }

    public int giveNormalBossCrate(CommandContext<CommandSource> context, ServerPlayerEntity player) {
        ServerWorld world = player.getLevel();
        LootContext.Builder builder = (new LootContext.Builder(world)).withRandom(world.random).withLuck(player.getLuck());
        LootContext ctx = builder.create(LootParameterSets.EMPTY);

        int level = PlayerVaultStatsData.get(world).getVaultStats((PlayerEntity) player).getVaultLevel();
        NonNullList<ItemStack> stacks = NonNullList.create();
        stacks.addAll(world.getServer().getLootTables().get(ModConfigs.LOOT_TABLES.getForLevel(level).getBossCrate()).getRandomItems(ctx));

        ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE, stacks);
        EntityHelper.giveItem((PlayerEntity) player, crate);
        return 0;
    }

    public int giveRaffleBossCrate(CommandContext<CommandSource> context, ServerPlayerEntity player, String bossName) {
        ServerWorld world = player.getLevel();
        LootContext.Builder builder = (new LootContext.Builder(world)).withRandom(world.random).withLuck(player.getLuck());
        LootContext ctx = builder.create(LootParameterSets.EMPTY);

        NonNullList<ItemStack> stacks = NonNullList.create();

        stacks.add(LootStatueBlockItem.getStatueBlockItem(bossName, StatueType.VAULT_BOSS));

        int level = PlayerVaultStatsData.get(world).getVaultStats((PlayerEntity) player).getVaultLevel();
        List<ItemStack> items = world.getServer().getLootTables().get(ModConfigs.LOOT_TABLES.getForLevel(level).getBossCrate()).getRandomItems(ctx);
        stacks.addAll(items);

        ItemStack crate = VaultCrateBlock.getCrateWithLoot(ModBlocks.VAULT_CRATE, stacks);
        EntityHelper.giveItem((PlayerEntity) player, crate);

        return 0;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\command\GiveLootCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */