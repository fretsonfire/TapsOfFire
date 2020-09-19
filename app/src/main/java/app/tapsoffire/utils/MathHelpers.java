package app.tapsoffire.utils;

public class MathHelpers {

    public static final float PI=3.141592653589793f;
    public static final float DEGREES_TO_RADIANS=PI/180;

    public static int roundUpPower2(int x) {
        x=x-1;
        x|=(x>>1);
        x|=(x>>2);
        x|=(x>>4);
        x|=(x>>8);
        x|=(x>>16);
        return x+1;
    }

    public static int round(float value) {
        if (value>0) {
            return (int)(value+0.5f);
        } else {
            return (int)(value-0.5f);
        }
    }

    public static int float2fixed(float value) {
        return (int)(value*65536);
    }

}
