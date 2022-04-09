// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.research.group.ResearchGroup;
import iskallia.vault.research.type.Research;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ResearchGroupConfig extends Config
{
    @Expose
    protected Map<String, ResearchGroup> groups;
    
    public ResearchGroupConfig() {
        this.groups = new HashMap<String, ResearchGroup>();
    }
    
    @Nonnull
    public Map<String, ResearchGroup> getGroups() {
        return this.groups;
    }
    
    @Nullable
    public ResearchGroup getResearchGroup(final Research research) {
        return this.getResearchGroup(research.getName());
    }
    
    @Nullable
    public ResearchGroup getResearchGroup(final String research) {
        for (final ResearchGroup group : this.getGroups().values()) {
            if (group.getResearch().contains(research)) {
                return group;
            }
        }
        return null;
    }
    
    @Nullable
    public ResearchGroup getResearchGroupById(final String groupId) {
        return this.getGroups().get(groupId);
    }
    
    @Nullable
    public String getResearchGroupId(final ResearchGroup group) {
        return this.getGroups().entrySet().stream().filter(entry -> entry.getValue().equals(group)).map(Map.Entry::getKey).findAny().orElse(null);
    }
    
    @Override
    public String getName() {
        return "researches_groups";
    }
    
    @Override
    protected void reset() {
        this.groups.clear();
        this.groups.put("StorageGroup", ResearchGroup.builder("Storage!").withResearchNodes("Storage Noob", "Storage Refined", "Storage Energistic", "Storage Enthusiast").withGlobalCostIncrease(0.5f).withGroupCostIncrease("MagicGroup", 2.0f).build());
        this.groups.put("MagicGroup", ResearchGroup.builder("Magic Thing(s)!").withResearchNodes("Natural Magical").withGlobalCostIncrease(0.5f).withGroupCostIncrease("StorageGroup", 1.0f).build());
        this.groups.put("DecorationGroup", ResearchGroup.builder("Decoration!").withResearchNodes("Decorator", "Decorator Pro").withGroupCostIncrease("StorageGroup", 1.5f).withGroupCostIncrease("MagicGroup", 0.5f).build());
    }
}
