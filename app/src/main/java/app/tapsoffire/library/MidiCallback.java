package app.tapsoffire.library;

import java.util.Arrays;

import app.tapsoffire.midi.InvalidMidiDataException;
import app.tapsoffire.midi.MidiCallbackHelper;
import app.tapsoffire.midi.MidiHeader;

public class MidiCallback extends MidiCallbackHelper {

    // Data
    private Song m_song;

    private int m_ticksPerBeat;
    private int m_currentTicks;
    private int m_currentTrack;

    private float m_lastBPM;
    private int m_lastBPMTicks;
    private float m_lastBPMMillis;

    private int[] m_tempos;
    private int m_tempoSyncIndex;
    private int m_tempoCount;

    private float[] m_noteStartTimes;

    private static final int MAX_CHANNEL = 15;

    public MidiCallback(Song song) {
        m_song = song;
        m_noteStartTimes = new float[(MAX_CHANNEL + 1) * NoteMap.SIZE];
        Arrays.fill(m_noteStartTimes,-1);
        m_tempos = new int[2*10];
        m_tempoSyncIndex = 0;
        m_tempoCount = 0;
    }

    public void onStart(MidiHeader header) {
        m_ticksPerBeat = header.resolution;
    }

    public void onTrackStart(int track) {
        m_currentTrack = track;
        m_currentTicks = 0;
        m_lastBPM = 0;
        m_lastBPMTicks = 0;
        m_lastBPMMillis = 0;
        m_tempoSyncIndex = 0;
    }

    public void onEventDeltaTime(int deltaTime) {
        m_currentTicks += deltaTime;
        syncTempo();
    }

    public void noteOnOff(boolean on,int channel,int note,int velocity) throws InvalidMidiDataException
    {
        if (m_currentTrack>1) {
            return;
        }
        int index = NoteMap.indexOf(note);
        if (index == -1) {
            return;
        }

        index += (channel + 1) * NoteMap.SIZE;
        if (on) {
            m_noteStartTimes[index] = getCurrentMillis();
        } else {
            float startMillis = m_noteStartTimes[index];
            {
                if (startMillis==-1) {
                    return;
                }

                m_noteStartTimes[index]=-1;
            }

            m_song.addNoteEvent(note,startMillis,getCurrentMillis());
        }
    }

    public void tempo(int value) throws InvalidMidiDataException
    {
        addTempo(m_currentTicks,value);
        applyTempo(m_currentTicks,value);
        m_song.addTempoEvent(new TempoEvent(m_lastBPM,m_lastBPMMillis));
    }

    private void syncTempo() {
        for (; m_tempoSyncIndex != m_tempoCount; ++m_tempoSyncIndex) {
            int tempoTicks = m_tempos[m_tempoSyncIndex * 2];
            int tempoValue = m_tempos[m_tempoSyncIndex * 2 + 1];
            if (tempoTicks > m_currentTicks) {
                break;
            }

            applyTempo(tempoTicks,tempoValue);
        }
    }

    private void applyTempo(int tempoTicks, int tempoValue) {
        float bpm = 60000000f/tempoValue;
        float currentMillis = m_lastBPMMillis +
                ticksToMillis(tempoTicks - m_lastBPMTicks,m_lastBPM);
        m_lastBPMMillis = currentMillis;
        m_lastBPM = bpm;
        m_lastBPMTicks = tempoTicks;
    }

    private void addTempo(int tempoTicks, int tempoValue) throws InvalidMidiDataException
    {
        if (m_tempoSyncIndex != m_tempoCount) {
            throw new InvalidMidiDataException(String.format(
                    "Unexpected tempo event at [%d]; "+
                            "current tempo sync index is %d [%d] (out of %d).",
                    tempoTicks,
                    m_tempoSyncIndex,m_tempos[m_tempoSyncIndex * 2],
                    m_tempoCount));
        }

        int index = m_tempoCount * 2;
        if (index == m_tempos.length) {
            int[] copy = new int[index * 2];
            System.arraycopy(m_tempos,0,copy,0,m_tempos.length);
            m_tempos = copy;
        }

        m_tempoCount += 1;
        m_tempoSyncIndex += 1;
        m_tempos[index] = tempoTicks;
        m_tempos[index + 1] = tempoValue;
    }

    private float getCurrentMillis() {
        return m_lastBPMMillis +
                ticksToMillis(m_currentTicks-m_lastBPMTicks,m_lastBPM);
    }

    private float ticksToMillis(int ticks, float bpm) {
        if (bpm == 0 || m_ticksPerBeat == 0) {
            return 0;
        }

        return (60000f * ticks)/(bpm * m_ticksPerBeat);
    }

}
