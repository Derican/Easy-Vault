package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.vault.gen.VaultRoomNames;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class VaultRuneItem
        extends Item {
    private final String roomName;

    public VaultRuneItem(ItemGroup group, ResourceLocation id, String roomName) {
        super((new Item.Properties())
                .tab(group)
                .stacksTo(8));
        this.roomName = roomName;
        setRegistryName(id);
    }

    public String getRoomName() {
        return this.roomName;
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        ITextComponent displayName = VaultRoomNames.getName(getRoomName());
        if (displayName == null) {
            return;
        }
        tooltip.add(StringTextComponent.EMPTY);
        tooltip.add((new StringTextComponent("Combine with a vault crystal to add")).withStyle(TextFormatting.GRAY));
        tooltip.add((new StringTextComponent("a room to the vault: ")).withStyle(TextFormatting.GRAY)
                .append(displayName));

        if (ModConfigs.VAULT_RUNE == null) {
            return;
        }
        ModConfigs.VAULT_RUNE.getMinimumLevel(this).ifPresent(minLevel -> {
            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add((new StringTextComponent("Only usable after level ")).withStyle(TextFormatting.GRAY).append((ITextComponent) (new StringTextComponent(String.valueOf(minLevel))).withStyle(TextFormatting.AQUA)));
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\VaultRuneItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */