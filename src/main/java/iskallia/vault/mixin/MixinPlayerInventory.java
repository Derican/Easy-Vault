package iskallia.vault.mixin;

import iskallia.vault.Vault;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.InventorySnapshotData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.VaultAttributeInfluence;
import iskallia.vault.world.vault.modifier.DurabilityDamageModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.CapabilityItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({PlayerInventory.class})
public class MixinPlayerInventory
        implements InventorySnapshotData.InventoryAccessor {
    @Shadow
    @Final
    public PlayerEntity player;
    @Shadow
    @Final
    private List<NonNullList<ItemStack>> compartments;

    @ModifyArg(method = {"hurtArmor"}, index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damageItem(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    public int limitMaxArmorDamage(int damageAmount) {
        if (this.player.level.dimension() == Vault.VAULT_KEY) {
            damageAmount = Math.min(damageAmount, 5);
        }

        if (this.player.getCommandSenderWorld() instanceof ServerWorld) {
            ServerWorld sWorld = (ServerWorld) this.player.getCommandSenderWorld();
            VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, this.player.blockPosition());
            if (vault != null) {
                for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                    if (influence.getType() == VaultAttributeInfluence.Type.DURABILITY_DAMAGE && !influence.isMultiplicative()) {
                        damageAmount = (int) (damageAmount + influence.getValue());
                    }
                }

                for (DurabilityDamageModifier modifier : vault.getActiveModifiersFor(PlayerFilter.of(new PlayerEntity[]{this.player}, ), DurabilityDamageModifier.class)) {
                    damageAmount = (int) (damageAmount * modifier.getDurabilityDamageTakenMultiplier());
                }

                for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                    if (influence.getType() == VaultAttributeInfluence.Type.DURABILITY_DAMAGE && influence.isMultiplicative()) {
                        damageAmount = (int) (damageAmount * influence.getValue());
                    }
                }
            }
        }

        return damageAmount;
    }

    @Inject(method = {"addItemStackToInventory"}, at = {@At("HEAD")}, cancellable = true)
    public void interceptItemAddition(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() != ModItems.SOUL_SHARD) {
            return;
        }
        if (this.player.containerMenu instanceof iskallia.vault.container.inventory.ShardPouchContainer) {
            return;
        }
        PlayerInventory thisInventory = (PlayerInventory) this;

        ItemStack pouchStack = ItemStack.EMPTY;
        for (int slot = 0; slot < thisInventory.getContainerSize(); slot++) {
            ItemStack invStack = thisInventory.getItem(slot);
            if (invStack.getItem() instanceof iskallia.vault.item.ItemShardPouch) {
                pouchStack = invStack;
                break;
            }
        }
        if (pouchStack.isEmpty()) {
            return;
        }

        pouchStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            ItemStack remainder = handler.insertItem(0, stack, false);
            stack.setCount(remainder.getCount());
            if (stack.isEmpty()) {
                cir.setReturnValue(Boolean.valueOf(true));
            }
        });
    }


    public int getSize() {
        return this.compartments.stream().mapToInt(NonNullList::size).sum();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\mixin\MixinPlayerInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */