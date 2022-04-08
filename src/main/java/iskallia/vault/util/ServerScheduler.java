package iskallia.vault.util;

import net.minecraft.util.Tuple;
import net.minecraftforge.event.TickEvent;

import java.util.Iterator;
import java.util.LinkedList;


public class ServerScheduler {
    public static final ServerScheduler INSTANCE = new ServerScheduler();
    private static final Object lock = new Object();

    private boolean inTick = false;
    private final LinkedList<Tuple<Runnable, Counter>> queue = new LinkedList<>();
    private final LinkedList<Tuple<Runnable, Integer>> waiting = new LinkedList<>();


    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }

        this.inTick = true;
        synchronized (lock) {
            this.inTick = true;
            Iterator<Tuple<Runnable, Counter>> iterator = this.queue.iterator();
            while (iterator.hasNext()) {
                Tuple<Runnable, Counter> r = iterator.next();
                ((Counter) r.getB()).decrement();
                if (((Counter) r.getB()).getValue() <= 0) {
                    ((Runnable) r.getA()).run();
                    iterator.remove();
                }
            }
            this.inTick = false;
            for (Tuple<Runnable, Integer> wait : this.waiting) {
                this.queue.addLast(new Tuple(wait.getA(), new Counter(((Integer) wait.getB()).intValue())));
            }
        }
        this.waiting.clear();
    }

    public void schedule(int tickDelay, Runnable r) {
        synchronized (lock) {
            if (this.inTick) {
                this.waiting.addLast(new Tuple(r, Integer.valueOf(tickDelay)));
            } else {
                this.queue.addLast(new Tuple(r, new Counter(tickDelay)));
            }
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\ServerScheduler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */