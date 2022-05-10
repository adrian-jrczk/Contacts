package contacts.console;

import java.util.Arrays;

enum InputRecognizer {

    EMPTY("^$"),
    ADD_CONTACT("(?i)add"),
    ADD_CONTACT_ITEM("(?i)add\\s+\\w+"),
    GET_ALL("(?i)get\\s+all"),
    DELETE_CONTACT("(?i)delete"),
    DELETE_FIELD("(?i)delete\\s+\\w+.*"),
    ACCESS("(?i)access\\s+[a-zA-Z]+.*"),
    EDIT_FIELD("(?i)edit\\s+\\w+.*"),
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
