package app.tapsoffire.library;

public abstract class EventList<E extends Event> {

    public abstract int count();
    public abstract E get(int index);

    public abstract int lowerBound(float time);
    public abstract int upperBound(float time);
    public abstract long range(float time,float endTime);

    public static int rangeBegin(long range) {
        return (int)(range >>> 32);
    }

    public static int rangeEnd(long range) {
        return (int)(range);
    }

    public static boolean rangeEmpty(long range) {
        return rangeBegin(range) == rangeEnd(range);
    }

    public static long rangeMake(int begin,int end) {
        return ((long)begin << 32) | end;
    }
}
