package com.example.feature_test_battery.battery_moon.scene;

import android.opengl.Matrix;


import com.example.feature_test_battery.battery_moon.math.Quaternion;
import com.example.feature_test_battery.battery_moon.math.Vector3;
import com.example.feature_test_battery.battery_moon.render.IMesh;


public class Object {

    private Vector3 mPos;

    private Quaternion mRotate;

    private Vector3 mScale;

    // matrices
    private final float[] mModel = new float[16];
    private float[] mWorking = new float[16];

    private final IMesh mMesh;

    public Vector3 getPos() {
        return mPos;
    }

    public void setPos(Vector3 mPos) {
        this.mPos = mPos;
    }

    public Quaternion getRotate() {
        return mRotate;
    }

    public void setRotate(Quaternion mRotate) {
        this.mRotate = mRotate;
    }

    public Vector3 getScale() {
        return mScale;
    }

    public void setScale(Vector3 mScale) {
        this.mScale = mScale;
    }

    public Object(IMesh mesh) {
        mMesh = mesh;
    }

    public void render(float[] m) {

        Matrix.setIdentityM(mWorking, 0);

        // scale
        if (null != mScale) {
            Matrix.scaleM(mWorking, 0, mScale.getX(), mScale.getY(), mScale.getZ());
        }

        // rotate
        if (null != mRotate) {
            Matrix.multiplyMM(mModel, 0, mWorking, 0, mRotate.toMatrix(), 0);
            mWorking = mModel; // reuse matrix
        }

        if (null != mPos) {
            Matrix.translateM(mWorking, 0, mPos.getX(), mPos.getY(), mPos.getZ());
        }

        Matrix.multiplyMM(mModel, 0, mWorking, 0, m, 0);
        mMesh.render(mModel);
    }
}
