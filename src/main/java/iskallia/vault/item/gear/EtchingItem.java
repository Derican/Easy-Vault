// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item.gear;

import iskallia.vault.Vault;
import iskallia.vault.attribute.EnumAttribute;
import iskallia.vault.config.EtchingConfig;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.BasicItem;
import iskallia.vault.util.MiscUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EtchingItem extends BasicItem
{
    public EtchingItem(final ResourceLocation id, final Item.Properties properties) {
        super(id, properties);
    }
    
    public static ItemStack createEtchingStack(final VaultGear.Set set) {
        final ItemStack etchingStack = new ItemStack((IItemProvider)ModItems.ETCHING);
        ModAttributes.GEAR_SET.create(etchingStack, set);
        ModAttributes.GEAR_STATE.create(etchingStack, VaultGear.State.IDENTIFIED);
        return etchingStack;
    }
    
    public ITextComponent getName(final ItemStack stack) {
        final ITextComponent name = super.getName(stack);
        ModAttributes.GEAR_SET.getValue(stack).ifPresent(set -> {
            final EtchingConfig.Etching etching = ModConfigs.ETCHING.getFor(set);
            final Style style = name.getStyle().withColor(Color.fromRgb(etching.color));
            ((IFormattableTextComponent)name).setStyle(style);
            return;
        });
        return name;
    }
    
    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        final ActionResult<ItemStack> result = (ActionResult<ItemStack>)super.use(world, player, hand);
        final ItemStack stack = player.getItemInHand(hand);
        if (world.isClientSide()) {
            return result;
        }
        if (world.dimension() != Vault.VAULT_KEY) {
            final Optional<EnumAttribute<VaultGear.State>> attribute = ModAttributes.GEAR_STATE.get(stack);
            if (attribute.isPresent() && attribute.get().getValue(stack) == VaultGear.State.UNIDENTIFIED) {
                attribute.get().setBaseValue(VaultGear.State.ROLLING);
                return (ActionResult<ItemStack>)ActionResult.fail(stack);
            }
        }
        return result;
    }
    
    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (entity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity)entity;
            if (world instanceof ServerWorld && stack.getCount() > 1) {
                while (stack.getCount() > 1) {
                    stack.shrink(1);
                    final ItemStack flask = new ItemStack((IItemProvider)this);
                    MiscUtils.giveItem(player, flask);
                }
            }
            if (ModAttributes.GEAR_STATE.getOrCreate(stack, VaultGear.State.UNIDENTIFIED).getValue(stack) == VaultGear.State.ROLLING) {
                this.tickRoll(stack, world, player, itemSlot, isSelected);
            }
        }
    }
    
    public void tickRoll(final ItemStack stack, final World world, final ServerPlayerEntity player, final int itemSlot, final boolean isSelected) {
        final int rollTicks = stack.getOrCreateTag().getInt("RollTicks");
        final int lastModelHit = stack.getOrCreateTag().getInt("LastModelHit");
        final double displacement = VaultGear.getDisplacement(rollTicks);
        if (rollTicks >= 120) {
            ModAttributes.GEAR_STATE.create(stack, VaultGear.State.IDENTIFIED);
            stack.getOrCreateTag().remove("RollTicks");
            stack.getOrCreateTag().remove("LastModelHit");
            world.playSound((PlayerEntity)null, player.blockPosition(), ModSounds.CONFETTI_SFX, SoundCategory.PLAYERS, 0.5f, 1.0f);
            return;
        }
        if ((int)displacement != lastModelHit) {
            final VaultGear.Set set = ModConfigs.ETCHING.getRandomSet();
            ModAttributes.GEAR_SET.create(stack, set);
            stack.getOrCreateTag().putInt("LastModelHit", (int)displacement);
            world.playSound((PlayerEntity)null, player.blockPosition(), ModSounds.RAFFLE_SFX, SoundCategory.PLAYERS, 0.4f, 1.0f);
        }
        stack.getOrCreateTag().putInt("RollTicks", rollTicks + 1);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(final ItemStack stack, final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        ModAttributes.GEAR_SET.get(stack).map(attribute -> attribute.getValue(stack)).ifPresent(value -> {
            if (value != VaultGear.Set.NONE) {
                final EtchingConfig.Etching etching = ModConfigs.ETCHING.getFor(value);
                tooltip.add(new StringTextComponent("Etching: ").append((ITextComponent)new StringTextComponent(value.name()).withStyle(Style.EMPTY.withColor(Color.fromRgb(etching.color)))));
                tooltip.add(StringTextComponent.EMPTY);

                final Iterator<TextComponent> iterator=this.split(etching.effectText).iterator();
                while (iterator.hasNext()) {
                    final TextComponent descriptionLine = iterator.next();
                    tooltip.add(descriptionLine.withStyle(TextFormatting.GRAY));
                }
            }
        });
    }
    
    private List<TextComponent> split(final String text) {
        final LinkedList<TextComponent> tooltip = new LinkedList<TextComponent>();
        StringBuilder sb = new StringBuilder();
        for (final String word : text.split("\\s+")) {
            sb.append(word).append(" ");
            if (sb.length() >= 30) {
                tooltip.add((TextComponent)new StringTextComponent(sb.toString().trim()));
                sb = new StringBuilder();
            }
        }
        if (sb.length() > 0) {
            tooltip.add((TextComponent)new StringTextComponent(sb.toString().trim()));
        }
        return tooltip;
    }
}
