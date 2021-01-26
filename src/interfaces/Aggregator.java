package interfaces;

import java.util.concurrent.Future;

public interface Aggregator<T> {
    void addFuture(Future<T> task);
    void aggregateResults();
}
