package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

public class DifficultyDescriptionConfig
        extends Config {
    @Expose
    private String description;

    public String getName() {
        return "difficulty_description";
    }


    protected void reset() {
        this.description = "[\"\",{\"text\":\"Please make sure to visit \",\"color\":\"black\"},{\"text\":\"www.vaulthunters.gg\",\"color\":\"#1155CC\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.vaulthunters.gg\"}},{\"text\":\" for more information about this mod.\",\"color\":\"black\"},{\"text\":\"\\n\\n\\n\"},{\"text\":\"Beware, Vault Hunters is a hard modpack, below you can select a difficulty that suits you!\",\"color\":\"black\"}]";
    }


    public IFormattableTextComponent getDescription() {
        return ITextComponent.Serializer.fromJsonLenient(this.description);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\DifficultyDescriptionConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */