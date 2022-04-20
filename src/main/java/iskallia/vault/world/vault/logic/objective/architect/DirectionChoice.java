package iskallia.vault.world.vault.logic.objective.architect;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.vault.logic.objective.architect.modifier.VoteModifier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DirectionChoice {
    private final Direction direction;
    private final TextFormatting chatColor;
    private final List<String> modifiers;
    private int votes;

    DirectionChoice(final Direction direction) {
        this.modifiers = new ArrayList<String>();
        this.direction = direction;
        this.chatColor = getDirectionColor(this.direction);
        this.votes = 1;
    }

    DirectionChoice(final CompoundNBT tag) {
        this.modifiers = new ArrayList<String>();
        this.direction = Direction.byName(tag.getString("direction"));
        this.chatColor = getDirectionColor(this.direction);
        this.votes = tag.getInt("votes");
        final ListNBT modifierList = tag.getList("modifiers", 8);
        for (int i = 0; i < modifierList.size(); ++i) {
            this.modifiers.add(modifierList.getString(i));
        }
    }

    public void addVote() {
        ++this.votes;
    }

    public int getVotes() {
        return this.votes;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public TextFormatting getChatColor() {
        return this.chatColor;
    }

    public ITextComponent getDirectionDisplay() {
        return this.getDirectionDisplay(null);
    }

    public ITextComponent getDirectionDisplay(@Nullable final String prefix) {
        final String directionName = ((prefix == null) ? "" : prefix) + StringUtils.capitalize(this.getDirection().getName());
        return (ITextComponent) new StringTextComponent(directionName).withStyle(this.getChatColor());
    }

    public void addModifier(final VoteModifier modifier) {
        this.modifiers.add(modifier.getName());
    }

    public List<VoteModifier> getModifiers() {
        final List<VoteModifier> modifierList = new ArrayList<VoteModifier>();
        this.modifiers.forEach(modifierStr -> {
            final VoteModifier modifier = ModConfigs.ARCHITECT_EVENT.getModifier(modifierStr);
            if (modifier != null) {
                modifierList.add(modifier);
            }
            return;
        });
        return modifierList;
    }

    CompoundNBT serialize() {
        final CompoundNBT tag = new CompoundNBT();
        tag.putString("direction", this.direction.getName());
        tag.putInt("votes", this.votes);
        final ListNBT modifierList = new ListNBT();
        this.modifiers.forEach(modifier -> modifierList.add(StringNBT.valueOf(modifier)));
        tag.put("modifiers", (INBT) modifierList);
        return tag;
    }

    public static int getVOffset(final Direction dir) {
        return 33 + (dir.ordinal() - 2) * 9;
    }

    private static TextFormatting getDirectionColor(final Direction dir) {
        if (dir != null) {
            switch (dir) {
                case NORTH: {
                    return TextFormatting.RED;
                }
                case SOUTH: {
                    return TextFormatting.AQUA;
                }
                case WEST: {
                    return TextFormatting.GOLD;
                }
                case EAST: {
                    return TextFormatting.GREEN;
                }
            }
        }
        return TextFormatting.WHITE;
    }
}
