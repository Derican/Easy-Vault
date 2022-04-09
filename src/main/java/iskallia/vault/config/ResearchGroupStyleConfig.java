// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.ResearchGroupStyle;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ResearchGroupStyleConfig extends Config
{
    @Expose
    protected Map<String, ResearchGroupStyle> groupStyles;
    
    public ResearchGroupStyleConfig() {
        this.groupStyles = new HashMap<String, ResearchGroupStyle>();
    }
    
    @Nullable
    public ResearchGroupStyle getStyle(final String groupId) {
        return this.groupStyles.get(groupId);
    }
    
    @Override
    public String getName() {
        return "researches_groups_styles";
    }
    
    @Override
    protected void reset() {
        this.groupStyles.clear();
        this.groupStyles.put("StorageGroup", ResearchGroupStyle.builder("StorageGroup").withHeaderColor(Color.ORANGE.getRGB()).withHeaderTextColor(Color.LIGHT_GRAY.getRGB()).withPosition(-25, -35).withBoxSize(125, 110).withIcon(208, 0).build());
        this.groupStyles.put("MagicGroup", ResearchGroupStyle.builder("MagicGroup").withHeaderColor(Color.BLUE.getRGB()).withHeaderTextColor(Color.WHITE.getRGB()).withPosition(-25, 115).withBoxSize(135, 150).withIcon(176, 16).build());
    }
}
