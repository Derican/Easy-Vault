// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.item;

import iskallia.vault.config.FlawedRubyConfig;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.gear.VaultGear;
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

public class FlawedRubyItem extends BasicTooltipItem
{
    public FlawedRubyItem(final ResourceLocation id, final Item.Properties properties, final ITextComponent... components) {
        super(id, properties, components);
    }
    
    public static void markApplied(final ItemStack gearPiece) {
        if (!(gearPiece.getItem() instanceof VaultGear)) {
            return;
        }
        final CompoundNBT nbt = gearPiece.getOrCreateTag();
        nbt.putBoolean("FlawedRubyApplied", true);
    }
    
    public static void handleOutcome(final ServerPlayerEntity player, final ItemStack gearPiece) {
        if (!(gearPiece.getItem() instanceof VaultGear)) {
            return;
        }
        if (shouldHandleOutcome(gearPiece)) {
            final World world = player.getCommandSenderWorld();
            if (!(world instanceof ServerWorld)) {
                return;
            }
            final ServerWorld serverWorld = (ServerWorld)world;
            FlawedRubyConfig.Outcome outcome = FlawedRubyConfig.Outcome.FAIL;
            final TalentTree talents = PlayerTalentsData.get(serverWorld).getTalents((PlayerEntity)player);
            if (talents.hasLearnedNode(ModConfigs.TALENTS.ARTISAN)) {
                outcome = ModConfigs.FLAWED_RUBY.getForArtisan();
            }
            else if (talents.hasLearnedNode(ModConfigs.TALENTS.TREASURE_HUNTER)) {
                outcome = ModConfigs.FLAWED_RUBY.getForTreasureHunter();
            }
            if (outcome == FlawedRubyConfig.Outcome.IMBUE) {
                final int max = ModAttributes.GEAR_MAX_LEVEL.getOrDefault(gearPiece, 0).getValue(gearPiece);
                ModAttributes.GEAR_MAX_LEVEL.create(gearPiece, max + 1);
                ModAttributes.IMBUED.create(gearPiece, true);
            }
            else if (outcome == FlawedRubyConfig.Outcome.BREAK) {
                gearPiece.setCount(0);
                player.getCommandSenderWorld().playSound((PlayerEntity)null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
            resetApplied(gearPiece);
        }
    }
    
    static void resetApplied(final ItemStack gearPiece) {
        if (!(gearPiece.getItem() instanceof VaultGear)) {
            return;
        }
        final CompoundNBT nbt = gearPiece.getOrCreateTag();
        nbt.putBoolean("FlawedRubyApplied", false);
    }
    
    public static boolean shouldHandleOutcome(final ItemStack gearPiece) {
        if (!(gearPiece.getItem() instanceof VaultGear)) {
            return false;
        }
        final CompoundNBT nbt = gearPiece.getOrCreateTag();
        return nbt.getBoolean("FlawedRubyApplied");
    }
}
