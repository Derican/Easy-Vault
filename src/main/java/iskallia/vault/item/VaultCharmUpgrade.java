package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VaultCharmUpgrade
        extends BasicItem {
    private final Tier tier;

    public VaultCharmUpgrade(ResourceLocation id, Tier tier, Item.Properties properties) {
        super(id, properties);
        this.tier = tier;
    }


    public ITextComponent getName(ItemStack stack) {
        return (ITextComponent) new StringTextComponent("Vault Charm Upgrade (" + this.tier.getName() + ")");
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(StringTextComponent.EMPTY);
        tooltip.addAll(getTooltipForTier(this.tier));
    }

    private static List<ITextComponent> getTooltipForTier(Tier tier) {
        List<ITextComponent> tooltip = new ArrayList<>();

        if (ModConfigs.VAULT_CHARM != null) {
            int slotCount = tier.getSlotAmount();
            tooltip.add(new StringTextComponent("Increases the amount of slots"));
            tooltip.add(new StringTextComponent("that items can be added to the"));
            tooltip.add(new StringTextComponent("Vault Charm Whitelist to " + TextFormatting.YELLOW + slotCount));
        }

        return tooltip;
    }

    public Tier getTier() {
        return this.tier;
    }

    public enum Tier {
        ONE("Tier 1", 1),
        TWO("Tier 2", 2),
        THREE("Tier 3", 3);

        private final String name;
        private final int tier;

        Tier(String name, int tier) {
            this.name = name;
            this.tier = tier;
        }

        public String getName() {
            return this.name;
        }

        public int getTier() {
            return this.tier;
        }

        public int getSlotAmount() {
            return ModConfigs.VAULT_CHARM.getMultiplierForTier(this.tier) * 9;
        }


        public static Tier getTierBySize(int size) {
            return getByValue(((Integer) ModConfigs.VAULT_CHARM.getMultipliers().entrySet()
                    .stream()
                    .filter(entrySet -> (((Integer) entrySet.getValue()).intValue() * 9 == size))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(Integer.valueOf(-1))).intValue());
        }


        public static Tier getByValue(int value) {
            switch (value) {
                case 1:
                    return ONE;
                case 2:
                    return TWO;
                case 3:
                    return THREE;
            }
            return null;
        }


        public Tier getNext() {
            switch (this) {
                case ONE:
                    return TWO;
                case TWO:
                    return THREE;
            }
            return null;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\VaultCharmUpgrade.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */