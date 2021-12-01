import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class HeaderGenerator {
    private final String browserHeadersFilename;
    private final String userAgentsFilename;
    private final ConsoleWriter consoleWriter;
    private final Random rand;

    public HeaderGenerator(
            String browserHeadersFilename,
            String userAgentsFilename,
            ConsoleWriter consoleWriter
    ) {
        this.browserHeadersFilename = browserHeadersFilename;
        this.userAgentsFilename = userAgentsFilename;
        this.consoleWriter = consoleWriter;
        this.rand = new Random();
    }

    public JsonObject loadJson(String fileName) {
        String content = "";
        try {
            content = Files.readString(Path.of(fileName));
        } catch (IOException e) {
            consoleWriter.writelnAndExit(e.getMessage());
        }

        var parser = new JsonParser();

        return parser.parse(content).getAsJsonObject();
    }

    public JsonObject generate() {
        var rand = new Random();

        var browserHeaders = this.loadJson(this.browserHeadersFilename);
        var randomBrowserName = getRandomKey(browserHeaders);

        var userAgents = this.loadJson(this.userAgentsFilename);
        var browserUserAgents = userAgents.get(randomBrowserName).getAsJsonArray();
        var randomUserAgent = browserUserAgents.get(rand.nextInt(browserUserAgents.size()-1));

        var randomBrowser = browserHeaders.get(randomBrowserName).getAsJsonObject();
        randomBrowser.add("User-Agent", randomUserAgent);

        return randomBrowser;
    }

    public String getRandomKey(JsonObject object) {
        var set = object.keySet();
        var item = this.rand.nextInt(set.size());
        var setObjectIndex = 0;

        for(String setItem : set)
        {
            if (setObjectIndex == item) {
                return setItem;
            }
            setObjectIndex++;
        }

        return null;
    }
}
