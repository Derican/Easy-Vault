package iskallia.vault.item;

import iskallia.vault.init.ModItems;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.NameProviderPublic;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemVaultRaffleSeal extends Item {
    public ItemVaultRaffleSeal(final ResourceLocation id) {
        super(new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(1));
        this.setRegistryName(id);
    }

    public static void setPlayerName(final ItemStack stack, final String name) {
        stack.getOrCreateTag().putString("PlayerName", name);
    }

    public static String getPlayerName(final ItemStack stack) {
        return stack.getOrCreateTag().getString("PlayerName");
    }

    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        if (!(world instanceof ServerWorld) || !(entity instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerWorld sWorld = (ServerWorld) world;
        final ServerPlayerEntity player = (ServerPlayerEntity) entity;
        if (stack.getCount() > 1) {
            while (stack.getCount() > 1) {
                stack.shrink(1);
                final ItemStack gearPiece = new ItemStack(this);
                MiscUtils.giveItem(player, gearPiece);
            }
        }
        final String raffleName = getPlayerName(stack);
        if (raffleName.isEmpty()) {
            setPlayerName(stack, NameProviderPublic.getRandomName());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        tooltip.add(new StringTextComponent("Turns a crystal into a ").withStyle(TextFormatting.GRAY).append(new StringTextComponent("Raffle").withStyle(TextFormatting.GOLD)).append(new StringTextComponent(" crystal.").withStyle(TextFormatting.GRAY)));
        final String raffleName = getPlayerName(stack);
        if (!raffleName.isEmpty()) {
            tooltip.add(new StringTextComponent("Player Boss: ").withStyle(TextFormatting.GRAY).append(new StringTextComponent(raffleName).withStyle(TextFormatting.GREEN)));
        } else {
            tooltip.add(new StringTextComponent("Player Boss: ").withStyle(TextFormatting.GRAY).append(new StringTextComponent("???").withStyle(TextFormatting.GREEN)));
        }
    }
}
