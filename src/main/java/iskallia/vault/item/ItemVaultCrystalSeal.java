package iskallia.vault.item;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;


public class ItemVaultCrystalSeal
        extends Item {
    private final ResourceLocation objectiveId;

    public ItemVaultCrystalSeal(ResourceLocation id, ResourceLocation objectiveId) {
        super((new Item.Properties())
                .tab(ModItems.VAULT_MOD_GROUP)
                .stacksTo(1));

        setRegistryName(id);
        this.objectiveId = objectiveId;
    }

    public ResourceLocation getObjectiveId() {
        return this.objectiveId;
    }

    @Nullable
    public static String getEventKey(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemVaultCrystalSeal)) {
            return null;
        }
        CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.contains("eventKey", 8)) {
            return null;
        }
        return tag.getString("eventKey");
    }

    public static void setEventKey(ItemStack stack, String eventKey) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemVaultCrystalSeal)) {
            return;
        }
        stack.getOrCreateTag().putString("eventKey", eventKey);
    }


    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isClientSide()) {
            return;
        }

        String eventKey = getEventKey(stack);
        if (eventKey == null) {
            return;
        }

        boolean hasEvent = (ModConfigs.ARCHITECT_EVENT.isEnabled() || ModConfigs.RAID_EVENT_CONFIG.isEnabled());
        if (!hasEvent) {
            stack.setCount(0);
        }
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        VaultObjective objective = VaultObjective.getObjective(this.objectiveId);
        if (objective != null) {
            tooltip.add((new StringTextComponent("Sets a vault crystal's objective")).withStyle(TextFormatting.GRAY));
            tooltip.add((new StringTextComponent("to: ")).withStyle(TextFormatting.GRAY).append(objective.getObjectiveDisplayName()));
        }

        String eventKey = getEventKey(stack);
        if (eventKey != null) {
            if (objective != null) {
                tooltip.add(StringTextComponent.EMPTY);
            }
            tooltip.add((new StringTextComponent("Event Item")).withStyle(TextFormatting.AQUA));
            tooltip.add((new StringTextComponent("Expires after the event finishes.")).withStyle(TextFormatting.GRAY));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemVaultCrystalSeal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */