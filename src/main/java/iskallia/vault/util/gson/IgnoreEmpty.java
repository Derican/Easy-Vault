// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class IgnoreEmpty
{
    public static class IntegerAdapter extends TypeAdapter<Integer>
    {
        public void write(final JsonWriter out, final Integer value) throws IOException {
            if (value == null || value == 0) {
                out.nullValue();
            }
            else {
                out.value((Number)value);
            }
        }
        
        public Integer read(final JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0;
            }
            return in.nextInt();
        }
    }
    
    public static class DoubleAdapter extends TypeAdapter<Double>
    {
        public void write(final JsonWriter out, final Double value) throws IOException {
            if (value == null || value == 0.0) {
                out.nullValue();
            }
            else {
                out.value((Number)value);
            }
        }
        
        public Double read(final JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return 0.0;
            }
            return in.nextDouble();
        }
    }
    
    public static class StringAdapter extends TypeAdapter<String>
    {
        public void write(final JsonWriter out, final String value) throws IOException {
            if (value == null || value.isEmpty()) {
                out.nullValue();
            }
            else {
                out.value(value);
            }
        }
        
        public String read(final JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return "";
            }
            return in.nextString();
        }
    }
}
