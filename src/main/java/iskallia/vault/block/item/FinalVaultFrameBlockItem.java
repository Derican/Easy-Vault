// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.item;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import java.util.function.Function;
import com.mojang.authlib.GameProfile;
import iskallia.vault.util.McClientHelper;
import java.util.UUID;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.item.Rarity;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import iskallia.vault.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import iskallia.vault.util.flag.ExplosionImmune;
import net.minecraft.item.BlockItem;

public class FinalVaultFrameBlockItem extends BlockItem implements ExplosionImmune
{
    public FinalVaultFrameBlockItem(final Block blockIn) {
        super(blockIn, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).fireResistant().stacksTo(1));
    }
    
    @Nonnull
    public Rarity getRarity(@Nonnull final ItemStack stack) {
        return Rarity.EPIC;
    }
    
    public void appendHoverText(@Nonnull final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        final CompoundNBT blockEntityTag = stack.getOrCreateTagElement("BlockEntityTag");
        final String stringUUID = blockEntityTag.getString("OwnerUUID");
        final UUID ownerUUID = stringUUID.isEmpty() ? new UUID(0L, 0L) : UUID.fromString(stringUUID);
        final String ownerNickname = blockEntityTag.getString("OwnerNickname");
        final String displayNickname = McClientHelper.getOnlineProfile(ownerUUID).map((Function<? super GameProfile, ? extends String>)GameProfile::getName).orElse(ownerNickname);
        final IFormattableTextComponent ownerText = new StringTextComponent("Owner:").withStyle(TextFormatting.GOLD);
        final IFormattableTextComponent displayText = new StringTextComponent(displayNickname).withStyle(TextFormatting.GOLD).withStyle(TextFormatting.BOLD);
        tooltip.add((ITextComponent)ownerText.append((ITextComponent)displayText));
        super.appendHoverText(stack, worldIn, (List)tooltip, flagIn);
    }
    
    public void inventoryTick(@Nonnull final ItemStack itemStack, final World world, @Nonnull final Entity entity, final int itemSlot, final boolean isSelected) {
        if (world.isClientSide) {
            return;
        }
        if (!(entity instanceof ServerPlayerEntity)) {
            return;
        }
        final CompoundNBT blockEntityTag = itemStack.getOrCreateTagElement("BlockEntityTag");
        if (blockEntityTag.contains("OwnerUUID")) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity)entity;
        writeToItemStack(itemStack, player);
        super.inventoryTick(itemStack, world, entity, itemSlot, isSelected);
    }
    
    public static void writeToItemStack(final ItemStack itemStack, final ServerPlayerEntity owner) {
        writeToItemStack(itemStack, owner.getUUID(), owner.getName().getString());
    }
    
    public static void writeToItemStack(final ItemStack itemStack, final UUID ownerUUID, final String ownerNickname) {
        final CompoundNBT blockEntityTag = itemStack.getOrCreateTagElement("BlockEntityTag");
        blockEntityTag.putString("OwnerUUID", ownerUUID.toString());
        blockEntityTag.putString("OwnerNickname", ownerNickname);
    }
}
