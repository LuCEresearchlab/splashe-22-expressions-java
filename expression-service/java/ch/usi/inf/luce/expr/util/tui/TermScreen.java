package ch.usi.inf.luce.expr.util.tui;

import java.io.Closeable;
import java.io.PrintStream;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("PMD.SystemPrintln")
public final class TermScreen {

    private TermScreen() {
    }

    /**
     * Provides a closeable that enables an
     * alternative buffer. To be used as
     * a try resource.
     */
    @NotNull
    public static Closeable altBuffer() {
        enableAltBuffer();
        return TermScreen::disableAltBuffer;
    }

    /**
     * Enable an alternative buffer so the
     * user doesn't lose the previous printed
     * content even if we display the ui in
     * "full screen".
     */
    public static void enableAltBuffer() {
        // Save the screen state and move to an
        // alternative buffer
        System.out.print(AnsiConstants.SAVE_CURSOR);
        System.out.print(AnsiConstants.SAVE_SCREEN);
        System.out.print(AnsiConstants.ENABLE_ALT_BUFFER);
    }

    /**
     * Disable the alternative buffer and restore the
     * old cursor and screen content.
     */
    public static void disableAltBuffer() {
        // Restore the old screen state
        System.out.print(AnsiConstants.DISABLE_ALT_BUFFER);
        System.out.print(AnsiConstants.RESTORE_SCREEN);
        System.out.print(AnsiConstants.RESTORE_CURSOR);
    }

    /**
     * Clear the current screen and reset the style.
     */
    public static void clearScreen(PrintStream out) {
        // Reset style
        out.print(AnsiConstants.STYLE_RESET);
        // Reset the screen
        out.print(AnsiConstants.ERASE_BEGINNING_TO_CURSOR);
        out.print(AnsiConstants.CURSOR_MOVE_HOME);
    }

    /**
     * Make the cursor blink.
     */
    public static void blinkCursor(PrintStream out) {
        out.print(AnsiConstants.STYLE_BLINK);
    }
}
