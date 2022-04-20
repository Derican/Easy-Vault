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

public class LootModifierDestructive extends LootModifier {
    private LootModifierDestructive(final ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    protected List<ItemStack> doApply(final List<ItemStack> generatedLoot, final LootContext context) {
        if (!LootUtils.doesContextFulfillSet(context, LootParameterSets.BLOCK)) {
            return generatedLoot;
        }
        final ItemStack tool = (ItemStack) context.getParamOrNull(LootParameters.TOOL);
        if (PaxelEnhancements.getEnhancement(tool) != PaxelEnhancements.DESTRUCTIVE) {
            return generatedLoot;
        }
        return new ArrayList<ItemStack>();
    }

    public static class Serializer extends GlobalLootModifierSerializer<LootModifierDestructive> {
        public LootModifierDestructive read(final ResourceLocation location, final JsonObject object, final ILootCondition[] lootConditions) {
            return new LootModifierDestructive(lootConditions);
        }

        public JsonObject write(final LootModifierDestructive instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}
