package iskallia.vault.util.nbt;

import net.minecraft.nbt.*;
import org.apache.commons.lang3.ArrayUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class NBTSerializer {
    public static final <T extends INBTSerializable> CompoundNBT serialize(final T object) throws IllegalAccessException, UnserializableClassException {
        final CompoundNBT t = new CompoundNBT();
        final Class<?> definition = object.getClass();
        final Field[] declaredFields;
        final Field[] df = declaredFields = definition.getDeclaredFields();
        for (final Field f : declaredFields) {
            if (f.isAnnotationPresent(NBTSerialize.class)) {
                f.setAccessible(true);
                final Object fv = f.get(object);
                if (fv != null) {
                    String tn = f.getAnnotation(NBTSerialize.class).name();
                    if (tn.equals("")) {
                        tn = f.getName();
                    }
                    final Class fc = f.getType();
                    if (fc.isAssignableFrom(Byte.TYPE)) {
                        t.putByte(tn, (byte) fv);
                    } else if (fc.isAssignableFrom(Boolean.TYPE)) {
                        t.putBoolean(tn, (boolean) fv);
                    } else if (fc.isAssignableFrom(Short.TYPE)) {
                        t.putShort(tn, (short) fv);
                    } else if (fc.isAssignableFrom(Integer.TYPE)) {
                        t.putInt(tn, (int) fv);
                    } else if (fc.isAssignableFrom(Long.TYPE)) {
                        t.putLong(tn, (long) fv);
                    } else if (fc.isAssignableFrom(Float.TYPE)) {
                        t.putFloat(tn, (float) fv);
                    } else if (fc.isAssignableFrom(Double.TYPE)) {
                        t.putDouble(tn, (double) fv);
                    } else {
                        t.put(tn, objectToTag((Class<Object>) fc, fv));
                    }
                }
            }
        }
        return t;
    }

    private static final <T, U extends T> INBT objectToTag(final Class<T> clazz, final U obj) throws IllegalAccessException, UnserializableClassException {
        if (obj == null) {
            return null;
        }
        if (clazz.isAssignableFrom(Byte.class)) {
            return (INBT) ByteNBT.valueOf((Byte) obj);
        }
        if (clazz.isAssignableFrom(Boolean.class)) {
            return (INBT) ByteNBT.valueOf((byte) (((Boolean) obj) ? 1 : 0));
        }
        if (clazz.isAssignableFrom(Short.class)) {
            return (INBT) ShortNBT.valueOf((Short) obj);
        }
        if (clazz.isAssignableFrom(Integer.class)) {
            return (INBT) IntNBT.valueOf((Integer) obj);
        }
        if (clazz.isAssignableFrom(Long.class)) {
            return (INBT) LongNBT.valueOf((Long) obj);
        }
        if (clazz.isAssignableFrom(Float.class)) {
            return (INBT) FloatNBT.valueOf((Float) obj);
        }
        if (clazz.isAssignableFrom(Double.class)) {
            return (INBT) DoubleNBT.valueOf((Double) obj);
        }
        if (clazz.isAssignableFrom(byte[].class)) {
            return (INBT) new ByteArrayNBT((byte[]) obj);
        }
        if (clazz.isAssignableFrom(Byte[].class)) {
            return (INBT) new ByteArrayNBT(ArrayUtils.toPrimitive((Byte[]) (Byte[]) obj));
        }
        if (clazz.isAssignableFrom(String.class)) {
            return (INBT) StringNBT.valueOf((String) obj);
        }
        if (clazz.isAssignableFrom(int[].class)) {
            return (INBT) new IntArrayNBT((int[]) obj);
        }
        if (clazz.isAssignableFrom(Integer[].class)) {
            return (INBT) new IntArrayNBT(ArrayUtils.toPrimitive((Integer[]) (Integer[]) obj));
        }
        if (INBTSerializable.class.isAssignableFrom(clazz)) {
            return (INBT) serialize((INBTSerializable) obj);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return (INBT) serializeCollection((Collection<Object>) obj);
        }
        if (Map.Entry.class.isAssignableFrom(clazz)) {
            return (INBT) serializeEntry((Map.Entry<Object, Object>) obj);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return (INBT) serializeCollection(((Map) obj).entrySet());
        }
        throw new UnserializableClassException(clazz);
    }

    private static final <T> ListNBT serializeCollection(final Collection<T> col) throws IllegalAccessException, UnserializableClassException {
        final ListNBT c = new ListNBT();
        if (col.size() <= 0) {
            return c;
        }
        final Class<T> subclass = (Class<T>) col.iterator().next().getClass();
        for (final T element : col) {
            final INBT tag = objectToTag(subclass, element);
            if (tag != null) {
                c.add(tag);
            }
        }
        return c;
    }

    private static final <K, V> CompoundNBT serializeEntry(final Map.Entry<K, V> entry) throws UnserializableClassException, IllegalAccessException {
        final Class<K> keyClass = (Class<K>) entry.getKey().getClass();
        final Class<V> valueClass = (Class<V>) entry.getValue().getClass();
        return serializeEntry((Map.Entry<? extends K, ? extends V>) entry, keyClass, valueClass);
    }

    private static final <K, V> CompoundNBT serializeEntry(final Map.Entry<? extends K, ? extends V> entry, final Class<K> keyClass, final Class<V> valueClass) throws IllegalAccessException, UnserializableClassException {
        final CompoundNBT te = new CompoundNBT();
        if (entry.getKey() != null) {
            final INBT keyTag = objectToTag(keyClass, entry.getKey());
            te.put("k", keyTag);
        }
        if (entry.getValue() != null) {
            final INBT valueTag = objectToTag(valueClass, entry.getValue());
            te.put("v", valueTag);
        }
        return te;
    }

    public static final <T extends INBTSerializable> T deserialize(final Class<T> definition, final CompoundNBT data) throws IllegalAccessException, InstantiationException, UnserializableClassException {
        final T instance = definition.newInstance();
        deserialize(instance, data, true);
        return instance;
    }

    public static final <T extends INBTSerializable> void deserialize(final T instance, final CompoundNBT data, final boolean interpretMissingFieldValuesAsNull) throws IllegalAccessException, InstantiationException, UnserializableClassException {
        final Field[] declaredFields;
        final Field[] df = declaredFields = instance.getClass().getDeclaredFields();
        for (final Field f : declaredFields) {
            if (f.isAnnotationPresent(NBTSerialize.class)) {
                String tn = f.getAnnotation(NBTSerialize.class).name();
                if (tn.equals("")) {
                    tn = f.getName();
                }
                if (!data.contains(tn)) {
                    if (interpretMissingFieldValuesAsNull) {
                        f.setAccessible(true);
                        if (f.getType().equals(Boolean.TYPE)) {
                            f.set(instance, false);
                        } else if (f.getType().equals(Integer.TYPE)) {
                            f.set(instance, 0);
                        } else {
                            f.set(instance, null);
                        }
                    }
                } else {
                    f.setAccessible(true);
                    final Class<?> forceInstantiateAs = f.getAnnotation(NBTSerialize.class).typeOverride();
                    Class<?> fc;
                    if (forceInstantiateAs.isAssignableFrom(Object.class)) {
                        fc = f.getType();
                    } else {
                        fc = forceInstantiateAs;
                    }
                    if (fc.isAssignableFrom(Byte.TYPE)) {
                        f.setByte(instance, data.getByte(tn));
                    } else if (fc.isAssignableFrom(Boolean.TYPE)) {
                        f.setBoolean(instance, data.getBoolean(tn));
                    } else if (fc.isAssignableFrom(Short.TYPE)) {
                        f.setShort(instance, data.getShort(tn));
                    } else if (fc.isAssignableFrom(Integer.TYPE)) {
                        f.setInt(instance, data.getInt(tn));
                    } else if (fc.isAssignableFrom(Long.TYPE)) {
                        f.setLong(instance, data.getLong(tn));
                    } else if (fc.isAssignableFrom(Float.TYPE)) {
                        f.setFloat(instance, data.getFloat(tn));
                    } else if (fc.isAssignableFrom(Double.TYPE)) {
                        f.setDouble(instance, data.getDouble(tn));
                    } else {
                        f.set(instance, tagToObject(data.get(tn), fc, f.getGenericType()));
                    }
                }
            }
        }
    }

    private static <T> Collection<T> deserializeCollection(final ListNBT list, final Class<? extends Collection> colClass, final Class<T> subclass, final Type subtype) throws InstantiationException, IllegalAccessException, UnserializableClassException {
        final Collection<T> c = (Collection<T>) colClass.newInstance();
        for (int i = 0; i < list.size(); ++i) {
            c.add(tagToObject(list.get(i), subclass, subtype));
        }
        return c;
    }

    private static <K, V> Map<K, V> deserializeMap(final ListNBT map, final Class<? extends Map> mapClass, final Class<K> keyClass, final Type keyType, final Class<V> valueClass, final Type valueType) throws InstantiationException, IllegalAccessException, UnserializableClassException {
        final Map<K, V> e = (Map<K, V>) mapClass.newInstance();
        for (int i = 0; i < map.size(); ++i) {
            final CompoundNBT kvp = (CompoundNBT) map.get(i);
            K key;
            if (kvp.contains("k")) {
                key = tagToObject(kvp.get("k"), keyClass, keyType);
            } else {
                key = null;
            }
            V value;
            if (kvp.contains("v")) {
                value = tagToObject(kvp.get("v"), valueClass, valueType);
            } else {
                value = null;
            }
            e.put(key, value);
        }
        return e;
    }

    private static <T> T tagToObject(final INBT tag, final Class<T> clazz, final Type subtype) throws IllegalAccessException, InstantiationException, UnserializableClassException {
        if (clazz.isAssignableFrom(Object.class) || clazz.isAssignableFrom(Number.class) || clazz.isAssignableFrom(CharSequence.class) || clazz.isAssignableFrom(Serializable.class) || clazz.isAssignableFrom(Comparable.class)) {
            throw new UnserializableClassException(clazz);
        }
        if (clazz.isAssignableFrom(Byte.class)) {
            return (T) Byte.valueOf(((ByteNBT) tag).getAsByte());
        }
        if (clazz.isAssignableFrom(Boolean.class)) {
            return (T) Boolean.valueOf(((ByteNBT) tag).getAsByte() != 0);
        }
        if (clazz.isAssignableFrom(Short.class)) {
            return (T) Short.valueOf(((ShortNBT) tag).getAsShort());
        }
        if (clazz.isAssignableFrom(Integer.class)) {
            return (T) Integer.valueOf(((IntNBT) tag).getAsInt());
        }
        if (clazz.isAssignableFrom(Long.class)) {
            return (T) Long.valueOf(((LongNBT) tag).getAsLong());
        }
        if (clazz.isAssignableFrom(Float.class)) {
            return (T) Float.valueOf(((FloatNBT) tag).getAsFloat());
        }
        if (clazz.isAssignableFrom(Double.class)) {
            return (T) Double.valueOf(((DoubleNBT) tag).getAsDouble());
        }
        if (clazz.isAssignableFrom(byte[].class)) {
            return (T) ((ByteArrayNBT) tag).getAsByteArray();
        }
        if (clazz.isAssignableFrom(Byte[].class)) {
            return (T) ArrayUtils.toObject(((ByteArrayNBT) tag).getAsByteArray());
        }
        if (clazz.isAssignableFrom(String.class)) {
            return (T) ((StringNBT) tag).getAsString();
        }
        if (clazz.isAssignableFrom(int[].class)) {
            return (T) ((IntArrayNBT) tag).getAsIntArray();
        }
        if (clazz.isAssignableFrom(Integer[].class)) {
            return (T) ArrayUtils.toObject(((IntArrayNBT) tag).getAsIntArray());
        }
        if (INBTSerializable.class.isAssignableFrom(clazz)) {
            final CompoundNBT ntc = (CompoundNBT) tag;
            return (T) deserialize((Class) clazz, ntc);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            final Type listType = ((ParameterizedType) subtype).getActualTypeArguments()[0];
            Class<?> lct;
            if (listType instanceof ParameterizedType) {
                lct = (Class) ((ParameterizedType) listType).getRawType();
            } else {
                lct = (Class) listType;
            }
            final ListNBT ntl = (ListNBT) tag;
            final Collection c2 = deserializeCollection(ntl, (Class<? extends Collection>) clazz, lct, listType);
            return (T) c2;
        }
        if (Map.class.isAssignableFrom(clazz)) {
            final Type[] types = ((ParameterizedType) subtype).getActualTypeArguments();
            final Type keyType = types[0];
            final Type valueType = types[1];
            Class<?> keyClass;
            if (keyType instanceof ParameterizedType) {
                keyClass = (Class) ((ParameterizedType) keyType).getRawType();
            } else {
                keyClass = (Class) keyType;
            }
            Class<?> valueClass;
            if (valueType instanceof ParameterizedType) {
                valueClass = (Class) ((ParameterizedType) valueType).getRawType();
            } else {
                valueClass = (Class) valueType;
            }
            final ListNBT ntl2 = (ListNBT) tag;
            final Map c3 = deserializeMap(ntl2, (Class<? extends Map>) clazz, keyClass, keyType, valueClass, valueType);
            return (T) c3;
        }
        throw new UnserializableClassException(clazz);
    }

    private static int getIDFromClass(final Class<?> clazz) {
        if (clazz.isAssignableFrom(Byte.TYPE) || clazz.isAssignableFrom(Byte.class) || clazz.isAssignableFrom(Boolean.TYPE) || clazz.isAssignableFrom(Boolean.class)) {
            return 1;
        }
        if (clazz.isAssignableFrom(Short.TYPE) || clazz.isAssignableFrom(Short.class)) {
            return 2;
        }
        if (clazz.isAssignableFrom(Integer.TYPE) || clazz.isAssignableFrom(Integer.class)) {
            return 3;
        }
        if (clazz.isAssignableFrom(Long.TYPE) || clazz.isAssignableFrom(Long.class)) {
            return 4;
        }
        if (clazz.isAssignableFrom(Float.TYPE) || clazz.isAssignableFrom(Float.class)) {
            return 5;
        }
        if (clazz.isAssignableFrom(Double.TYPE) || clazz.isAssignableFrom(Double.class)) {
            return 6;
        }
        if (clazz.isAssignableFrom(byte[].class) || clazz.isAssignableFrom(Byte[].class)) {
            return 7;
        }
        if (clazz.isAssignableFrom(String.class)) {
            return 8;
        }
        if (clazz.isAssignableFrom(int[].class) || clazz.isAssignableFrom(Integer[].class)) {
            return 11;
        }
        if (INBTSerializable.class.isAssignableFrom(clazz)) {
            return 10;
        }
        if (Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz)) {
            return 9;
        }
        return 10;
    }
}
