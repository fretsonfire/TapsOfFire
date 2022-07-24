package app.tapsoffire.midi;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MidiDataInputStream extends DataInputStream {

    public MidiDataInputStream(InputStream is) {
        super(is);
    }

    public int readVariableLengthInt() throws IOException {
        int result = readByte();
        if ((result & 0x80) == 0) {
            return result;
        }

        result &= 0x7F;
        while (true) {
            int b = readByte();
            result = (result << 7) + (b & 0x7F);
            if ((b & 0x80) == 0) {
                break;
            }
        }

        return result;
    }

}
