package iskallia.vault.backup;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.UUIDArgument;

import java.util.UUID;


public abstract class BackupListArgument
        implements ArgumentType<String> {
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    protected abstract UUID getPlayerRef(CommandContext<CommandSource> paramCommandContext);

    public static class Player extends BackupListArgument {
        protected UUID getPlayerRef(CommandContext<CommandSource> ctx) {
            try {
                return EntityArgument.getPlayer(ctx, "player").getUUID();
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class UUIDRef
            extends BackupListArgument {
        protected UUID getPlayerRef(CommandContext<CommandSource> ctx) {
            return UUIDArgument.getUuid(ctx, "player");
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\backup\BackupListArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */