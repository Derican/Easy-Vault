package iskallia.vault.item.catalyst;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import iskallia.vault.config.VaultCrystalCatalystConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.util.EnumCodec;
import iskallia.vault.world.vault.modifier.VaultModifier;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ModifierRollResult {
    static {
        CODEC = RecordCodecBuilder.create(rollInstance -> rollInstance.group((App) EnumCodec.of(ModifierRollType.class).fieldOf("type").forGetter(()), (App) Codec.STRING.fieldOf("value").forGetter(())).apply((Applicative) rollInstance, ModifierRollResult::new));
    }


    public static final Codec<ModifierRollResult> CODEC;

    private final ModifierRollType type;

    private final String value;


    private ModifierRollResult(ModifierRollType type, String value) {
        this.type = type;
        this.value = (type == ModifierRollType.ADD_SPECIFIC_MODIFIER) ? VaultModifier.migrateModifierName(value) : value;
    }

    public static ModifierRollResult ofModifier(String modifier) {
        return new ModifierRollResult(ModifierRollType.ADD_SPECIFIC_MODIFIER, modifier);
    }

    public static ModifierRollResult ofPool(String pool) {
        return new ModifierRollResult(ModifierRollType.ADD_RANDOM_MODIFIER, pool);
    }

    @OnlyIn(Dist.CLIENT)
    public List<ITextComponent> getTooltipDescription(String prefix, boolean canAddDetail) {
        return getTooltipDescription((IFormattableTextComponent) new StringTextComponent(prefix), canAddDetail);
    }

    @OnlyIn(Dist.CLIENT)
    public List<ITextComponent> getTooltipDescription(IFormattableTextComponent prefix, boolean canAddDetail) {
        List<ITextComponent> description = new ArrayList<>();
        description.add(prefix.append(getDescription()));
        if (canAddDetail && Screen.hasShiftDown()) {
            IFormattableTextComponent modifierDescription = getModifierDescription();
            if (modifierDescription != null) {
                description.add((new StringTextComponent("   ")).append((ITextComponent) modifierDescription.withStyle(TextFormatting.DARK_GRAY)));
            }
        }
        return description;
    }

    public ITextComponent getDescription() {
        IFormattableTextComponent iFormattableTextComponent = null;
        StringTextComponent stringTextComponent = new StringTextComponent(this.value);
        if (this.type == ModifierRollType.ADD_RANDOM_MODIFIER) {
            VaultCrystalCatalystConfig.TaggedPool pool = ModConfigs.VAULT_CRYSTAL_CATALYST.getPool(this.value);
            if (pool != null) {
                iFormattableTextComponent = pool.getDisplayName();
            }
        } else {
            VaultModifier modifier = ModConfigs.VAULT_MODIFIERS.getByName(this.value);
            if (modifier != null) {
                iFormattableTextComponent.setStyle(Style.EMPTY.withColor(Color.fromRgb(modifier.getColor())));
            }
        }
        return this.type.getDescription((ITextComponent) iFormattableTextComponent);
    }

    @Nullable
    public IFormattableTextComponent getModifierDescription() {
        if (this.type == ModifierRollType.ADD_SPECIFIC_MODIFIER) {
            VaultModifier modifier = ModConfigs.VAULT_MODIFIERS.getByName(this.value);
            if (modifier != null) {
                return (IFormattableTextComponent) new StringTextComponent(modifier.getDescription());
            }
        }
        return null;
    }

    @Nullable
    public String getModifier(Random random, Predicate<String> modifierFilter) {
        if (this.type == ModifierRollType.ADD_SPECIFIC_MODIFIER) {
            if (!modifierFilter.test(this.value)) {
                return this.value;
            }
        } else {
            VaultCrystalCatalystConfig.TaggedPool pool = ModConfigs.VAULT_CRYSTAL_CATALYST.getPool(this.value);
            if (pool != null) {
                return pool.getModifier(random, modifierFilter);
            }
        }
        return null;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\catalyst\ModifierRollResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */