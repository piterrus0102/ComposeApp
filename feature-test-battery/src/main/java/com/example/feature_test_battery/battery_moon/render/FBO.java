package com.example.feature_test_battery.battery_moon.render;

import android.opengl.GLES20;

public class FBO {

    private int fboId;
    private int fboTex;
    private int renderBufferId;

    public static FBO create(int fboWidth, int fboHeight) {
        FBO fbo = new FBO();
        int[] temp = new int[1];
        //generate fbo id
        GLES20.glGenFramebuffers(1, temp, 0);
        fbo.fboId = temp[0];
        //generate texture
        GLES20.glGenTextures(1, temp, 0);
        fbo.fboTex = temp[0];
        //generate render buffer
        GLES20.glGenRenderbuffers(1, temp, 0);
        fbo.renderBufferId = temp[0];
        //Bind Frame buffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fbo.fboId);
        //Bind texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fbo.fboTex);
        //Define texture parameters
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, fboWidth, fboHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        //Bind render buffer and define buffer dimension
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, fbo.renderBufferId);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, fboWidth, fboHeight);
        //Attach texture FBO color attachment
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, fbo.fboTex, 0);
        //Attach render buffer to depth attachment
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, fbo.renderBufferId);
        //we are done, reset
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        return fbo;
    }

    public void bindRT() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
    }

    public void bind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTex);
    }
}
