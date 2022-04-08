package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.ResearchGroupStyle;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ResearchGroupStyleConfig
        extends Config {
    @Expose
    protected Map<String, ResearchGroupStyle> groupStyles = new HashMap<>();


    @Nullable
    public ResearchGroupStyle getStyle(String groupId) {
        return this.groupStyles.get(groupId);
    }


    public String getName() {
        return "researches_groups_styles";
    }


    protected void reset() {
        this.groupStyles.clear();

        this.groupStyles.put("StorageGroup", ResearchGroupStyle.builder("StorageGroup")
                .withHeaderColor(Color.ORANGE.getRGB())
                .withHeaderTextColor(Color.LIGHT_GRAY.getRGB())
                .withPosition(-25, -35)
                .withBoxSize(125, 110)
                .withIcon(208, 0)
                .build());

        this.groupStyles.put("MagicGroup", ResearchGroupStyle.builder("MagicGroup")
                .withHeaderColor(Color.BLUE.getRGB())
                .withHeaderTextColor(Color.WHITE.getRGB())
                .withPosition(-25, 115)
                .withBoxSize(135, 150)
                .withIcon(176, 16)
                .build());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\ResearchGroupStyleConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */