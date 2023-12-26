package com.example.feature_test_battery.battery_moon.utility;

public class SizeOf {

    public static int type(Class dataType) {
        if (dataType == null)
            throw new NullPointerException();

        if (dataType == byte.class || dataType == Byte.class) return 1;
        if (dataType == short.class || dataType == Short.class) return 2;
        if (dataType == char.class || dataType == Character.class) return 2;
        if (dataType == int.class || dataType == Integer.class) return 4;
        if (dataType == float.class || dataType == Float.class) return 4;
        if (dataType == long.class || dataType == Long.class) return 8;
        if (dataType == double.class || dataType == Double.class) return 8;

        return 4; // 32-bit memory pointer...
        // (I'm not sure how this works on a 64-bit OS)
    }

    public static <T> int el(T array) {
        return SizeOf.type(array.getClass().getComponentType());
    }
}
