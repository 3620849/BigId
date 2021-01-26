package interfaces;

import model.WordPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

public interface Matcher {
    Future<HashMap<String, ArrayList<WordPosition>>> match(String bufferedText,long currentLinePosition);
    void shoutDown();
}
