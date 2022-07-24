package app.tapsoffire.library;

public class NoteMap {

    public static final int SIZE = 20;
    private static final int GROUP_SIZE = 5;

    public static int indexOf(int note) {
        if (note >= 0x60 && note <= 0x64) {
            return GROUP_SIZE * 0 + (note - 0x60);
        } else if (note >= 0x54 && note <= 0x58) {
            return GROUP_SIZE * 1 + (note - 0x54);
        } else if (note >= 0x48 && note <= 0x4C) {
            return GROUP_SIZE * 2 + (note - 0x48);
        } else if (note >= 0x3C && note <= 0x40) {
            return GROUP_SIZE * 3 + (note - 0x3C);
        } else {
            return -1;
        }
    }

    public static int indexToSkill(int index) {
        if (index < 0 || index >= SIZE) {
            return -1;
        }

        return 1 << (index/GROUP_SIZE);
    }

    public static int indexToString(int index) {
        if (index < 0 || index >= SIZE) {
            return -1;
        }

        return (index%GROUP_SIZE);
    }

    public static int skillOf(int note) {
        return indexToSkill(indexOf(note));
    }

    public static int stringOf(int note) {
        return indexToString(indexOf(note));
    }

}