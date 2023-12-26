package com.example.feature_test_battery.battery_moon.render.primitives;


import com.example.feature_test_battery.battery_moon.render.IMesh;
import com.example.feature_test_battery.battery_moon.render.MaterialT1;
import com.example.feature_test_battery.battery_moon.utility.SizeOf;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class QuadT1 implements IMesh {

    private static final short[] mIndices = new short[]{0, 1, 2, 0, 2, 3};

    // Create our UV coordinates.
    private static final float[] UVs = new float[]{
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    // Buffers
    private final FloatBuffer mVertexBuffer;
    private final ShortBuffer mDrawListBuffer;
    private final FloatBuffer mUVBuffer;

    private final MaterialT1 mShader;

    public QuadT1(MaterialT1 shader) {

        mShader = shader;

        // The texture buffer
        ByteBuffer ubb = ByteBuffer.allocateDirect(UVs.length * 4);
        ubb.order(ByteOrder.nativeOrder());
        mUVBuffer = ubb.asFloatBuffer();
        mUVBuffer.put(UVs);
        mUVBuffer.position(0);

        int heightPixels = 1;
        int widthPixels = 1;

        // We have to create the mVertices of our triangle.
        // Geometric variables
        float[] vertices = new float[]{
                0f, heightPixels, 0.0f,
                0f, 0f, 0.0f,
                widthPixels, 0f, 0.0f,
                widthPixels, heightPixels, 0.0f,
        };

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * SizeOf.el(vertices));
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(mIndices.length * SizeOf.el(mIndices));
        dlb.order(ByteOrder.nativeOrder());
        mDrawListBuffer = dlb.asShortBuffer();
        mDrawListBuffer.put(mIndices);
        mDrawListBuffer.position(0);
    }

    public void render(float[] m) {
        mShader.bind();
        mShader.render(m, mVertexBuffer, mDrawListBuffer, 0, mIndices.length, mUVBuffer);
    }
}
