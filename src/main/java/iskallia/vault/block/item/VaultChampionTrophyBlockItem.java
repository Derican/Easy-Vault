// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block.item;

import net.minecraft.util.IItemProvider;
import iskallia.vault.init.ModBlocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.text.IFormattableTextComponent;
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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.player.ServerPlayerEntity;
import iskallia.vault.block.VaultChampionTrophy;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemGroup;
import iskallia.vault.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class VaultChampionTrophyBlockItem extends BlockItem
{
    public static final String NBT_OWNER_UUID = "OwnerUUID";
    public static final String NBT_OWNER_NICK = "OwnerNickname";
    public static final String NBT_VARIANT = "Variant";
    public static final String NBT_SCORE = "Score";
    
    public VaultChampionTrophyBlockItem(final Block block) {
        super(block, new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(1));
    }
    
    public void fillItemCategory(@Nonnull final ItemGroup group, @Nonnull final NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            items.add(create(null, VaultChampionTrophy.Variant.GOLDEN));
            items.add(create(null, VaultChampionTrophy.Variant.PLATINUM));
            items.add(create(null, VaultChampionTrophy.Variant.BLUE_SILVER));
            items.add(create(null, VaultChampionTrophy.Variant.SILVER));
        }
    }
    
    public void inventoryTick(@Nonnull final ItemStack itemStack, @Nonnull final World world, @Nonnull final Entity entity, final int itemSlot, final boolean isSelected) {
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
        blockEntityTag.putString("OwnerUUID", player.getUUID().toString());
        blockEntityTag.putString("OwnerNickname", player.getName().getString());
        super.inventoryTick(itemStack, world, entity, itemSlot, isSelected);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull final ItemStack stack, @Nullable final World worldIn, @Nonnull final List<ITextComponent> tooltip, @Nonnull final ITooltipFlag flag) {
        super.appendHoverText(stack, worldIn, (List)tooltip, flag);
        final CompoundNBT blockEntityTag = stack.getOrCreateTagElement("BlockEntityTag");
        final String uuidString = blockEntityTag.getString("OwnerUUID");
        final UUID ownerUUID = uuidString.isEmpty() ? null : UUID.fromString(uuidString);
        final String ownerNickname = McClientHelper.getOnlineProfile(ownerUUID).map(GameProfile::getName).orElse(blockEntityTag.getString("OwnerNickname"));
        final int score = blockEntityTag.getInt("Score");
        final IFormattableTextComponent titleText = new StringTextComponent("Vault Champion").withStyle(TextFormatting.GOLD);
        final IFormattableTextComponent championText = new StringTextComponent("Mighty " + ownerNickname).withStyle(TextFormatting.GOLD).withStyle(TextFormatting.BOLD);
        final IFormattableTextComponent scoreText = new StringTextComponent("Score: ").withStyle(TextFormatting.GOLD).append((ITextComponent)new StringTextComponent(String.valueOf(score)).withStyle(TextFormatting.AQUA));
        tooltip.add((ITextComponent)new StringTextComponent(""));
        tooltip.add((ITextComponent)titleText);
        tooltip.add((ITextComponent)championText);
        tooltip.add((ITextComponent)scoreText);
    }
    
    public static void setScore(final ItemStack itemStack, final int score) {
        final CompoundNBT blockEntityTag = itemStack.getOrCreateTagElement("BlockEntityTag");
        blockEntityTag.putInt("Score", score);
    }
    
    public static ItemStack create(final ServerPlayerEntity owner, final VaultChampionTrophy.Variant variant) {
        return create((owner == null) ? null : owner.getUUID(), (owner == null) ? null : owner.getName().getString(), variant);
    }
    
    public static ItemStack create(final UUID ownerUUID, final String ownerNickname, final VaultChampionTrophy.Variant variant) {
        final ItemStack itemStack = new ItemStack((IItemProvider)ModBlocks.VAULT_CHAMPION_TROPHY_BLOCK_ITEM);
        final CompoundNBT nbt = itemStack.getOrCreateTag();
        final CompoundNBT blockEntityTag = itemStack.getOrCreateTagElement("BlockEntityTag");
        if (ownerUUID != null) {
            blockEntityTag.putString("OwnerUUID", ownerUUID.toString());
        }
        if (ownerNickname != null) {
            blockEntityTag.putString("OwnerNickname", ownerNickname);
        }
        blockEntityTag.putString("Variant", variant.getSerializedName());
        nbt.putInt("CustomModelData", variant.ordinal());
        return itemStack;
    }
}
