package iskallia.vault.world.vault.logic.task;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class CompoundVaultTask
        extends VaultTask {
    private List<String> postfix = new ArrayList<>();


    protected CompoundVaultTask(IVaultTask task, List<String> postfix, Consumer<List<String>> action) {
        super(null, task);
        this.postfix.addAll(postfix);
        action.accept(this.postfix);
    }

    public CompoundVaultTask(VaultTask a, VaultTask b, String operator, IVaultTask result) {
        super(null, result);

        if (a.getId() == null) {
            throw new IllegalStateException("Parent id can't be null!");
        }

        this.postfix.add(a.getId().toString());

        if (b instanceof CompoundVaultTask) {
            this.postfix.addAll(((CompoundVaultTask) b).postfix);
        } else if (b != null) {
            this.postfix.add(b.getId().toString());
        }

        this.postfix.add(operator);
    }


    public VaultTask then(VaultTask other) {
        return new CompoundVaultTask(this.task.then(other), this.postfix, postfix -> {
            if (other instanceof CompoundVaultTask) {
                postfix.addAll(((CompoundVaultTask) other).postfix);
            } else {
                postfix.add(other.getId().toString());
            }
            postfix.add(">");
        });
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Postfix", String.join(" ", (Iterable) this.postfix));
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        Stack<Object> stack = new Stack();
        String[] data = nbt.getString("Postfix").split(Pattern.quote(" "));

        for (String s : data) {
            IVaultTask a, b;
            this.postfix.add(s);


            switch (s) {
                case ">":
                    a = (stack.peek() instanceof ResourceLocation) ? REGISTRY.get(stack.pop()) : (IVaultTask) stack.pop();
                    b = (stack.peek() instanceof ResourceLocation) ? REGISTRY.get(stack.pop()) : (IVaultTask) stack.pop();
                    stack.push(a.then(b));
                    break;
                default:
                    stack.push(new ResourceLocation(s));
                    break;
            }
        }
        if (stack.size() != 1) {
            throw new IllegalStateException("Invalid end stack " + stack);
        }

        this.task = (IVaultTask) stack.pop();
    }

    public static CompoundVaultTask fromNBT(CompoundNBT nbt) {
        CompoundVaultTask condition = new CompoundVaultTask();
        condition.deserializeNBT(nbt);
        return condition;
    }

    protected CompoundVaultTask() {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\task\CompoundVaultTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */