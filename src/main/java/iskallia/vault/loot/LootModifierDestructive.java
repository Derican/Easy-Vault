package iskallia.vault.loot;

import com.google.gson.JsonObject;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.util.LootUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class LootModifierDestructive
        extends LootModifier {
    private LootModifierDestructive(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }


    @Nonnull
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (!LootUtils.doesContextFulfillSet(context, LootParameterSets.BLOCK)) {
            return generatedLoot;
        }
        ItemStack tool = (ItemStack) context.getParamOrNull(LootParameters.TOOL);
        if (PaxelEnhancements.getEnhancement(tool) != PaxelEnhancements.DESTRUCTIVE) {
            return generatedLoot;
        }
        return new ArrayList<>();
    }

    public static class Serializer
            extends GlobalLootModifierSerializer<LootModifierDestructive> {
        public LootModifierDestructive read(ResourceLocation location, JsonObject object, ILootCondition[] lootConditions) {
            return new LootModifierDestructive(lootConditions);
        }


        public JsonObject write(LootModifierDestructive instance) {
            return makeConditions(instance.conditions);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\loot\LootModifierDestructive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */