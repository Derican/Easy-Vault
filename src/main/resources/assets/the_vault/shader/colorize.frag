varying vec2 v_texCoord;
uniform sampler2D texture_0;
uniform float colorR;
uniform float colorG;
uniform float colorB;
uniform float brightness;
uniform float grayscaleFactor;

vec4 toGrayscale(in vec4 color, in float factor)
{
    float grey = 0.2 * color.r + 0.7 * color.g + 0.07 * color.b;
    float r = color.r * (1.0 - factor) + grey * factor;
    float g = color.g * (1.0 - factor) + grey * factor;
    float b = color.b * (1.0 - factor) + grey * factor;
    return vec4(r, g, b, color.a);
}

vec4 colorize(in vec4 grayscale, in vec4 color)
{
    return (grayscale * color);
}

void main()
{
    vec4 color = vec4(colorR, colorG, colorB, 1.0);
    vec4 inputColor = texture2D(texture_0, v_texCoord);
    vec4 grayscale = toGrayscale(inputColor, grayscaleFactor);
    vec4 brightnessVec = vec4(brightness, brightness, brightness, 1.0);
    vec4 colorizedOutput = colorize(grayscale, color);

    gl_FragColor = colorizedOutput * brightnessVec;
}
