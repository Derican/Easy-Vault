package iskallia.vault.item;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import iskallia.vault.world.data.PlayerFavourData;

public class GodEssenceItem extends BasicItem
{
    protected PlayerFavourData.VaultGodType godType;

    public GodEssenceItem(final ResourceLocation id, final PlayerFavourData.VaultGodType godType, final Item.Properties properties) {
        super(id, properties);
        this.godType = godType;
    }

    public PlayerFavourData.VaultGodType getGodType() {
        return this.godType;
    }

    @Nonnull
    public ITextComponent getName(@Nonnull final ItemStack stack) {
        return (ITextComponent)((IFormattableTextComponent)super.getName(stack)).withStyle(this.godType.getChatColor());
    }
}
