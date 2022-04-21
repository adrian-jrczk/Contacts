package contacts.console;

import java.util.Arrays;

enum InputRecognizer {

    EMPTY("^$"),
    ADD("(?i)add"),
    GET_ALL("(?i)get\\s+all"),
    DELETE("(?i)delete"),
    ACCESS("(?i)access\\s+[a-zA-Z]+.*"),
    EDIT_FIELD("(?i)edit\\s+(name|number|email|address|(birth date)|note)"),
    SEARCH("(?i)search\\s+[a-zA-Z0-9]+.*"),
    IMPORT("(?i)import\\s+[a-zA-Z]+.*"),
    HELP("(?i)help"),
    RETURN("(?i)return"),
    EXIT("(?i)exit"),
    INCORRECT(".*");

    private final String PATTERN;

    InputRecognizer(String pattern) {
        this.PATTERN = pattern;
    }

    static InputRecognizer recognize(String line) {
        return Arrays.stream(InputRecognizer.values())
                .filter(x -> line.matches(x.PATTERN))
                .findFirst()
                .get();
    }

}
