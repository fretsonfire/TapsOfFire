package app.tapsoffire.library;

import java.io.IOException;
import java.io.InputStream;

import app.tapsoffire.midi.InvalidMidiDataException;
import app.tapsoffire.midi.MidiCallbackHelper;
import app.tapsoffire.midi.MidiReader;

public class SkillGatherer extends MidiCallbackHelper {

    private int m_skills;

    public static int gather(InputStream notesFileStream) throws InvalidSongException
    {
        SkillGatherer callback = new SkillGatherer();
        try {
            MidiReader.read(callback,notesFileStream);
            return callback.m_skills;
        } catch (InvalidMidiDataException|IOException e) {
            throw new InvalidSongException(e);
        }
    }

    public void noteOnOff(boolean on,int channel,int note,int velocity) {
        if (!on) {
            return;
        }

        int skill = NoteMap.skillOf(note);
        if (skill != -1) {
            m_skills |= skill;
        }
    }

}
