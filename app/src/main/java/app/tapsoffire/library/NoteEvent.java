package app.tapsoffire.library;

public class NoteEvent extends Event {

    private int m_string;
    public static final int STATE_INTACT = 0;
    public static final int STATE_PICKED = 1;
    public static final int STATE_REPICKED = 2;
    public static final int STATE_UNPICKED = 3;
    public static final int STATE_PICK_ENDED = 4;
    public static final int STATE_MISSED = 5;

    public int state = STATE_INTACT;
    public int unpickPosition = 0;

    public NoteEvent(int string,float time,float endTime) {
        super(time,endTime);
        m_string=string;
    }

    public int getString() {
        return m_string;
    }

    /////////////////////////////////// runtime

    /* This really should not be here, as NoteEvent is meant
     * to represent notes in song file, and therefore be not
     * modifiable.
     *
     * I will certainly refactor this out of here.
     */

    public void makeIntact() {
        state = STATE_INTACT;
        unpickPosition = 0;
    }

    public boolean isIntact() {
        return state == STATE_INTACT;
    }

    public boolean isPicked() {
        return state == STATE_PICKED || state == STATE_REPICKED;
    }

    public void pick() {
        if (state == STATE_UNPICKED) {
            state = STATE_REPICKED;
        } else {
            state = STATE_PICKED;
        }
    }

    public boolean isRepicked() {
        return state == STATE_REPICKED;
    }

    public boolean isUnpicked() {
        return state == STATE_UNPICKED;
    }

    public int getUnpickPosition() {
        return unpickPosition;
    }

    public void unpick(int position) {
        state = STATE_UNPICKED;
        unpickPosition = position;
    }

    public boolean isPickEnded() {
        return state == STATE_PICK_ENDED;
    }

    public void endPick() {
        state = STATE_PICK_ENDED;
    }

    public boolean isMissed() {
        return state == STATE_MISSED;
    }

    public void setMissed() {
        state = STATE_MISSED;
    }

    void setString(int string) {
        m_string = string;
    }

}
