package com.example.feature_test_battery.battery_moon.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import com.example.feature_test_battery.battery_moon.materials.MaterialMoon;
import com.example.feature_test_battery.battery_moon.render.primitives.QuadT0;
import com.example.feature_test_battery.battery_moon.scene.Object;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements Renderer {

    // Matrices
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Misc
    private final Context mContext;

    private int mScreenHeight;
    private int mScreenWidth;

    private Object mMesh;
    private MaterialMoon mShader;

    public GLRenderer(Context c) {
        mContext = c;
    }

    public void onPause() {
        /* Do stuff to pause the renderer */
    }

    public void onResume() {
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Render our example
        Render(mtrxProjectionAndView);
    }

    private void Render(float[] m) {

        GLES20.glViewport(0, 0, mScreenWidth, mScreenHeight);

        // clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        mMesh.render(m);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        if (0 == width || 0 == height)
            return;

        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        mShader.setScreenSize(width, height);

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, width, height);

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, 1f, 0.0f, 1f, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        mShader = new MaterialMoon(mContext.getResources(), mScreenWidth, mScreenHeight);
        mMesh = new Object(new QuadT0(mShader));
    }

}
