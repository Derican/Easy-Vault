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

public class ItemVaultCrystalSeal extends Item {
    private final ResourceLocation objectiveId;

    public ItemVaultCrystalSeal(final ResourceLocation id, final ResourceLocation objectiveId) {
        super(new Item.Properties().tab(ModItems.VAULT_MOD_GROUP).stacksTo(1));
        this.setRegistryName(id);
        this.objectiveId = objectiveId;
    }

    public ResourceLocation getObjectiveId() {
        return this.objectiveId;
    }

    @Nullable
    public static String getEventKey(final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemVaultCrystalSeal)) {
            return null;
        }
        final CompoundNBT tag = stack.getOrCreateTag();
        if (!tag.contains("eventKey", 8)) {
            return null;
        }
        return tag.getString("eventKey");
    }

    public static void setEventKey(final ItemStack stack, final String eventKey) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemVaultCrystalSeal)) {
            return;
        }
        stack.getOrCreateTag().putString("eventKey", eventKey);
    }

    public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        if (world.isClientSide()) {
            return;
        }
        final String eventKey = getEventKey(stack);
        if (eventKey == null) {
            return;
        }
        final boolean hasEvent = ModConfigs.ARCHITECT_EVENT.isEnabled() || ModConfigs.RAID_EVENT_CONFIG.isEnabled();
        if (!hasEvent) {
            stack.setCount(0);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        final VaultObjective objective = VaultObjective.getObjective(this.objectiveId);
        if (objective != null) {
            tooltip.add(new StringTextComponent("Sets a vault crystal's objective").withStyle(TextFormatting.GRAY));
            tooltip.add(new StringTextComponent("to: ").withStyle(TextFormatting.GRAY).append(objective.getObjectiveDisplayName()));
        }
        final String eventKey = getEventKey(stack);
        if (eventKey != null) {
            if (objective != null) {
                tooltip.add(StringTextComponent.EMPTY);
            }
            tooltip.add(new StringTextComponent("Event Item").withStyle(TextFormatting.AQUA));
            tooltip.add(new StringTextComponent("Expires after the event finishes.").withStyle(TextFormatting.GRAY));
        }
    }
}
