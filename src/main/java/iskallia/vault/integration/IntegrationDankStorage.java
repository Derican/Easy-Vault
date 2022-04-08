package iskallia.vault.integration;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tfar.dankstorage.utils.Utils;


public class IntegrationDankStorage {
    @SubscribeEvent
    public static void onStewFinish(LivingEntityUseItemEvent.Finish event) {
        ItemStack dank = event.getItem();
        if (!(dank.getItem() instanceof tfar.dankstorage.item.DankItem) || !Utils.isConstruction(dank)) {
            return;
        }
        ItemStack dankUsedStack = Utils.getItemStackInSelectedSlot(dank);
        if (!(dankUsedStack.getItem() instanceof net.minecraft.item.SuspiciousStewItem)) {
            return;
        }
        CompoundNBT tag = dankUsedStack.getTag();
        if (tag != null && tag.contains("Effects", 9)) {
            ListNBT effectList = tag.getList("Effects", 10);

            for (int i = 0; i < effectList.size(); i++) {
                int duration = 160;
                CompoundNBT effectTag = effectList.getCompound(i);
                if (effectTag.contains("EffectDuration", 3)) {
                    duration = effectTag.getInt("EffectDuration");
                }

                Effect effect = Effect.byId(effectTag.getByte("EffectId"));
                if (effect != null)
                    event.getEntityLiving().addEffect(new EffectInstance(effect, duration));
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\integration\IntegrationDankStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */