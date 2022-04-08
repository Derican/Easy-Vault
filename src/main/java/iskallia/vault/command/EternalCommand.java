package iskallia.vault.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.UUID;


public class EternalCommand
        extends Command {
    public String getName() {
        return "eternal";
    }


    public int getRequiredPermissionLevel() {
        return 2;
    }


    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("remove")
                .then(Commands.argument("uuid", (ArgumentType) UUIDArgument.uuid())
                        .executes(this::removeEternal)));
        builder.then(Commands.literal("list")
                .then(Commands.argument("playerId", (ArgumentType) UUIDArgument.uuid())
                        .executes(this::listEternals)));
        builder.then(Commands.literal("set")
                .then(Commands.argument("uuid", (ArgumentType) UUIDArgument.uuid())
                        .executes(this::setEternal)));
    }

    private int setEternal(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity sPlayer = ((CommandSource) context.getSource()).getPlayerOrException();
        ItemStack held = sPlayer.getItemInHand(Hand.MAIN_HAND);
        if (held.isEmpty() || !(held.getItem() instanceof BlockItem) || !(((BlockItem) held.getItem()).getBlock() instanceof iskallia.vault.block.CryoChamberBlock)) {
            sPlayer.sendMessage((ITextComponent) (new StringTextComponent("Not holding cryochamber!")).withStyle(TextFormatting.RED), Util.NIL_UUID);
            return 0;
        }

        UUID eternalUUID = UUIDArgument.getUuid(context, "uuid");
        EternalsData data = EternalsData.get(sPlayer.getLevel());
        EternalData eternal = data.getEternal(eternalUUID);
        if (eternal == null) {
            sPlayer.sendMessage((ITextComponent) (new StringTextComponent("Specified eternal does not exist!")).withStyle(TextFormatting.RED), Util.NIL_UUID);
            return 0;
        }

        CompoundNBT tag = held.getOrCreateTagElement("BlockEntityTag");
        tag.putUUID("EternalId", eternalUUID);
        sPlayer.sendMessage((ITextComponent) (new StringTextComponent("Eternal set!")).withStyle(TextFormatting.GREEN), Util.NIL_UUID);
        return 0;
    }

    private int listEternals(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity sPlayer = ((CommandSource) context.getSource()).getPlayerOrException();
        UUID playerId = UUIDArgument.getUuid(context, "playerId");
        EternalsData data = EternalsData.get(sPlayer.getLevel());
        EternalsData.EternalGroup group = data.getEternals(playerId);
        sPlayer.sendMessage((ITextComponent) (new StringTextComponent("Eternals:")).withStyle(TextFormatting.GREEN), Util.NIL_UUID);
        for (EternalData eternal : group.getEternals()) {
            StringTextComponent stringTextComponent = new StringTextComponent(eternal.getId().toString() + " / " + eternal.getName());
            stringTextComponent.withStyle(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Copy UUID"))));
            stringTextComponent.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, eternal.getId().toString())));
            sPlayer.sendMessage((ITextComponent) stringTextComponent, Util.NIL_UUID);
        }
        return 0;
    }

    private int removeEternal(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity sPlayer = ((CommandSource) context.getSource()).getPlayerOrException();
        UUID eternalUUID = UUIDArgument.getUuid(context, "uuid");
        EternalsData data = EternalsData.get(sPlayer.getLevel());

        if (data.removeEternal(eternalUUID)) {
            sPlayer.sendMessage((ITextComponent) (new StringTextComponent("Eternal removed!")).withStyle(TextFormatting.GREEN), Util.NIL_UUID);
            return 0;
        }
        sPlayer.sendMessage((ITextComponent) (new StringTextComponent("Specified eternal does not exist!")).withStyle(TextFormatting.RED), Util.NIL_UUID);
        return 0;
    }


    public boolean isDedicatedServerOnly() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\command\EternalCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */