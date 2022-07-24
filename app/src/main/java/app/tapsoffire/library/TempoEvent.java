package app.tapsoffire.library;

public class TempoEvent extends Event {
    private float m_bpm;

    public TempoEvent(float bpm, float time) {
        super(time,time);
        m_bpm = bpm;
    }

    public float getBPM() {
        return m_bpm;
    }
}

