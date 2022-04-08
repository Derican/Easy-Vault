package iskallia.vault.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.world.data.SoulShardTraderData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;


public class InternalCommand
        extends Command {
    public String getName() {
        return "internal";
    }


    public int getRequiredPermissionLevel() {
        return 2;
    }


    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("reset_shard_trades")
                .executes(this::resetShardTrader));

        builder.then(Commands.literal("player_vote")
                .then(Commands.argument("a", (ArgumentType) StringArgumentType.word())
                        .then(Commands.argument("b", (ArgumentType) StringArgumentType.word())
                                .executes(ctx -> voteFor((CommandSource) ctx.getSource(), StringArgumentType.getString(ctx, "a"), Direction.byName(StringArgumentType.getString(ctx, "b")))))));
    }


    private int resetShardTrader(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        SoulShardTraderData.get(((CommandSource) ctx.getSource()).getServer()).resetTrades();
        return 0;
    }

    private int voteFor(CommandSource src, String voter, Direction direction) throws CommandSyntaxException {
        ServerPlayerEntity sPlayer = src.getPlayerOrException();
        VaultRaid vault = VaultRaidData.get(sPlayer.getLevel()).getActiveFor(sPlayer);
        if (direction == null) {
            return 0;
        }
        if (vault == null) {
            return 0;
        }
        if (!((Boolean) vault.getActiveObjective(ArchitectObjective.class).map(objective -> Boolean.valueOf(objective.handleVote(voter, direction))).orElse(Boolean.valueOf(false))).booleanValue()) {
            return 0;
        }
        return 1;
    }


    public boolean isDedicatedServerOnly() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\command\InternalCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */