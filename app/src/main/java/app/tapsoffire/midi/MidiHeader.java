package app.tapsoffire.midi;

public class MidiHeader {

    public final int tracks;
    public final int type;
    public final float divisionType;
    public final int resolution;

    public MidiHeader(int type, float divisionType, int resolution, int tracks) {
        this.type = type;
        this.divisionType = divisionType;
        this.resolution = resolution;
        this.tracks = tracks;
    }

}
