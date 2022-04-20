package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.client.gui.helper.SkillFrame;
import iskallia.vault.config.entry.SkillStyle;

import java.util.HashMap;

public class AbilitiesGUIConfig extends Config {
    @Expose
    private HashMap<String, SkillStyle> styles;

    @Override
    public String getName() {
        return "abilities_gui_styles";
    }

    public HashMap<String, SkillStyle> getStyles() {
        return this.styles;
    }

    @Override
    protected void reset() {
        (this.styles = new HashMap<String, SkillStyle>()).put("Vein Miner", new SkillStyle(40, -130, 16, 0));
        this.styles.put("Rampage", new SkillStyle(80, -80, 128, 0));
        this.styles.put("Ghost Walk", new SkillStyle(120, -30, 112, 0));
        this.styles.put("Dash", new SkillStyle(80, 20, 0, 0));
        this.styles.put("Mega Jump", new SkillStyle(30, 50, 48, 0));
        this.styles.put("Execute", new SkillStyle(-40, -130, 176, 0));
        this.styles.put("Cleanse", new SkillStyle(-80, -80, 144, 0));
        this.styles.put("Tank", new SkillStyle(-120, -30, 160, 0));
        this.styles.put("Summon Eternal", new SkillStyle(-80, 20, 192, 0));
        this.styles.put("Hunter", new SkillStyle(-25, 60, 208, 0));
        this.styles.put("Vein Miner_Size", new SkillStyle(10, -170, 16, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Vein Miner_Fortune", new SkillStyle(35, -200, 32, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Vein Miner_Durability", new SkillStyle(70, -160, 48, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Vein Miner_Void", new SkillStyle(120, -160, 64, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Rampage_Dot", new SkillStyle(100, -120, 128, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Rampage_Leech", new SkillStyle(150, -110, 128, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Rampage_Time", new SkillStyle(140, -70, 128, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Ghost Walk_Damage", new SkillStyle(180, -50, 112, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Ghost Walk_Regen", new SkillStyle(200, -20, 112, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Ghost Walk_Parry", new SkillStyle(160, 0, 112, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Dash_Buff", new SkillStyle(130, 40, 0, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Dash_Damage", new SkillStyle(100, 90, 0, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Dash_Heal", new SkillStyle(140, 80, 0, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Mega Jump_Break", new SkillStyle(70, 100, 48, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Mega Jump_Damage", new SkillStyle(50, 130, 48, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Mega Jump_Knockback", new SkillStyle(20, 110, 48, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Execute_Buff", new SkillStyle(-30, -190, 176, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Execute_Damage", new SkillStyle(-70, -170, 176, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Cleanse_Applynearby", new SkillStyle(-90, -130, 144, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Cleanse_Effect", new SkillStyle(-130, -120, 144, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Cleanse_Heal", new SkillStyle(-140, -90, 144, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Cleanse_Immune", new SkillStyle(-175, -120, 144, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Tank_Parry", new SkillStyle(-160, -60, 160, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Tank_Reflect", new SkillStyle(-180, -20, 160, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Tank_Slow", new SkillStyle(-150, 10, 160, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Summon Eternal_Additional", new SkillStyle(-130, 40, 192, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Summon Eternal_Damage", new SkillStyle(-140, 80, 192, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Summon Eternal_Debuffs", new SkillStyle(-90, 90, 192, 0, SkillFrame.RECTANGULAR));
        this.styles.put("Hunter_Spawners", new SkillStyle(-10, 120, 208, 16, SkillFrame.RECTANGULAR));
        this.styles.put("Hunter_Chests", new SkillStyle(-40, 150, 208, 32, SkillFrame.RECTANGULAR));
        this.styles.put("Hunter_Blocks", new SkillStyle(-55, 100, 208, 48, SkillFrame.RECTANGULAR));
    }
}
