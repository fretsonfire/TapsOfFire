package app.tapsoffire.utils;

import java.io.DataInput;
import java.io.IOException;

public class DataStreamHelpers {

    public static void checkTag(DataInput input, int expectedTag) throws IOException {
        int tag = input.readInt();
        if (tag != expectedTag) {
            throw new IOException(String.format(
                    "Invalid tag %08X (expecting %08X).", tag, expectedTag
            ));
        }
    }

    public static IOException inconsistentStateException() {
        return new IOException("Inconsistent state.");
    }

}
