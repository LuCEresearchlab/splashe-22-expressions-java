package pmd;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.sourceforge.pmd.PMD;

/**
 * Wrap PMD to print errors and violations on STDERR
 */
public final class PmdWrapper {

    public static void main(String[] args) {
        final PMD.StatusCode result = PMD.runPmd(args);
        if (result == PMD.StatusCode.OK) {
            return;
        }

        int idxReportFile = -1;
        for (int i = 0; i < args.length - 1; i++) {
            if ("--report-file".equals(args[i])) {
                idxReportFile = i + 1;
                break;
            }
        }

        if (idxReportFile > 0) {
            final Path filePath = Paths.get(args[idxReportFile]);
            try {
                final String fileContent = Files.readString(filePath);
                System.err.println(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.exit(result.toInt());
    }
}
