package iskallia.vault.research.group;

import com.google.gson.annotations.Expose;

import java.util.*;

public class ResearchGroup {
    @Expose
    protected String title = "";
    @Expose
    protected List<String> research = new ArrayList<>();
    @Expose
    protected float globalCostIncrease = 0.0F;
    @Expose
    protected Map<String, Float> groupCostIncrease = new HashMap<>();

    public static Builder builder(String title) {
        return new Builder(title);
    }

    public String getTitle() {
        return this.title;
    }

    public List<String> getResearch() {
        return this.research;
    }

    public float getGlobalCostIncrease() {
        return this.globalCostIncrease;
    }

    public float getGroupIncreasedResearchCost(String researchGroup) {
        return ((Float) this.groupCostIncrease.getOrDefault(researchGroup, Float.valueOf(getGlobalCostIncrease()))).floatValue();
    }

    public Map<String, Float> getGroupCostIncrease() {
        return this.groupCostIncrease;
    }

    public static class Builder {
        private final ResearchGroup group = new ResearchGroup();

        private Builder(String title) {
            this.group.title = title;
        }

        public Builder withResearchNodes(String... nodes) {
            this.group.research.addAll(Arrays.asList(nodes));
            return this;
        }

        public Builder withGlobalCostIncrease(float increase) {
            this.group.globalCostIncrease = increase;
            return this;
        }

        public Builder withGroupCostIncrease(String researchGroup, float increase) {
            this.group.groupCostIncrease.put(researchGroup, Float.valueOf(increase));
            return this;
        }

        public ResearchGroup build() {
            return this.group;
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\research\group\ResearchGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */