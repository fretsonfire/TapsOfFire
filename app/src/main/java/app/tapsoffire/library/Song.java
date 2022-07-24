package app.tapsoffire.library;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import app.tapsoffire.midi.InvalidMidiDataException;
import app.tapsoffire.midi.MidiCallbackHelper;
import app.tapsoffire.midi.MidiHeader;
import app.tapsoffire.midi.MidiReader;

public class Song {

    // Constants
    public static final int INVALID_SKILL = 0;
    public static final int SKILL_AMAZING = 0x1;
    public static final int SKILL_MEDIUM = 0x2;
    public static final int SKILL_EASY	= 0x4;
    public static final int SKILL_SUPAEASY = 0x8;

    public static final int SKILL_COUNT	= 4;
    public static final int ALL_SKILLS	= (1 << SKILL_COUNT) - 1;

    public static final int STRING_COUNT = 3;

    // Data
    private SongConfig m_config;

    private int m_selectedSkillIndex;
    private EventListBuilder<TempoEvent> m_tempoEvents;
    private EventListBuilder<NoteEvent>[][] m_noteEvents;

    private static final int ACTUAL_STRING_COUNT = 5;
    private static final float MERGE_MARGIN = 10f;

    @SuppressWarnings("unchecked")
    public Song(SongConfig config) throws InvalidSongException {
        m_config = config;
        m_tempoEvents = new EventListBuilder<TempoEvent>();
        m_noteEvents = (EventListBuilder<NoteEvent>[][])
                new EventListBuilder[SKILL_COUNT][];

        for (int i = 0; i != m_noteEvents.length; ++i) {
            EventListBuilder<NoteEvent>[] events =
                    (EventListBuilder<NoteEvent>[])
                            new EventListBuilder[ACTUAL_STRING_COUNT];

            for (int j = 0; j != ACTUAL_STRING_COUNT; ++j) {
                events[j] = new EventListBuilder<NoteEvent>();
            }

            m_noteEvents[i] = events;
        }

        m_selectedSkillIndex = -1;

        try {
            MidiReader.read(
                    new MidiCallback(this),
                    new FileInputStream(config.getNotesFile()));
        }
        catch (InvalidMidiDataException|IOException e) {
            throw new InvalidSongException(e);
        }

        mergeNoteEvents();
    }

    public SongConfig getConfig() {
        return m_config;
    }

    public SongIni getIni() {
        return m_config.getIni();
    }

    public int getSelectedSkill() {
        return indexToSkill(m_selectedSkillIndex);
    }

    public boolean selectSkill(int skill) {
        if ((skill & getIni().getSkills()) == 0) {
            return false;
        }

        m_selectedSkillIndex = skillToIndex(skill);
        return (m_selectedSkillIndex != -1);
    }

    public void selectAnySkill() {
        if (selectSkill(Song.SKILL_SUPAEASY) ||
                selectSkill(Song.SKILL_EASY) ||
                selectSkill(Song.SKILL_MEDIUM) ||
                selectSkill(Song.SKILL_AMAZING));
    }

    public EventList<TempoEvent> getTempoEvents() {
        return m_tempoEvents;
    }

    public void glueNoteEvents(float minDistance) {
        checkSkillSelected();
        EventListBuilder<NoteEvent> events;
        for (int string = 0; string != Song.STRING_COUNT; ++string) {
            events = m_noteEvents[m_selectedSkillIndex][string];
            int eventsRemoved = 0;
            for (int i = 0;;) {
                if (i > events.count() - 2) {
                    break;
                }

                NoteEvent event = events.get(i);
                NoteEvent nextEvent = events.get(i + 1);
                float distance = (nextEvent.getTime() - event.getTime());
                if (distance > minDistance) {
                    ++i;
                    continue;
                }

                NoteEvent newEvent = new NoteEvent(
                        string,
                        event.getTime(),
                        nextEvent.getTime());
                events.remove(i,2);
                eventsRemoved++;
                events.add(newEvent);
            }
        }
    }

