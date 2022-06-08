import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.HashMap;
import java.util.regex.Pattern;

public class FileManager {
    public static final String FAILED_REQUESTS_TXT = "failed_requests.txt";

    private final ConsoleWriter consoleWriter;

    public FileManager(ConsoleWriter consoleWriter) {
        this.consoleWriter = consoleWriter;
    }

    public List<String> readUrlList() {
        return this.readListFromFile(Settings.URL_LIST_NAME);
    }

    public HashMap<String, String> readProxyMap() {
        var proxyList = this.readListFromFile(Settings.PROXY_LIST_NAME);
        if (proxyList == null) {
            return null;
        }

        var pattern = Pattern.compile(Settings.PROXY_REGEX);

        var proxyMap = new HashMap<String, String>();
        for (var proxyUrl : proxyList) {
            var matcher = pattern.matcher(proxyUrl);
            var hasMatched = matcher.matches();
            if (!hasMatched) {
                proxyMap.put(Settings.DEFAULT_PROXY_INDEX_NAME,  proxyUrl);
                continue;
            }

            var country = matcher.group("country").toUpperCase();
            proxyMap.put(country,  proxyUrl);
        }

        return proxyMap;
    }

    private List<String> readListFromFile(String fileName) {
        this.consoleWriter.writeln("Reading from the list...");

        try {
            return Files.readAllLines(Paths.get(fileName));
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
