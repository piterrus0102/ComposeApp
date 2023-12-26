package com.example.feature_test_battery.battery_moon.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.example.feature_test_battery.battery_moon.render.GLRenderer;

public class GLSurface extends GLSurfaceView {

    private final GLRenderer mRenderer;

    public GLSurface(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mRenderer = new GLRenderer(context);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRenderer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRenderer.onResume();
    }
}
