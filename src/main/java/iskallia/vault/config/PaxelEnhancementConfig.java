package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Random;

public class PaxelEnhancementConfig
        extends Config {
    @Expose
    private WeightedList<String> ENHANCEMENT_WEIGHTS;

    public String getName() {
        return "paxel_enhancement";
    }

    @Nullable
    public PaxelEnhancement getRandomEnhancement(Random random) {
        String enhancementSid = (String) this.ENHANCEMENT_WEIGHTS.getRandom(random);
        if (enhancementSid == null) {
            return null;
        }
        return (PaxelEnhancement) PaxelEnhancements.REGISTRY.get(new ResourceLocation(enhancementSid));
    }


    protected void reset() {
        this.ENHANCEMENT_WEIGHTS = new WeightedList();
        PaxelEnhancements.REGISTRY.keySet().forEach(enhancementId -> {
            String enhancementSid = enhancementId.toString();
            this.ENHANCEMENT_WEIGHTS.add(enhancementSid, 1);
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\PaxelEnhancementConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */