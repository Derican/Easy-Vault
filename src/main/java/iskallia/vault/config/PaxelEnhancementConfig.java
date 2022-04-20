package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancement;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Random;

public class PaxelEnhancementConfig extends Config {
    @Expose
    private WeightedList<String> ENHANCEMENT_WEIGHTS;

    @Override
    public String getName() {
        return "paxel_enhancement";
    }

    @Nullable
    public PaxelEnhancement getRandomEnhancement(final Random random) {
        final String enhancementSid = this.ENHANCEMENT_WEIGHTS.getRandom(random);
        if (enhancementSid == null) {
            return null;
        }
        return PaxelEnhancements.REGISTRY.get(new ResourceLocation(enhancementSid));
    }

    @Override
    protected void reset() {
        this.ENHANCEMENT_WEIGHTS = new WeightedList<String>();
        PaxelEnhancements.REGISTRY.keySet().forEach(enhancementId -> {
            final String enhancementSid = enhancementId.toString();
            this.ENHANCEMENT_WEIGHTS.add(enhancementSid, 1);
        });
    }
}
