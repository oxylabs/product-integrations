import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileManager {
    public static final String FAILED_REQUESTS_TXT = "failed_requests.txt";

    private final ConsoleWriter consoleWriter;

    public FileManager(ConsoleWriter consoleWriter) {
        this.consoleWriter = consoleWriter;
    }

    public List<String> readUrlList() {
        this.consoleWriter.writeln("Reading from the list...");

        try {
            return Files.readAllLines(Paths.get(Settings.URL_LIST_NAME));
        } catch (IOException e) {
            this.consoleWriter.writelnAndExit("Failed to read from file: " + e.getMessage());
            return null;
        }
    }

    public void writeError(String contents) {
        try {
            var openOption = StandardOpenOption.CREATE;
            if (Files.exists(Path.of(FAILED_REQUESTS_TXT))) {
                openOption = StandardOpenOption.APPEND;
            }

            Files.write(Paths.get(FAILED_REQUESTS_TXT), (contents + "\n").getBytes(), openOption);

        } catch (IOException e) {
            this.consoleWriter.writelnError("Failed to write to file: " + e.getMessage());
        }
    }

    public void writeSuccess(int position, String contents) {
        try {
            Files.write(Paths.get("result_" + position + ".html"), contents.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            this.consoleWriter.writelnError("Failed to write to file: " + e.getMessage());
        }
    }
}
