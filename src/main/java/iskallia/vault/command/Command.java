package iskallia.vault.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;


public abstract class Command {
    public void registerCommand(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal(getName());
        builder.requires(sender -> sender.hasPermission(getRequiredPermissionLevel()));
        build(builder);
        dispatcher.register((LiteralArgumentBuilder) Commands.literal("the_vault").then((ArgumentBuilder) builder));
    }


    protected final void sendFeedback(CommandContext<CommandSource> context, String message, boolean showOps) {
        ((CommandSource) context.getSource()).sendSuccess((ITextComponent) new StringTextComponent(message), showOps);
    }

    public abstract String getName();

    public abstract int getRequiredPermissionLevel();

    public abstract void build(LiteralArgumentBuilder<CommandSource> paramLiteralArgumentBuilder);

    public abstract boolean isDedicatedServerOnly();
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\command\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */