package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.client.gui.helper.SkillFrame;
import iskallia.vault.config.entry.SkillStyle;

import java.util.HashMap;

public class ResearchesGUIConfig
        extends Config {
    @Expose
    private HashMap<String, SkillStyle> styles;

    public String getName() {
        return "researches_gui_styles";
    }

    public HashMap<String, SkillStyle> getStyles() {
        return this.styles;
    }


    protected void reset() {
        this.styles = new HashMap<>();

        SkillStyle style = new SkillStyle(0, 0, 0, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Backpacks!", style);

        style = new SkillStyle(50, 0, 32, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Waystones", style);

        style = new SkillStyle(100, 0, 80, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Safety First", style);

        style = new SkillStyle(150, 0, 96, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Organisation", style);

        style = new SkillStyle(200, 0, 112, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Super Builder", style);

        style = new SkillStyle(0, 50, 144, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Super Miner", style);

        style = new SkillStyle(50, 50, 160, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Storage Noob", style);

        style = new SkillStyle(100, 50, 176, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Storage Master", style);

        style = new SkillStyle(150, 50, 192, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Storage Refined", style);

        style = new SkillStyle(200, 50, 208, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Storage Energistic", style);

        style = new SkillStyle(0, 100, 224, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Storage Enthusiast", style);

        style = new SkillStyle(50, 100, 240, 0);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Decorator", style);

        style = new SkillStyle(100, 100, 0, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Decorator Pro", style);

        style = new SkillStyle(150, 100, 16, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Engineer", style);

        style = new SkillStyle(200, 100, 32, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Super Engineer", style);

        style = new SkillStyle(0, 150, 48, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("One with Ender", style);

        style = new SkillStyle(50, 150, 80, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("The Chef", style);

        style = new SkillStyle(100, 150, 112, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Traveller", style);

        style = new SkillStyle(150, 150, 128, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Adventurer", style);

        style = new SkillStyle(200, 150, 144, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Hacker", style);

        style = new SkillStyle(0, 200, 160, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Redstoner", style);

        style = new SkillStyle(50, 200, 176, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Natural Magical", style);

        style = new SkillStyle(100, 200, 192, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Tech Freak", style);

        style = new SkillStyle(150, 200, 208, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("The Emerald King", style);

        style = new SkillStyle(200, 200, 224, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Quarry", style);

        style = new SkillStyle(0, 250, 240, 16);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Spaceman", style);

        style = new SkillStyle(50, 250, 0, 32);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Total Control", style);

        style = new SkillStyle(100, 250, 16, 32);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Let there be light!", style);

        style = new SkillStyle(150, 250, 32, 32);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Energetic", style);

        style = new SkillStyle(200, 250, 48, 32);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Thermal Technician", style);

        style = new SkillStyle(0, 300, 64, 32);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Plastic Technician", style);

        style = new SkillStyle(50, 300, 80, 32);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Extended Possibilities", style);

        style = new SkillStyle(100, 300, 96, 32);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Power Overwhelming", style);

        style = new SkillStyle(150, 300, 112, 32);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Nuclear Power", style);

        style = new SkillStyle(200, 300, 144, 32);

        style.frameType = SkillFrame.STAR;
        this.styles.put("Automatic Genius", style);


        style = new SkillStyle(0, 350, 176, 32);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Creator", style);

        style = new SkillStyle(50, 350, 192, 32);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Piper", style);

        style = new SkillStyle(100, 350, 208, 32);

        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put("Uber Sand", style);

        style = new SkillStyle(150, 350, 160, 32);

        style.frameType = SkillFrame.STAR;
        this.styles.put("Power Manager", style);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\ResearchesGUIConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */