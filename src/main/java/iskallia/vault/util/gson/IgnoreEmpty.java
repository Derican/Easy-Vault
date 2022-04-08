package iskallia.vault.util.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class IgnoreEmpty {
    public static class IntegerAdapter
            extends TypeAdapter<Integer> {
        public void write(JsonWriter out, Integer value) throws IOException {
            if (value == null || value.intValue() == 0) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }


        public Integer read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return Integer.valueOf(0);
            }

            return Integer.valueOf(in.nextInt());
        }
    }

    public static class DoubleAdapter
            extends TypeAdapter<Double> {
        public void write(JsonWriter out, Double value) throws IOException {
            if (value == null || value.doubleValue() == 0.0D) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }


        public Double read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return Double.valueOf(0.0D);
            }

            return Double.valueOf(in.nextDouble());
        }
    }

    public static class StringAdapter
            extends TypeAdapter<String> {
        public void write(JsonWriter out, String value) throws IOException {
            if (value == null || value.isEmpty()) {
                out.nullValue();
            } else {
                out.value(value);
            }
        }


        public String read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return "";
            }

            return in.nextString();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\gson\IgnoreEmpty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */