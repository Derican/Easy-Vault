package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.EnchantedBookEntry;
import net.minecraft.util.text.*;

import java.util.LinkedList;
import java.util.List;

public class OverLevelEnchantConfig extends Config {
    @Expose
    private List<EnchantedBookEntry> BOOK_TIERS;

    public List<EnchantedBookEntry> getBookTiers() {
        return this.BOOK_TIERS;
    }

    public EnchantedBookEntry getTier(final int overlevel) {
        for (final EnchantedBookEntry tier : this.BOOK_TIERS) {
            if (tier.getExtraLevel() == overlevel) {
                return tier;
            }
        }
        return null;
    }

    public IFormattableTextComponent getPrefixFor(final int overlevel) {
        final EnchantedBookEntry tier = this.getTier(overlevel);
        if (tier == null) {
            return null;
        }
        final StringTextComponent prefix = new StringTextComponent(tier.getPrefix() + " ");
        prefix.setStyle(Style.EMPTY.withColor(Color.parseColor(tier.getColorHex())));
        return (IFormattableTextComponent) prefix;
    }

    public IFormattableTextComponent format(final ITextComponent baseName, final int overlevel) {
        final EnchantedBookEntry tier = this.getTier(overlevel);
        if (tier == null) {
            return null;
        }
        final IFormattableTextComponent prefix = new StringTextComponent(tier.getPrefix() + " ").append(baseName);
        prefix.setStyle(Style.EMPTY.withColor(Color.parseColor(tier.getColorHex())));
        return prefix;
    }

    @Override
    public String getName() {
        return "overlevel_enchant";
    }

    @Override
    protected void reset() {
        (this.BOOK_TIERS = new LinkedList<EnchantedBookEntry>()).add(new EnchantedBookEntry(1, 40, "Ancient", "#ffae00"));
        this.BOOK_TIERS.add(new EnchantedBookEntry(2, 60, "Super", "#ff6c00"));
        this.BOOK_TIERS.add(new EnchantedBookEntry(3, 80, "Legendary", "#ff3600"));
    }
}
