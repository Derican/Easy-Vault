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

public class TieredVaultItem extends BasicTooltipItem {
    private final int vaultGearTier;

    public TieredVaultItem(final ResourceLocation id, final Item.Properties properties, final int vaultGearTier, final ITextComponent... components) {
        super(id, properties, components);
        this.vaultGearTier = vaultGearTier;
    }

    public TieredVaultItem(final ResourceLocation id, final Item.Properties properties, final int vaultGearTier, final List<ITextComponent> components) {
        super(id, properties, components);
        this.vaultGearTier = vaultGearTier;
    }

    public int getVaultGearTier() {
        return this.vaultGearTier;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        final ITextComponent display = this.getTierDisplayLock();
        if (display != null) {
            tooltip.add(StringTextComponent.EMPTY);
            tooltip.add((ITextComponent) new StringTextComponent("Only usable on Vault Gear ").withStyle(TextFormatting.GRAY).append(display));
        }
    }

    @Nullable
    public ITextComponent getTierDisplayLock() {
        if (ModConfigs.VAULT_GEAR == null) {
            return null;
        }
        final VaultGearConfig.General.TierConfig cfg = ModConfigs.VAULT_GEAR.getTierConfig(this.getVaultGearTier());
        if (cfg != null && !cfg.getDisplay().getString().isEmpty()) {
            return (ITextComponent) new StringTextComponent("Tier: ").withStyle(TextFormatting.GRAY).append(cfg.getDisplay());
        }
        return null;
    }
}
