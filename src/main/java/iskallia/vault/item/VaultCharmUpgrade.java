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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VaultCharmUpgrade extends BasicItem {
    private final Tier tier;

    public VaultCharmUpgrade(final ResourceLocation id, final Tier tier, final Item.Properties properties) {
        super(id, properties);
        this.tier = tier;
    }

    public ITextComponent getName(final ItemStack stack) {
        return (ITextComponent) new StringTextComponent("Vault Charm Upgrade (" + this.tier.getName() + ")");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
        tooltip.add(StringTextComponent.EMPTY);
        tooltip.addAll(getTooltipForTier(this.tier));
    }

    private static List<ITextComponent> getTooltipForTier(final Tier tier) {
        final List<ITextComponent> tooltip = new ArrayList<ITextComponent>();
        if (ModConfigs.VAULT_CHARM != null) {
            final int slotCount = tier.getSlotAmount();
            tooltip.add((ITextComponent) new StringTextComponent("Increases the amount of slots"));
            tooltip.add((ITextComponent) new StringTextComponent("that items can be added to the"));
            tooltip.add((ITextComponent) new StringTextComponent("Vault Charm Whitelist to " + TextFormatting.YELLOW + slotCount));
        }
        return tooltip;
    }

    public Tier getTier() {
        return this.tier;
    }

    public enum Tier {
        ONE("Tier 1", 1),
        TWO("Tier 2", 2),
        THREE("Tier 3", 3),
        FOUR("Tier 4", 4);

        private final String name;
        private final int tier;

        private Tier(final String name, final int tier) {
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

        public static Tier getTierBySize(final int size) {
            return getByValue(ModConfigs.VAULT_CHARM.getMultipliers().entrySet().stream().filter(entrySet -> entrySet.getValue() * 9 == size).map(Map.Entry::getKey).findFirst().orElse(-1));
        }

        public static Tier getByValue(final int value) {
            return Arrays.stream(values()).filter(tier -> tier.getTier() == value).findFirst().orElse(null);
        }

        public Tier getNext() {
            switch (this) {
                case ONE: {
                    return Tier.TWO;
                }
                case TWO: {
                    return Tier.THREE;
                }
                case THREE: {
                    return Tier.FOUR;
                }
                default: {
                    return null;
                }
            }
        }
    }
}
