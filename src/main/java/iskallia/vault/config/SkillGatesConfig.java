package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.SkillGates;

public class SkillGatesConfig
        extends Config {
    @Expose
    private SkillGates SKILL_GATES;

    public String getName() {
        return "skill_gates";
    }

    public SkillGates getGates() {
        return this.SKILL_GATES;
    }


    protected void reset() {
        this.SKILL_GATES = new SkillGates();


        SkillGates.Entry gateEntry = new SkillGates.Entry();
        gateEntry.setLockedBy(new String[]{ModConfigs.TALENTS.TREASURE_HUNTER.getParentName()});
        this.SKILL_GATES.addEntry(ModConfigs.TALENTS.ARTISAN.getParentName(), gateEntry);

        gateEntry = new SkillGates.Entry();
        gateEntry.setLockedBy(new String[]{ModConfigs.TALENTS.ARTISAN.getParentName()});
        this.SKILL_GATES.addEntry(ModConfigs.TALENTS.TREASURE_HUNTER.getParentName(), gateEntry);


        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn(new String[]{ModConfigs.TALENTS.LOOTER.getParentName()});
        this.SKILL_GATES.addEntry(ModConfigs.TALENTS.TREASURE_HUNTER.getParentName(), gateEntry);


        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn(new String[]{"Storage Noob"});
        this.SKILL_GATES.addEntry("Storage Master", gateEntry);

        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn(new String[]{"Storage Master"});
        this.SKILL_GATES.addEntry("Storage Refined", gateEntry);

        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn(new String[]{"Storage Refined"});
        this.SKILL_GATES.addEntry("Storage Energistic", gateEntry);

        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn(new String[]{"Storage Energistic"});
        this.SKILL_GATES.addEntry("Storage Enthusiast", gateEntry);

        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn(new String[]{"Decorator"});
        this.SKILL_GATES.addEntry("Decorator Pro", gateEntry);

        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn(new String[]{"Tech Freak"});
        this.SKILL_GATES.addEntry("Nuclear Power", gateEntry);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\SkillGatesConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */