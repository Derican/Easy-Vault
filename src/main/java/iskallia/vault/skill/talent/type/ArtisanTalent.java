package iskallia.vault.skill.talent.type;

import com.google.gson.annotations.Expose;

public class ArtisanTalent extends PlayerTalent {
    @Expose
    private final String defaultRoll;

    public ArtisanTalent(int cost, String defaultRoll) {
        super(cost);
        this.defaultRoll = defaultRoll;
    }

    public String getDefaultRoll() {
        return this.defaultRoll;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\type\ArtisanTalent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */