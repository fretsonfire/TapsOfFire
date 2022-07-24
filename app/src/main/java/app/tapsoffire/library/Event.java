package app.tapsoffire.library;

public class Event {

    private float m_time;
    private float m_endTime;

    public Event(float time,float endTime) {
        m_time = time;
        m_endTime = endTime;
    }

    public final float getTime() {
        return m_time;
    }

    public final float getLength() {
        return m_endTime - m_time;
    }

    public final float getEndTime() {
        return m_endTime;
    }

}
