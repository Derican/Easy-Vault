package iskallia.vault.item;

import iskallia.vault.entity.DrillArrowEntity;
import iskallia.vault.util.MiscUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemDrillArrow
        extends ArrowItem {
    public ItemDrillArrow(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(63));
        setRegistryName(id);
    }


    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            for (ArrowTier tier : ArrowTier.values()) {
                ItemStack stack = new ItemStack((IItemProvider) this);
                setArrowTier(stack, tier);
                items.add(stack);
            }
        }
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add((new TranslationTextComponent(getArrowTier(stack).getName())).withStyle(TextFormatting.GOLD));
    }


    public AbstractArrowEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return (AbstractArrowEntity) (new DrillArrowEntity(world, shooter)).setMaxBreakCount(getArrowTier(stack).getBreakCount());
    }


    public boolean isInfinite(ItemStack stack, ItemStack bow, PlayerEntity player) {
        return false;
    }

    @Nonnull
    public static ArrowTier getArrowTier(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemDrillArrow)) {
            return ArrowTier.NORMAL;
        }
        int tierOrd = stack.getOrCreateTag().getInt("tier");
        return (ArrowTier) MiscUtils.getEnumEntry(ArrowTier.class, tierOrd);
    }

    public static void setArrowTier(ItemStack stack, @Nonnull ArrowTier tier) {
        if (!(stack.getItem() instanceof ItemDrillArrow)) {
            return;
        }

        stack.getOrCreateTag().putInt("tier", tier.ordinal());
    }

    public enum ArrowTier {
        NORMAL(400),
        RARE(700),
        EPIC(1000);

        private final int breakCount;

        ArrowTier(int breakCount) {
            this.breakCount = breakCount;
        }

        public int getBreakCount() {
            return this.breakCount;
        }

        public String getName() {
            return "item.the_vault.drill_arrow." + name().toLowerCase();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemDrillArrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */