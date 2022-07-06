// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.mixin;

import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GameRules.BooleanValue.class })
public interface MixinBooleanValue
{
    @Invoker("create")
    static GameRules.RuleType<GameRules.BooleanValue> create(final boolean defaultValue) {
        throw new AssertionError();
    }
}
