package iskallia.vault.world.vault.logic.task;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class CompoundVaultTask extends VaultTask {
    private List<String> postfix;

    protected CompoundVaultTask() {
        this.postfix = new ArrayList<String>();
    }

    protected CompoundVaultTask(final IVaultTask task, final List<String> postfix, final Consumer<List<String>> action) {
        super(null, task);
        (this.postfix = new ArrayList<String>()).addAll(postfix);
        action.accept(this.postfix);
    }

    public CompoundVaultTask(final VaultTask a, final VaultTask b, final String operator, final IVaultTask result) {
        super(null, result);
        this.postfix = new ArrayList<String>();
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

    @Override
    public VaultTask then(final VaultTask other) {
        return new CompoundVaultTask(this.task.then(other), this.postfix, postfix -> {
            if (other instanceof CompoundVaultTask) {
                postfix.addAll(((CompoundVaultTask) other).postfix);
            } else {
                postfix.add(other.getId().toString());
            }
            postfix.add(">");
        });
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putString("Postfix", String.join(" ", this.postfix));
        return nbt;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        final Stack<Object> stack = new Stack<Object>();
        final String[] split;
        final String[] data = split = nbt.getString("Postfix").split(Pattern.quote(" "));
        for (final String s : split) {
            this.postfix.add(s);
            final String s2 = s;
            switch (s2) {
                case ">": {
                    final IVaultTask a = (stack.peek() instanceof ResourceLocation) ? CompoundVaultTask.REGISTRY.get(stack.pop()) : (IVaultTask) stack.pop();
                    final IVaultTask b = (stack.peek() instanceof ResourceLocation) ? CompoundVaultTask.REGISTRY.get(stack.pop()) : (IVaultTask) stack.pop();
                    stack.push(a.then(b));
                    break;
                }
                default: {
                    stack.push(new ResourceLocation(s));
                    break;
                }
            }
        }
        if (stack.size() != 1) {
            throw new IllegalStateException("Invalid end stack " + stack);
        }
        this.task = (IVaultTask) stack.pop();
    }

    public static CompoundVaultTask fromNBT(final CompoundNBT nbt) {
        final CompoundVaultTask condition = new CompoundVaultTask();
        condition.deserializeNBT(nbt);
        return condition;
    }
}
