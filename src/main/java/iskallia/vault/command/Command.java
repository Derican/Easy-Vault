// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public abstract class Command
{
    public abstract String getName();
    
    public abstract int getRequiredPermissionLevel();
    
    public void registerCommand(final CommandDispatcher<CommandSource> dispatcher) {
        final LiteralArgumentBuilder<CommandSource> builder = (LiteralArgumentBuilder<CommandSource>)Commands.literal(this.getName());
        builder.requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()));
        this.build(builder);
        dispatcher.register((LiteralArgumentBuilder)Commands.literal("the_vault").then((ArgumentBuilder)builder));
    }
    
    public abstract void build(final LiteralArgumentBuilder<CommandSource> p0);
    
    public abstract boolean isDedicatedServerOnly();
    
    protected final void sendFeedback(final CommandContext<CommandSource> context, final String message, final boolean showOps) {
        ((CommandSource)context.getSource()).sendSuccess((ITextComponent)new StringTextComponent(message), showOps);
    }
}
