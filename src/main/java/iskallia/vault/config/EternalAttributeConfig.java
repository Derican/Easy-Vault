package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.FloatRangeEntry;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class EternalAttributeConfig extends Config {
    @Expose
    private final Map<String, FloatRangeEntry> initialAttributes;
    @Expose
    private FloatRangeEntry healthPerLevel;
    @Expose
    private FloatRangeEntry damagePerLevel;
    @Expose
    private FloatRangeEntry moveSpeedPerLevel;

    public EternalAttributeConfig() {
        this.initialAttributes = new HashMap<String, FloatRangeEntry>();
    }

    @Override
    public String getName() {
        return "eternal_attributes";
    }

    public Map<Attribute, Float> createAttributes() {
        final Map<Attribute, Float> selectedAttributes = new HashMap<Attribute, Float>();
        this.initialAttributes.forEach((attrKey, valueRange) -> {
            final Attribute attribute = (Attribute) ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attrKey));
            if (attribute != null) {
                selectedAttributes.put(attribute, valueRange.getRandom());
            }
            return;
        });
        return selectedAttributes;
    }

    public FloatRangeEntry getHealthRollRange() {
        return this.healthPerLevel;
    }

    public FloatRangeEntry getDamageRollRange() {
        return this.damagePerLevel;
    }

    public FloatRangeEntry getMoveSpeedRollRange() {
        return this.moveSpeedPerLevel;
    }

    @Override
    protected void reset() {
        this.initialAttributes.clear();
        this.initialAttributes.put(Attributes.MAX_HEALTH.getRegistryName().toString(), new FloatRangeEntry(20.0f, 30.0f));
        this.initialAttributes.put(Attributes.ATTACK_DAMAGE.getRegistryName().toString(), new FloatRangeEntry(4.0f, 7.0f));
        this.initialAttributes.put(Attributes.MOVEMENT_SPEED.getRegistryName().toString(), new FloatRangeEntry(0.2f, 0.23f));
        this.healthPerLevel = new FloatRangeEntry(4.0f, 8.0f);
        this.damagePerLevel = new FloatRangeEntry(2.0f, 3.0f);
        this.moveSpeedPerLevel = new FloatRangeEntry(0.02f, 0.03f);
    }
}
