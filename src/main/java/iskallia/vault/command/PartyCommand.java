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

public class PartyCommand extends Command {
    @Override
    public void registerCommand(final CommandDispatcher<CommandSource> dispatcher) {
        final LiteralArgumentBuilder<CommandSource> builder = Commands.literal(this.getName());
        builder.requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()));
        this.build(builder);
        dispatcher.register(builder);
    }

    @Override
    public String getName() {
        return "party";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void build(final LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("create").executes(this::create));
        builder.then(Commands.literal("invite").then(Commands.argument("target", (ArgumentType) EntityArgument.player()).executes(this::invite)));
        builder.then(Commands.literal("accept_invite").then(Commands.argument("target", (ArgumentType) EntityArgument.player()).executes(this::accept)));
        builder.then(Commands.literal("remove").then(Commands.argument("target", (ArgumentType) EntityArgument.player()).executes(this::remove)));
        builder.then(Commands.literal("leave").executes(this::leave));
        builder.then(Commands.literal("disband").executes(this::disband));
        builder.then(Commands.literal("list").executes(this::list));
    }

    private int list(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final VaultPartyData data = VaultPartyData.get(ctx.getSource().getLevel());
        final ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
        final Optional<VaultPartyData.Party> party = data.getParty(player.getUUID());
        if (!party.isPresent()) {
            player.sendMessage(new StringTextComponent("You are not in a party!").withStyle(TextFormatting.RED), player.getUUID());
            return 0;
        }
        final PlayerList players = player.getServer().getPlayerList();
        final IFormattableTextComponent members = new StringTextComponent("Members: ").withStyle(TextFormatting.GREEN);
        final List<ITextComponent> playerNames = party.get().getMembers().stream().map(players::getPlayer).filter(Objects::nonNull).map(PlayerEntity::getName).collect(Collectors.toList());
        for (int i = 0; i < playerNames.size(); ++i) {
            if (i != 0) {
                members.append(", ");
            }
            members.append(playerNames.get(i));
        }
        player.sendMessage(members, player.getUUID());
        return 0;
    }

    private int invite(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final VaultPartyData data = VaultPartyData.get(ctx.getSource().getLevel());
        final ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
        final ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
        final Optional<VaultPartyData.Party> party = data.getParty(player.getUUID());
        if (!party.isPresent()) {
            player.sendMessage(new StringTextComponent("You are not in a party!").withStyle(TextFormatting.RED), player.getUUID());
            return 0;
        }
        if (data.getParty(target.getUUID()).isPresent()) {
            player.sendMessage(new StringTextComponent("This player is already in another party.").withStyle(TextFormatting.RED), player.getUUID());
        } else {
            party.get().getMembers().forEach(uuid -> {
                final ServerPlayerEntity player2 = ctx.getSource().getServer().getPlayerList().getPlayer(uuid);
                if (player2 == null) {
                    return;
                } else {
                    final StringTextComponent stringTextComponent = new StringTextComponent("Inviting " + target.getName().getString() + " to the party.");

                    player2.sendMessage(stringTextComponent.withStyle(TextFormatting.GREEN), player.getUUID());
                    return;
                }
            });
            final String partyAccept = "/party accept_invite " + player.getName().getString();
            final IFormattableTextComponent acceptTxt = new StringTextComponent(partyAccept).withStyle(TextFormatting.AQUA);
            acceptTxt.withStyle(style -> {
                final HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click to accept!"));

                return style.withHoverEvent(hoverEvent).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, partyAccept));
            });
            final ITextComponent acceptMessage = new StringTextComponent("").append(new StringTextComponent("Run '").withStyle(TextFormatting.GREEN)).append(acceptTxt).append(new StringTextComponent("' to accept their invite!").withStyle(TextFormatting.GREEN));
            party.get().invite(target.getUUID());
            target.sendMessage(new StringTextComponent(player.getName().getString() + " has invited you to their party.").withStyle(TextFormatting.GREEN), player.getUUID());
            target.sendMessage(acceptMessage, player.getUUID());
        }
        return 0;
    }

    private int accept(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final VaultPartyData data = VaultPartyData.get(ctx.getSource().getLevel());
        final ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
        final ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
        final Optional<VaultPartyData.Party> party = data.getParty(player.getUUID());
        if (party.isPresent()) {
            player.sendMessage(new StringTextComponent("You already are in a party!").withStyle(TextFormatting.RED), player.getUUID());
            return 0;
        }
        if (!data.getParty(target.getUUID()).isPresent()) {
            player.sendMessage(new StringTextComponent("This player has left their party.").withStyle(TextFormatting.RED), player.getUUID());
        } else {
            data.getParty(target.getUUID()).get().getMembers().forEach(uuid -> {
                final ServerPlayerEntity player2 = ctx.getSource().getServer().getPlayerList().getPlayer(uuid);
                if (player2 == null) {
                    return;
                } else {
                    final StringTextComponent stringTextComponent = new StringTextComponent("Successfully added " + player.getName().getString() + " to the party.");

                    player2.sendMessage(stringTextComponent.withStyle(TextFormatting.GREEN), player.getUUID());
                    return;
                }
            });
            if (data.getParty(target.getUUID()).get().confirmInvite(player.getUUID())) {
                VaultPartyData.broadcastPartyData(player.getLevel());
                player.sendMessage(new StringTextComponent("You have been added to " + target.getName().getString() + "'s party.").withStyle(TextFormatting.GREEN), player.getUUID());
            } else {
                player.sendMessage(new StringTextComponent("You are not invited to " + target.getName().getString() + "'s party.").withStyle(TextFormatting.RED), player.getUUID());
            }
        }
        return 0;
    }

    private int remove(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final VaultPartyData data = VaultPartyData.get(ctx.getSource().getLevel());
        final ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
        final ServerPlayerEntity target = EntityArgument.getPlayer(ctx, "target");
        final Optional<VaultPartyData.Party> party = data.getParty(player.getUUID());
        if (!party.isPresent()) {
            player.sendMessage(new StringTextComponent("You are not in a party!").withStyle(TextFormatting.RED), player.getUUID());
            return 0;
        }
        final Optional<VaultPartyData.Party> other = data.getParty(target.getUUID());
        if (other.isPresent() && other.get() != party.get()) {
            player.sendMessage(new StringTextComponent("This player is in another party.").withStyle(TextFormatting.RED), player.getUUID());
        } else if (party.get().remove(target.getUUID())) {
            party.get().getMembers().forEach(uuid -> {
                final ServerPlayerEntity player2 = ctx.getSource().getServer().getPlayerList().getPlayer(uuid);
                if (player2 == null) {
                    return;
                } else {
                    final StringTextComponent stringTextComponent = new StringTextComponent(target.getName().getString() + " was removed from the party.");

                    player2.sendMessage(stringTextComponent.withStyle(TextFormatting.GREEN), player.getUUID());
                    return;
                }
            });
            target.sendMessage(new StringTextComponent("You have been removed from " + player.getName().getString() + "'s party.").withStyle(TextFormatting.GREEN), player.getUUID());
            VaultPartyData.broadcastPartyData(player.getLevel());
        } else {
            player.sendMessage(new StringTextComponent("This player not in your party.").withStyle(TextFormatting.RED), player.getUUID());
        }
        return 0;
    }

    private int create(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final VaultPartyData data = VaultPartyData.get(ctx.getSource().getLevel());
        final ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
        if (data.createParty(player.getUUID())) {
            player.sendMessage(new StringTextComponent("Successfully created a party.").withStyle(TextFormatting.GREEN), player.getUUID());
            VaultPartyData.broadcastPartyData(player.getLevel());
        } else {
            player.sendMessage(new StringTextComponent("You are already in a party! Please leave or disband it first.").withStyle(TextFormatting.RED), player.getUUID());
        }
        return 0;
    }

    private int leave(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final VaultPartyData data = VaultPartyData.get(ctx.getSource().getLevel());
        final ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
        final Optional<VaultPartyData.Party> party = data.getParty(player.getUUID());
        if (party.isPresent()) {
            if (party.get().remove(player.getUUID())) {
                party.get().getMembers().forEach(uuid -> {
                    final ServerPlayerEntity player2 = ctx.getSource().getServer().getPlayerList().getPlayer(uuid);
                    if (player2 == null) {
                        return;
                    } else {
                        final StringTextComponent stringTextComponent = new StringTextComponent(player.getName().getString() + " has left the party.");

                        player2.sendMessage(stringTextComponent.withStyle(TextFormatting.GREEN), player.getUUID());
                        return;
                    }
                });
                player.sendMessage(new StringTextComponent("Successfully left the party.").withStyle(TextFormatting.GREEN), player.getUUID());
                VaultPartyData.broadcastPartyData(player.getLevel());
            } else {
                player.sendMessage(new StringTextComponent("You are not in a party!").withStyle(TextFormatting.RED), player.getUUID());
            }
        } else {
            player.sendMessage(new StringTextComponent("You are not in a party!").withStyle(TextFormatting.RED), player.getUUID());
        }
        return 0;
    }

    private int disband(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final VaultPartyData data = VaultPartyData.get(ctx.getSource().getLevel());
        final ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
        final VaultPartyData.Party party = data.getParty(player.getUUID()).orElse(null);
        if (party != null && data.disbandParty(player.getUUID())) {
            party.getMembers().forEach(uuid -> {
                final ServerPlayerEntity player2 = ctx.getSource().getServer().getPlayerList().getPlayer(uuid);
                if (player2 == null) {
                    return;
                } else {
                    player2.sendMessage(new StringTextComponent("The party was disbanded.").withStyle(TextFormatting.GREEN), player.getUUID());
                    return;
                }
            });
            VaultPartyData.broadcastPartyData(player.getLevel());
        } else {
            player.sendMessage(new StringTextComponent("You are not in a party!").withStyle(TextFormatting.RED), player.getUUID());
        }
        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }
}
