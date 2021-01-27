package services;

import interfaces.Aggregator;
import model.WordPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CommonAggregator implements Aggregator {
    ArrayList<Future<HashMap<String, ArrayList<WordPosition>>>> taskList;
    HashMap<String, ArrayList<WordPosition>> resultMap = new HashMap<>();

    public CommonAggregator() {
        this.taskList = new ArrayList<>();
    }

    @Override
    public void addFuture(Future task) {
        this.taskList.add(task);
    }

    /**
     * this method trips in after text will be iterated and oblige main thread wait till all task will be finished
     */
    @Override
    public void aggregateResults() {
        this.taskList.forEach(task -> {
            try {
                HashMap<String, ArrayList<WordPosition>> threadResult = task.get();
                if (threadResult != null) {
                    threadResult.forEach((key, val) -> {
                        resultMap.putIfAbsent(key, new ArrayList<>());
                        resultMap.get(key).addAll(val);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        String formatedRes =
                resultMap.toString()
                        .replace("{", "[")
                        .replace("}", "]")
                        .replace("=[", "-->");
        System.out.println(formatedRes);
    }
}
