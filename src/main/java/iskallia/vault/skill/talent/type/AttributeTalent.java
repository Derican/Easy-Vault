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

    public AttributeTalent(final int cost, final Attribute attribute, final Modifier modifier) {
        this(cost, Registry.ATTRIBUTE.getKey(attribute).toString(), modifier);
    }

    public AttributeTalent(final int cost, final String attribute, final Modifier modifier) {
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

    @Override
    public void onAdded(final PlayerEntity player) {
        this.onRemoved(player);
        this.runIfPresent(player, attributeData -> attributeData.addTransientModifier(this.getModifier().toMCModifier()));
    }

    @Override
    public void tick(final PlayerEntity player) {
        this.runIfPresent(player, attributeData -> {
            if (!attributeData.hasModifier(this.getModifier().toMCModifier())) {
                this.onAdded(player);
            }
        });
    }

    @Override
    public void onRemoved(final PlayerEntity player) {
        this.runIfPresent(player, attributeData -> attributeData.removeModifier(UUID.fromString(this.getModifier().id)));
    }

    public boolean runIfPresent(final PlayerEntity player, final Consumer<ModifiableAttributeInstance> action) {
        final ModifiableAttributeInstance attributeData = player.getAttribute(this.getAttribute());
        if (attributeData == null) {
            return false;
        }
        action.accept(attributeData);
        return true;
    }

    public static class Modifier {
        @Expose
        public final String id;
        @Expose
        public final String name;
        @Expose
        public final double amount;
        @Expose
        public final int operation;

        public Modifier(final String name, final double amount, final AttributeModifier.Operation operation) {
            this.id = MathHelper.createInsecureUUID(new Random(name.hashCode())).toString();
            this.name = name;
            this.amount = amount;
            this.operation = operation.toValue();
        }

        public AttributeModifier toMCModifier() {
            return new AttributeModifier(UUID.fromString(this.id), this.name, this.amount, AttributeModifier.Operation.fromValue(this.operation));
        }
    }
}
