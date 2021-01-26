package services;

import interfaces.Matcher;
import model.WordPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CommonMatcher implements Matcher {
    private String[] searchWords;
    ExecutorService es;
    public CommonMatcher(String searchWords) {
        this.searchWords = searchWords.split(",");
        this.es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public Future< HashMap<String, ArrayList<WordPosition>>> match(String bufferedText,long currentLinePosition){
        return es.submit(()->{
            HashMap<String, ArrayList<WordPosition>> res = new HashMap<>();
            for(String s: searchWords){
                ArrayList<WordPosition> positions = new ArrayList<>();
                findWords(s,bufferedText,currentLinePosition,positions,0);
                if(positions.size()>0){
                    res.put(s,positions);
                }
            }
            if(res.size()==0){
                return null;
            }
            return res;
        });
    }

    private void findWords(String searchWord, String bufferedText, long currentLinePosition, ArrayList<WordPosition> positions, int fromIndex) {
        int index = bufferedText.indexOf(searchWord,fromIndex);
        if(index==-1){
            return;
        }
        positions.add(new WordPosition(currentLinePosition,index));
        fromIndex = index+searchWord.length();
        findWords(searchWord,bufferedText,currentLinePosition,positions,fromIndex);
    }

    public void shoutDown(){
        this.es.shutdown();
    }
}