    public EventList<NoteEvent> getNoteEvents(int string) {
        checkSkillSelected();
        if (string < 0 || string >= STRING_COUNT) {
            throw new IllegalArgumentException(
                    String.format("Invalid string index %d.",string)
            );
        }
        return m_noteEvents[m_selectedSkillIndex][string];
    }

    public int getTotalNoteEventCount() {
        checkSkillSelected();
        int count = 0;
        for (int string = 0; string != Song.STRING_COUNT; ++string) {
            count += m_noteEvents[m_selectedSkillIndex][string].count();
        }
        return count;
    }

    public static int skillToIndex(int skill) {
        if (skill == SKILL_AMAZING) {
            return 0;
        } else if (skill == SKILL_MEDIUM) {
            return 1;
        } else if (skill == SKILL_EASY) {
            return 2;
        } else if (skill == SKILL_SUPAEASY) {
            return 3;
        } else {
            return -1;
        }
    }

    public static int indexToSkill(int skillIndex) {
        return 1 << skillIndex;
    }

    public void addTempoEvent(TempoEvent event) {
        m_tempoEvents.add(event);
    }

    public void addNoteEvent(int note,float time,float endTime) {
        int noteIndex = NoteMap.indexOf(note);
        if (noteIndex == -1) {
            return;
        }

        int skillIndex = skillToIndex(NoteMap.indexToSkill(noteIndex));
        EventListBuilder<NoteEvent>[] events = m_noteEvents[skillIndex];
        events[NoteMap.indexToString(noteIndex)].add(
                new NoteEvent(NoteMap.indexToString(noteIndex),time,endTime)
        );
    }

    private void mergeNoteEvents() {
        for (int i = 0; i != SKILL_COUNT; ++i) {
            if (m_noteEvents[i] != null) {
                mergeNoteEvents(m_noteEvents[i],m_noteEvents[i][3]);
                mergeNoteEvents(m_noteEvents[i],m_noteEvents[i][4]);
            }
        }
    }

    private void mergeNoteEvents(
            EventListBuilder<NoteEvent>[] stringEvents,
            EventListBuilder<NoteEvent> events)
    {
        long[] stringWeights = new long[STRING_COUNT];
        for (int i = 0; i != STRING_COUNT; ++i) {
            long count=stringEvents[i].count();
            stringWeights[i] = (count << 32) | i;
        }

        Arrays.sort(stringWeights);
        for (int i = 0; i != events.count(); ++i) {
            NoteEvent note = events.get(i);

            int index = 0;
            for (int j = 0; j != STRING_COUNT; ++j) {
                int string = (int)stringWeights[j];
                index = stringEvents[string].add(note);
                if (index >= 0) {
                    note.setString(string);
                    break;
                }
            }

            if (index < 0) {
                int string = (int)stringWeights[STRING_COUNT - 1];
                mergeNoteEvent(
                        stringEvents[string],
                        -index - 1,
                        string,note.getTime(),note.getEndTime());
            }
        }
    }

    private static void mergeNoteEvent(
            EventListBuilder<NoteEvent> events,
            int fromIndex,
            int string,float time,float endTime)
    {
        for (; fromIndex != 0; --fromIndex) {
            NoteEvent mergeEvent = events.get(fromIndex-1);
            if (mergeEvent.getEndTime() < (time - MERGE_MARGIN)) {
                break;
            }
        }

        int toIndex = fromIndex;
        for (;toIndex != events.count(); ++toIndex) {
            NoteEvent mergeEvent = events.get(toIndex);
            if (!((endTime + MERGE_MARGIN) > mergeEvent.getTime())) {
                break;
            }

            time = Math.min(time,mergeEvent.getTime());
            endTime = Math.max(endTime,mergeEvent.getEndTime());
        }

        events.remove(fromIndex,toIndex-fromIndex);
        events.add(new NoteEvent(string,time,endTime));
    }

    private void checkSkillSelected() {
        if (m_selectedSkillIndex == -1) {
            throw new IllegalStateException("Skill is not selected.");
        }
    }
}
