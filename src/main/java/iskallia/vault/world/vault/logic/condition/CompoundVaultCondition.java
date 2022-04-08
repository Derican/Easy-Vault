package iskallia.vault.world.vault.logic.condition;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class CompoundVaultCondition
        extends VaultCondition {
    private final List<String> postfix = new ArrayList<>();


    protected CompoundVaultCondition(IVaultCondition condition, List<String> postfix, Consumer<List<String>> action) {
        super(null, condition);
        this.postfix.addAll(postfix);
        action.accept(this.postfix);
    }

    protected CompoundVaultCondition(VaultCondition a, VaultCondition b, String operator, IVaultCondition result) {
        super(null, result);

        if (a.getId() == null) {
            throw new IllegalStateException("Parent id can't be null!");
        }

        this.postfix.add(a.getId().toString());

        if (b instanceof CompoundVaultCondition) {
            this.postfix.addAll(((CompoundVaultCondition) b).postfix);
        } else if (b != null) {
            this.postfix.add(b.getId().toString());
        }

        this.postfix.add(operator);
    }


    public VaultCondition negate() {
        return new CompoundVaultCondition(this.condition.negate(), this.postfix, postfix -> postfix.add("~"));
    }


    public VaultCondition and(VaultCondition other) {
        return new CompoundVaultCondition(this.condition.and(other), this.postfix, postfix -> {
            if (other instanceof CompoundVaultCondition) {
                postfix.addAll(((CompoundVaultCondition) other).postfix);
            } else {
                postfix.add(other.getId().toString());
            }
            postfix.add("&");
        });
    }


    public VaultCondition or(VaultCondition other) {
        return new CompoundVaultCondition(this.condition.or(other), this.postfix, postfix -> {
            if (other instanceof CompoundVaultCondition) {
                postfix.addAll(((CompoundVaultCondition) other).postfix);
            } else {
                postfix.add(other.getId().toString());
            }
            postfix.add("|");
        });
    }


    public VaultCondition xor(VaultCondition other) {
        return new CompoundVaultCondition(this.condition.xor(other), this.postfix, postfix -> {
            if (other instanceof CompoundVaultCondition) {
                postfix.addAll(((CompoundVaultCondition) other).postfix);
            } else {
                postfix.add(other.getId().toString());
            }
            postfix.add("^");
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
            IVaultCondition a, b;
            this.postfix.add(s);


            switch (s) {
                case "~":
                    a = (stack.peek() instanceof ResourceLocation) ? REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    stack.push(a.negate());
                    break;
                case "&":
                    a = (stack.peek() instanceof ResourceLocation) ? REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    b = (stack.peek() instanceof ResourceLocation) ? REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    stack.push(a.and(b));
                    break;
                case "|":
                    a = (stack.peek() instanceof ResourceLocation) ? REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    b = (stack.peek() instanceof ResourceLocation) ? REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    stack.push(a.or(b));
                    break;
                case "^":
                    a = (stack.peek() instanceof ResourceLocation) ? REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    b = (stack.peek() instanceof ResourceLocation) ? REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    stack.push(a.xor(b));
                    break;
                default:
                    stack.push(new ResourceLocation(s));
                    break;
            }
        }
        if (stack.size() != 1) {
            throw new IllegalStateException("Invalid end stack " + stack);
        }

        this.condition = (IVaultCondition) stack.pop();
    }

    public static CompoundVaultCondition fromNBT(CompoundNBT nbt) {
        CompoundVaultCondition condition = new CompoundVaultCondition();
        condition.deserializeNBT(nbt);
        return condition;
    }

    protected CompoundVaultCondition() {
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\condition\CompoundVaultCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */