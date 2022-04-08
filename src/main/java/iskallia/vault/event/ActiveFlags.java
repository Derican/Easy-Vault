package iskallia.vault.event;

public enum ActiveFlags {
    IS_AOE_MINING,
    IS_FORTUNE_MINING,
    IS_DOT_ATTACKING,
    IS_LEECHING,
    IS_AOE_ATTACKING,
    IS_REFLECT_ATTACKING;

    ActiveFlags() {
        this.activeReferences = 0;
    }

    public boolean isSet() {
        return (this.activeReferences > 0);
    }

    private int activeReferences;

    public synchronized void runIfNotSet(Runnable run) {
        if (!isSet()) {
            this.activeReferences++;
            try {
                run.run();
            } finally {
                this.activeReferences--;
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\event\ActiveFlags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */