package ch.usi.inf.luce.expr.util.tui;

/**
 * See the
 * <a href="https://en.wikipedia.org/wiki/ANSI_escape_code">wikipedia page</a>
 * for more information about how the values and functions in this
 * class work.
 */
final class AnsiConstants {

    private static final String ANSI_ESCAPE = "\033[";

    public static final String STYLE_RESET = ANSI_ESCAPE + "0m";
    public static final String STYLE_BOLD = ANSI_ESCAPE + "1m";
    public static final String STYLE_ITALIC = ANSI_ESCAPE + "2m";
    public static final String STYLE_UNDERLINE = ANSI_ESCAPE + "4m";
    public static final String STYLE_BLINK = ANSI_ESCAPE + "5m";
    public static final String STYLE_INVERSE = ANSI_ESCAPE + "7m";

    public static final String STYLE_COLOR_FG_BLACK = ANSI_ESCAPE + "30m";
    public static final String STYLE_COLOR_FG_RED = ANSI_ESCAPE + "31m";
    public static final String STYLE_COLOR_FG_GREEN = ANSI_ESCAPE + "32m";
    public static final String STYLE_COLOR_FG_YELLOW = ANSI_ESCAPE + "33m";
    public static final String STYLE_COLOR_FG_BLUE = ANSI_ESCAPE + "34m";
    public static final String STYLE_COLOR_FG_MAGENTA = ANSI_ESCAPE + "35m";
    public static final String STYLE_COLOR_FG_CYAN = ANSI_ESCAPE + "36m";
    public static final String STYLE_COLOR_FG_WHITE = ANSI_ESCAPE + "37m";

    public static final String STYLE_COLOR_BG_BLACK = ANSI_ESCAPE + "40m";
    public static final String STYLE_COLOR_BG_RED = ANSI_ESCAPE + "41m";
    public static final String STYLE_COLOR_BG_GREEN = ANSI_ESCAPE + "42m";
    public static final String STYLE_COLOR_BG_YELLOW = ANSI_ESCAPE + "43m";
    public static final String STYLE_COLOR_BG_BLUE = ANSI_ESCAPE + "44m";
    public static final String STYLE_COLOR_BG_MAGENTA = ANSI_ESCAPE + "45m";
    public static final String STYLE_COLOR_BG_CYAN = ANSI_ESCAPE + "46m";
    public static final String STYLE_COLOR_BG_WHITE = ANSI_ESCAPE + "47m";

    public static final String SAVE_SCREEN = ANSI_ESCAPE + "?47h";
    public static final String RESTORE_SCREEN = ANSI_ESCAPE + "?47l";

    public static final String ENABLE_ALT_BUFFER = ANSI_ESCAPE + "?1049h";
    public static final String DISABLE_ALT_BUFFER = ANSI_ESCAPE + "?1049l";

    public static final String SAVE_CURSOR = ANSI_ESCAPE + 's';
    public static final String RESTORE_CURSOR = ANSI_ESCAPE + 'u';

    public static final String ERASE_BEGINNING_TO_CURSOR = ANSI_ESCAPE + "1J";
    public static final String CURSOR_MOVE_HOME = ANSI_ESCAPE + "H";

    private AnsiConstants() {
    }
}
