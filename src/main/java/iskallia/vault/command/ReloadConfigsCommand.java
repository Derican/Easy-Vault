package iskallia.vault.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.ShardGlobalTradeMessage;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.PacketDistributor;

public class ReloadConfigsCommand
        extends Command {
    public String getName() {
        return "reloadcfg";
    }


    public int getRequiredPermissionLevel() {
        return 2;
    }


    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(this::reloadConfigs);
    }

    private int reloadConfigs(CommandContext<CommandSource> context) {

        try {
            ModConfigs.register();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new ShardGlobalTradeMessage(ModConfigs.SOUL_SHARD.getShardTrades()));
        ((CommandSource) context.getSource()).sendSuccess((ITextComponent) (new StringTextComponent("Configs reloaded!")).withStyle(TextFormatting.GREEN), true);
        return 0;
    }


    public boolean isDedicatedServerOnly() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\command\ReloadConfigsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */