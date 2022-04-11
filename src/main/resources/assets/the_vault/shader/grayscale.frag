varying vec2 v_texCoord;
uniform sampler2D texture_0;
uniform float grayFactor;
uniform float brightness;

void main()
{
	vec4 color = texture2D(texture_0, v_texCoord);
	float grey = 0.2 * color.r + 0.7 * color.g + 0.07 * color.b;
	color = vec4(color.r * grayFactor + grey * (1.0 - grayFactor), color.g * grayFactor + grey * (1.0 - grayFactor), color.b * grayFactor + grey * (1.0 - grayFactor), color.w);

	vec4 brVector = vec4(brightness, brightness, brightness, 1);
	color = color * brVector;

	gl_FragColor = color;
}