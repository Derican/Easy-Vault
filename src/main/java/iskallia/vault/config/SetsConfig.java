// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.skill.set.*;

import java.util.Arrays;
import java.util.List;

public class SetsConfig extends Config {
    @Expose
    public SetGroup<DragonSet> DRAGON;
    @Expose
    public SetGroup<PlayerSet> ZOD;
    @Expose
    public SetGroup<NinjaSet> NINJA;
    @Expose
    public SetGroup<GolemSet> GOLEM;
    @Expose
    public SetGroup<PlayerSet> RIFT;
    @Expose
    public SetGroup<CarapaceSet> CARAPACE;
    @Expose
    public SetGroup<PlayerSet> DIVINITY;
    @Expose
    public SetGroup<DryadSet> DRYAD;
    @Expose
    public SetGroup<BloodSet> BLOOD;
    @Expose
    public SetGroup<VampirismSet> VAMPIRE;
    @Expose
    public SetGroup<TreasureSet> TREASURE;
    @Expose
    public SetGroup<PlayerSet> PHOENIX;
    @Expose
    public SetGroup<AssassinSet> ASSASSIN;
    @Expose
    public SetGroup<DreamSet> DREAM;
    @Expose
    public SetGroup<PorcupineSet> PORCUPINE;

    @Override
    public String getName() {
        return "sets";
    }

    public List<SetGroup<?>> getAll() {
        return (List<SetGroup<?>>) Arrays.asList(this.DRAGON, this.RIFT, this.GOLEM, this.ASSASSIN, this.VAMPIRE, this.DRYAD, this.NINJA, this.TREASURE, this.ZOD, this.DIVINITY, this.CARAPACE, this.BLOOD, this.DREAM, this.PORCUPINE);
    }

    public SetGroup<?> getByName(String name) {
        return (SetGroup) getAll().stream().filter(group -> group.getParentName().equals(name)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown set with name " + name));
    }

    @Override
    protected void reset() {
        this.DRAGON = SetGroup.of("Dragon", 1, i -> new DragonSet(0.5f));
        this.ZOD = SetGroup.of("Zod", 1, i -> new PlayerSet(VaultGear.Set.ZOD));
        this.NINJA = SetGroup.of("Ninja", 1, i -> new NinjaSet(0.3f, 0.1f));
        this.GOLEM = SetGroup.of("Golem", 1, i -> new GolemSet(0.0f, 20.0f));
        this.RIFT = SetGroup.of("Rift", 1, i -> new PlayerSet(VaultGear.Set.RIFT));
        this.CARAPACE = SetGroup.of("Carapace", 1, i -> new CarapaceSet(0.5f));
        this.DIVINITY = SetGroup.of("Divinity", 1, i -> new PlayerSet(VaultGear.Set.DIVINITY));
        this.DRYAD = SetGroup.of("Dryad", 1, i -> new DryadSet(20.0f));
        this.BLOOD = SetGroup.of("Blood", 1, i -> new BloodSet(2.0f));
        this.VAMPIRE = SetGroup.of("Vampire", 1, i -> new VampirismSet(0.05f));
        this.TREASURE = SetGroup.of("Treasure", 1, i -> new TreasureSet(2.0f));
        this.PHOENIX = SetGroup.of("Phoenix", 1, i -> new PlayerSet(VaultGear.Set.PHOENIX));
        this.ASSASSIN = SetGroup.of("Assassin", 1, i -> new AssassinSet(0.4f));
        this.DREAM = SetGroup.of("Dream", 1, i -> new DreamSet(0.5f, 2, 0.1f, 0.1f, 0.25f));
        this.PORCUPINE = SetGroup.of("Porcupine", 1, i -> new PorcupineSet(0.4f, 1.0f));
    }
}
