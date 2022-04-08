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
    private final List<String> modifiers = new ArrayList<>();
    private int votes;

    DirectionChoice(Direction direction) {
        this.direction = direction;
        this.chatColor = getDirectionColor(this.direction);
        this.votes = 1;
    }

    DirectionChoice(CompoundNBT tag) {
        this.direction = Direction.byName(tag.getString("direction"));
        this.chatColor = getDirectionColor(this.direction);
        this.votes = tag.getInt("votes");
        ListNBT modifierList = tag.getList("modifiers", 8);
        for (int i = 0; i < modifierList.size(); i++) {
            this.modifiers.add(modifierList.getString(i));
        }
    }

    public void addVote() {
        this.votes++;
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
        return getDirectionDisplay(null);
    }

    public ITextComponent getDirectionDisplay(@Nullable String prefix) {
        String directionName = ((prefix == null) ? "" : prefix) + StringUtils.capitalize(getDirection().getName());
        return (ITextComponent) (new StringTextComponent(directionName)).withStyle(getChatColor());
    }

    public void addModifier(VoteModifier modifier) {
        this.modifiers.add(modifier.getName());
    }

    public List<VoteModifier> getModifiers() {
        List<VoteModifier> modifierList = new ArrayList<>();
        this.modifiers.forEach(modifierStr -> {
            VoteModifier modifier = ModConfigs.ARCHITECT_EVENT.getModifier(modifierStr);
            if (modifier != null) {
                modifierList.add(modifier);
            }
        });
        return modifierList;
    }

    CompoundNBT serialize() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("direction", this.direction.getName());
        tag.putInt("votes", this.votes);
        ListNBT modifierList = new ListNBT();
        this.modifiers.forEach(modifier -> modifierList.add(StringNBT.valueOf(modifier)));
        tag.put("modifiers", (INBT) modifierList);
        return tag;
    }

    public static int getVOffset(Direction dir) {
        return 33 + (dir.ordinal() - 2) * 9;
    }

    private static TextFormatting getDirectionColor(Direction dir) {
        if (dir != null) {
            switch (dir) {
                case NORTH:
                    return TextFormatting.RED;
                case SOUTH:
                    return TextFormatting.AQUA;
                case WEST:
                    return TextFormatting.GOLD;
                case EAST:
                    return TextFormatting.GREEN;
            }
        }
        return TextFormatting.WHITE;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\DirectionChoice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */