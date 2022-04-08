package iskallia.vault.util.nbt;

import net.minecraft.nbt.*;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class NBTSerializer {
    public static final <T extends INBTSerializable> CompoundNBT serialize(T object) throws IllegalAccessException, UnserializableClassException {
        CompoundNBT t = new CompoundNBT();

        Class<?> definition = object.getClass();
        Field[] df = definition.getDeclaredFields();
        for (Field f : df) {
            if (f.isAnnotationPresent((Class) NBTSerialize.class)) {

                f.setAccessible(true);

                Object fv = f.get(object);
                if (fv != null) {

                    String tn = ((NBTSerialize) f.<NBTSerialize>getAnnotation(NBTSerialize.class)).name();
                    if (tn.equals("")) tn = f.getName();

                    Class<?> fc = f.getType();

                    if (fc.isAssignableFrom(byte.class)) {
                        t.putByte(tn, ((Byte) fv).byteValue());
                    } else if (fc.isAssignableFrom(boolean.class)) {
                        t.putBoolean(tn, ((Boolean) fv).booleanValue());
                    } else if (fc.isAssignableFrom(short.class)) {
                        t.putShort(tn, ((Short) fv).shortValue());
                    } else if (fc.isAssignableFrom(int.class)) {
                        t.putInt(tn, ((Integer) fv).intValue());
                    } else if (fc.isAssignableFrom(long.class)) {
                        t.putLong(tn, ((Long) fv).longValue());
                    } else if (fc.isAssignableFrom(float.class)) {
                        t.putFloat(tn, ((Float) fv).floatValue());
                    } else if (fc.isAssignableFrom(double.class)) {
                        t.putDouble(tn, ((Double) fv).doubleValue());
                    } else {
                        t.put(tn, objectToTag(fc, fv));
                    }

                }
            }
        }
        return t;
    }


    private static final <T, U extends T> INBT objectToTag(Class<T> clazz, U obj) throws IllegalAccessException, UnserializableClassException {
        if (obj == null) return null;

        if (clazz.isAssignableFrom(Byte.class)) return (INBT) ByteNBT.valueOf(((Byte) obj).byteValue());
        if (clazz.isAssignableFrom(Boolean.class))
            return (INBT) ByteNBT.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        if (clazz.isAssignableFrom(Short.class)) return (INBT) ShortNBT.valueOf(((Short) obj).shortValue());
        if (clazz.isAssignableFrom(Integer.class)) return (INBT) IntNBT.valueOf(((Integer) obj).intValue());
        if (clazz.isAssignableFrom(Long.class)) return (INBT) LongNBT.valueOf(((Long) obj).longValue());
        if (clazz.isAssignableFrom(Float.class)) return (INBT) FloatNBT.valueOf(((Float) obj).floatValue());
        if (clazz.isAssignableFrom(Double.class)) return (INBT) DoubleNBT.valueOf(((Double) obj).doubleValue());
        if (clazz.isAssignableFrom(byte[].class)) return (INBT) new ByteArrayNBT((byte[]) obj);
        if (clazz.isAssignableFrom(Byte[].class)) return (INBT) new ByteArrayNBT(ArrayUtils.toPrimitive((Byte[]) obj));
        if (clazz.isAssignableFrom(String.class)) return (INBT) StringNBT.valueOf((String) obj);
        if (clazz.isAssignableFrom(int[].class)) return (INBT) new IntArrayNBT((int[]) obj);
        if (clazz.isAssignableFrom(Integer[].class)) {
            return (INBT) new IntArrayNBT(ArrayUtils.toPrimitive((Integer[]) obj));
        }
        if (INBTSerializable.class.isAssignableFrom(clazz)) return (INBT) serialize((INBTSerializable) obj);
        if (Collection.class.isAssignableFrom(clazz)) return (INBT) serializeCollection((Collection) obj);

        if (Map.Entry.class.isAssignableFrom(clazz)) return (INBT) serializeEntry((Map.Entry<?, ?>) obj);
        if (Map.class.isAssignableFrom(clazz)) return (INBT) serializeCollection(((Map) obj).entrySet());

        throw new UnserializableClassException(clazz);
    }


    private static final <T> ListNBT serializeCollection(Collection<T> col) throws IllegalAccessException, UnserializableClassException {
        ListNBT c = new ListNBT();

        if (col.size() <= 0) return c;
        Class<T> subclass = (Class) col.iterator().next().getClass();

        for (T element : col) {

            INBT tag = objectToTag(subclass, element);
            if (tag != null) {
                c.add(tag);
            }
        }

        return c;
    }


    private static final <K, V> CompoundNBT serializeEntry(Map.Entry<K, V> entry) throws UnserializableClassException, IllegalAccessException {
        Class<K> keyClass = (Class) entry.getKey().getClass();
        Class<V> valueClass = (Class) entry.getValue().getClass();

        return serializeEntry(entry, keyClass, valueClass);
    }


    private static final <K, V> CompoundNBT serializeEntry(Map.Entry<? extends K, ? extends V> entry, Class<K> keyClass, Class<V> valueClass) throws IllegalAccessException, UnserializableClassException {
        CompoundNBT te = new CompoundNBT();

        if (entry.getKey() != null) {
            INBT keyTag = objectToTag(keyClass, entry.getKey());
            te.put("k", keyTag);
        }
        if (entry.getValue() != null) {
            INBT valueTag = objectToTag(valueClass, entry.getValue());
            te.put("v", valueTag);
        }

        return te;
    }


    public static final <T extends INBTSerializable> T deserialize(Class<T> definition, CompoundNBT data) throws IllegalAccessException, InstantiationException, UnserializableClassException {
        INBTSerializable iNBTSerializable = (INBTSerializable) definition.newInstance();

        deserialize(iNBTSerializable, data, true);

        return (T) iNBTSerializable;
    }


    public static final <T extends INBTSerializable> void deserialize(T instance, CompoundNBT data, boolean interpretMissingFieldValuesAsNull) throws IllegalAccessException, InstantiationException, UnserializableClassException {
        Field[] df = instance.getClass().getDeclaredFields();
        for (Field f : df) {
            if (f.isAnnotationPresent((Class) NBTSerialize.class)) {

                String tn = ((NBTSerialize) f.<NBTSerialize>getAnnotation(NBTSerialize.class)).name();
                if (tn.equals("")) tn = f.getName();

                if (!data.contains(tn)) {
                    if (interpretMissingFieldValuesAsNull) {

                        f.setAccessible(true);
                        if (f.getType().equals(boolean.class)) {
                            f.set(instance, Boolean.valueOf(false));
                        } else if (f.getType().equals(int.class)) {
                            f.set(instance, Integer.valueOf(0));
                        } else {
                            f.set(instance, (Object) null);
                        }
                    }
                } else {
                    Class<?> fc;


                    f.setAccessible(true);


                    Class<?> forceInstantiateAs = ((NBTSerialize) f.<NBTSerialize>getAnnotation(NBTSerialize.class)).typeOverride();

                    if (forceInstantiateAs.isAssignableFrom(Object.class)) {
                        fc = f.getType();
                    } else {
                        fc = forceInstantiateAs;
                    }

                    if (fc.isAssignableFrom(byte.class)) {
                        f.setByte(instance, data.getByte(tn));
                    } else if (fc.isAssignableFrom(boolean.class)) {
                        f.setBoolean(instance, data.getBoolean(tn));
                    } else if (fc.isAssignableFrom(short.class)) {
                        f.setShort(instance, data.getShort(tn));
                    } else if (fc.isAssignableFrom(int.class)) {
                        f.setInt(instance, data.getInt(tn));
                    } else if (fc.isAssignableFrom(long.class)) {
                        f.setLong(instance, data.getLong(tn));
                    } else if (fc.isAssignableFrom(float.class)) {
                        f.setFloat(instance, data.getFloat(tn));
                    } else if (fc.isAssignableFrom(double.class)) {
                        f.setDouble(instance, data.getDouble(tn));
                    } else {
                        f.set(instance, tagToObject(data.get(tn), fc, f.getGenericType()));
                    }

                }
            }
        }
    }

    private static <T> Collection<T> deserializeCollection(ListNBT list, Class<? extends Collection> colClass, Class<T> subclass, Type subtype) throws InstantiationException, IllegalAccessException, UnserializableClassException {
        Collection<T> c = colClass.newInstance();

        for (int i = 0; i < list.size(); i++) {
            c.add(tagToObject(list.get(i), subclass, subtype));
        }

        return c;
    }


    private static <K, V> Map<K, V> deserializeMap(ListNBT map, Class<? extends Map> mapClass, Class<K> keyClass, Type keyType, Class<V> valueClass, Type valueType) throws InstantiationException, IllegalAccessException, UnserializableClassException {
        Map<K, V> e = mapClass.newInstance();

        for (int i = 0; i < map.size(); i++) {
            K key;

            V value;
            CompoundNBT kvp = (CompoundNBT) map.get(i);

            if (kvp.contains("k")) {
                key = tagToObject(kvp.get("k"), keyClass, keyType);
            } else {
                key = null;
            }
            if (kvp.contains("v")) {
                value = tagToObject(kvp.get("v"), valueClass, valueType);
            } else {
                value = null;
            }

            e.put(key, value);
        }

        return e;
    }


    private static <T> T tagToObject(INBT tag, Class<T> clazz, Type subtype) throws IllegalAccessException, InstantiationException, UnserializableClassException {
        if (clazz.isAssignableFrom(Object.class) || clazz.isAssignableFrom(Number.class) || clazz
                .isAssignableFrom(CharSequence.class) || clazz.isAssignableFrom(Serializable.class) || clazz
                .isAssignableFrom(Comparable.class)) {
            throw new UnserializableClassException(clazz);
        }
        if (clazz.isAssignableFrom(Byte.class)) return (T) Byte.valueOf(((ByteNBT) tag).getAsByte());
        if (clazz.isAssignableFrom(Boolean.class)) return (T) Boolean.valueOf((((ByteNBT) tag).getAsByte() != 0));
        if (clazz.isAssignableFrom(Short.class)) return (T) Short.valueOf(((ShortNBT) tag).getAsShort());
        if (clazz.isAssignableFrom(Integer.class)) return (T) Integer.valueOf(((IntNBT) tag).getAsInt());
        if (clazz.isAssignableFrom(Long.class)) return (T) Long.valueOf(((LongNBT) tag).getAsLong());
        if (clazz.isAssignableFrom(Float.class)) return (T) Float.valueOf(((FloatNBT) tag).getAsFloat());
        if (clazz.isAssignableFrom(Double.class)) return (T) Double.valueOf(((DoubleNBT) tag).getAsDouble());
        if (clazz.isAssignableFrom(byte[].class)) return (T) ((ByteArrayNBT) tag).getAsByteArray();
        if (clazz.isAssignableFrom(Byte[].class))
            return (T) ArrayUtils.toObject(((ByteArrayNBT) tag).getAsByteArray());
        if (clazz.isAssignableFrom(String.class)) return (T) ((StringNBT) tag).getAsString();
        if (clazz.isAssignableFrom(int[].class)) return (T) ((IntArrayNBT) tag).getAsIntArray();
        if (clazz.isAssignableFrom(Integer[].class)) {
            return (T) ArrayUtils.toObject(((IntArrayNBT) tag).getAsIntArray());
        }
        if (INBTSerializable.class.isAssignableFrom(clazz)) {

            CompoundNBT ntc = (CompoundNBT) tag;
            return (T) deserialize((Class) clazz, ntc);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            Class<?> lct;
            Type listType = ((ParameterizedType) subtype).getActualTypeArguments()[0];


            if (listType instanceof ParameterizedType) {
                lct = (Class) ((ParameterizedType) listType).getRawType();
            } else {
                lct = (Class) listType;
            }

            ListNBT ntl = (ListNBT) tag;

            Collection<?> c2 = deserializeCollection(ntl, (Class) clazz, lct, listType);
            return (T) c2;
        }
        if (Map.class.isAssignableFrom(clazz)) {
            Class<?> keyClass, valueClass;
            Type[] types = ((ParameterizedType) subtype).getActualTypeArguments();
            Type keyType = types[0];
            Type valueType = types[1];


            if (keyType instanceof ParameterizedType) {
                keyClass = (Class) ((ParameterizedType) keyType).getRawType();
            } else {
                keyClass = (Class) keyType;
            }
            if (valueType instanceof ParameterizedType) {
                valueClass = (Class) ((ParameterizedType) valueType).getRawType();
            } else {
                valueClass = (Class) valueType;
            }

            ListNBT ntl = (ListNBT) tag;

            Map<?, ?> c2 = deserializeMap(ntl, (Class) clazz, keyClass, keyType, valueClass, valueType);
            return (T) c2;
        }
        throw new UnserializableClassException(clazz);
    }


    private static int getIDFromClass(Class<?> clazz) {
        if (clazz.isAssignableFrom(byte.class) || clazz.isAssignableFrom(Byte.class) || clazz
                .isAssignableFrom(boolean.class) || clazz.isAssignableFrom(Boolean.class))
            return 1;
        if (clazz.isAssignableFrom(short.class) || clazz.isAssignableFrom(Short.class))
            return 2;
        if (clazz.isAssignableFrom(int.class) || clazz.isAssignableFrom(Integer.class))
            return 3;
        if (clazz.isAssignableFrom(long.class) || clazz.isAssignableFrom(Long.class))
            return 4;
        if (clazz.isAssignableFrom(float.class) || clazz.isAssignableFrom(Float.class))
            return 5;
        if (clazz.isAssignableFrom(double.class) || clazz.isAssignableFrom(Double.class))
            return 6;
        if (clazz.isAssignableFrom(byte[].class) || clazz.isAssignableFrom(Byte[].class))
            return 7;
        if (clazz.isAssignableFrom(String.class)) return 8;
        if (clazz.isAssignableFrom(int[].class) || clazz.isAssignableFrom(Integer[].class))
            return 11;
        if (INBTSerializable.class.isAssignableFrom(clazz)) return 10;
        if (Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz))
            return 9;
        return 10;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\nbt\NBTSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */