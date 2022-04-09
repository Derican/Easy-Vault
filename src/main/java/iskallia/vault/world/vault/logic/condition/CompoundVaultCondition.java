// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.condition;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class CompoundVaultCondition extends VaultCondition
{
    private final List<String> postfix;
    
    protected CompoundVaultCondition() {
        this.postfix = new ArrayList<String>();
    }
    
    protected CompoundVaultCondition(final IVaultCondition condition, final List<String> postfix, final Consumer<List<String>> action) {
        super(null, condition);
        (this.postfix = new ArrayList<String>()).addAll(postfix);
        action.accept(this.postfix);
    }
    
    protected CompoundVaultCondition(final VaultCondition a, final VaultCondition b, final String operator, final IVaultCondition result) {
        super(null, result);
        this.postfix = new ArrayList<String>();
        if (a.getId() == null) {
            throw new IllegalStateException("Parent id can't be null!");
        }
        this.postfix.add(a.getId().toString());
        if (b instanceof CompoundVaultCondition) {
            this.postfix.addAll(((CompoundVaultCondition)b).postfix);
        }
        else if (b != null) {
            this.postfix.add(b.getId().toString());
        }
        this.postfix.add(operator);
    }
    
    @Override
    public VaultCondition negate() {
        return new CompoundVaultCondition(this.condition.negate(), this.postfix, postfix -> postfix.add("~"));
    }
    
    @Override
    public VaultCondition and(final VaultCondition other) {
        return new CompoundVaultCondition(this.condition.and(other), this.postfix, postfix -> {
            if (other instanceof CompoundVaultCondition) {
                postfix.addAll(((CompoundVaultCondition)other).postfix);
            }
            else {
                postfix.add(other.getId().toString());
            }
            postfix.add("&");
        });
    }
    
    @Override
    public VaultCondition or(final VaultCondition other) {
        return new CompoundVaultCondition(this.condition.or(other), this.postfix, postfix -> {
            if (other instanceof CompoundVaultCondition) {
                postfix.addAll(((CompoundVaultCondition)other).postfix);
            }
            else {
                postfix.add(other.getId().toString());
            }
            postfix.add("|");
        });
    }
    
    @Override
    public VaultCondition xor(final VaultCondition other) {
        return new CompoundVaultCondition(this.condition.xor(other), this.postfix, postfix -> {
            if (other instanceof CompoundVaultCondition) {
                postfix.addAll(((CompoundVaultCondition)other).postfix);
            }
            else {
                postfix.add(other.getId().toString());
            }
            postfix.add("^");
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
                case "~": {
                    final IVaultCondition a = (stack.peek() instanceof ResourceLocation) ? CompoundVaultCondition.REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    stack.push(a.negate());
                    break;
                }
                case "&": {
                    final IVaultCondition a = (stack.peek() instanceof ResourceLocation) ? CompoundVaultCondition.REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    final IVaultCondition b = (stack.peek() instanceof ResourceLocation) ? CompoundVaultCondition.REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    stack.push(a.and(b));
                    break;
                }
                case "|": {
                    final IVaultCondition a = (stack.peek() instanceof ResourceLocation) ? CompoundVaultCondition.REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    final IVaultCondition b = (stack.peek() instanceof ResourceLocation) ? CompoundVaultCondition.REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    stack.push(a.or(b));
                    break;
                }
                case "^": {
                    final IVaultCondition a = (stack.peek() instanceof ResourceLocation) ? CompoundVaultCondition.REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    final IVaultCondition b = (stack.peek() instanceof ResourceLocation) ? CompoundVaultCondition.REGISTRY.get(stack.pop()) : (IVaultCondition) stack.pop();
                    stack.push(a.xor(b));
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
        this.condition = (IVaultCondition) stack.pop();
    }
    
    public static CompoundVaultCondition fromNBT(final CompoundNBT nbt) {
        final CompoundVaultCondition condition = new CompoundVaultCondition();
        condition.deserializeNBT(nbt);
        return condition;
    }
}
