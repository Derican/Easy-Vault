// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;

import javax.annotation.Nullable;
import java.awt.*;

public class ResearchGroupStyle
{
    @Expose
    protected String group;
    @Expose
    protected int headerColor;
    @Expose
    protected int headerTextColor;
    @Expose
    protected int x;
    @Expose
    protected int y;
    @Expose
    protected int boxWidth;
    @Expose
    protected int boxHeight;
    @Expose
    protected Icon icon;
    
    public ResearchGroupStyle() {
        this.group = "";
        this.headerColor = Color.YELLOW.getRGB();
        this.headerTextColor = Color.BLACK.getRGB();
        this.x = 0;
        this.y = 0;
        this.boxWidth = 0;
        this.boxHeight = 0;
        this.icon = null;
    }
    
    public static Builder builder(final String group) {
        return new Builder(group);
    }
    
    public String getGroup() {
        return this.group;
    }
    
    public int getHeaderColor() {
        return this.headerColor;
    }
    
    public int getHeaderTextColor() {
        return this.headerTextColor;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getBoxWidth() {
        return this.boxWidth;
    }
    
    public int getBoxHeight() {
        return this.boxHeight;
    }
    
    @Nullable
    public Icon getIcon() {
        return this.icon;
    }
    
    public static class Builder
    {
        private final ResearchGroupStyle groupStyle;
        
        private Builder(final String group) {
            this.groupStyle = new ResearchGroupStyle();
            this.groupStyle.group = group;
        }
        
        public Builder withPosition(final int x, final int y) {
            this.groupStyle.x = x;
            this.groupStyle.y = y;
            return this;
        }
        
        public Builder withBoxSize(final int width, final int height) {
            this.groupStyle.boxWidth = width;
            this.groupStyle.boxHeight = height;
            return this;
        }
        
        public Builder withHeaderColor(final int color) {
            this.groupStyle.headerColor = color;
            return this;
        }
        
        public Builder withHeaderTextColor(final int color) {
            this.groupStyle.headerTextColor = color;
            return this;
        }
        
        public Builder withIcon(final int u, final int v) {
            this.groupStyle.icon = new Icon(u, v);
            return this;
        }
        
        public ResearchGroupStyle build() {
            return this.groupStyle;
        }
    }
    
    public static class Icon
    {
        @Expose
        private final int u;
        @Expose
        private final int v;
        
        private Icon(final int u, final int v) {
            this.u = u;
            this.v = v;
        }
        
        public int getU() {
            return this.u;
        }
        
        public int getV() {
            return this.v;
        }
    }
}
