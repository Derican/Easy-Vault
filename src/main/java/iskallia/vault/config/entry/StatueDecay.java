package iskallia.vault.config.entry;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.MathUtilities;

public class StatueDecay {
    @Expose
    private int MIN;
    public static final StatueDecay NONE = new StatueDecay(-1, -1);
    @Expose
    private int MAX;

    public StatueDecay(int min, int max) {
        this.MIN = min;
        this.MAX = max;
    }

    public int getDecay() {
        return MathUtilities.getRandomInt(this.MIN, this.MAX);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\entry\StatueDecay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */