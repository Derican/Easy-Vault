varying vec2 v_texCoord;
attribute vec4 gl_MultiTexCoord0;

void main()
{
    v_texCoord = vec2(gl_MultiTexCoord0);

    gl_Position = ftransform();
}