// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.client;

import net.minecraft.util.text.ITextComponent;
import iskallia.vault.network.message.SandEventUpdateMessage;
import net.minecraftforge.event.TickEvent;
import java.util.function.Consumer;
import net.minecraftforge.common.MinecraftForge;
import java.util.Collections;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Deque;

public class ClientSandEventData
{
    private static final ClientSandEventData INSTANCE;
    private final Deque<ContributorDisplay> contributors;
    private float filledPercentage;
    private int collectedSand;
    private int totalSand;
    private int timeout;
    
    private ClientSandEventData() {
        this.contributors = new LinkedList<ContributorDisplay>();
        this.filledPercentage = 0.0f;
        this.collectedSand = 0;
        this.totalSand = 0;
        this.timeout = 0;
    }
    
    public static ClientSandEventData getInstance() {
        return ClientSandEventData.INSTANCE;
    }
    
    public float getFilledPercentage() {
        return this.filledPercentage;
    }
    
    public int getCollectedSand() {
        return this.collectedSand;
    }
    
    public int getTotalSand() {
        return this.totalSand;
    }
    
    public boolean isValid() {
        return this.timeout > 0;
    }
    
    public Collection<ContributorDisplay> getContributors() {
        synchronized (this.contributors) {
            return Collections.unmodifiableCollection(this.contributors);
        }
    }
    
    public void init() {
        MinecraftForge.EVENT_BUS.addListener(this::onTick);
    }
    
    private void onTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (this.isValid()) {
                --this.timeout;
            }
            synchronized (this.contributors) {
                this.contributors.removeIf(contributor -> {
                    contributor.timeout--;
                    return contributor.timeout <= 0;
                });
            }
        }
    }
    
    public void receive(final SandEventUpdateMessage pkt) {
        this.filledPercentage = pkt.getPercentFilled();
        this.collectedSand = pkt.getSandCollected();
        this.totalSand = pkt.getSandSpawned();
        this.timeout = 60;
    }
    
    public void addContributor(final ITextComponent contributorDisplay) {
        synchronized (this.contributors) {
            this.contributors.addFirst(new ContributorDisplay(contributorDisplay));
            if (this.contributors.size() > 6) {
                this.contributors.removeLast();
            }
        }
    }
    
    static {
        INSTANCE = new ClientSandEventData();
    }
    
    public static class ContributorDisplay
    {
        public static final int TICK_TOTAL_DISPLAY = 30;
        private final ITextComponent contributorDisplay;
        private int timeout;
        
        public ContributorDisplay(final ITextComponent contributorDisplay) {
            this.contributorDisplay = contributorDisplay;
            this.timeout = 40;
        }
        
        public ITextComponent getContributorDisplay() {
            return this.contributorDisplay;
        }
        
        public float getRenderOpacity() {
            final float half = 15.0f;
            if (this.timeout > half) {
                return 1.0f;
            }
            return this.timeout / half;
        }
    }
}
