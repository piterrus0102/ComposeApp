package com.example.composeapp.tests.battery.battery_moon.materials;

import android.content.res.Resources;
import android.opengl.GLES20;

import com.example.composeapp.R;
import com.example.composeapp.tests.battery.battery_moon.render.MaterialT0;
import com.example.composeapp.tests.battery.battery_moon.utility.IO;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MaterialMoon extends MaterialT0 {

    private static final int sMAX_TIMESTEP = 1000 / 20;

    private final int timeUni;
    private final int resolutionUni;
    private float time;
    private long lastTick;
    private int screenHeight;
    private int screenWidth;

    public MaterialMoon(Resources res, int screenWidth, int screenHeight) {
        super(IO.readRawText(res, R.raw.moon));
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        timeUni = getUniformLocation("u_time");
        resolutionUni = getUniformLocation("iResolution");
        lastTick = System.currentTimeMillis();
    }

    public void render(float[] m, FloatBuffer vertexBuffer, ShortBuffer indexBuffer, int from, int count) {
        long delta = System.currentTimeMillis() - lastTick;
        if (delta > sMAX_TIMESTEP)
            delta = sMAX_TIMESTEP;
        lastTick = System.currentTimeMillis();
        time += delta * 0.001f;
        GLES20.glUniform2f(resolutionUni, screenWidth, screenHeight);
        GLES20.glUniform1f(timeUni, time);
        super.render(m, vertexBuffer, indexBuffer, from, count);
    }

    public void setScreenSize(int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }
}
