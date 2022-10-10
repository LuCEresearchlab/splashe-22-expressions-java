package ch.usi.inf.luce.expr.util.tui;

import org.jetbrains.annotations.NotNull;

/**
 * Terminal text formatting class.
 */
@SuppressWarnings("unused")
public final class TermText {
    private final Object[] children;
    @NotNull
    private final String chars;

    private TermText(@NotNull String chars, Object... children) {
        this.children = children;
        this.chars = chars;
    }

    /**
     * Makes the text bold.
     */
    @NotNull
    public static TermText bold(Object... children) {
        return new TermText(AnsiConstants.STYLE_BOLD, children);
    }

    /**
     * Makes the text italics.
     * Not all terminals support this.
     */
    @NotNull
    public static TermText italic(Object... children) {
        return new TermText(AnsiConstants.STYLE_ITALIC, children);
    }

    /**
     * Makes the text underlined.
     */
    @NotNull
    public static TermText underline(Object... children) {
        return new TermText(AnsiConstants.STYLE_UNDERLINE, children);
    }

    /**
     * Inverts the text styling.
     */
    @NotNull
    public static TermText inverse(Object... children) {
        return new TermText(AnsiConstants.STYLE_INVERSE, children);
    }

    /**
     * Sets the text background color using one of the default terminal colors.
     */
    @NotNull
    @SuppressWarnings("PMD.CyclomaticComplexity")
    public static TermText background(@NotNull TermTextColor color, Object... children) {
        switch (color) {
            case BLACK:
                return new TermText(AnsiConstants.STYLE_COLOR_BG_BLACK, children);
            case RED:
                return new TermText(AnsiConstants.STYLE_COLOR_BG_RED, children);
            case GREEN:
                return new TermText(AnsiConstants.STYLE_COLOR_BG_GREEN, children);
            case YELLOW:
                return new TermText(AnsiConstants.STYLE_COLOR_BG_YELLOW, children);
            case BLUE:
                return new TermText(AnsiConstants.STYLE_COLOR_BG_BLUE, children);
            case MAGENTA:
                return new TermText(AnsiConstants.STYLE_COLOR_BG_MAGENTA, children);
            case CYAN:
                return new TermText(AnsiConstants.STYLE_COLOR_BG_CYAN, children);
            case WHITE:
                return new TermText(AnsiConstants.STYLE_COLOR_BG_WHITE, children);
            default:
                throw new IllegalArgumentException(String.valueOf(color));
        }
    }

    /**
     * Sets the text foreground color using one of the default terminal colors.
     */
    @NotNull
    @SuppressWarnings("PMD.CyclomaticComplexity")
    public static TermText foreground(@NotNull TermTextColor color, Object... children) {
        switch (color) {
            case BLACK:
                return new TermText(AnsiConstants.STYLE_COLOR_FG_BLACK, children);
            case RED:
                return new TermText(AnsiConstants.STYLE_COLOR_FG_RED, children);
            case GREEN:
                return new TermText(AnsiConstants.STYLE_COLOR_FG_GREEN, children);
            case YELLOW:
                return new TermText(AnsiConstants.STYLE_COLOR_FG_YELLOW, children);
            case BLUE:
                return new TermText(AnsiConstants.STYLE_COLOR_FG_BLUE, children);
            case MAGENTA:
                return new TermText(AnsiConstants.STYLE_COLOR_FG_MAGENTA, children);
            case CYAN:
                return new TermText(AnsiConstants.STYLE_COLOR_FG_CYAN, children);
            case WHITE:
                return new TermText(AnsiConstants.STYLE_COLOR_FG_WHITE, children);
            default:
                throw new IllegalArgumentException(String.valueOf(color));
        }
    }

    @NotNull
    private String toString(boolean resetStyle, @NotNull String stylingPrefix) {
        final StringBuilder sb = new StringBuilder();

        switch (children.length) {
            case 0: {
                sb.append(chars);
            }
            break;
            case 1: {
                sb.append(chars);
                final Object child = children[0];
                if (child instanceof TermText) {
                    final TermText p = (TermText) child;
                    sb.append(p.toString(false, stylingPrefix + chars));
                } else {
                    sb.append(child == null ? "null" : child.toString());
                }
            }
            break;
            default: {
                for (final Object child : children) {
                    sb.append(chars);
                    if (child instanceof TermText) {
                        final TermText p = (TermText) child;
                        sb.append(p.toString(true, stylingPrefix + chars));
                    } else {
                        sb.append(child);
                    }
                }
            }
            break;
        }

        if (resetStyle) {
            sb.append(AnsiConstants.STYLE_RESET);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return toString(true, "");
    }
}
