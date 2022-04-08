package iskallia.vault.item.gear.applicable;

import iskallia.vault.config.VaultGearConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.BasicTooltipItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
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

public class TieredVaultItem
        extends BasicTooltipItem {
    private final int vaultGearTier;

    public TieredVaultItem(ResourceLocation id, Item.Properties properties, int vaultGearTier, ITextComponent... components) {
        super(id, properties, components);
        this.vaultGearTier = vaultGearTier;
    }

    public TieredVaultItem(ResourceLocation id, Item.Properties properties, int vaultGearTier, List<ITextComponent> components) {
        super(id, properties, components);
        this.vaultGearTier = vaultGearTier;
    }

    public int getVaultGearTier() {
        return this.vaultGearTier;
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        ITextComponent display = getTierDisplayLock();
        if (display != null) {
            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add((new StringTextComponent("Only usable on Vault Gear ")).withStyle(TextFormatting.GRAY).append(display));
        }
    }

    @Nullable
    public ITextComponent getTierDisplayLock() {
        if (ModConfigs.VAULT_GEAR == null) {
            return null;
        }

        VaultGearConfig.General.TierConfig cfg = ModConfigs.VAULT_GEAR.getTierConfig(getVaultGearTier());
        if (cfg != null && !cfg.getDisplay().getString().isEmpty()) {
            return (ITextComponent) (new StringTextComponent("Tier: ")).withStyle(TextFormatting.GRAY).append(cfg.getDisplay());
        }
        return null;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\gear\applicable\TieredVaultItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */