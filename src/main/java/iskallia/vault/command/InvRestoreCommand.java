// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.backup.BackupListArgument;
import iskallia.vault.backup.BackupManager;
import iskallia.vault.block.entity.VaultChestTileEntity;
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

public class InvRestoreCommand extends Command {
    @Override
    public String getName() {
        return "inv_restore";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(final LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("list").then(Commands.argument("playerUUID", (ArgumentType) UUIDArgument.uuid()).executes(this::listTimestamps)));
        builder.then(Commands.literal("restore").then(Commands.argument("playerUUID", (ArgumentType) UUIDArgument.uuid()).then(Commands.argument("target", (ArgumentType) new BackupListArgument.UUIDRef()).executes(this::restoreUUID))));
    }

    private int listTimestamps(final CommandContext<CommandSource> ctx) {
        final CommandSource src = (CommandSource) ctx.getSource();
        final UUID playerRef = UUIDArgument.getUuid((CommandContext) ctx, "playerUUID");
        final List<String> timestamps = BackupManager.getMostRecentBackupFileTimestamps(src.getServer(), playerRef);
        src.sendSuccess((ITextComponent) new StringTextComponent("Last 5 available backups:"), true);
        timestamps.forEach(timestamp -> {
            final String restoreCmd = String.format("/%s %s restore %s %s", "the_vault", this.getName(), playerRef.toString(), timestamp);
            final ClickEvent ce = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, restoreCmd);
            final HoverEvent he =
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click to get restore command!"));
            final StringTextComponent feedback = new StringTextComponent(timestamp);
            feedback.setStyle(Style.EMPTY.withClickEvent(ce).withHoverEvent(he));
            src.sendSuccess((ITextComponent) feedback, true);
            return;
        });
        return 0;
    }

    private int restoreUUID(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final UUID playerRef = UUIDArgument.getUuid((CommandContext) ctx, "playerUUID");
        if (this.attemptRestore((CommandSource) ctx.getSource(), playerRef, (String) ctx.getArgument("target", (Class) String.class))) {
            return 1;
        }
        return 0;
    }

    private boolean attemptRestore(final CommandSource src, final UUID playerRef, final String target) throws CommandSyntaxException {
        final ServerPlayerEntity playerSource = src.getPlayerOrException();
        final MinecraftServer srv = src.getServer();
        return BackupManager.getStoredItemStacks(srv, playerRef, target).map(stacks -> {
            if (stacks.isEmpty()) {
                src.sendSuccess((ITextComponent) new StringTextComponent("Backup file did not contain any items!").withStyle(TextFormatting.RED), true);
                return false;
            } else {
                final ServerWorld world = playerSource.getLevel();
                final BlockPos offset = playerSource.blockPosition();
                final int chestsRequired = MathHelper.ceil(stacks.size() / 27.0f);
                int i = 0;
                while (i < 2 + chestsRequired) {
                    final BlockPos chestPos = offset.offset(0, 2 + i, 0);
                    if (!World.isInWorldBounds(chestPos) || !world.isEmptyBlock(chestPos)) {
                        src.sendSuccess((ITextComponent) new StringTextComponent("Empty space above the player is required!").withStyle(TextFormatting.RED), true);
                        return false;
                    } else {
                        ++i;
                    }
                }
                for (int j = 0; j < chestsRequired; ++j) {
                    final BlockPos chestPos2 = offset.offset(0, 2 + j, 0);
                    final List<ItemStack> subStacks = stacks.subList(j * 27, Math.min(stacks.size(), (j + 1) * 27));
                    if (world.setBlock(chestPos2, ModBlocks.VAULT_CHEST.defaultBlockState(), 3)) {
                        final TileEntity te = world.getBlockEntity(chestPos2);
                        if (te instanceof VaultChestTileEntity) {
                            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(inv -> {
                                for (int index = 0; index < subStacks.size(); ++index) {
                                    inv.insertItem(index, (ItemStack) subStacks.get(index), false);
                                }
                            });
                        }
                    }
                }
                return true;
            }
        }).orElseGet(() -> {
            src.sendSuccess((ITextComponent) new StringTextComponent("No such backup file found!").withStyle(TextFormatting.RED), true);
            return false;
        });
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }
}
