package iskallia.vault.client.util;

import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import iskallia.vault.Vault;

import java.util.HashMap;

import org.lwjgl.opengl.ARBShaderObjects;

import javax.annotation.Nullable;
import java.util.Map;

public class ShaderUtil {
    private static final String PREFIX = "/assets/the_vault/shader/";
    public static int GRAYSCALE_SHADER;
    public static int COLORIZE_SHADER;
    private static final Map<Integer, Map<String, Integer>> UNIFORM_CONSTANTS;

    public static void initShaders() {
        ShaderUtil.GRAYSCALE_SHADER = createProgram("grayscale.vert", "grayscale.frag");
        ShaderUtil.COLORIZE_SHADER = createProgram("colorize.vert", "colorize.frag");
    }

    public static void useShader(final int shader) {
        useShader(shader, null);
    }

    public static void useShader(final int shader, @Nullable final Runnable setter) {
        ARBShaderObjects.glUseProgramObjectARB(shader);
        if (shader != 0) {
            ARBShaderObjects.glUniform1iARB(getUniformLocation(shader, "texture_0"), 0);
            if (setter != null) {
                setter.run();
            }
        }
    }

    public static int getUniformLocation(final int shaderProgram, final String uniform) {
        final Map<String, Integer> uniforms = ShaderUtil.UNIFORM_CONSTANTS.computeIfAbsent(Integer.valueOf(shaderProgram), program -> new HashMap());
        return uniforms.computeIfAbsent(uniform, uniformKey -> ARBShaderObjects.glGetUniformLocationARB(shaderProgram, (CharSequence) uniformKey));
    }

    public static void releaseShader() {
        useShader(0, null);
    }

    private static int createProgram(@Nullable final String vert, @Nullable final String frag) {
        int vertId = 0;
        int fragId = 0;
        if (vert != null) {
            vertId = createShader("/assets/the_vault/shader/" + vert, 35633);
        }
        if (frag != null) {
            fragId = createShader("/assets/the_vault/shader/" + frag, 35632);
        }
        final int program = ARBShaderObjects.glCreateProgramObjectARB();
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

    private static int createShader(final String filename, final int shaderType) {
        int shader = 0;
        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
            if (shader == 0) {
                return 0;
            }
            ARBShaderObjects.glShaderSourceARB(shader, (CharSequence) readFile(filename));
            ARBShaderObjects.glCompileShaderARB(shader);
            if (ARBShaderObjects.glGetObjectParameteriARB(shader, 35713) == 0) {
                throw new RuntimeException("Error creating shader \"" + filename + "\": " + getLogInfo(shader));
            }
            return shader;
        } catch (final Exception exc) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            exc.printStackTrace();
            return -1;
        }
    }

    private static String getLogInfo(final int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, 35716));
    }

    private static String readFile(final String filename) throws Exception {
        final InputStream in = ShaderUtil.class.getResourceAsStream(filename);
        if (in == null) {
            return "";
        }
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            final StringBuilder result = new StringBuilder();
            while (true) {
                final String line = reader.readLine();
                if (line == null) {
                    break;
                }
                result.append(line).append('\n');
            }
            return result.toString();
        }
    }

    static {
        ShaderUtil.GRAYSCALE_SHADER = 0;
        ShaderUtil.COLORIZE_SHADER = 0;
        UNIFORM_CONSTANTS = new HashMap<Integer, Map<String, Integer>>();
    }
}
