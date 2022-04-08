package iskallia.vault.client.gui.helper;

import iskallia.vault.Vault;
import iskallia.vault.util.ResourceBoundary;

public enum SkillFrame {
    STAR(new ResourceBoundary(Vault.id("textures/gui/skill-widget.png"), 0, 31, 30, 30)),
    RECTANGULAR(new ResourceBoundary(Vault.id("textures/gui/skill-widget.png"), 30, 31, 30, 30));

    ResourceBoundary resourceBoundary;

    SkillFrame(ResourceBoundary resourceBoundary) {
        this.resourceBoundary = resourceBoundary;
    }

    public ResourceBoundary getResourceBoundary() {
        return this.resourceBoundary;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\gui\helper\SkillFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */