// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import iskallia.vault.init.ModItems;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nonnull;
import iskallia.vault.world.data.PlayerFavourData;
import net.minecraft.item.Item;

public class FinalVaultKeystoneItem extends Item
{
    @Nonnull
    private final PlayerFavourData.VaultGodType associatedGod;
    
    public FinalVaultKeystoneItem(final ResourceLocation id, final PlayerFavourData.VaultGodType associatedGod) {
        super(new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(1));
        this.associatedGod = associatedGod;
        this.setRegistryName(id);
    }
    
    @Nonnull
    public PlayerFavourData.VaultGodType getAssociatedGod() {
        return this.associatedGod;
    }
    
    public void appendHoverText(@Nonnull final ItemStack stack, @Nullable final World world, @Nonnull final List<ITextComponent> tooltip, @Nonnull final ITooltipFlag flag) {
        super.appendHoverText(stack, world, (List)tooltip, flag);
        final IFormattableTextComponent godNameText = new StringTextComponent(this.associatedGod.getName()).withStyle(this.associatedGod.getChatColor());
        final IFormattableTextComponent godTitleText = new StringTextComponent(this.associatedGod.getTitle()).withStyle(this.associatedGod.getChatColor()).withStyle(TextFormatting.BOLD);
        tooltip.add((ITextComponent)new StringTextComponent(""));
        tooltip.add((ITextComponent)new StringTextComponent("Keystone of ").append((ITextComponent)godNameText));
        tooltip.add((ITextComponent)godTitleText);
    }
}
