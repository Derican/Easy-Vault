package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;
import iskallia.vault.client.gui.helper.SkillFrame;

public class SkillStyle {
    @Expose
    public int x;
    @Expose
    public int y;
    @Expose
    public SkillFrame frameType;
    @Expose
    public int u;
    @Expose
    public int v;

    public SkillStyle(final int x, final int y, final int u, final int v) {
        this(x, y, u, v, SkillFrame.STAR);
    }

    public SkillStyle(final int x, final int y, final int u, final int v, final SkillFrame skillFrame) {
        this.x = x;
        this.y = y;
        this.frameType = skillFrame;
        this.u = u;
        this.v = v;
    }
}
