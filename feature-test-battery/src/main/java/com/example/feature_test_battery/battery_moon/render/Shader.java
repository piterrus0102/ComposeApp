package com.example.feature_test_battery.battery_moon.render;

import android.opengl.GLES20;

public class Shader {

    private ShaderHolder mShader;

    private static class ShaderHolder {
        private final int shader;
        private final int[] attached;

        public ShaderHolder(int shader, int[] attached) {
            this.shader = shader;
            this.attached = attached;
        }
    }

    public Shader(ShaderHolder shader) {
        mShader = shader;
    }

    public static ShaderHolder create(String vertex, String fragment) {

        // Create the shaders
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertex);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragment);

        int shader = GLES20.glCreateProgram();         // create empty OpenGL ES Program
        GLES20.glAttachShader(shader, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(shader, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(shader);                  // creates OpenGL ES program executables
        return new ShaderHolder(shader, new int[]{vertexShader, fragmentShader});
    }

    private static int loadShader(int type, String shaderCode) throws RuntimeException {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            GLES20.glDeleteShader(shader);
            String log = GLES20.glGetShaderInfoLog(shader);
            throw new RuntimeException("Could not compile program: " + log + " | " + shaderCode);
        }
        // return the shader
        return shader;
    }

    public void bind() {
        GLES20.glUseProgram(mShader.shader);
    }

    public int getUniformLocation(String uniform) {
        return GLES20.glGetUniformLocation(mShader.shader, uniform);
    }

    public int getAttribLocation(String attribute) {
        return GLES20.glGetAttribLocation(mShader.shader, attribute);
    }

    public void dispose() {
        if (null == mShader)
            return;
        GLES20.glDeleteShader(mShader.shader);
        for (int shader : mShader.attached) {
            GLES20.glDeleteShader(shader);
        }
        mShader = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

}
