package com.example.composeapp.tests.battery.battery_moon.math;
/*
   Copyright 2010 Nick howes

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

/**
 * Quaternions are data structures built from unicorn horns.
 * <p>
 * I nabbed this implementation from The Internet.
 */
public final class Quaternion {
    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion(final Quaternion q) {
        this(q.x, q.y, q.z, q.w);
    }

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void set(final Quaternion q) {
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
        this.w = q.w;
    }

    public Quaternion(Vector3 axis, float angle) {
        set(axis, angle);
    }

    public float norm() {
        return (float) Math.sqrt(dot(this));
    }

    public float getW() {
        return w;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    /**
     * @param axis  rotation axis, unit vector
     * @param angle the rotation angle
     * @return this
     */
    public Quaternion set(Vector3 axis, float angle) {
        float s = (float) Math.sin(angle / 2);
        w = (float) Math.cos(angle / 2);
        x = axis.getX() * s;
        y = axis.getY() * s;
        z = axis.getZ() * s;
        return this;
    }

    public Quaternion mulThis(Quaternion q) {
        float nw = w * q.w - x * q.x - y * q.y - z * q.z;
        float nx = w * q.x + x * q.w + y * q.z - z * q.y;
        float ny = w * q.y + y * q.w + z * q.x - x * q.z;
        z = w * q.z + z * q.w + x * q.y - y * q.x;
        w = nw;
        x = nx;
        y = ny;
        return this;
    }

    public Quaternion scaleThis(float scale) {
        if (scale != 1) {
            w *= scale;
            x *= scale;
            y *= scale;
            z *= scale;
        }
        return this;
    }

    public Quaternion divThis(float scale) {
        if (scale != 1) {
            w /= scale;
            x /= scale;
            y /= scale;
            z /= scale;
        }
        return this;
    }

    public float dot(Quaternion q) {
        return x * q.x + y * q.y + z * q.z + w * q.w;
    }

    public boolean equals(Quaternion q) {
        return x == q.x && y == q.y && z == q.z && w == q.w;
    }

    public Quaternion interpolateThis(Quaternion q, float t) {
        if (!equals(q)) {
            float d = dot(q);
            float qx, qy, qz, qw;

            if (d < 0f) {
                qx = -q.x;
                qy = -q.y;
                qz = -q.z;
                qw = -q.w;
                d = -d;
            } else {
                qx = q.x;
                qy = q.y;
                qz = q.z;
                qw = q.w;
            }

            float f0, f1;

            if ((1 - d) > 0.1f) {
                float angle = (float) Math.acos(d);
                float s = (float) Math.sin(angle);
                float tAngle = t * angle;
                f0 = (float) Math.sin(angle - tAngle) / s;
                f1 = (float) Math.sin(tAngle) / s;
            } else {
                f0 = 1 - t;
                f1 = t;
            }

            x = f0 * x + f1 * qx;
            y = f0 * y + f1 * qy;
            z = f0 * z + f1 * qz;
            w = f0 * w + f1 * qw;
        }

        return this;
    }

    public Quaternion normalizeThis() {
        return divThis(norm());
    }

    public Quaternion interpolate(Quaternion q, float t) {
        return new Quaternion(this).interpolateThis(q, t);
    }

    /**
     * Converts this Quaternion into a matrix, returning it as a float array.
     */
    public float[] toMatrix() {
        float[] matrix = new float[16];
        toMatrix(matrix);
        return matrix;
    }

    /**
     * Converts this Quaternion into a matrix, placing the values into the given array.
     *
     * @param matrix 16-length float array.
     */
    public void toMatrix(float[] matrix) {
        matrix[3] = 0.0f;
        matrix[7] = 0.0f;
        matrix[11] = 0.0f;
        matrix[12] = 0.0f;
        matrix[13] = 0.0f;
        matrix[14] = 0.0f;
        matrix[15] = 1.0f;

        matrix[0] = 1.0f - (2.0f * ((y * y) + (z * z)));
        matrix[1] = 2.0f * ((x * y) - (z * w));
        matrix[2] = 2.0f * ((x * z) + (y * w));

        matrix[4] = 2.0f * ((x * y) + (z * w));
        matrix[5] = 1.0f - (2.0f * ((x * x) + (z * z)));
        matrix[6] = 2.0f * ((y * z) - (x * w));

        matrix[8] = 2.0f * ((x * z) - (y * w));
        matrix[9] = 2.0f * ((y * z) + (x * w));
        matrix[10] = 1.0f - (2.0f * ((x * x) + (y * y)));
    }

}
