import interfaces.Aggregator;
import interfaces.Matcher;
import services.CommonAggregator;
import services.CommonMatcher;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    private static final String TEXT_LOCATION = "http://norvig.com/big.txt";
    private static final String NAMES = "James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas,Christopher,Daniel,Paul,Mark,Donald" +
            ",George,Kenneth,Steven,Edward,Brian,Ronald,Anthony,Kevin,Jason,Matthew,Gary,Timothy,Jose,Larry,Jeffrey," +
            "Frank,Scott,Eric,Stephen,Andrew,Raymond,Gregory,Joshua,Jerry,Dennis,Walter,Patrick,Peter,Harold,Douglas," +
            "Henry,Carl,Arthur,Ryan,Roger";

    private static final Integer BUFFER_SIZE = 1000;
    private static Long currentLinePosition = 0L;
    private static StringBuilder bufferedLine = new StringBuilder("");
    static Matcher matcher = new CommonMatcher(NAMES);
    static Aggregator aggregator = new CommonAggregator();

    public static void main(String[] args) {
        readLines();
        aggregator.aggregateResults();
        matcher.shoutDown();
    }

/**
 * read every 1000 lines in string and execute async matching search words
 */
    static void readLines() {
        try {
            URL url = new URL(TEXT_LOCATION);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    batchLine(line);
                }
                if(bufferedLine.length()>0){
                    aggregator.addFuture(matcher.match(bufferedLine.toString(),currentLinePosition-BUFFER_SIZE));
                }
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace(System.out);
        }
    }

    static void batchLine(String line){
        if(currentLinePosition > 0 && currentLinePosition % BUFFER_SIZE == 0){
            aggregator.addFuture(matcher.match(bufferedLine.toString(),currentLinePosition-BUFFER_SIZE));
            bufferedLine.setLength(0);
            System.out.println(currentLinePosition);
        }
        bufferedLine.append(line);
        ++currentLinePosition;
    }
}
