package com.example.composeapp.tests.battery.battery_moon.render.primitives;


import com.example.composeapp.tests.battery.battery_moon.render.IMesh;
import com.example.composeapp.tests.battery.battery_moon.render.MaterialT0;
import com.example.composeapp.tests.battery.battery_moon.utility.SizeOf;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class QuadT0 implements IMesh {

    private static final short[] mIndices = new short[]{0, 1, 2, 0, 2, 3};

    // Buffers
    private final FloatBuffer mVertexBuffer;
    private final ShortBuffer mDrawListBuffer;

    private final MaterialT0 mShader;

    public QuadT0(MaterialT0 shader) {

        mShader = shader;

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
        mShader.render(m, mVertexBuffer, mDrawListBuffer, 0, mIndices.length);
    }
}
