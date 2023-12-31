package com.example.feature_test_battery.battery_moon.math;
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

public final class Vector3 {
    private float x, y, z;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Vector3(float ix, float iy, float iz) {
        x = ix;
        y = iy;
        z = iz;
    }

    public void set(float ix, float iy, float iz) {
        x = ix;
        y = iy;
        z = iz;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void multiply(float f) {
        x *= f;
        y *= f;
        z *= f;
    }

    public void normalise() {
        double mag = magnitude();
        x /= mag;
        y /= mag;
        z /= mag;
    }
}
