package iskallia.vault.world.gen.ruletest;

import com.mojang.serialization.Codec;
import iskallia.vault.Vault;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.template.IRuleTestType;
import net.minecraft.world.gen.feature.template.RuleTest;

import java.util.Random;

public class VaultRuleTest
        extends RuleTest {
    public static final VaultRuleTest INSTANCE = new VaultRuleTest();
    public static final Codec<VaultRuleTest> CODEC = Codec.unit(() -> INSTANCE);
    public static final IRuleTestType<VaultRuleTest> TYPE = register("vault_stone_match", CODEC);


    public boolean test(BlockState state, Random random) {
        return (state.canOcclude() && state.getMaterial() == Material.STONE);
    }


    protected IRuleTestType<?> getType() {
        return TYPE;
    }

    static <P extends RuleTest> IRuleTestType<P> register(String name, Codec<P> codec) {
        return (IRuleTestType<P>) Registry.register(Registry.RULE_TEST, Vault.id(name), () -> codec);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\ruletest\VaultRuleTest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */