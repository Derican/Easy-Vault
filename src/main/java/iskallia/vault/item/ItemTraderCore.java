// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item;

import iskallia.vault.config.entry.vending.TradeEntry;
import iskallia.vault.container.RenamingContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.util.NameProviderPublic;
import iskallia.vault.util.RenameType;
import iskallia.vault.util.nbt.NBTSerializer;
import iskallia.vault.vending.Product;
import iskallia.vault.vending.Trade;
import iskallia.vault.vending.TraderCore;
import iskallia.vault.world.data.PlayerVaultStatsData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTraderCore extends Item
{
    public ItemTraderCore(final ItemGroup group, final ResourceLocation id) {
        super(new Item.Properties().tab(group).stacksTo(1));
        this.setRegistryName(id);
    }
    
    public static ItemStack generate(final String nickname, final int vaultLevel) {
        final TradeEntry tradeEntry = ModConfigs.TRADER_CORE_COMMON.getRandomTrade(vaultLevel);
        if (tradeEntry == null) {
            return ItemStack.EMPTY;
        }
        return getStackFromCore(new TraderCore(nickname, tradeEntry.toTrade()));
    }
    
    public static ItemStack getStackFromCore(final TraderCore core) {
        final ItemStack stack = new ItemStack((IItemProvider)ModItems.TRADER_CORE, 1);
        final CompoundNBT nbt = new CompoundNBT();
        try {
            nbt.put("core", (INBT)NBTSerializer.serialize(core));
            stack.setTag(nbt);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return stack;
    }
    
    public static TraderCore getCoreFromStack(final ItemStack itemStack) {
        final CompoundNBT nbt = itemStack.getTag();
        if (nbt == null) {
            return null;
        }
        try {
            return NBTSerializer.deserialize(TraderCore.class, nbt.getCompound("core"));
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        final CompoundNBT nbt = stack.getOrCreateTag();
        if (nbt.contains("core")) {
            TraderCore core;
            try {
                core = NBTSerializer.deserialize(TraderCore.class, (CompoundNBT)nbt.get("core"));
            }
            catch (final Exception e) {
                e.printStackTrace();
                return;
            }
            final Trade trade = core.getTrade();
            if (!trade.isValid()) {
                return;
            }
            final Product buy = trade.getBuy();
            final Product extra = trade.getExtra();
            final Product sell = trade.getSell();
            tooltip.add((ITextComponent)new StringTextComponent(""));
            tooltip.add((ITextComponent)new StringTextComponent("Trader: "));
            final StringTextComponent traderName = new StringTextComponent(" " + core.getName());
            traderName.setStyle(Style.EMPTY.withColor(Color.fromRgb(16755200)));
            tooltip.add((ITextComponent)traderName);
            tooltip.add((ITextComponent)new StringTextComponent(""));
            tooltip.add((ITextComponent)new StringTextComponent("Trades: "));
            if (buy != null && buy.isValid()) {
                final StringTextComponent comp = new StringTextComponent(" - Buy: ");
                final TranslationTextComponent name = new TranslationTextComponent(buy.getItem().getDescriptionId());
                name.setStyle(Style.EMPTY.withColor(Color.fromRgb(16755200)));
                comp.append((ITextComponent)name).append((ITextComponent)new StringTextComponent(" x" + buy.getAmount()));
                tooltip.add((ITextComponent)comp);
            }
            if (extra != null && extra.isValid()) {
                final StringTextComponent comp = new StringTextComponent(" - Extra: ");
                final TranslationTextComponent name = new TranslationTextComponent(extra.getItem().getDescriptionId());
                name.setStyle(Style.EMPTY.withColor(Color.fromRgb(16755200)));
                comp.append((ITextComponent)name).append((ITextComponent)new StringTextComponent(" x" + extra.getAmount()));
                tooltip.add((ITextComponent)comp);
            }
            if (sell != null && sell.isValid()) {
                final StringTextComponent comp = new StringTextComponent(" - Sell: ");
                final TranslationTextComponent name = new TranslationTextComponent(sell.getItem().getDescriptionId());
                name.setStyle(Style.EMPTY.withColor(Color.fromRgb(16755200)));
                comp.append((ITextComponent)name).append((ITextComponent)new StringTextComponent(" x" + sell.getAmount()));
                tooltip.add((ITextComponent)comp);
            }
            tooltip.add((ITextComponent)new StringTextComponent(""));
            if (trade.getTradesLeft() == 0) {
                final StringTextComponent comp = new StringTextComponent("[0] Sold out, sorry!");
                comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)));
                tooltip.add((ITextComponent)comp);
            }
            else if (trade.getTradesLeft() == -1) {
                final StringTextComponent comp = new StringTextComponent("[\u221e] Has unlimited trades.");
                comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(43775)));
                tooltip.add((ITextComponent)comp);
            }
            else {
                final StringTextComponent comp = new StringTextComponent("[" + trade.getTradesLeft() + "] Has a limited stock.");
                comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(16755200)));
                tooltip.add((ITextComponent)comp);
            }
        }
    }
    
    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (!(entity instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity sPlayer = (ServerPlayerEntity)entity;
        final CompoundNBT nbt = stack.getOrCreateTag();
        if (!nbt.contains("core", 10)) {
            final PlayerVaultStats stats = PlayerVaultStatsData.get(sPlayer.getLevel()).getVaultStats((PlayerEntity)sPlayer);
            final ItemStack generated = generate(NameProviderPublic.getRandomName(), stats.getVaultLevel());
            if (!generated.isEmpty()) {
                stack.setTag(generated.getTag());
            }
        }
        if (nbt.contains("rename_trader", 8)) {
            final String rename = nbt.getString("rename_trader");
            stack.getOrCreateTag().getCompound("core").putString("NAME", rename);
            stack.getOrCreateTag().remove("rename_trader");
        }
    }
    
    public Rarity getRarity(final ItemStack stack) {
        return Rarity.EPIC;
    }
    
    public ITextComponent getName(final ItemStack stack) {
        ITextComponent text = super.getName(stack);
        final CompoundNBT nbt = stack.getOrCreateTag();
        if (nbt.contains("core", 10)) {
            try {
                final TraderCore core = NBTSerializer.deserialize(TraderCore.class, nbt.getCompound("core"));
                text = (ITextComponent)new StringTextComponent(core.getName());
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }
    
    public ActionResult<ItemStack> use(final World worldIn, final PlayerEntity player, final Hand handIn) {
        if (worldIn.isClientSide) {
            return (ActionResult<ItemStack>)super.use(worldIn, player, handIn);
        }
        if (handIn == Hand.OFF_HAND) {
            return (ActionResult<ItemStack>)super.use(worldIn, player, handIn);
        }
        final ItemStack stack = player.getMainHandItem();
        if (player.isShiftKeyDown()) {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("RenameType", RenameType.TRADER_CORE.ordinal());
            nbt.put("Data", (INBT)stack.serializeNBT());
            NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider)new INamedContainerProvider() {
                public ITextComponent getDisplayName() {
                    return (ITextComponent)new StringTextComponent("Trader Core");
                }
                
                @Nullable
                public Container createMenu(final int windowId, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
                    return new RenamingContainer(windowId, nbt);
                }
            }, buffer -> buffer.writeNbt(nbt));
        }
        return (ActionResult<ItemStack>)super.use(worldIn, player, handIn);
    }
}
