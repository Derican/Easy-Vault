package iskallia.vault.util.nbt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface NBTSerialize {
    String name() default "";

    Class<?> typeOverride() default Object.class;
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\nbt\NBTSerialize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */