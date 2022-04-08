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

public class ItemVaultRaffleSeal
        extends Item {
    public ItemVaultRaffleSeal(ResourceLocation id) {
        super((new Item.Properties())
                .tab(ModItems.VAULT_MOD_GROUP)
                .stacksTo(1));

        setRegistryName(id);
    }

    public static void setPlayerName(ItemStack stack, String name) {
        stack.getOrCreateTag().putString("PlayerName", name);
    }

    public static String getPlayerName(ItemStack stack) {
        return stack.getOrCreateTag().getString("PlayerName");
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!(world instanceof ServerWorld) || !(entity instanceof ServerPlayerEntity)) {
            return;
        }
        ServerWorld sWorld = (ServerWorld) world;
        ServerPlayerEntity player = (ServerPlayerEntity) entity;

        if (stack.getCount() > 1) {
            while (stack.getCount() > 1) {
                stack.shrink(1);

                ItemStack gearPiece = new ItemStack((IItemProvider) this);
                MiscUtils.giveItem(player, gearPiece);
            }
        }

        String raffleName = getPlayerName(stack);
        if (raffleName.isEmpty()) {
            setPlayerName(stack, NameProviderPublic.getRandomName());
        }
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add((new StringTextComponent("Turns a crystal into a ")).withStyle(TextFormatting.GRAY)
                .append((ITextComponent) (new StringTextComponent("Raffle")).withStyle(TextFormatting.GOLD))
                .append((ITextComponent) (new StringTextComponent(" crystal.")).withStyle(TextFormatting.GRAY)));
        String raffleName = getPlayerName(stack);
        if (!raffleName.isEmpty()) {
            tooltip.add((new StringTextComponent("Player Boss: ")).withStyle(TextFormatting.GRAY)
                    .append((ITextComponent) (new StringTextComponent(raffleName)).withStyle(TextFormatting.GREEN)));
        } else {
            tooltip.add((new StringTextComponent("Player Boss: ")).withStyle(TextFormatting.GRAY)
                    .append((ITextComponent) (new StringTextComponent("???")).withStyle(TextFormatting.GREEN)));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemVaultRaffleSeal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */