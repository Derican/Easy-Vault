package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.client.gui.helper.SkillFrame;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.init.ModConfigs;

import java.util.HashMap;

public class TalentsGUIConfig
        extends Config {
    @Expose
    private HashMap<String, SkillStyle> styles;

    public String getName() {
        return "talents_gui_styles";
    }

    public HashMap<String, SkillStyle> getStyles() {
        return this.styles;
    }


    protected void reset() {
        this.styles = new HashMap<>();

        SkillStyle style = new SkillStyle(0, 0, 96, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.HASTE.getParentName(), style);


        style = new SkillStyle(210, 0, 112, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.RESISTANCE.getParentName(), style);

        style = new SkillStyle(280, 0, 128, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.STRENGTH.getParentName(), style);

        style = new SkillStyle(350, 0, 64, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.FIRE_RESISTANCE.getParentName(), style);

        style = new SkillStyle(0, 70, 144, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.SPEED.getParentName(), style);

        style = new SkillStyle(70, 70, 0, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.WATER_BREATHING.getParentName(), style);

        style = new SkillStyle(140, 70, 32, 0);
        style.frameType = SkillFrame.STAR;
        this.styles.put(ModConfigs.TALENTS.WELL_FIT.getParentName(), style);

        style = new SkillStyle(210, 70, 208, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.TWERKER.getParentName(), style);

        style = new SkillStyle(280, 70, 176, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.ELVISH.getParentName(), style);

        style = new SkillStyle(350, 70, 224, 0);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.ANGEL.getParentName(), style);

        style = new SkillStyle(0, 140, 160, 0);
        style.frameType = SkillFrame.STAR;
        this.styles.put(ModConfigs.TALENTS.REACH.getParentName(), style);


        style = new SkillStyle(70, 140, 0, 16);
        style.frameType = SkillFrame.STAR;
        this.styles.put(ModConfigs.TALENTS.EXPERIENCED.getParentName(), style);

        style = new SkillStyle(140, 140, 240, 0);
        style.frameType = SkillFrame.STAR;
        this.styles.put(ModConfigs.TALENTS.PARRY.getParentName(), style);

        style = new SkillStyle(210, 140, 16, 16);
        style.frameType = SkillFrame.STAR;
        this.styles.put(ModConfigs.TALENTS.STONE_SKIN.getParentName(), style);

        style = new SkillStyle(280, 140, 32, 16);
        style.frameType = SkillFrame.STAR;
        this.styles.put(ModConfigs.TALENTS.UNBREAKABLE.getParentName(), style);

        style = new SkillStyle(350, 140, 48, 16);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.CRITICAL_STRIKE.getParentName(), style);

        style = new SkillStyle(0, 210, 64, 16);
        style.frameType = SkillFrame.STAR;
        this.styles.put(ModConfigs.TALENTS.LOOTER.getParentName(), style);


        style = new SkillStyle(140, 210, 96, 16);
        style.frameType = SkillFrame.STAR;
        this.styles.put(ModConfigs.TALENTS.CHUNKY.getParentName(), style);


        style = new SkillStyle(280, 210, 128, 16);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.STEP.getParentName(), style);


        style = new SkillStyle(70, 280, 176, 16);
        style.frameType = SkillFrame.STAR;
        this.styles.put(ModConfigs.TALENTS.TREASURE_HUNTER.getParentName(), style);

        style = new SkillStyle(140, 280, 192, 16);
        style.frameType = SkillFrame.STAR;
        this.styles.put(ModConfigs.TALENTS.ARTISAN.getParentName(), style);

        style = new SkillStyle(210, 280, 208, 16);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.BREAKABLE.getParentName(), style);

        style = new SkillStyle(280, 280, 224, 16);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.CARELESS.getParentName(), style);

        style = new SkillStyle(350, 280, 0, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.LUCKY_ALTAR.getParentName(), style);


        style = new SkillStyle(0, 350, 16, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.FATAL_STRIKE.getParentName(), style);

        style = new SkillStyle(70, 350, 32, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.FATAL_STRIKE_CHANCE.getParentName(), style);

        style = new SkillStyle(140, 350, 48, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.FATAL_STRIKE_DAMAGE.getParentName(), style);

        style = new SkillStyle(210, 350, 64, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.THORNS.getParentName(), style);

        style = new SkillStyle(280, 350, 96, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.THORNS_CHANCE.getParentName(), style);

        style = new SkillStyle(350, 350, 80, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.THORNS_DAMAGE.getParentName(), style);


        style = new SkillStyle(0, 420, 112, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.BARBARIC.getParentName(), style);

        style = new SkillStyle(70, 420, 128, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.COMMANDER.getParentName(), style);

        style = new SkillStyle(140, 420, 144, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.FRENZY.getParentName(), style);

        style = new SkillStyle(210, 420, 160, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.GLASS_CANNON.getParentName(), style);

        style = new SkillStyle(280, 420, 176, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.WARD.getParentName(), style);

        style = new SkillStyle(350, 420, 192, 32);
        style.frameType = SkillFrame.RECTANGULAR;
        this.styles.put(ModConfigs.TALENTS.SOUL_HUNTER.getParentName(), style);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\TalentsGUIConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */