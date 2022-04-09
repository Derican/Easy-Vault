// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.gen.ruletest;

import com.mojang.serialization.Codec;
import iskallia.vault.Vault;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IRuleTestType;
import net.minecraft.world.gen.feature.template.RuleTest;

import java.util.Random;

public class VaultRuleTest extends RuleTest
{
    public static final VaultRuleTest INSTANCE;
    public static final Codec<VaultRuleTest> CODEC;
    public static final IRuleTestType<VaultRuleTest> TYPE;
    
    public boolean test(final BlockState state, final Random random) {
        return state.canOcclude() && state.getMaterial() == Material.STONE;
    }
    
    protected IRuleTestType<?> getType() {
        return VaultRuleTest.TYPE;
    }
    
    static <P extends RuleTest> IRuleTestType<P> register(final String name, final Codec<P> codec) {
        return Registry.register(Registry.RULE_TEST, Vault.id(name), (() -> codec));
    }
    
    static {
        INSTANCE = new VaultRuleTest();
        CODEC = Codec.unit(() -> VaultRuleTest.INSTANCE);
        TYPE = register("vault_stone_match", VaultRuleTest.CODEC);
    }
}
