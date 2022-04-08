package iskallia.vault.item.catalyst;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CompoundModifierOutcome {
    @Expose
    private final List<SingleModifierOutcome> rolls;

    public CompoundModifierOutcome() {
        this(new ArrayList<>());
    }

    private CompoundModifierOutcome(List<SingleModifierOutcome> rolls) {
        this.rolls = rolls;
    }

    public CompoundModifierOutcome addOutcome(SingleModifierOutcome outcome) {
        this.rolls.add(outcome);
        return this;
    }

    public List<SingleModifierOutcome> getRolls() {
        return Collections.unmodifiableList(this.rolls);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\catalyst\CompoundModifierOutcome.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */