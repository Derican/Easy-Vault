package iskallia.vault.config;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;

import java.util.HashMap;

public class SkillDescriptionsConfig
        extends Config {
    @Expose
    private HashMap<String, JsonElement> descriptions;

    public String getName() {
        return "skill_descriptions";
    }

    public IFormattableTextComponent getDescriptionFor(String skillName) {
        JsonElement element = this.descriptions.get(skillName);
        if (element == null) {
            return ITextComponent.Serializer.fromJsonLenient("[{text:'No description for ', color:'#192022'},{text: '" + skillName + "', color: '#fcf5c5'},{text: ', yet', color: '#192022'}]");
        }


        return ITextComponent.Serializer.fromJson(element);
    }


    protected void reset() {
        this.descriptions = new HashMap<>();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\SkillDescriptionsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */