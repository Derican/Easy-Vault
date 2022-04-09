// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.attribute.VAttribute;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.item.gear.VaultGearHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.server.command.EnumArgument;

public class GearCommand extends Command
{
    @Override
    public String getName() {
        return "gear";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public void build(final LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("add").then(Commands.argument("modifier", (ArgumentType)StringArgumentType.string()).executes(this::addModifier)));
        builder.then(Commands.literal("remove").then(Commands.argument("modifier", (ArgumentType)StringArgumentType.string()).executes(this::removeModifier)));
        builder.then(Commands.literal("rarity").then(Commands.argument("rarity", (ArgumentType)EnumArgument.enumArgument((Class)VaultGear.Rarity.class)).executes(this::setRarity)));
        builder.then(Commands.literal("tier").then(Commands.argument("tier", (ArgumentType)IntegerArgumentType.integer(0, 2)).executes(this::setTier)));
    }
    
    private int addModifier(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity player = ((CommandSource)context.getSource()).getPlayerOrException();
        final String modifierName = StringArgumentType.getString((CommandContext)context, "modifier");
        final VAttribute<?, ?> attribute = ModAttributes.REGISTRY.get(new ResourceLocation(modifierName));
        if (attribute == null) {
            player.sendMessage((ITextComponent)new StringTextComponent("No modifier with name " + modifierName), Util.NIL_UUID);
            return 0;
        }
        final ItemStack held = player.getMainHandItem();
        if (held.isEmpty() || !(held.getItem() instanceof VaultGear)) {
            player.sendMessage((ITextComponent)new StringTextComponent("No vault gear in hand!"), Util.NIL_UUID);
            return 0;
        }
        final VaultGear.Rarity gearRarity = ModAttributes.GEAR_RARITY.getBase(held).orElse(VaultGear.Rarity.COMMON);
        final int tier = ModAttributes.GEAR_TIER.getBase(held).orElse(0);
        VaultGearHelper.applyGearModifier(held, gearRarity, tier, attribute);
        return 0;
    }
    
    private int removeModifier(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity player = ((CommandSource)context.getSource()).getPlayerOrException();
        final String modifierName = StringArgumentType.getString((CommandContext)context, "modifier");
        final VAttribute<?, ?> attribute = ModAttributes.REGISTRY.get(new ResourceLocation(modifierName));
        if (attribute == null) {
            player.sendMessage((ITextComponent)new StringTextComponent("No modifier with name " + modifierName), Util.NIL_UUID);
            return 0;
        }
        final ItemStack held = player.getMainHandItem();
        if (held.isEmpty() || !(held.getItem() instanceof VaultGear)) {
            player.sendMessage((ITextComponent)new StringTextComponent("No vault gear in hand!"), Util.NIL_UUID);
            return 0;
        }
        VaultGearHelper.removeAttribute(held, attribute);
        return 0;
    }
    
    private int setRarity(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity player = ((CommandSource)context.getSource()).getPlayerOrException();
        final VaultGear.Rarity rarity = (VaultGear.Rarity)context.getArgument("rarity", (Class)VaultGear.Rarity.class);
        final ItemStack held = player.getMainHandItem();
        if (held.isEmpty() || !(held.getItem() instanceof VaultGear)) {
            player.sendMessage((ITextComponent)new StringTextComponent("No vault gear in hand!"), Util.NIL_UUID);
            return 0;
        }
        ModAttributes.GEAR_RARITY.create(held, rarity);
        return 0;
    }
    
    private int setTier(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity player = ((CommandSource)context.getSource()).getPlayerOrException();
        final int tier = IntegerArgumentType.getInteger((CommandContext)context, "tier");
        final ItemStack held = player.getMainHandItem();
        if (held.isEmpty() || !(held.getItem() instanceof VaultGear)) {
            player.sendMessage((ITextComponent)new StringTextComponent("No vault gear in hand!"), Util.NIL_UUID);
            return 0;
        }
        ModAttributes.GEAR_TIER.create(held, tier);
        return 0;
    }
    
    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }
}
