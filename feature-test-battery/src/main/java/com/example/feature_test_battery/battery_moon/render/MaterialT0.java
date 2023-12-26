package com.example.feature_test_battery.battery_moon.render;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MaterialT0 extends Shader {

    // handle to vertex shader's vPosition member
    private int positionAttrib;
    // handle to shape's transformation matrix
    private int mtxUni;

    public static final String default_vertex =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    public MaterialT0(String fragment) {
        super(create(default_vertex, fragment));
        init();
    }

    private void init() {
        positionAttrib = getAttribLocation("vPosition");
        mtxUni = getUniformLocation("uMVPMatrix");
    }

    @Override
    public void bind() {
        super.bind();
    }

    public void render(float[] m, FloatBuffer vertexBuffer, ShortBuffer indexBuffer, int from, int count) {

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(positionAttrib);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                positionAttrib,
                3,
                GLES20.GL_FLOAT,
                false,
                0,
                vertexBuffer);

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtxUni, 1, false, m, 0);

        // Draw the triangle
        ShortBuffer shortBuffer = indexBuffer.asReadOnlyBuffer();
        shortBuffer.position(from);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,
                count,
                GLES20.GL_UNSIGNED_SHORT,
                shortBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionAttrib);
    }
}
