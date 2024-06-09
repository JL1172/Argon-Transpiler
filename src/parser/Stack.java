package parser;

import java.util.HashMap;

public class Stack {
    private HashMap<Integer, String> storage = new HashMap<>();
    private int count = 0;
    public void push(String value) {
        this.storage.put(this.count, value);
        this.count++;
    }
    public String pop() {
        if (this.storage.size() > 0) {
        this.count--;
        String valueToReturn = this.storage.get(count);
        this.storage.remove(this.count);
        return valueToReturn;
        } else {
            return null;
        }
    }
    public int size() {
        return this.storage.size();
    }
}
