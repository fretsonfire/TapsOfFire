package app.tapsoffire.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import android.graphics.Color;

public class IniFile {

    private enum ParseState {
        NORMAL,
        ESCAPE,
        ESC_CRNL,
        COMMENT
    }

    private Section m_globalSection;
    private HashMap<String,Section> m_sections;

    public IniFile(InputStream in) throws IOException {
        m_globalSection = new Section(null);
        m_sections = new HashMap<String,Section>();
        load(in);
    }

    public Section getGlobalSection() {
        return m_globalSection;
    }

    public Section getSection(String name) {
        return m_sections.get(name);
    }

    public static class Section {

        private String name;
        private HashMap<String,String> values;

        private Section(String name) {
            this.name = name;
            this.values = new HashMap<String,String>();
        }

        public String getName() {
            return this.name;
        }

        public String getValue(String key) {
            return this.values.get(key);
        }

        public String getStringValue(String key, String defaultValue) {
            String value = getValue(key);
            return value != null ? value : defaultValue;
        }

        public int getIntValue(String key, int defaultValue) {
            String value = getValue(key);
            if (value == null) {
                return defaultValue;
            }

            try {
                return Integer.parseInt(value);
            }
            catch (NumberFormatException e) {
                return defaultValue;
            }
        }

        public float getFloatValue(String key, float defaultValue) {
            String value = getValue(key);
            if (value == null) {
                return defaultValue;
            }

            try {
                return Float.parseFloat(value);
            }
            catch (NumberFormatException e) {
                return defaultValue;
            }
        }

        public int getColorValue(String key, int defaultValue) {
            String value = getValue(key);
            if (value == null) {
                return defaultValue;
            }

            try {
                return Color.parseColor(value);
            }
            catch (IllegalArgumentException e) {
                return defaultValue;
            }
        }

    }

    private void load(InputStream in) throws IOException {
        int bufSize = 4096;
        byte[] buffer = new byte[bufSize];
        int n = in.read(buffer,0,bufSize);

        ParseState state = ParseState.NORMAL;
        boolean sectionOpen = false;
        Section currentSection = null;
        String key = null,value = null;
        StringBuilder sb = new StringBuilder();

        while (n >= 0) {
            for (int i = 0; i < n; i++) {
                char c = (char)buffer[i];
                if (state == ParseState.COMMENT) { // comment, skip to end of line
                    if ((c == '\r') || (c == '\n')) {
                        state = ParseState.NORMAL;
                    } else {
                        continue;
                    }
                }

                if (state == ParseState.ESCAPE) {
                    sb.append(c);
                    if (c == '\r') {
                        // if the EOL is \r\n, \ escapes both chars
                        state = ParseState.ESC_CRNL;
                    } else {
                        state = ParseState.NORMAL;
                    }
                    continue;
                }

                switch (c) {
                    case '[': // start section
                        getStringReset(sb);
                        sectionOpen = true;
                        break;

                    case ']': // end section
                        if (sectionOpen) {
                            currentSection = new Section(getStringReset(sb));
                            m_sections.put(currentSection.name,currentSection);
                            sectionOpen = false;
                        } else {
                            sb.append(c);
                        }
                        break;

                    case '\\': // escape char, take the next char as is
                        state = ParseState.ESCAPE;
                        break;

                    case '#':
                    case ';':
                        if (key == null && !sectionOpen) {
                            state = ParseState.COMMENT;
                        } else {
                            sb.append(c);
                        }
                        break;

                    case '=': // assignment operator
                    case ':':
                        if (key == null && !sectionOpen) {
                            key = getStringReset(sb);
                        } else {
                            sb.append(c);
                        }
                        break;

                    case '\r':
                    case '\n':
                        if ((state == ParseState.ESC_CRNL) && (c == '\n')) {
                            sb.append(c);
                            state = ParseState.NORMAL;
                        } else {
                            if (sb.length() > 0) {
                                value = getStringReset(sb);
                                if (key != null) {
                                    if (currentSection == null) {
                                        m_globalSection.values.put(key,value);
                                    } else {
                                        currentSection.values.put(key,value);
                                    }
                                }
                            }
                            key = null;
                            value = null;
                        }
                        break;

                    default:
                        sb.append(c);
                }
            }

            n = in.read(buffer,0,bufSize);
        }

        if (sb.length() > 0) {
            value = getStringReset(sb);
            if (key != null) {
                if (currentSection == null) {
                    m_globalSection.values.put(key,value);
                } else {
                    currentSection.values.put(key,value);
                }
            }
        }
    }

    private static String getStringReset(StringBuilder sb) {
        String result = sb.toString().trim();
        sb.delete(0,sb.length());
        return result;
    }
}
