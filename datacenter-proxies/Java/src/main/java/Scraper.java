import okhttp3.Response;

import java.io.IOException;

public class Scraper {
    private final ApiClient apiClient;
    private final FileManager fileManager;
    private final ConsoleWriter consoleWriter;

    public Scraper(
            ApiClient apiClient,
            FileManager fileManager,
            ConsoleWriter consoleWriter
    ) {
        this.apiClient = apiClient;
        this.fileManager = fileManager;
        this.consoleWriter = consoleWriter;
    }

    public void scrape(int position, String proxy, String url) {
        int retry = 0;

        do {
            Response response = null;
            try {
                response = this.apiClient.fetchPage(proxy, url);
            } catch (Exception e) {
                this.fileManager.writeError(String.format("%s - Response %s", url, e.getMessage()));
                this.consoleWriter.writelnError(String.format("%s - Response %s", url, e.getMessage()));
            }

            if (response != null && response.code() != 200) {
                this.fileManager.writeError(String.format("%s - Response code %d", url, response.code()));
                this.consoleWriter.writelnError(String.format("%s - Response code %d", url, response.code()));
            }

            if (response != null && response.code() == 200) {
                try {
                    this.fileManager.writeSuccess(position, response.body().string());
                } catch (Exception e) {
                    this.consoleWriter.writelnError(String.format("%s - Error when parsing response: %s", url, e.getMessage()));
                }
                break;
            }

            retry += 1;
            if (retry >= Settings.RETRIES_NUM) {
                break;
            }
        } while (true);
    }
}
