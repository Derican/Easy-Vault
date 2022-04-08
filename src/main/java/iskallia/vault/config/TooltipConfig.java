package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModItems;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TooltipConfig
        extends Config {
    @Expose
    private final List<TooltipEntry> tooltips = new ArrayList<>();

    public Optional<String> getTooltipString(Item item) {
        String itemRegistryName = item.getRegistryName().toString();
        return this.tooltips.stream()
                .filter(entry -> entry.item.equals(itemRegistryName))
                .map(TooltipEntry::getValue)
                .findFirst();
    }


    public String getName() {
        return "tooltip";
    }


    protected void reset() {
        this.tooltips.clear();

        this.tooltips.add(new TooltipEntry(ModItems.POISONOUS_MUSHROOM.getRegistryName().toString(), "Rare - Crafting ingredient for Mystery Stews and Burgers"));
    }

    public static class TooltipEntry {
        @Expose
        private String item;

        public TooltipEntry(String item, String value) {
            this.item = item;
            this.value = value;
        }

        @Expose
        private String value;

        public String getItem() {
            return this.item;
        }

        public String getValue() {
            return this.value;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\TooltipConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */