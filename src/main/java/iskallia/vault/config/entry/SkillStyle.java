package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;
import iskallia.vault.client.gui.helper.SkillFrame;

public class SkillStyle {
    @Expose
    public int x;
    @Expose
    public int y;

    public SkillStyle(int x, int y, int u, int v) {
        this(x, y, u, v, SkillFrame.STAR);
    }

    @Expose
    public SkillFrame frameType;
    @Expose
    public int u;
    @Expose
    public int v;

    public SkillStyle(int x, int y, int u, int v, SkillFrame skillFrame) {
        this.x = x;
        this.y = y;
        this.frameType = skillFrame;
        this.u = u;
        this.v = v;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\SkillStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */