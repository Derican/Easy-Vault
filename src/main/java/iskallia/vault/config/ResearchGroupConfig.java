package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.research.group.ResearchGroup;
import iskallia.vault.research.type.Research;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ResearchGroupConfig
        extends Config {
    @Expose
    protected Map<String, ResearchGroup> groups = new HashMap<>();

    @Nonnull
    public Map<String, ResearchGroup> getGroups() {
        return this.groups;
    }

    @Nullable
    public ResearchGroup getResearchGroup(Research research) {
        return getResearchGroup(research.getName());
    }

    @Nullable
    public ResearchGroup getResearchGroup(String research) {
        for (ResearchGroup group : getGroups().values()) {
            if (group.getResearch().contains(research)) {
                return group;
            }
        }
        return null;
    }

    @Nullable
    public ResearchGroup getResearchGroupById(String groupId) {
        return getGroups().get(groupId);
    }

    @Nullable
    public String getResearchGroupId(ResearchGroup group) {
        return getGroups().entrySet().stream()
                .filter(entry -> ((ResearchGroup) entry.getValue()).equals(group))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
    }


    public String getName() {
        return "researches_groups";
    }


    protected void reset() {
        this.groups.clear();

        this.groups.put("StorageGroup", ResearchGroup.builder("Storage!")
                .withResearchNodes(new String[]{"Storage Noob", "Storage Refined", "Storage Energistic", "Storage Enthusiast"
                }).withGlobalCostIncrease(0.5F)
                .withGroupCostIncrease("MagicGroup", 2.0F)
                .build());

        this.groups.put("MagicGroup", ResearchGroup.builder("Magic Thing(s)!")
                .withResearchNodes(new String[]{"Natural Magical"
                }).withGlobalCostIncrease(0.5F)
                .withGroupCostIncrease("StorageGroup", 1.0F)
                .build());

        this.groups.put("DecorationGroup", ResearchGroup.builder("Decoration!")
                .withResearchNodes(new String[]{"Decorator", "Decorator Pro"
                }).withGroupCostIncrease("StorageGroup", 1.5F)
                .withGroupCostIncrease("MagicGroup", 0.5F)
                .build());
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\ResearchGroupConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */