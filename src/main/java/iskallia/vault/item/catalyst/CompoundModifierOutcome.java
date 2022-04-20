package iskallia.vault.item.catalyst;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompoundModifierOutcome {
    @Expose
    private final List<SingleModifierOutcome> rolls;

    public CompoundModifierOutcome() {
        this(new ArrayList<SingleModifierOutcome>());
    }

    private CompoundModifierOutcome(final List<SingleModifierOutcome> rolls) {
        this.rolls = rolls;
    }

    public CompoundModifierOutcome addOutcome(final SingleModifierOutcome outcome) {
        this.rolls.add(outcome);
        return this;
    }

    public List<SingleModifierOutcome> getRolls() {
        return Collections.unmodifiableList((List<? extends SingleModifierOutcome>) this.rolls);
    }
}
