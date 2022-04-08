package iskallia.vault.item;

import iskallia.vault.config.FlawedRubyConfig;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class FlawedRubyItem extends BasicTooltipItem {
    public FlawedRubyItem(ResourceLocation id, Item.Properties properties, ITextComponent... components) {
        super(id, properties, components);
    }

    public static void markApplied(ItemStack gearPiece) {
        if (!(gearPiece.getItem() instanceof iskallia.vault.item.gear.VaultGear))
            return;
        CompoundNBT nbt = gearPiece.getOrCreateTag();
        nbt.putBoolean("FlawedRubyApplied", true);
    }

    public static void handleOutcome(ServerPlayerEntity player, ItemStack gearPiece) {
        if (!(gearPiece.getItem() instanceof iskallia.vault.item.gear.VaultGear))
            return;
        if (shouldHandleOutcome(gearPiece)) {
            World world = player.getCommandSenderWorld();
            if (!(world instanceof ServerWorld))
                return;
            ServerWorld serverWorld = (ServerWorld) world;

            FlawedRubyConfig.Outcome outcome = FlawedRubyConfig.Outcome.FAIL;

            TalentTree talents = PlayerTalentsData.get(serverWorld).getTalents((PlayerEntity) player);
            if (talents.hasLearnedNode(ModConfigs.TALENTS.ARTISAN)) {
                outcome = ModConfigs.FLAWED_RUBY.getForArtisan();
            } else if (talents.hasLearnedNode(ModConfigs.TALENTS.TREASURE_HUNTER)) {
                outcome = ModConfigs.FLAWED_RUBY.getForTreasureHunter();
            }

            if (outcome == FlawedRubyConfig.Outcome.IMBUE) {
                int max = ((Integer) ((IntegerAttribute) ModAttributes.GEAR_MAX_LEVEL.getOrDefault(gearPiece, Integer.valueOf(0))).getValue(gearPiece)).intValue();
                ModAttributes.GEAR_MAX_LEVEL.create(gearPiece, Integer.valueOf(max + 1));
                ModAttributes.IMBUED.create(gearPiece, Boolean.valueOf(true));
            } else if (outcome == FlawedRubyConfig.Outcome.BREAK) {
                gearPiece.setCount(0);
                player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            resetApplied(gearPiece);
        }
    }

    static void resetApplied(ItemStack gearPiece) {
        if (!(gearPiece.getItem() instanceof iskallia.vault.item.gear.VaultGear))
            return;
        CompoundNBT nbt = gearPiece.getOrCreateTag();
        nbt.putBoolean("FlawedRubyApplied", false);
    }

    public static boolean shouldHandleOutcome(ItemStack gearPiece) {
        if (!(gearPiece.getItem() instanceof iskallia.vault.item.gear.VaultGear)) return false;
        CompoundNBT nbt = gearPiece.getOrCreateTag();
        return nbt.getBoolean("FlawedRubyApplied");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\FlawedRubyItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */