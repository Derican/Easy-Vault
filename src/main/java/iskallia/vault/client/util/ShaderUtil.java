package iskallia.vault.client.util;

import iskallia.vault.Vault;
import org.lwjgl.opengl.ARBShaderObjects;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class ShaderUtil {
    private static final String PREFIX = "/assets/the_vault/shader/";
    public static int GRAYSCALE_SHADER = 0;

    private static final Map<Integer, Map<String, Integer>> UNIFORM_CONSTANTS = new HashMap<>();

    public static void initShaders() {
        GRAYSCALE_SHADER = createProgram("grayscale.vert", "grayscale.frag");
    }

    public static void useShader(int shader) {
        useShader(shader, null);
    }

    public static void useShader(int shader, @Nullable Runnable setter) {
        ARBShaderObjects.glUseProgramObjectARB(shader);
        if (shader != 0) {
            ARBShaderObjects.glUniform1iARB(getUniformLocation(shader, "texture_0"), 0);
            if (setter != null) {
                setter.run();
            }
        }
    }

    public static int getUniformLocation(int shaderProgram, String uniform) {
        Map<String, Integer> uniforms = UNIFORM_CONSTANTS.computeIfAbsent(Integer.valueOf(shaderProgram), program -> new HashMap<>());
        return ((Integer) uniforms.computeIfAbsent(uniform, uniformKey -> Integer.valueOf(ARBShaderObjects.glGetUniformLocationARB(shaderProgram, uniformKey)))).intValue();
    }

    public static void releaseShader() {
        useShader(0, null);
    }

    private static int createProgram(@Nullable String vert, @Nullable String frag) {
        int vertId = 0, fragId = 0;
        if (vert != null) {
            vertId = createShader("/assets/the_vault/shader/" + vert, 35633);
        }
        if (frag != null) {
            fragId = createShader("/assets/the_vault/shader/" + frag, 35632);
        }

        int program = ARBShaderObjects.glCreateProgramObjectARB();
        if (program == 0) {
            return 0;
        }

        if (vert != null) {
            ARBShaderObjects.glAttachObjectARB(program, vertId);
        }
        if (frag != null) {
            ARBShaderObjects.glAttachObjectARB(program, fragId);
        }

        ARBShaderObjects.glLinkProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, 35714) == 0) {
            Vault.LOGGER.error(getLogInfo(program));
            return 0;
        }

        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, 35715) == 0) {
            Vault.LOGGER.error(getLogInfo(program));
            return 0;
        }

        return program;
    }

    private static int createShader(String filename, int shaderType) {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
            if (shader == 0) {
                return 0;
            }

            ARBShaderObjects.glShaderSourceARB(shader, readFile(filename));
            ARBShaderObjects.glCompileShaderARB(shader);

            if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
                throw new RuntimeException("Error creating shader \"" + filename + "\": " + getLogInfo(shader));
            }

            return shader;
        } catch (Exception exc) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            exc.printStackTrace();
            return -1;
        }
    }

    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, 35716));
    }

    private static String readFile(String filename) throws Exception {
        InputStream in = ShaderUtil.class.getResourceAsStream(filename);
        if (in == null) {
            return "";
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            StringBuilder result = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                result.append(line).append('\n');
            }
            return result.toString();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\clien\\util\ShaderUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */