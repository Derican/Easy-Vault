package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TooltipConfig extends Config {
    @Expose
    private final List<TooltipEntry> tooltips;

    public TooltipConfig() {
        this.tooltips = new ArrayList<TooltipEntry>();
    }

    public Optional<String> getTooltipString(final Item item) {
        final String itemRegistryName = item.getRegistryName().toString();
        return this.tooltips.stream().filter(entry -> entry.item.equals(itemRegistryName)).map(TooltipEntry::getValue).findFirst();
    }

    @Override
    public String getName() {
        return "tooltip";
    }

    @Override
    protected void reset() {
        this.tooltips.clear();
        this.tooltips.add(new TooltipEntry(ModItems.POISONOUS_MUSHROOM.getRegistryName().toString(), "Rare - Crafting ingredient for Mystery Stews and Burgers"));
    }

    public static class TooltipEntry {
        @Expose
        private String item;
        @Expose
        private String value;

        public TooltipEntry(final String item, final String value) {
            this.item = item;
            this.value = value;
        }

        public String getItem() {
            return this.item;
        }

        public String getValue() {
            return this.value;
        }
    }
}
