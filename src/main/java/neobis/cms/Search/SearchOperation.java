package neobis.cms.Search;

public enum SearchOperation {
    GREATER_THAN_EQUAL,
    LESS_THAN_EQUAL,
    NOT_EQUAL,
    EQUAL,
    MATCH;
    public static final String[] SIMPLE_OPERATION_SET = { ":", "!", ">", "<", "~" };
    public static SearchOperation getSimpleOperation(final char input) {
        switch (input) {
            case ':' : return EQUAL;
            case '!' : return NOT_EQUAL;
            case '>' : return GREATER_THAN_EQUAL;
            case '<' : return LESS_THAN_EQUAL;
            case '~' : return MATCH;
            default : return null;
        }
    }
}
