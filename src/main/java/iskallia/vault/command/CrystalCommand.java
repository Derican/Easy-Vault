// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.VaultRoomNames;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.modifier.VaultModifier;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.server.command.EnumArgument;

public class CrystalCommand extends Command
{
    @Override
    public String getName() {
        return "crystal";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public void build(final LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("preventRandomModifiers").then(Commands.argument("random", (ArgumentType)BoolArgumentType.bool()).executes(this::setRollsRandom)));
        builder.then(Commands.literal("canTriggerInfluences").then(Commands.argument("trigger", (ArgumentType)BoolArgumentType.bool()).executes(this::setCanTriggerInfluences)));
        builder.then(Commands.literal("canGenerateTreasureRooms").then(Commands.argument("generate", (ArgumentType)BoolArgumentType.bool()).executes(this::canGenerateTreasureRooms)));
        builder.then(Commands.literal("setModifiable").then(Commands.argument("modifiable", (ArgumentType)BoolArgumentType.bool()).executes(this::setModifiable)));
        builder.then(Commands.literal("addModifier").then(Commands.argument("modifier", (ArgumentType)StringArgumentType.string()).executes(this::addModifier)));
        builder.then(Commands.literal("addRoom").then(Commands.argument("roomKey", (ArgumentType)StringArgumentType.string()).then(Commands.argument("amount", (ArgumentType)IntegerArgumentType.integer(1, 100)).executes(this::addRoom))));
        builder.then(Commands.literal("objectiveCount").then(Commands.argument("count", (ArgumentType)IntegerArgumentType.integer(1)).executes(this::setObjectiveCount)));
        builder.then(Commands.literal("objective").then(Commands.argument("crystalObjective", (ArgumentType)StringArgumentType.string()).executes(this::setObjective)));
        builder.then(Commands.literal("clearObjective").executes(this::clearObjective));
        builder.then(Commands.literal("type").then(Commands.argument("crystalType", (ArgumentType)EnumArgument.enumArgument((Class)CrystalData.Type.class)).executes(this::setType)));
    }
    
    private int canGenerateTreasureRooms(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ItemStack crystal = this.getCrystal(ctx);
        final CrystalData data = VaultCrystalItem.getData(crystal);
        final boolean generateTreasureRooms = BoolArgumentType.getBool((CommandContext)ctx, "generate");
        data.setCanGenerateTreasureRooms(generateTreasureRooms);
        return 0;
    }
    
    private int setCanTriggerInfluences(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ItemStack crystal = this.getCrystal(ctx);
        final CrystalData data = VaultCrystalItem.getData(crystal);
        final boolean triggerInfluences = BoolArgumentType.getBool((CommandContext)ctx, "trigger");
        data.setCanTriggerInfluences(triggerInfluences);
        return 0;
    }
    
    private int setRollsRandom(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ItemStack crystal = this.getCrystal(ctx);
        final CrystalData data = VaultCrystalItem.getData(crystal);
        final boolean randomModifiers = BoolArgumentType.getBool((CommandContext)ctx, "random");
        data.setPreventsRandomModifiers(randomModifiers);
        return 0;
    }
    
    private int setModifiable(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ItemStack crystal = this.getCrystal(ctx);
        final CrystalData data = VaultCrystalItem.getData(crystal);
        final boolean modifiable = BoolArgumentType.getBool((CommandContext)ctx, "modifiable");
        data.setModifiable(modifiable);
        return 0;
    }
    
    private int addRoom(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ItemStack crystal = this.getCrystal(ctx);
        final CrystalData data = VaultCrystalItem.getData(crystal);
        final String roomKey = StringArgumentType.getString((CommandContext)ctx, "roomKey");
        if (VaultRoomNames.getName(roomKey) == null) {
            ((CommandSource)ctx.getSource()).getPlayerOrException().sendMessage((ITextComponent)new StringTextComponent("Unknown Room: " + roomKey), Util.NIL_UUID);
            return 0;
        }
        for (int amount = IntegerArgumentType.getInteger((CommandContext)ctx, "amount"), i = 0; i < amount; ++i) {
            data.addGuaranteedRoom(roomKey);
        }
        return 0;
    }
    
    private int addModifier(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ItemStack crystal = this.getCrystal(ctx);
        final CrystalData data = VaultCrystalItem.getData(crystal);
        final String modifierStr = StringArgumentType.getString((CommandContext)ctx, "modifier");
        final VaultModifier modifier = ModConfigs.VAULT_MODIFIERS.getByName(modifierStr);
        if (modifier == null) {
            ((CommandSource)ctx.getSource()).getPlayerOrException().sendMessage((ITextComponent)new StringTextComponent("Unknown Modifier: " + modifierStr), Util.NIL_UUID);
            return 0;
        }
        data.addModifier(modifierStr);
        return 0;
    }
    
    private int setObjectiveCount(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ItemStack crystal = this.getCrystal(ctx);
        final CrystalData data = VaultCrystalItem.getData(crystal);
        final int count = IntegerArgumentType.getInteger((CommandContext)ctx, "count");
        data.setTargetObjectiveCount(count);
        return 0;
    }
    
    private int clearObjective(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ItemStack crystal = this.getCrystal(ctx);
        final CrystalData data = VaultCrystalItem.getData(crystal);
        data.setSelectedObjective(null);
        return 0;
    }
    
    private int setObjective(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ItemStack crystal = this.getCrystal(ctx);
        final CrystalData data = VaultCrystalItem.getData(crystal);
        final String objectiveStr = StringArgumentType.getString((CommandContext)ctx, "crystalObjective");
        VaultRaid.ARCHITECT_EVENT.get();
        final VaultObjective objective = VaultObjective.getObjective(new ResourceLocation(objectiveStr));
        if (objective == null) {
            ((CommandSource)ctx.getSource()).getPlayerOrException().sendMessage((ITextComponent)new StringTextComponent("Unknown Objective: " + objectiveStr), Util.NIL_UUID);
            return 0;
        }
        data.setSelectedObjective(objective.getId());
        return 0;
    }
    
    private int setType(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ItemStack crystal = this.getCrystal(ctx);
        final CrystalData data = VaultCrystalItem.getData(crystal);
        final CrystalData.Type type = (CrystalData.Type)ctx.getArgument("crystalType", (Class)CrystalData.Type.class);
        if (type == CrystalData.Type.RAFFLE) {
            data.setPlayerBossName(((CommandSource)ctx.getSource()).getPlayerOrException().getName().getString());
        }
        else {
            if (data.getPlayerBossName() != null) {
                data.setPlayerBossName("");
            }
            data.setType(type);
        }
        return 0;
    }
    
    private ItemStack getCrystal(final CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        final ServerPlayerEntity player = ((CommandSource)ctx.getSource()).getPlayerOrException();
        final ItemStack held = player.getItemInHand(Hand.MAIN_HAND);
        if (held.isEmpty() || !(held.getItem() instanceof VaultCrystalItem)) {
            player.sendMessage((ITextComponent)new StringTextComponent("Not holding crystal!"), Util.NIL_UUID);
            throw new RuntimeException();
        }
        return held;
    }
    
    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }
}
