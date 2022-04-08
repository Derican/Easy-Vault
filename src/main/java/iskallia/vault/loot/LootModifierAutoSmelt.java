package iskallia.vault.loot;

import com.google.gson.JsonObject;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.util.LootUtils;
import iskallia.vault.util.RecipeUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LootModifierAutoSmelt extends LootModifier {
    private LootModifierAutoSmelt(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }


    @Nonnull
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (!LootUtils.doesContextFulfillSet(context, LootParameterSets.BLOCK) || !context.hasParam(LootParameters.THIS_ENTITY)) {
            return generatedLoot;
        }
        Entity e = (Entity) context.getParamOrNull(LootParameters.THIS_ENTITY);
        if (!(e instanceof ServerPlayerEntity)) {
            return generatedLoot;
        }
        ServerPlayerEntity player = (ServerPlayerEntity) e;
        ItemStack tool = (ItemStack) context.getParamOrNull(LootParameters.TOOL);
        if (PaxelEnhancements.getEnhancement(tool) != PaxelEnhancements.AUTO_SMELT) {
            return generatedLoot;
        }
        ServerWorld world = context.getLevel();
        Vector3d pos = (Vector3d) context.getParamOrNull(LootParameters.ORIGIN);
        return (List<ItemStack>) generatedLoot.stream()
                .filter(stack -> !stack.isEmpty())
                .map(stack -> {
                    Optional<Tuple<ItemStack, Float>> furnaceResult = RecipeUtil.findSmeltingResult((World) context.getLevel(), stack);


//                    furnaceResult.ifPresent(());


                    return furnaceResult.<ItemStack>map(Tuple::getA).orElse(stack);
                }).collect(Collectors.toList());
    }

    public static class Serializer
            extends GlobalLootModifierSerializer<LootModifierAutoSmelt> {
        public LootModifierAutoSmelt read(ResourceLocation location, JsonObject object, ILootCondition[] lootConditions) {
            return new LootModifierAutoSmelt(lootConditions);
        }


        public JsonObject write(LootModifierAutoSmelt instance) {
            return makeConditions(instance.conditions);
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\loot\LootModifierAutoSmelt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */