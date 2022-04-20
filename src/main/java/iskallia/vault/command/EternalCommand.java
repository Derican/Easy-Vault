package iskallia.vault.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.block.CryoChamberBlock;
import iskallia.vault.entity.eternal.EternalData;
import iskallia.vault.world.data.EternalsData;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.UUIDArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.UUID;

public class EternalCommand extends Command {
    @Override
    public String getName() {
        return "eternal";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(final LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("remove").then(Commands.argument("uuid", (ArgumentType) UUIDArgument.uuid()).executes(this::removeEternal)));
        builder.then(Commands.literal("list").then(Commands.argument("playerId", (ArgumentType) UUIDArgument.uuid()).executes(this::listEternals)));
        builder.then(Commands.literal("set").then(Commands.argument("uuid", (ArgumentType) UUIDArgument.uuid()).executes(this::setEternal)));
    }

    private int setEternal(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity sPlayer = ((CommandSource) context.getSource()).getPlayerOrException();
        final ItemStack held = sPlayer.getItemInHand(Hand.MAIN_HAND);
        if (held.isEmpty() || !(held.getItem() instanceof BlockItem) || !(((BlockItem) held.getItem()).getBlock() instanceof CryoChamberBlock)) {
            sPlayer.sendMessage((ITextComponent) new StringTextComponent("Not holding cryochamber!").withStyle(TextFormatting.RED), Util.NIL_UUID);
            return 0;
        }
        final UUID eternalUUID = UUIDArgument.getUuid((CommandContext) context, "uuid");
        final EternalsData data = EternalsData.get(sPlayer.getLevel());
        final EternalData eternal = data.getEternal(eternalUUID);
        if (eternal == null) {
            sPlayer.sendMessage((ITextComponent) new StringTextComponent("Specified eternal does not exist!").withStyle(TextFormatting.RED), Util.NIL_UUID);
            return 0;
        }
        final CompoundNBT tag = held.getOrCreateTagElement("BlockEntityTag");
        tag.putUUID("EternalId", eternalUUID);
        sPlayer.sendMessage((ITextComponent) new StringTextComponent("Eternal set!").withStyle(TextFormatting.GREEN), Util.NIL_UUID);
        return 0;
    }

    private int listEternals(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity sPlayer = ((CommandSource) context.getSource()).getPlayerOrException();
        final UUID playerId = UUIDArgument.getUuid((CommandContext) context, "playerId");
        final EternalsData data = EternalsData.get(sPlayer.getLevel());
        final EternalsData.EternalGroup group = data.getEternals(playerId);
        sPlayer.sendMessage((ITextComponent) new StringTextComponent("Eternals:").withStyle(TextFormatting.GREEN), Util.NIL_UUID);
        for (final EternalData eternal : group.getEternals()) {
            final IFormattableTextComponent txt = (IFormattableTextComponent) new StringTextComponent(eternal.getId().toString() + " / " + eternal.getName());
            txt.withStyle(style -> {
                final HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Copy UUID"));
                return style.withHoverEvent(hoverEvent);
            });
            txt.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, eternal.getId().toString())));
            sPlayer.sendMessage((ITextComponent) txt, Util.NIL_UUID);
        }
        return 0;
    }

    private int removeEternal(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity sPlayer = ((CommandSource) context.getSource()).getPlayerOrException();
        final UUID eternalUUID = UUIDArgument.getUuid((CommandContext) context, "uuid");
        final EternalsData data = EternalsData.get(sPlayer.getLevel());
        if (data.removeEternal(eternalUUID)) {
            sPlayer.sendMessage((ITextComponent) new StringTextComponent("Eternal removed!").withStyle(TextFormatting.GREEN), Util.NIL_UUID);
            return 0;
        }
        sPlayer.sendMessage((ITextComponent) new StringTextComponent("Specified eternal does not exist!").withStyle(TextFormatting.RED), Util.NIL_UUID);
        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }
}
