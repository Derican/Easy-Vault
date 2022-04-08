package iskallia.vault.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerResearchesData;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class VaultLevelCommand
        extends Command {
    public String getName() {
        return "vault_level";
    }


    public int getRequiredPermissionLevel() {
        return 2;
    }


    public boolean isDedicatedServerOnly() {
        return false;
    }


    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                Commands.literal("add_exp")
                        .then(Commands.argument("exp", (ArgumentType) IntegerArgumentType.integer())
                                .executes(this::addExp)));


        builder.then(
                Commands.literal("set_level")
                        .then(Commands.argument("level", (ArgumentType) IntegerArgumentType.integer())
                                .executes(this::setLevel)));


        builder.then(
                Commands.literal("reset_all")
                        .executes(this::resetAll));
    }


    private int setLevel(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int level = IntegerArgumentType.getInteger(context, "level");
        CommandSource source = (CommandSource) context.getSource();
        PlayerVaultStatsData.get(source.getLevel()).setVaultLevel(source.getPlayerOrException(), level);
        return 0;
    }

    private int addExp(CommandContext<CommandSource> context) throws CommandSyntaxException {
        int exp = IntegerArgumentType.getInteger(context, "exp");
        CommandSource source = (CommandSource) context.getSource();
        PlayerVaultStatsData.get(source.getLevel()).addVaultExp(source.getPlayerOrException(), exp);
        return 0;
    }

    private int resetAll(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = (CommandSource) context.getSource();
        PlayerVaultStatsData.get(source.getLevel()).reset(source.getPlayerOrException());
        PlayerAbilitiesData.get(source.getLevel()).resetAbilityTree(source.getPlayerOrException());
        PlayerTalentsData.get(source.getLevel()).resetTalentTree(source.getPlayerOrException());
        PlayerResearchesData.get(source.getLevel()).resetResearchTree(source.getPlayerOrException());
        return 0;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\command\VaultLevelCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */