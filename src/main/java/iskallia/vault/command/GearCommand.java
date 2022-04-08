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

public class GearCommand
        extends Command {
    public String getName() {
        return "gear";
    }


    public int getRequiredPermissionLevel() {
        return 2;
    }


    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(Commands.literal("add")
                .then(Commands.argument("modifier", (ArgumentType) StringArgumentType.string())
                        .executes(this::addModifier)));
        builder.then(Commands.literal("remove")
                .then(Commands.argument("modifier", (ArgumentType) StringArgumentType.string())
                        .executes(this::removeModifier)));
        builder.then(Commands.literal("rarity")
                .then(Commands.argument("rarity", (ArgumentType) EnumArgument.enumArgument(VaultGear.Rarity.class))
                        .executes(this::setRarity)));
        builder.then(Commands.literal("tier")
                .then(Commands.argument("tier", (ArgumentType) IntegerArgumentType.integer(0, 2))
                        .executes(this::setTier)));
    }

    private int addModifier(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = ((CommandSource) context.getSource()).getPlayerOrException();
        String modifierName = StringArgumentType.getString(context, "modifier");
        VAttribute<?, ?> attribute = (VAttribute<?, ?>) ModAttributes.REGISTRY.get(new ResourceLocation(modifierName));
        if (attribute == null) {
            player.sendMessage((ITextComponent) new StringTextComponent("No modifier with name " + modifierName), Util.NIL_UUID);
            return 0;
        }
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty() || !(held.getItem() instanceof VaultGear)) {
            player.sendMessage((ITextComponent) new StringTextComponent("No vault gear in hand!"), Util.NIL_UUID);
            return 0;
        }
        VaultGear.Rarity gearRarity = ModAttributes.GEAR_RARITY.getBase(held).orElse(VaultGear.Rarity.COMMON);
        int tier = ((Integer) ModAttributes.GEAR_TIER.getBase(held).orElse(Integer.valueOf(0))).intValue();

        VaultGearHelper.applyGearModifier(held, gearRarity, tier, attribute);
        return 0;
    }

    private int removeModifier(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = ((CommandSource) context.getSource()).getPlayerOrException();
        String modifierName = StringArgumentType.getString(context, "modifier");
        VAttribute<?, ?> attribute = (VAttribute<?, ?>) ModAttributes.REGISTRY.get(new ResourceLocation(modifierName));
        if (attribute == null) {
            player.sendMessage((ITextComponent) new StringTextComponent("No modifier with name " + modifierName), Util.NIL_UUID);
            return 0;
        }
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty() || !(held.getItem() instanceof VaultGear)) {
            player.sendMessage((ITextComponent) new StringTextComponent("No vault gear in hand!"), Util.NIL_UUID);
            return 0;
        }

        VaultGearHelper.removeAttribute(held, attribute);
        return 0;
    }

    private int setRarity(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = ((CommandSource) context.getSource()).getPlayerOrException();
        VaultGear.Rarity rarity = (VaultGear.Rarity) context.getArgument("rarity", VaultGear.Rarity.class);
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty() || !(held.getItem() instanceof VaultGear)) {
            player.sendMessage((ITextComponent) new StringTextComponent("No vault gear in hand!"), Util.NIL_UUID);
            return 0;
        }

        ModAttributes.GEAR_RARITY.create(held, rarity);
        return 0;
    }

    private int setTier(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = ((CommandSource) context.getSource()).getPlayerOrException();
        int tier = IntegerArgumentType.getInteger(context, "tier");
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty() || !(held.getItem() instanceof VaultGear)) {
            player.sendMessage((ITextComponent) new StringTextComponent("No vault gear in hand!"), Util.NIL_UUID);
            return 0;
        }

        ModAttributes.GEAR_TIER.create(held, Integer.valueOf(tier));
        return 0;
    }


    public boolean isDedicatedServerOnly() {
        return false;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\command\GearCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */