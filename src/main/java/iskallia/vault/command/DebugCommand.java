package iskallia.vault.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.*;
import net.minecraft.world.server.ServerWorld;

public class DebugCommand extends Command {
    @Override
    public String getName() {
        return "debug";
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
        builder.then(Commands.literal("vault_kick").then(Commands.argument("player", (ArgumentType) EntityArgument.player()).executes(this::kickFromVault)));
    }

    private int kickFromVault(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity player = EntityArgument.getPlayer((CommandContext) context, "player");
        final ServerWorld world = player.getLevel();
        final VaultRaid vault = VaultRaidData.get(world).getActiveFor(player);
        if (vault == null) {
            ((CommandSource) context.getSource()).sendSuccess((ITextComponent) new StringTextComponent(player.getName().getString() + " is not in a vault!"), true);
            return 0;
        }
        if (vault.getPlayers().size() > 1) {
            vault.getPlayer((PlayerEntity) player).ifPresent(vPlayer -> {
                VaultRaid.RUNNER_TO_SPECTATOR.execute(vault, vPlayer, world);
                VaultRaid.HIDE_OVERLAY.execute(vault, vPlayer, world);
                return;
            });
        } else {
            vault.getPlayer((PlayerEntity) player).ifPresent(vPlayer -> VaultRaid.REMOVE_SCAVENGER_ITEMS.then(VaultRaid.REMOVE_INVENTORY_RESTORE_SNAPSHOTS).then(VaultRaid.EXIT_SAFELY).execute(vault, vPlayer, world));
        }
        final IFormattableTextComponent playerName = player.getDisplayName().copy();
        playerName.setStyle(Style.EMPTY.withColor(Color.fromRgb(9974168)));
        final StringTextComponent suffix = new StringTextComponent(" bailed.");
        MiscUtils.broadcast((ITextComponent) new StringTextComponent("").append((ITextComponent) playerName).append((ITextComponent) suffix));
        return 0;
    }
}
