package iskallia.vault.item;

import iskallia.vault.Vault;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.MiscUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class BasicScavengerItem
        extends BasicTooltipItem {
    private static final ITextComponent SCAVENGER_ITEM_HINT = (ITextComponent) (new TranslationTextComponent("tooltip.the_vault.scavenger_item"))
            .withStyle(TextFormatting.GOLD);

    public BasicScavengerItem(String id) {
        super(Vault.id("scavenger_" + id), (new Item.Properties()).tab(ModItems.SCAVENGER_GROUP), Collections.singletonList(SCAVENGER_ITEM_HINT));
    }

    public BasicScavengerItem(ResourceLocation id, Item.Properties properties, List<ITextComponent> components) {
        super(id, properties, MiscUtils.concat(components, (Object[]) new ITextComponent[]{SCAVENGER_ITEM_HINT}));
    }

    public static void setVaultIdentifier(ItemStack stack, UUID identifier) {
        if (!(stack.getItem() instanceof BasicScavengerItem)) {
            return;
        }
        stack.getOrCreateTag().putUUID("vault_id", identifier);
    }

    @Nullable
    public static UUID getVaultIdentifier(ItemStack stack) {
        if (!(stack.getItem() instanceof BasicScavengerItem)) {
            return null;
        }
        CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.hasUUID("vault_id")) {
            return null;
        }
        return stack.getOrCreateTag().getUUID("vault_id");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\BasicScavengerItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */