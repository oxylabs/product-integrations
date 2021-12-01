import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main implements Runnable {
    public void run() {
        long startTime = System.nanoTime();

        var consoleWriter = new ConsoleWriter();

        var header = new HeaderGenerator(
                Settings.BROWSER_LIST_NAME,
                Settings.AGENT_LIST_NAME,
                consoleWriter
        );

        var fileManager = new FileManager(consoleWriter);
        var apiClient = new ApiClient(consoleWriter, header);
        var scraper = new Scraper(apiClient, fileManager, consoleWriter);

        var urlList = fileManager.readUrlList();
        var proxyList = apiClient.getProxyList();

        var roundRobinProxies = new RoundRobin<>(Arrays.asList(proxyList));
        Iterator<String> iterator = roundRobinProxies.iterator();

        consoleWriter.writeln("Gathering results...");
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < urlList.size(); i++) {
            var proxy = iterator.next();

            int position = i;
            var future = CompletableFuture.runAsync(() -> {
                scraper.scrape(position + 1, proxy, urlList.get(position));
            });

            futures.add(future);
        }

        for (var future : futures) {
            future.join();
        }

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        consoleWriter.writeln("Script finished after " + timeElapsed / 1000000000 + "s");
        System.exit(0);
    }

    public static void main(String[] args) {
        new Thread(new Main()).start();
    }
}