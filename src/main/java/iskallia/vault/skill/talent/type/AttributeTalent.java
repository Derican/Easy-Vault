package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

public class AttributeTalent extends PlayerTalent {
    @Expose
    private final String attribute;
    @Expose
    private final Modifier modifier;

    public AttributeTalent(int cost, Attribute attribute, Modifier modifier) {
        this(cost, Registry.ATTRIBUTE.getKey(attribute).toString(), modifier);
    }

    public AttributeTalent(int cost, String attribute, Modifier modifier) {
        super(cost);
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public Attribute getAttribute() {
        return (Attribute) Registry.ATTRIBUTE.get(new ResourceLocation(this.attribute));
    }

    public Modifier getModifier() {
        return this.modifier;
    }


    public void onAdded(PlayerEntity player) {
        onRemoved(player);
        runIfPresent(player, attributeData -> attributeData.addTransientModifier(getModifier().toMCModifier()));
    }


    public void tick(PlayerEntity player) {
        runIfPresent(player, attributeData -> {
            if (!attributeData.hasModifier(getModifier().toMCModifier())) {
                onAdded(player);
            }
        });
    }


    public void onRemoved(PlayerEntity player) {
        runIfPresent(player, attributeData -> attributeData.removeModifier(UUID.fromString((getModifier()).id)));
    }

    public boolean runIfPresent(PlayerEntity player, Consumer<ModifiableAttributeInstance> action) {
        ModifiableAttributeInstance attributeData = player.getAttribute(getAttribute());
        if (attributeData == null) return false;
        action.accept(attributeData);
        return true;
    }

    public static class Modifier {
        @Expose
        public final String id;
        @Expose
        public final String name;

        public Modifier(String name, double amount, AttributeModifier.Operation operation) {
            this.id = MathHelper.createInsecureUUID(new Random(name.hashCode())).toString();
            this.name = name;
            this.amount = amount;
            this.operation = operation.toValue();
        }

        @Expose
        public final double amount;
        @Expose
        public final int operation;

        public AttributeModifier toMCModifier() {
            return new AttributeModifier(UUID.fromString(this.id), this.name, this.amount,
                    AttributeModifier.Operation.fromValue(this.operation));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\AttributeTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */