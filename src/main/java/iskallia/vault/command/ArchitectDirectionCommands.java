package iskallia.vault.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class ArchitectDirectionCommands {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) Commands.literal("north").executes(cmd -> voteFor((CommandSource) cmd.getSource(), Direction.NORTH)));
        dispatcher.register((LiteralArgumentBuilder) Commands.literal("east").executes(cmd -> voteFor((CommandSource) cmd.getSource(), Direction.EAST)));
        dispatcher.register((LiteralArgumentBuilder) Commands.literal("south").executes(cmd -> voteFor((CommandSource) cmd.getSource(), Direction.SOUTH)));
        dispatcher.register((LiteralArgumentBuilder) Commands.literal("west").executes(cmd -> voteFor((CommandSource) cmd.getSource(), Direction.WEST)));
    }

    private static int voteFor(CommandSource src, Direction direction) throws CommandSyntaxException {
        ServerPlayerEntity sPlayer = src.getPlayerOrException();
        VaultRaid vault = VaultRaidData.get(sPlayer.getLevel()).getActiveFor(sPlayer);
        if (vault == null) {
            sPlayer.sendMessage((ITextComponent) (new StringTextComponent("Not in an Architect Vault!")).withStyle(TextFormatting.RED), Util.NIL_UUID);
            return 0;
        }
        if (!((Boolean) vault.getActiveObjective(ArchitectObjective.class).map(objective -> Boolean.valueOf(objective.handleVote(sPlayer.getName().getString(), direction))).orElse(Boolean.valueOf(false))).booleanValue()) {
            sPlayer.sendMessage((ITextComponent) (new StringTextComponent("No vote active or already voted!")).withStyle(TextFormatting.RED), Util.NIL_UUID);
            return 0;
        }
        return 1;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\command\ArchitectDirectionCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */