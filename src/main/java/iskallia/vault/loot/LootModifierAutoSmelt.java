// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.loot;

import com.google.gson.JsonObject;
import iskallia.vault.item.paxel.enhancement.PaxelEnhancements;
import iskallia.vault.util.LootUtils;
import iskallia.vault.util.RecipeUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraftforge.fml.hooks.BasicEventHooks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LootModifierAutoSmelt extends LootModifier
{
    private LootModifierAutoSmelt(final ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }
    
    @Nonnull
    protected List<ItemStack> doApply(final List<ItemStack> generatedLoot, final LootContext context) {
        if (!LootUtils.doesContextFulfillSet(context, LootParameterSets.BLOCK) || !context.hasParam(LootParameters.THIS_ENTITY)) {
            return generatedLoot;
        }
        final Entity e = (Entity)context.getParamOrNull(LootParameters.THIS_ENTITY);
        if (!(e instanceof ServerPlayerEntity)) {
            return generatedLoot;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity)e;
        final ItemStack tool = (ItemStack)context.getParamOrNull(LootParameters.TOOL);
        if (PaxelEnhancements.getEnhancement(tool) != PaxelEnhancements.AUTO_SMELT) {
            return generatedLoot;
        }
        final ServerWorld world = context.getLevel();
        final Vector3d pos = (Vector3d)context.getParamOrNull(LootParameters.ORIGIN);
        return generatedLoot.stream().filter(stack -> !stack.isEmpty()).map(stack -> {
            final Optional<Tuple<ItemStack, Float>> furnaceResult = RecipeUtil.findSmeltingResult((World)context.getLevel(), stack);
            furnaceResult.ifPresent(result -> {
                BasicEventHooks.firePlayerSmeltedEvent((PlayerEntity)player, (ItemStack)result.getA());
                final float exp = (float)result.getB();
                if (exp > 0.0f) {
                    int iExp = (int)exp;
                    final float partialExp = exp - iExp;
                    if (partialExp > 0.0f && partialExp > context.getRandom().nextFloat()) {
                        ++iExp;
                    }
                    while (iExp > 0) {
                        final int expPart = ExperienceOrbEntity.getExperienceValue(iExp);
                        iExp -= expPart;
                        world.addFreshEntity((Entity)new ExperienceOrbEntity((World)world, pos.x() + 0.5, pos.y() + 0.5, pos.z() + 0.5, expPart));
                    }
                }
                return;
            });
            return (ItemStack)furnaceResult.map(Tuple::getA).orElse(stack);
        }).collect(Collectors.toList());
    }
    
    public static class Serializer extends GlobalLootModifierSerializer<LootModifierAutoSmelt>
    {
        public LootModifierAutoSmelt read(final ResourceLocation location, final JsonObject object, final ILootCondition[] lootConditions) {
            return new LootModifierAutoSmelt(lootConditions);
        }
        
        public JsonObject write(final LootModifierAutoSmelt instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}
