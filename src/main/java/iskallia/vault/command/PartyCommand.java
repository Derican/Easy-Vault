package iskallia.vault.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.world.data.VaultPartyData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


public class PartyCommand
        extends Command {
    public void registerCommand(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal(getName());
        builder.requires(sender -> sender.hasPermission(getRequiredPermissionLevel()));
        build(builder);
        dispatcher.register(builder);
    }


    public String getName() {
        return "party";
    }


    public int getRequiredPermissionLevel() {
        return 0;
    }


    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("create")
                .executes(this::create));
        builder.then(Commands.literal("invite")
                .then(Commands.argument("target", (ArgumentType) EntityArgument.player())
                        .executes(this::invite)));
        builder.then(Commands.literal("accept_invite")
                .then(Commands.argument("target", (ArgumentType) EntityArgument.player())
                        .executes(this::accept)));
        builder.then(Commands.literal("remove")
                .then(Commands.argument("target", (ArgumentType) EntityArgument.player())
                        .executes(this::remove)));
        builder.then(Commands.literal("leave")
                .executes(this::leave));
        builder.then(Commands.literal("disband")
                .executes(this::disband));
        builder.then(Commands.literal("list")
                .executes(this::list));
    }

    private int list(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        VaultPartyData data = VaultPartyData.get(((CommandSource) ctx.getSource()).getLevel());
        ServerPlayerEntity player = ((CommandSource) ctx.getSource()).getPlayerOrException();

        Optional<VaultPartyData.Party> party = data.getParty(player.getUUID());

        if (!party.isPresent()) {
            player.sendMessage((ITextComponent) (new StringTextComponent("You are not in a party!")).withStyle(TextFormatting.RED), player.getUUID());
            return 0;
        }

        PlayerList players = player.getServer().getPlayerList();
        IFormattableTextComponent members = (new StringTextComponent("Members: ")).withStyle(TextFormatting.GREEN);


        List<ITextComponent> playerNames = (List<ITextComponent>) ((VaultPartyData.Party) party.get()).getMembers().stream().map(players::getPlayer).filter(Objects::nonNull).map(PlayerEntity::getName).collect(Collectors.toList());
        for (int i = 0; i < playerNames.size(); i++) {
            if (i != 0) {
                members.append(", ");
            }
            members.append(playerNames.get(i));
        }

        player.sendMessage((ITextComponent) members, player.getUUID());
        return 0;
    }

    private int invite(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        VaultPartyData data = VaultPartyData.get(((CommandSource) ctx.getSource()).getLevel());
        ServerPlayerEntity player = ((CommandSource) ctx.getSource()).getPlayerOrException();
        ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");

        Optional<VaultPartyData.Party> party = data.getParty(player.getUUID());

        if (!party.isPresent()) {
            player.sendMessage((ITextComponent) (new StringTextComponent("You are not in a party!")).withStyle(TextFormatting.RED), player.getUUID());
            return 0;
        }

        if (data.getParty(target.getUUID()).isPresent()) {
            player.sendMessage((ITextComponent) (new StringTextComponent("This player is already in another party.")).withStyle(TextFormatting.RED), player.getUUID());
        } else {
            ((VaultPartyData.Party) party.get()).getMembers().forEach(uuid -> {
                ServerPlayerEntity player2 = ((CommandSource) ctx.getSource()).getServer().getPlayerList().getPlayer(uuid);
                if (player2 == null)
                    return;
                player2.sendMessage((ITextComponent) (new StringTextComponent("Inviting " + target.getName().getString() + " to the party.")).withStyle(TextFormatting.GREEN), player.getUUID());
            });
            String partyAccept = "/party accept_invite " + player.getName().getString();
            IFormattableTextComponent acceptTxt = (new StringTextComponent(partyAccept)).withStyle(TextFormatting.AQUA);
            acceptTxt.withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click to accept!"))).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, partyAccept)));


            IFormattableTextComponent iFormattableTextComponent1 = (new StringTextComponent("")).append((ITextComponent) (new StringTextComponent("Run '")).withStyle(TextFormatting.GREEN)).append((ITextComponent) acceptTxt).append((ITextComponent) (new StringTextComponent("' to accept their invite!")).withStyle(TextFormatting.GREEN));

            ((VaultPartyData.Party) party.get()).invite(target.getUUID());
            target.sendMessage((ITextComponent) (new StringTextComponent(player.getName().getString() + " has invited you to their party.")).withStyle(TextFormatting.GREEN), player.getUUID());
            target.sendMessage((ITextComponent) iFormattableTextComponent1, player.getUUID());
        }

        return 0;
    }

    private int accept(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        VaultPartyData data = VaultPartyData.get(((CommandSource) ctx.getSource()).getLevel());
        ServerPlayerEntity player = ((CommandSource) ctx.getSource()).getPlayerOrException();
        ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");

        Optional<VaultPartyData.Party> party = data.getParty(player.getUUID());

        if (party.isPresent()) {
            player.sendMessage((ITextComponent) (new StringTextComponent("You already are in a party!")).withStyle(TextFormatting.RED), player.getUUID());
            return 0;
        }

        if (!data.getParty(target.getUUID()).isPresent()) {
            player.sendMessage((ITextComponent) (new StringTextComponent("This player has left their party.")).withStyle(TextFormatting.RED), player.getUUID());
        } else {
            ((VaultPartyData.Party) data.getParty(target.getUUID()).get()).getMembers().forEach(uuid -> {
                ServerPlayerEntity player2 = ((CommandSource) ctx.getSource()).getServer().getPlayerList().getPlayer(uuid);
                if (player2 == null)
                    return;
                player2.sendMessage((ITextComponent) (new StringTextComponent("Successfully added " + player.getName().getString() + " to the party.")).withStyle(TextFormatting.GREEN), player.getUUID());
            });
            if (((VaultPartyData.Party) data.getParty(target.getUUID()).get()).confirmInvite(player.getUUID())) {
                VaultPartyData.broadcastPartyData(player.getLevel());
                player.sendMessage((ITextComponent) (new StringTextComponent("You have been added to " + target.getName().getString() + "'s party.")).withStyle(TextFormatting.GREEN), player.getUUID());
            } else {
                player.sendMessage((ITextComponent) (new StringTextComponent("You are not invited to " + target.getName().getString() + "'s party.")).withStyle(TextFormatting.RED), player.getUUID());
            }
        }

        return 0;
    }

    private int remove(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        VaultPartyData data = VaultPartyData.get(((CommandSource) ctx.getSource()).getLevel());
        ServerPlayerEntity player = ((CommandSource) ctx.getSource()).getPlayerOrException();
        ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");

        Optional<VaultPartyData.Party> party = data.getParty(player.getUUID());

        if (!party.isPresent()) {
            player.sendMessage((ITextComponent) (new StringTextComponent("You are not in a party!")).withStyle(TextFormatting.RED), player.getUUID());
            return 0;
        }

        Optional<VaultPartyData.Party> other = data.getParty(target.getUUID());

        if (other.isPresent() && other.get() != party.get()) {
            player.sendMessage((ITextComponent) (new StringTextComponent("This player is in another party.")).withStyle(TextFormatting.RED), player.getUUID());
        } else if (((VaultPartyData.Party) party.get()).remove(target.getUUID())) {
            ((VaultPartyData.Party) party.get()).getMembers().forEach(uuid -> {
                ServerPlayerEntity player2 = ((CommandSource) ctx.getSource()).getServer().getPlayerList().getPlayer(uuid);
                if (player2 == null)
                    return;
                player2.sendMessage((ITextComponent) (new StringTextComponent(target.getName().getString() + " was removed from the party.")).withStyle(TextFormatting.GREEN), player.getUUID());
            });
            target.sendMessage((ITextComponent) (new StringTextComponent("You have been removed from " + player.getName().getString() + "'s party.")).withStyle(TextFormatting.GREEN), player.getUUID());
            VaultPartyData.broadcastPartyData(player.getLevel());
        } else {
            player.sendMessage((ITextComponent) (new StringTextComponent("This player not in your party.")).withStyle(TextFormatting.RED), player.getUUID());
        }


        return 0;
    }

    private int create(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        VaultPartyData data = VaultPartyData.get(((CommandSource) ctx.getSource()).getLevel());
        ServerPlayerEntity player = ((CommandSource) ctx.getSource()).getPlayerOrException();

        if (data.createParty(player.getUUID())) {
            player.sendMessage((ITextComponent) (new StringTextComponent("Successfully created a party.")).withStyle(TextFormatting.GREEN), player.getUUID());
            VaultPartyData.broadcastPartyData(player.getLevel());
        } else {
            player.sendMessage((ITextComponent) (new StringTextComponent("You are already in a party! Please leave or disband it first.")).withStyle(TextFormatting.RED), player.getUUID());
        }

        return 0;
    }

    private int leave(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        VaultPartyData data = VaultPartyData.get(((CommandSource) ctx.getSource()).getLevel());
        ServerPlayerEntity player = ((CommandSource) ctx.getSource()).getPlayerOrException();
        Optional<VaultPartyData.Party> party = data.getParty(player.getUUID());

        if (party.isPresent()) {
            if (((VaultPartyData.Party) party.get()).remove(player.getUUID())) {
                ((VaultPartyData.Party) party.get()).getMembers().forEach(uuid -> {
                    ServerPlayerEntity player2 = ((CommandSource) ctx.getSource()).getServer().getPlayerList().getPlayer(uuid);
                    if (player2 == null)
                        return;
                    player2.sendMessage((ITextComponent) (new StringTextComponent(player.getName().getString() + " has left the party.")).withStyle(TextFormatting.GREEN), player.getUUID());
                });
                player.sendMessage((ITextComponent) (new StringTextComponent("Successfully left the party.")).withStyle(TextFormatting.GREEN), player.getUUID());
                VaultPartyData.broadcastPartyData(player.getLevel());
            } else {
                player.sendMessage((ITextComponent) (new StringTextComponent("You are not in a party!")).withStyle(TextFormatting.RED), player.getUUID());
            }
        } else {
            player.sendMessage((ITextComponent) (new StringTextComponent("You are not in a party!")).withStyle(TextFormatting.RED), player.getUUID());
        }

        return 0;
    }

    private int disband(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        VaultPartyData data = VaultPartyData.get(((CommandSource) ctx.getSource()).getLevel());
        ServerPlayerEntity player = ((CommandSource) ctx.getSource()).getPlayerOrException();

        VaultPartyData.Party party = data.getParty(player.getUUID()).orElse(null);
        if (party != null && data.disbandParty(player.getUUID())) {
            party.getMembers().forEach(uuid -> {
                ServerPlayerEntity player2 = ((CommandSource) ctx.getSource()).getServer().getPlayerList().getPlayer(uuid);
                if (player2 == null)
                    return;
                player2.sendMessage((ITextComponent) (new StringTextComponent("The party was disbanded.")).withStyle(TextFormatting.GREEN), player.getUUID());
            });
            VaultPartyData.broadcastPartyData(player.getLevel());
        } else {
            player.sendMessage((ITextComponent) (new StringTextComponent("You are not in a party!")).withStyle(TextFormatting.RED), player.getUUID());
        }

        return 0;
    }


    public boolean isDedicatedServerOnly() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\command\PartyCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */