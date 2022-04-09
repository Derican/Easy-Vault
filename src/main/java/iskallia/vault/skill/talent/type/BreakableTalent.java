// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BreakableTalent extends PlayerTalent
{
    @Expose
    private final float damagePreventionChance;
    @Expose
    private final float damageAsDurabilityMultiplier;
    
    public BreakableTalent(final int cost, final float damagePreventionChance, final float damageAsDurabilityMultiplier) {
        super(cost);
        this.damagePreventionChance = damagePreventionChance;
        this.damageAsDurabilityMultiplier = damageAsDurabilityMultiplier;
    }
    
    @SubscribeEvent
    public static void onPlayerDamage(final LivingHurtEvent event) {
        if (event.getEntityLiving().level.isClientSide) {
            return;
        }
        if (!(event.getEntityLiving() instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity player = (ServerPlayerEntity)event.getEntityLiving();
        int armorPieces = 0;
        final EquipmentSlotType[] values = EquipmentSlotType.values();
        EquipmentSlotType slotType = null;
        for (int length = values.length, i = 0; i < length; ++i) {
            slotType = values[i];
            if (slotType.getType() != EquipmentSlotType.Group.HAND) {
                final ItemStack stack = player.getItemBySlot(slotType);
                if (!stack.isEmpty() && stack.isDamageableItem()) {
                    ++armorPieces;
                }
            }
        }
        if (armorPieces <= 0) {
            return;
        }
        float durabilityDamageMultiplier = 1.0f;
        float preventionChance = 0.0f;
        final TalentTree talents = PlayerTalentsData.get(player.getLevel()).getTalents((PlayerEntity)player);
        for (final Object talent : talents.getTalents(BreakableTalent.class)) {
            preventionChance += ((BreakableTalent)talent).damagePreventionChance;
            durabilityDamageMultiplier += ((BreakableTalent)talent).damageAsDurabilityMultiplier;
        }
        if (preventionChance <= 0.0f || BreakableTalent.rand.nextFloat() >= preventionChance) {
            return;
        }
        final float dmgAmount = event.getAmount();
        final float postArmorAmount = dmgAmount / 4.0f * (4 - armorPieces);
        final float armorDmgHit = dmgAmount / 4.0f * durabilityDamageMultiplier;
        for (final EquipmentSlotType slotType2 : EquipmentSlotType.values()) {
            if (slotType2.getType() != EquipmentSlotType.Group.HAND) {
                final ItemStack stack2 = player.getItemBySlot(slotType2);
                if (!stack2.isEmpty() && stack2.isDamageableItem()) {
                    stack2.hurtAndBreak(MathHelper.ceil(armorDmgHit), (LivingEntity)player, brokenStack -> player.broadcastBreakEvent(slotType2));
                }
            }
        }
        player.getCommandSenderWorld().playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.IRON_GOLEM_DAMAGE, SoundCategory.MASTER, 0.5f, 1.0f);
        event.setAmount(postArmorAmount);
        if (armorPieces >= 4) {
            event.setAmount(0.0f);
        }
    }
}
