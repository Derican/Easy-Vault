package iskallia.vault.world.vault.influence;

import iskallia.vault.Vault;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;


@EventBusSubscriber
public class DamageTakenInfluence
        extends VaultInfluence {
    public static final ResourceLocation ID = Vault.id("dmg_taken");

    private float damageTakenMultiplier;

    DamageTakenInfluence() {
        super(ID);
    }

    public DamageTakenInfluence(float damageTakenMultiplier) {
        this();
        this.damageTakenMultiplier = damageTakenMultiplier;
    }

    public float getDamageTakenMultiplier() {
        return this.damageTakenMultiplier;
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.getCommandSenderWorld();
        if (world.isClientSide()) {
            return;
        }
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity sPlayer = (ServerPlayerEntity) entity;
            VaultRaid vault = VaultRaidData.get(sPlayer.getLevel()).getAt(sPlayer.getLevel(), sPlayer.blockPosition());
            if (vault != null) {
                float dmg = event.getAmount();
                for (DamageTakenInfluence influence : vault.getInfluences().<DamageTakenInfluence>getInfluences(DamageTakenInfluence.class)) {
                    dmg *= influence.getDamageTakenMultiplier();
                }
                event.setAmount(dmg);
            }
        }
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT tag = super.serializeNBT();
        tag.putFloat("dmg", this.damageTakenMultiplier);
        return tag;
    }


    public void deserializeNBT(CompoundNBT tag) {
        super.deserializeNBT(tag);
        this.damageTakenMultiplier = tag.getFloat("dmg");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\influence\DamageTakenInfluence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */