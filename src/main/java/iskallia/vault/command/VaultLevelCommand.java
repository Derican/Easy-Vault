package iskallia.vault.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class VaultLevelCommand extends Command {
    @Override
    public String getName() {
        return "vault_level";
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
        builder.then(Commands.literal("add_exp").then(Commands.argument("exp", (ArgumentType) IntegerArgumentType.integer()).executes(this::addExp)));
        builder.then(Commands.literal("set_level").then(Commands.argument("level", (ArgumentType) IntegerArgumentType.integer()).executes(this::setLevel)));
        builder.then(Commands.literal("reset_all").executes(this::resetAll));
    }

    private int setLevel(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final int level = IntegerArgumentType.getInteger((CommandContext) context, "level");
        final CommandSource source = (CommandSource) context.getSource();
        PlayerVaultStatsData.get(source.getLevel()).setVaultLevel(source.getPlayerOrException(), level);
        return 0;
    }

    private int addExp(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final int exp = IntegerArgumentType.getInteger((CommandContext) context, "exp");
        final CommandSource source = (CommandSource) context.getSource();
        PlayerVaultStatsData.get(source.getLevel()).addVaultExp(source.getPlayerOrException(), exp);
        return 0;
    }

    private int resetAll(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final CommandSource source = (CommandSource) context.getSource();
        PlayerVaultStatsData.get(source.getLevel()).reset(source.getPlayerOrException());
        PlayerAbilitiesData.get(source.getLevel()).resetAbilityTree(source.getPlayerOrException());
        PlayerTalentsData.get(source.getLevel()).resetTalentTree(source.getPlayerOrException());
        return 0;
    }
}
