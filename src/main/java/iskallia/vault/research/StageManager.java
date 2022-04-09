// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.research;

import iskallia.vault.util.SideOnlyFixer;
import iskallia.vault.world.data.PlayerResearchesData;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = "the_vault")
public class StageManager
{
    public static ResearchTree RESEARCH_TREE;
    
    public static ResearchTree getResearchTree(final PlayerEntity player) {
        if (player.level.isClientSide) {
            return (StageManager.RESEARCH_TREE != null) ? StageManager.RESEARCH_TREE : new ResearchTree(player.getUUID());
        }
        return PlayerResearchesData.get((ServerWorld)player.level).getResearches(player);
    }
    
    private static void warnResearchRequirement(final String researchName, final String i18nKey) {
        final TextComponent name = (TextComponent)new StringTextComponent(researchName);
        final Style style = Style.EMPTY.withColor(Color.fromRgb(-203978));
        name.setStyle(style);
        final TextComponent text = (TextComponent)new TranslationTextComponent("overlay.requires_research." + i18nKey, new Object[] { name });
        Minecraft.getInstance().gui.setOverlayMessage((ITextComponent)text, false);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemCrafted(final PlayerEvent.ItemCraftedEvent event) {
        final PlayerEntity player = event.getPlayer();
        final ResearchTree researchTree = getResearchTree(player);
        final ItemStack craftedItemStack = event.getCrafting();
        final IInventory craftingMatrix = event.getInventory();
        final String restrictedBy = researchTree.restrictedBy(craftedItemStack.getItem(), Restrictions.Type.CRAFTABILITY);
        if (restrictedBy == null) {
            return;
        }
        if (event.getPlayer().level.isClientSide) {
            warnResearchRequirement(restrictedBy, "craft");
        }
        for (int i = 0; i < craftingMatrix.getContainerSize(); ++i) {
            final ItemStack itemStack = craftingMatrix.getItem(i);
            if (itemStack != ItemStack.EMPTY) {
                final ItemStack itemStackToDrop = itemStack.copy();
                itemStackToDrop.setCount(1);
                player.drop(itemStackToDrop, false, false);
            }
        }
        final int slot = SideOnlyFixer.getSlotFor(player.inventory, craftedItemStack);
        if (slot != -1) {
            final ItemStack stackInSlot = player.inventory.getItem(slot);
            if (stackInSlot.getCount() < craftedItemStack.getCount()) {
                craftedItemStack.setCount(stackInSlot.getCount());
            }
            stackInSlot.shrink(craftedItemStack.getCount());
        }
        else {
            craftedItemStack.shrink(craftedItemStack.getCount());
        }
    }
    
    @SubscribeEvent
    public static void onItemUse(final PlayerInteractEvent.RightClickItem event) {
        if (!event.isCancelable()) {
            return;
        }
        final PlayerEntity player = event.getPlayer();
        final ResearchTree researchTree = getResearchTree(player);
        final Item usedItem = event.getItemStack().getItem();
        final String restrictedBy = researchTree.restrictedBy(usedItem, Restrictions.Type.USABILITY);
        if (restrictedBy == null) {
            return;
        }
        if (event.getSide() == LogicalSide.CLIENT) {
            warnResearchRequirement(restrictedBy, "usage");
        }
        event.setCanceled(true);
    }
    
    @SubscribeEvent
    public static void onRightClickEmpty(final PlayerInteractEvent.RightClickEmpty event) {
        if (!event.isCancelable()) {
            return;
        }
        final PlayerEntity player = event.getPlayer();
        final ResearchTree researchTree = getResearchTree(player);
        final Item usedItem = event.getItemStack().getItem();
        final String restrictedBy = researchTree.restrictedBy(usedItem, Restrictions.Type.USABILITY);
        if (restrictedBy == null) {
            return;
        }
        if (event.getSide() == LogicalSide.CLIENT) {
            warnResearchRequirement(restrictedBy, "usage");
        }
        event.setCanceled(true);
    }
    
    @SubscribeEvent
    public static void onBlockInteraction(final PlayerInteractEvent.RightClickBlock event) {
        if (!event.isCancelable()) {
            return;
        }
        final PlayerEntity player = event.getPlayer();
        final ResearchTree researchTree = getResearchTree(player);
        final BlockState blockState = player.level.getBlockState(event.getPos());
        String restrictedBy = researchTree.restrictedBy(blockState.getBlock(), Restrictions.Type.BLOCK_INTERACTABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "interact_block");
            }
            event.setCanceled(true);
            return;
        }
        final ItemStack itemStack = event.getItemStack();
        if (itemStack == ItemStack.EMPTY) {
            return;
        }
        final Item item = itemStack.getItem();
        restrictedBy = researchTree.restrictedBy(item, Restrictions.Type.USABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "usage");
            }
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onBlockHit(final PlayerInteractEvent.LeftClickBlock event) {
        if (!event.isCancelable()) {
            return;
        }
        final PlayerEntity player = event.getPlayer();
        final ResearchTree researchTree = getResearchTree(player);
        final BlockState blockState = player.level.getBlockState(event.getPos());
        String restrictedBy = researchTree.restrictedBy(blockState.getBlock(), Restrictions.Type.HITTABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "hit");
            }
            event.setCanceled(true);
            return;
        }
        final ItemStack itemStack = event.getItemStack();
        if (itemStack == ItemStack.EMPTY) {
            return;
        }
        final Item item = itemStack.getItem();
        restrictedBy = researchTree.restrictedBy(item, Restrictions.Type.USABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "usage");
            }
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onEntityInteraction(final PlayerInteractEvent.EntityInteract event) {
        if (!event.isCancelable()) {
            return;
        }
        final PlayerEntity player = event.getPlayer();
        final ResearchTree researchTree = getResearchTree(player);
        final Entity entity = event.getEntity();
        String restrictedBy = researchTree.restrictedBy((EntityType<?>)entity.getType(), Restrictions.Type.ENTITY_INTERACTABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "interact_entity");
            }
            event.setCanceled(true);
            return;
        }
        final ItemStack itemStack = event.getItemStack();
        if (itemStack == ItemStack.EMPTY) {
            return;
        }
        final Item item = itemStack.getItem();
        restrictedBy = researchTree.restrictedBy(item, Restrictions.Type.USABILITY);
        if (restrictedBy != null) {
            if (event.getSide() == LogicalSide.CLIENT) {
                warnResearchRequirement(restrictedBy, "usage");
            }
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onPlayerAttack(final AttackEntityEvent event) {
        if (!event.isCancelable()) {
            return;
        }
        final PlayerEntity player = event.getPlayer();
        final ResearchTree researchTree = getResearchTree(player);
        final Entity entity = event.getEntity();
        String restrictedBy = researchTree.restrictedBy((EntityType<?>)entity.getType(), Restrictions.Type.ENTITY_INTERACTABILITY);
        if (restrictedBy != null) {
            if (player.level.isClientSide) {
                warnResearchRequirement(restrictedBy, "interact_entity");
            }
            event.setCanceled(true);
            return;
        }
        final ItemStack itemStack = player.getMainHandItem();
        if (itemStack == ItemStack.EMPTY) {
            return;
        }
        final Item item = itemStack.getItem();
        restrictedBy = researchTree.restrictedBy(item, Restrictions.Type.USABILITY);
        if (restrictedBy != null) {
            if (player.level.isClientSide) {
                warnResearchRequirement(restrictedBy, "usage");
            }
            event.setCanceled(true);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemTooltip(final ItemTooltipEvent event) {
        final PlayerEntity player = event.getPlayer();
        if (player == null) {
            return;
        }
        final ResearchTree researchTree = getResearchTree(player);
        final Item item = event.getItemStack().getItem();
        final String restrictionCausedBy = Arrays.stream(Restrictions.Type.values()).map(type -> researchTree.restrictedBy(item, type)).filter(Objects::nonNull).findFirst().orElseGet(() -> null);
        if (restrictionCausedBy == null) {
            return;
        }
        final List<ITextComponent> toolTip = event.getToolTip();
        final Style textStyle = Style.EMPTY.withColor(Color.fromRgb(-5723992));
        final Style style = Style.EMPTY.withColor(Color.fromRgb(-203978));
        final TextComponent text = (TextComponent)new TranslationTextComponent("tooltip.requires_research");
        final TextComponent name = (TextComponent)new StringTextComponent(" " + restrictionCausedBy);
        text.setStyle(textStyle);
        name.setStyle(style);
        toolTip.add((ITextComponent)new StringTextComponent(""));
        toolTip.add((ITextComponent)text);
        toolTip.add((ITextComponent)name);
    }
}
