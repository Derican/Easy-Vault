// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.research.group;

import com.google.gson.annotations.Expose;

import java.util.*;

public class ResearchGroup
{
    @Expose
    protected String title;
    @Expose
    protected List<String> research;
    @Expose
    protected float globalCostIncrease;
    @Expose
    protected Map<String, Float> groupCostIncrease;
    
    public ResearchGroup() {
        this.title = "";
        this.research = new ArrayList<String>();
        this.globalCostIncrease = 0.0f;
        this.groupCostIncrease = new HashMap<String, Float>();
    }
    
    public static Builder builder(final String title) {
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
    
    public float getGroupIncreasedResearchCost(final String researchGroup) {
        return this.groupCostIncrease.getOrDefault(researchGroup, this.getGlobalCostIncrease());
    }
    
    public Map<String, Float> getGroupCostIncrease() {
        return this.groupCostIncrease;
    }
    
    public static class Builder
    {
        private final ResearchGroup group;
        
        private Builder(final String title) {
            this.group = new ResearchGroup();
            this.group.title = title;
        }
        
        public Builder withResearchNodes(final String... nodes) {
            this.group.research.addAll(Arrays.asList(nodes));
            return this;
        }
        
        public Builder withGlobalCostIncrease(final float increase) {
            this.group.globalCostIncrease = increase;
            return this;
        }
        
        public Builder withGroupCostIncrease(final String researchGroup, final float increase) {
            this.group.groupCostIncrease.put(researchGroup, increase);
            return this;
        }
        
        public ResearchGroup build() {
            return this.group;
        }
    }
}
