package iskallia.vault.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.backup.BackupListArgument;
import iskallia.vault.backup.BackupManager;
import iskallia.vault.init.ModBlocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.UUIDArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;
import java.util.UUID;


public class InvRestoreCommand
        extends Command {
    public String getName() {
        return "inv_restore";
    }


    public int getRequiredPermissionLevel() {
        return 2;
    }


    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("list")
                .then(Commands.argument("playerUUID", (ArgumentType) UUIDArgument.uuid())
                        .executes(this::listTimestamps)));
        builder.then(Commands.literal("restore")
                .then(Commands.argument("playerUUID", (ArgumentType) UUIDArgument.uuid())
                        .then(Commands.argument("target", (ArgumentType) new BackupListArgument.UUIDRef())
                                .executes(this::restoreUUID))));
    }

    private int listTimestamps(CommandContext<CommandSource> ctx) {
        CommandSource src = (CommandSource) ctx.getSource();
        UUID playerRef = UUIDArgument.getUuid(ctx, "playerUUID");
        List<String> timestamps = BackupManager.getMostRecentBackupFileTimestamps(src.getServer(), playerRef);

        src.sendSuccess((ITextComponent) new StringTextComponent("Last 5 available backups:"), true);
        timestamps.forEach(timestamp -> {
            String restoreCmd = String.format("/%s %s restore %s %s", new Object[]{"the_vault", getName(), playerRef.toString(), timestamp});

            ClickEvent ce = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, restoreCmd);
            HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click to get restore command!"));
            StringTextComponent feedback = new StringTextComponent(timestamp);
            feedback.setStyle(Style.EMPTY.withClickEvent(ce).withHoverEvent(he));
            src.sendSuccess((ITextComponent) feedback, true);
        });
        return 0;
    }

    private int restoreUUID(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        UUID playerRef = UUIDArgument.getUuid(ctx, "playerUUID");
        if (attemptRestore((CommandSource) ctx.getSource(), playerRef, (String) ctx.getArgument("target", String.class))) {
            return 1;
        }
        return 0;
    }

    private boolean attemptRestore(CommandSource src, UUID playerRef, String target) throws CommandSyntaxException {
        ServerPlayerEntity playerSource = src.getPlayerOrException();
        MinecraftServer srv = src.getServer();
        return ((Boolean) BackupManager.getStoredItemStacks(srv, playerRef, target).map(stacks -> {
            if (stacks.isEmpty()) {
                src.sendSuccess((ITextComponent) (new StringTextComponent("Backup file did not contain any items!")).withStyle(TextFormatting.RED), true);

                return Boolean.valueOf(false);
            }

            ServerWorld world = playerSource.getLevel();

            BlockPos offset = playerSource.blockPosition();

            int chestsRequired = MathHelper.ceil(stacks.size() / 27.0F);

            int i;

            for (i = 0; i < 2 + chestsRequired; i++) {
                BlockPos chestPos = offset.offset(0, 2 + i, 0);

                if (!World.isInWorldBounds(chestPos) || !world.isEmptyBlock(chestPos)) {
                    src.sendSuccess((ITextComponent) (new StringTextComponent("Empty space above the player is required!")).withStyle(TextFormatting.RED), true);
                    return Boolean.valueOf(false);
                }
            }
            for (i = 0; i < chestsRequired; i++) {
                BlockPos chestPos = offset.offset(0, 2 + i, 0);
                List<ItemStack> subStacks = stacks.subList(i * 27, Math.min(stacks.size(), (i + 1) * 27));
                if (world.setBlock(chestPos, ModBlocks.VAULT_CHEST.defaultBlockState(), 3)) {
                    TileEntity te = world.getBlockEntity(chestPos);
                    if (te instanceof iskallia.vault.block.entity.VaultChestTileEntity) {
                        te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((net.minecraftforge.items.IItemHandler handler) -> {
                        });
                    }
                }
            }
            return Boolean.valueOf(true);
        }).orElseGet(() -> {
            src.sendSuccess((ITextComponent) (new StringTextComponent("No such backup file found!")).withStyle(TextFormatting.RED), true);
            return Boolean.valueOf(false);
        })).booleanValue();
    }


    public boolean isDedicatedServerOnly() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\command\InvRestoreCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */