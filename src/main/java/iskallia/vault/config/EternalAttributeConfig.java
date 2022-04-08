package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.FloatRangeEntry;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class EternalAttributeConfig
        extends Config {
    @Expose
    private final Map<String, FloatRangeEntry> initialAttributes = new HashMap<>();


    @Expose
    private FloatRangeEntry healthPerLevel;


    public String getName() {
        return "eternal_attributes";
    }

    @Expose
    private FloatRangeEntry damagePerLevel;
    @Expose
    private FloatRangeEntry moveSpeedPerLevel;

    public Map<Attribute, Float> createAttributes() {
        Map<Attribute, Float> selectedAttributes = new HashMap<>();
        this.initialAttributes.forEach((attrKey, valueRange) -> {
            Attribute attribute = (Attribute) ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attrKey));
            if (attribute != null) {
                selectedAttributes.put(attribute, Float.valueOf(valueRange.getRandom()));
            }
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


    protected void reset() {
        this.initialAttributes.clear();
        this.initialAttributes.put(Attributes.MAX_HEALTH.getRegistryName().toString(), new FloatRangeEntry(20.0F, 30.0F));
        this.initialAttributes.put(Attributes.ATTACK_DAMAGE.getRegistryName().toString(), new FloatRangeEntry(4.0F, 7.0F));
        this.initialAttributes.put(Attributes.MOVEMENT_SPEED.getRegistryName().toString(), new FloatRangeEntry(0.2F, 0.23F));

        this.healthPerLevel = new FloatRangeEntry(4.0F, 8.0F);
        this.damagePerLevel = new FloatRangeEntry(2.0F, 3.0F);
        this.moveSpeedPerLevel = new FloatRangeEntry(0.02F, 0.03F);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\EternalAttributeConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */