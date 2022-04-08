package iskallia.vault.item;

import iskallia.vault.config.entry.vending.TradeEntry;
import iskallia.vault.container.RenamingContainer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.skill.PlayerVaultStats;
import iskallia.vault.util.NameProviderPublic;
import iskallia.vault.util.RenameType;
import iskallia.vault.util.nbt.INBTSerializable;
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

import javax.annotation.Nullable;
import java.util.List;

public class ItemTraderCore extends Item {
    public ItemTraderCore(ItemGroup group, ResourceLocation id) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(1));

        setRegistryName(id);
    }

    public static ItemStack generate(String nickname, int vaultLevel) {
        TradeEntry tradeEntry = ModConfigs.TRADER_CORE_COMMON.getRandomTrade(vaultLevel);
        if (tradeEntry == null) {
            return ItemStack.EMPTY;
        }
        return getStackFromCore(new TraderCore(nickname, tradeEntry.toTrade()));
    }

    public static ItemStack getStackFromCore(TraderCore core) {
        ItemStack stack = new ItemStack((IItemProvider) ModItems.TRADER_CORE, 1);
        CompoundNBT nbt = new CompoundNBT();
        try {
            nbt.put("core", (INBT) NBTSerializer.serialize((INBTSerializable) core));
            stack.setTag(nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stack;
    }

    public static TraderCore getCoreFromStack(ItemStack itemStack) {
        CompoundNBT nbt = itemStack.getTag();
        if (nbt == null) return null;
        try {
            return (TraderCore) NBTSerializer.deserialize(TraderCore.class, nbt.getCompound("core"));
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if (nbt.contains("core")) {
            TraderCore core;
            try {
                core = (TraderCore) NBTSerializer.deserialize(TraderCore.class, (CompoundNBT) nbt.get("core"));
            } catch (Exception e) {
                e.printStackTrace();

                return;
            }
            Trade trade = core.getTrade();
            if (!trade.isValid()) {
                return;
            }

            Product buy = trade.getBuy();
            Product extra = trade.getExtra();
            Product sell = trade.getSell();
            tooltip.add(new StringTextComponent(""));
            tooltip.add(new StringTextComponent("Trader: "));
            StringTextComponent traderName = new StringTextComponent(" " + core.getName());
            traderName.setStyle(Style.EMPTY.withColor(Color.fromRgb(16755200)));
            tooltip.add(traderName);
            tooltip.add(new StringTextComponent(""));
            tooltip.add(new StringTextComponent("Trades: "));
            if (buy != null && buy.isValid()) {
                StringTextComponent comp = new StringTextComponent(" - Buy: ");
                TranslationTextComponent name = new TranslationTextComponent(buy.getItem().getDescriptionId());
                name.setStyle(Style.EMPTY.withColor(Color.fromRgb(16755200)));
                comp.append((ITextComponent) name).append((ITextComponent) new StringTextComponent(" x" + buy.getAmount()));
                tooltip.add(comp);
            }
            if (extra != null && extra.isValid()) {
                StringTextComponent comp = new StringTextComponent(" - Extra: ");
                TranslationTextComponent name = new TranslationTextComponent(extra.getItem().getDescriptionId());
                name.setStyle(Style.EMPTY.withColor(Color.fromRgb(16755200)));
                comp.append((ITextComponent) name).append((ITextComponent) new StringTextComponent(" x" + extra.getAmount()));
                tooltip.add(comp);
            }
            if (sell != null && sell.isValid()) {
                StringTextComponent comp = new StringTextComponent(" - Sell: ");
                TranslationTextComponent name = new TranslationTextComponent(sell.getItem().getDescriptionId());
                name.setStyle(Style.EMPTY.withColor(Color.fromRgb(16755200)));
                comp.append((ITextComponent) name).append((ITextComponent) new StringTextComponent(" x" + sell.getAmount()));
                tooltip.add(comp);
            }

            tooltip.add(new StringTextComponent(""));
            if (trade.getTradesLeft() == 0) {
                StringTextComponent comp = new StringTextComponent("[0] Sold out, sorry!");
                comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(16711680)));
                tooltip.add(comp);
            } else if (trade.getTradesLeft() == -1) {
                StringTextComponent comp = new StringTextComponent("[∞] Has unlimited trades.");
                comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(43775)));
                tooltip.add(comp);
            } else {
                StringTextComponent comp = new StringTextComponent("[" + trade.getTradesLeft() + "] Has a limited stock.");
                comp.setStyle(Style.EMPTY.withColor(Color.fromRgb(16755200)));
                tooltip.add(comp);
            }
        }
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, world, entity, itemSlot, isSelected);
        if (!(entity instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity sPlayer = (ServerPlayerEntity) entity;

        CompoundNBT nbt = stack.getOrCreateTag();

        if (!nbt.contains("core", 10)) {
            PlayerVaultStats stats = PlayerVaultStatsData.get(sPlayer.getLevel()).getVaultStats((PlayerEntity) sPlayer);

            ItemStack generated = generate(NameProviderPublic.getRandomName(), stats.getVaultLevel());
            if (!generated.isEmpty()) {
                stack.setTag(generated.getTag());
            }
        }

        if (nbt.contains("rename_trader", 8)) {
            String rename = nbt.getString("rename_trader");
            stack.getOrCreateTag().getCompound("core").putString("NAME", rename);
            stack.getOrCreateTag().remove("rename_trader");
        }
    }


    public Rarity getRarity(ItemStack stack) {
        return Rarity.EPIC;
    }

    public ITextComponent getName(ItemStack stack) {
        StringTextComponent stringTextComponent;
        ITextComponent text = super.getName(stack);

        CompoundNBT nbt = stack.getOrCreateTag();
        if (nbt.contains("core", 10)) {
            try {
                TraderCore core = (TraderCore) NBTSerializer.deserialize(TraderCore.class, nbt.getCompound("core"));
                stringTextComponent = new StringTextComponent(core.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return (ITextComponent) stringTextComponent;
    }


    public ActionResult<ItemStack> use(World worldIn, PlayerEntity player, Hand handIn) {
        if (worldIn.isClientSide) return super.use(worldIn, player, handIn);
        if (handIn == Hand.OFF_HAND) return super.use(worldIn, player, handIn);
        ItemStack stack = player.getMainHandItem();

        if (player.isShiftKeyDown()) {
            final CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("RenameType", RenameType.TRADER_CORE.ordinal());
            nbt.put("Data", (INBT) stack.serializeNBT());
            NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {

                public ITextComponent getDisplayName() {
                    return (ITextComponent) new StringTextComponent("Trader Core");
                }


                @Nullable
                public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return (Container) new RenamingContainer(windowId, nbt);
                }
            }buffer -> buffer.writeNbt(nbt));
        }


        return super.use(worldIn, player, handIn);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemTraderCore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */