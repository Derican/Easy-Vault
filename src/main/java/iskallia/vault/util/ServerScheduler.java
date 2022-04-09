// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.util.Tuple;
import net.minecraftforge.event.TickEvent;

import java.util.Iterator;
import java.util.LinkedList;

public class ServerScheduler
{
    public static final ServerScheduler INSTANCE;
    private static final Object lock;
    private boolean inTick;
    private final LinkedList<Tuple<Runnable, Counter>> queue;
    private final LinkedList<Tuple<Runnable, Integer>> waiting;
    
    private ServerScheduler() {
        this.inTick = false;
        this.queue = new LinkedList<Tuple<Runnable, Counter>>();
        this.waiting = new LinkedList<Tuple<Runnable, Integer>>();
    }
    
    public void onServerTick(final TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        this.inTick = true;
        synchronized (ServerScheduler.lock) {
            this.inTick = true;
            final Iterator<Tuple<Runnable, Counter>> iterator = this.queue.iterator();
            while (iterator.hasNext()) {
                final Tuple<Runnable, Counter> r = iterator.next();
                ((Counter)r.getB()).decrement();
                if (((Counter)r.getB()).getValue() <= 0) {
                    ((Runnable)r.getA()).run();
                    iterator.remove();
                }
            }
            this.inTick = false;
            for (final Tuple<Runnable, Integer> wait : this.waiting) {
                this.queue.addLast((Tuple<Runnable, Counter>)new Tuple(wait.getA(), new Counter((int)wait.getB())));
            }
        }
        this.waiting.clear();
    }
    
    public void schedule(final int tickDelay, final Runnable r) {
        synchronized (ServerScheduler.lock) {
            if (this.inTick) {
                this.waiting.addLast((Tuple<Runnable, Integer>)new Tuple(r, tickDelay));
            }
            else {
                this.queue.addLast((Tuple<Runnable, Counter>)new Tuple(r, new Counter(tickDelay)));
            }
        }
    }
    
    static {
        INSTANCE = new ServerScheduler();
        lock = new Object();
    }
}
