// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.VaultRarity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLegendaryTreasure extends Item
{
    private VaultRarity vaultRarity;
    
    public ItemLegendaryTreasure(final ItemGroup group, final ResourceLocation id, final VaultRarity vaultRarity) {
        super(new Item.Properties().tab(group).stacksTo(1));
        this.setRegistryName(id);
        this.vaultRarity = vaultRarity;
    }
    
    public ActionResult<ItemStack> use(final World worldIn, final PlayerEntity playerIn, final Hand handIn) {
        if (worldIn.isClientSide) {
            return (ActionResult<ItemStack>)super.use(worldIn, playerIn, handIn);
        }
        if (handIn != Hand.MAIN_HAND) {
            return (ActionResult<ItemStack>)super.use(worldIn, playerIn, handIn);
        }
        final ItemStack stack = playerIn.getMainHandItem();
        if (stack.getItem() instanceof ItemLegendaryTreasure) {
            final ItemLegendaryTreasure item = (ItemLegendaryTreasure)stack.getItem();
            ItemStack toDrop = ItemStack.EMPTY;
            switch (item.getRarity()) {
                case COMMON: {
                    toDrop = ModConfigs.LEGENDARY_TREASURE_NORMAL.getRandom();
                    break;
                }
                case RARE: {
                    toDrop = ModConfigs.LEGENDARY_TREASURE_RARE.getRandom();
                    break;
                }
                case EPIC: {
                    toDrop = ModConfigs.LEGENDARY_TREASURE_EPIC.getRandom();
                    break;
                }
                case OMEGA: {
                    toDrop = ModConfigs.LEGENDARY_TREASURE_OMEGA.getRandom();
                    break;
                }
            }
            playerIn.drop(toDrop, false);
            stack.shrink(1);
            ItemRelicBoosterPack.successEffects(worldIn, playerIn.position());
        }
        return (ActionResult<ItemStack>)super.use(worldIn, playerIn, handIn);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        if (stack.getItem() instanceof ItemLegendaryTreasure) {
            final ItemLegendaryTreasure item = (ItemLegendaryTreasure)stack.getItem();
            tooltip.add((ITextComponent)new StringTextComponent(TextFormatting.GOLD + "Right-Click to identify..."));
            tooltip.add((ITextComponent)new StringTextComponent("Rarity: " + item.getRarity().color + item.getRarity()));
        }
        super.appendHoverText(stack, worldIn, (List)tooltip, flagIn);
    }
    
    public ITextComponent getName(final ItemStack stack) {
        if (stack.getItem() instanceof ItemLegendaryTreasure) {
            final ItemLegendaryTreasure item = (ItemLegendaryTreasure)stack.getItem();
            return (ITextComponent)new StringTextComponent(item.getRarity().color + "Legendary Treasure");
        }
        return super.getName(stack);
    }
    
    public VaultRarity getRarity() {
        return this.vaultRarity;
    }
}
