package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;

import javax.annotation.Nullable;
import java.awt.*;

public class ResearchGroupStyle {
    @Expose
    protected String group = "";
    @Expose
    protected int headerColor = Color.YELLOW.getRGB();
    @Expose
    protected int headerTextColor = Color.BLACK.getRGB();
    @Expose
    protected int x = 0;
    @Expose
    protected int y = 0;
    @Expose
    protected int boxWidth = 0;
    @Expose
    protected int boxHeight = 0;
    @Expose
    protected Icon icon = null;

    public static Builder builder(String group) {
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

    public static class Builder {
        private final ResearchGroupStyle groupStyle = new ResearchGroupStyle();

        private Builder(String group) {
            this.groupStyle.group = group;
        }

        public Builder withPosition(int x, int y) {
            this.groupStyle.x = x;
            this.groupStyle.y = y;
            return this;
        }

        public Builder withBoxSize(int width, int height) {
            this.groupStyle.boxWidth = width;
            this.groupStyle.boxHeight = height;
            return this;
        }

        public Builder withHeaderColor(int color) {
            this.groupStyle.headerColor = color;
            return this;
        }

        public Builder withHeaderTextColor(int color) {
            this.groupStyle.headerTextColor = color;
            return this;
        }

        public Builder withIcon(int u, int v) {
            this.groupStyle.icon = new ResearchGroupStyle.Icon(u, v);
            return this;
        }

        public ResearchGroupStyle build() {
            return this.groupStyle;
        }
    }

    public static class Icon {
        @Expose
        private final int u;
        @Expose
        private final int v;

        private Icon(int u, int v) {
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


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\ResearchGroupStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */