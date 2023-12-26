package com.example.feature_test_battery.battery_moon.render;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MaterialT1 extends Shader {

    private Texture mTexture;
    // handle to vertex shader's vPosition member
    private int positionAttrib;
    // handle to texture coordinates location
    private int texCoordAttrib;
    // handle to shape's transformation matrix
    private int mtxUni;
    // handle to textures locations
    private int samplerUni;

    public static final String default_vertex =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_texCoord;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";

    public static final String default_fragment =
            "precision highp float;" +
                    "varying vec2 v_texCoord;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "  vec4 tex = texture2D( s_texture, v_texCoord );" +
                    "  gl_FragColor = vec4(tex);" +
                    "}";

    public MaterialT1(Texture texture) {
        super(create(default_vertex, default_fragment));
        init(texture);
    }

    public MaterialT1(Texture texture, String fragment) {
        super(create(default_vertex, fragment));
        init(texture);
    }

    private void init(Texture texture) {
        mTexture = texture;

        positionAttrib = getAttribLocation("vPosition");
        texCoordAttrib = getAttribLocation("a_texCoord");

        samplerUni = getUniformLocation("s_texture");
        mtxUni = getUniformLocation("uMVPMatrix");
    }

    @Override
    public void bind() {
        super.bind();
        mTexture.bind(0);
        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(samplerUni, 0);
    }

    public void render(float[] m, FloatBuffer vertexBuffer, ShortBuffer indexBuffer, int from, int count, FloatBuffer uvs) {

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

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(texCoordAttrib);

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer(
                texCoordAttrib,
                2,
                GLES20.GL_FLOAT,
                false,
                0, uvs);

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
        GLES20.glDisableVertexAttribArray(texCoordAttrib);
    }

}
