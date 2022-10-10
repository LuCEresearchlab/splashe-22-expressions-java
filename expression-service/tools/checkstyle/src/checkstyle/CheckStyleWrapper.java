package checkstyle;

import com.puppycrawl.tools.checkstyle.Main;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CheckStyleWrapper {

    public static void main(String[] args) throws IOException {
        int result = 1;

        Main.main(args);

        int outputIdx = -1;
        for (int i = 0; i < args.length - 1; i++) {
            if ("-o".equals(args[i])) {
                outputIdx = i + 1;
                break;
            }
        }

        if (outputIdx > 0) {
            final String content = Files.readString(Path.of(args[outputIdx]));
            final Pattern pattern = Pattern.compile("\\[((WARN)|(ERROR))]");
            final Matcher matcher = pattern.matcher(content);

            if (matcher.find()) {
                System.err.println(content);
                result = 2;
            } else {
                // No warning / error detected
                result = 0;
            }
        }

        System.exit(result);
    }
}