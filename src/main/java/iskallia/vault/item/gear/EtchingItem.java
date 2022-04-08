package iskallia.vault.item.gear;

import iskallia.vault.attribute.EnumAttribute;
import iskallia.vault.config.EtchingConfig;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.BasicItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EtchingItem extends BasicItem {
    public EtchingItem(ResourceLocation id, Item.Properties properties) {
        super(id, properties);
    }

    public static ItemStack createEtchingStack(VaultGear.Set set) {
        ItemStack etchingStack = new ItemStack((IItemProvider) ModItems.ETCHING);
        ModAttributes.GEAR_SET.create(etchingStack, set);
        ModAttributes.GEAR_STATE.create(etchingStack, VaultGear.State.IDENTIFIED);
        return etchingStack;
    }


    public ITextComponent getName(ItemStack stack) {
        ITextComponent name = super.getName(stack);

        ModAttributes.GEAR_SET.getValue(stack).ifPresent(set -> {
            EtchingConfig.Etching etching = ModConfigs.ETCHING.getFor(set);

            Style style = name.getStyle().withColor(Color.fromRgb(etching.color));
            ((IFormattableTextComponent) name).setStyle(style);
        });
        return name;
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ActionResult<ItemStack> result = super.use(world, player, hand);

        ItemStack stack = player.getItemInHand(hand);

        if (world.isClientSide()) {
            return result;
        }

        if (world.dimension() != Vault.VAULT_KEY) {
            Optional<EnumAttribute<VaultGear.State>> attribute = ModAttributes.GEAR_STATE.get(stack);

            if (attribute.isPresent() && ((EnumAttribute) attribute.get()).getValue(stack) == VaultGear.State.UNIDENTIFIED) {
                ((EnumAttribute) attribute.get()).setBaseValue(VaultGear.State.ROLLING);
                return ActionResult.fail(stack);
            }
        }

        return result;
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);

        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            if (world instanceof net.minecraft.world.server.ServerWorld &&
                    stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);

                    ItemStack flask = new ItemStack((IItemProvider) this);
                    MiscUtils.giveItem(player, flask);
                }
            }


            if (((EnumAttribute) ModAttributes.GEAR_STATE.getOrCreate(stack, VaultGear.State.UNIDENTIFIED)).getValue(stack) == VaultGear.State.ROLLING) {
                tickRoll(stack, world, player, itemSlot, isSelected);
            }
        }
    }

    public void tickRoll(ItemStack stack, World world, ServerPlayerEntity player, int itemSlot, boolean isSelected) {
        int rollTicks = stack.getOrCreateTag().getInt("RollTicks");
        int lastModelHit = stack.getOrCreateTag().getInt("LastModelHit");
        double displacement = VaultGear.getDisplacement(rollTicks);

        if (rollTicks >= 120) {
            ModAttributes.GEAR_STATE.create(stack, VaultGear.State.IDENTIFIED);
            stack.getOrCreateTag().remove("RollTicks");
            stack.getOrCreateTag().remove("LastModelHit");
            world.playSound(null, player.blockPosition(), ModSounds.CONFETTI_SFX, SoundCategory.PLAYERS, 0.5F, 1.0F);

            return;
        }
        if ((int) displacement != lastModelHit) {
            VaultGear.Set set = ModConfigs.ETCHING.getRandomSet();
            ModAttributes.GEAR_SET.create(stack, set);

            stack.getOrCreateTag().putInt("LastModelHit", (int) displacement);
            world.playSound(null, player.blockPosition(), ModSounds.RAFFLE_SFX, SoundCategory.PLAYERS, 0.4F, 1.0F);
        }

        stack.getOrCreateTag().putInt("RollTicks", rollTicks + 1);
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);

        ModAttributes.GEAR_SET.get(stack).map(attribute -> (VaultGear.Set) attribute.getValue(stack)).ifPresent(value -> {
            if (value == VaultGear.Set.NONE) {
                return;
            }
            EtchingConfig.Etching etching = ModConfigs.ETCHING.getFor(value);
            tooltip.add((new StringTextComponent("Etching: ")).append((ITextComponent) (new StringTextComponent(value.name())).withStyle(Style.EMPTY.withColor(Color.fromRgb(etching.color)))));
            tooltip.add(StringTextComponent.EMPTY);
            for (TextComponent descriptionLine : split(etching.effectText)) {
                tooltip.add(descriptionLine.withStyle(TextFormatting.GRAY));
            }
        });
    }

    private List<TextComponent> split(String text) {
        LinkedList<TextComponent> tooltip = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        for (String word : text.split("\\s+")) {
            sb.append(word).append(" ");

            if (sb.length() >= 30) {
                tooltip.add(new StringTextComponent(sb.toString().trim()));
                sb = new StringBuilder();
            }
        }

        if (sb.length() > 0) {
            tooltip.add(new StringTextComponent(sb.toString().trim()));
        }

        return tooltip;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\EtchingItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */