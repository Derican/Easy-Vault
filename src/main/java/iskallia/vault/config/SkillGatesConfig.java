// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.SkillGates;

public class SkillGatesConfig extends Config
{
    @Expose
    private SkillGates SKILL_GATES;
    
    @Override
    public String getName() {
        return "skill_gates";
    }
    
    public SkillGates getGates() {
        return this.SKILL_GATES;
    }
    
    @Override
    protected void reset() {
        this.SKILL_GATES = new SkillGates();
        SkillGates.Entry gateEntry = new SkillGates.Entry();
        gateEntry.setLockedBy(ModConfigs.TALENTS.TREASURE_HUNTER.getParentName());
        this.SKILL_GATES.addEntry(ModConfigs.TALENTS.ARTISAN.getParentName(), gateEntry);
        gateEntry = new SkillGates.Entry();
        gateEntry.setLockedBy(ModConfigs.TALENTS.ARTISAN.getParentName());
        this.SKILL_GATES.addEntry(ModConfigs.TALENTS.TREASURE_HUNTER.getParentName(), gateEntry);
        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn(ModConfigs.TALENTS.LOOTER.getParentName());
        this.SKILL_GATES.addEntry(ModConfigs.TALENTS.TREASURE_HUNTER.getParentName(), gateEntry);
        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn("Storage Noob");
        this.SKILL_GATES.addEntry("Storage Master", gateEntry);
        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn("Storage Master");
        this.SKILL_GATES.addEntry("Storage Refined", gateEntry);
        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn("Storage Refined");
        this.SKILL_GATES.addEntry("Storage Energistic", gateEntry);
        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn("Storage Energistic");
        this.SKILL_GATES.addEntry("Storage Enthusiast", gateEntry);
        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn("Decorator");
        this.SKILL_GATES.addEntry("Decorator Pro", gateEntry);
        gateEntry = new SkillGates.Entry();
        gateEntry.setDependsOn("Tech Freak");
        this.SKILL_GATES.addEntry("Nuclear Power", gateEntry);
    }
}
