package com.example.feature_test_battery.battery_moon.render;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {

    private int mTexture;

    public Texture(int texture) {
        mTexture = texture;
    }

    public static Texture create(Resources resources, int drawable) {
        return new Texture(loadTexture(resources, drawable));
    }

    public void bind(int slot) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + slot);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture);
    }

    public void dispose() {
        if (0 == mTexture)
            return;
        GLES20.glDeleteTextures(1, new int[]{mTexture}, 0);
        mTexture = 0;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dispose();
    }

    private static int loadTexture(Resources resources, int drawable) {

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(resources, drawable);

        // Generate Texture, if more needed, alter these numbers.
        int[] texturenames = new int[1];
        GLES20.glGenTextures(1, texturenames, 0);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();
        return texturenames[0];
    }

}
