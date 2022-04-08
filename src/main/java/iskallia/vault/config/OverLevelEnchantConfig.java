package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.EnchantedBookEntry;
import net.minecraft.util.text.*;

import java.util.LinkedList;
import java.util.List;

public class OverLevelEnchantConfig extends Config {
    public List<EnchantedBookEntry> getBookTiers() {
        return this.BOOK_TIERS;
    }

    @Expose
    private List<EnchantedBookEntry> BOOK_TIERS;

    public EnchantedBookEntry getTier(int overlevel) {
        for (EnchantedBookEntry tier : this.BOOK_TIERS) {
            if (tier.getExtraLevel() == overlevel) {
                return tier;
            }
        }
        return null;
    }

    public IFormattableTextComponent getPrefixFor(int overlevel) {
        EnchantedBookEntry tier = getTier(overlevel);

        if (tier == null) return null;

        StringTextComponent prefix = new StringTextComponent(tier.getPrefix() + " ");
        prefix.setStyle(Style.EMPTY.withColor(Color.parseColor(tier.getColorHex())));
        return (IFormattableTextComponent) prefix;
    }

    public IFormattableTextComponent format(ITextComponent baseName, int overlevel) {
        EnchantedBookEntry tier = getTier(overlevel);

        if (tier == null) return null;


        IFormattableTextComponent prefix = (new StringTextComponent(tier.getPrefix() + " ")).append(baseName);
        prefix.setStyle(Style.EMPTY.withColor(Color.parseColor(tier.getColorHex())));
        return prefix;
    }


    public String getName() {
        return "overlevel_enchant";
    }


    protected void reset() {
        this.BOOK_TIERS = new LinkedList<>();
        this.BOOK_TIERS.add(new EnchantedBookEntry(1, 40, "Ancient", "#ffae00"));
        this.BOOK_TIERS.add(new EnchantedBookEntry(2, 60, "Super", "#ff6c00"));
        this.BOOK_TIERS.add(new EnchantedBookEntry(3, 80, "Legendary", "#ff3600"));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\OverLevelEnchantConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */