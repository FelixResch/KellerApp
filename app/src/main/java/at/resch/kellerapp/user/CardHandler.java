package at.resch.kellerapp.user;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by felix on 8/5/14.
 */
public class CardHandler {

    private static CardHandler instance;

    private Queue<String> cardStack;
    private Stack<CardListener> listeners;

    private boolean block;

    public CardHandler() {
        instance = this;
        cardStack = new LinkedList<String>();
        listeners = new Stack<CardListener>();
    }

    public static CardHandler get() {
        return instance;
    }

    public void block() {
        block = true;
    }

    public void release() {
        block = false;
        handleStack();
    }

    private void handleStack() {
        String card;
        while ((card = cardStack.poll()) != null) {
            listeners.peek().onCardDetected(card);
        }
    }

    public void handle(String card) {
        cardStack.add(card);
        if (!block)
            handleStack();
    }

    public void addCardListener(CardListener listener) {
        listeners.push(listener);
    }

    public void removeCardListener() {
        if (listeners.size() > 1)
            listeners.pop();
    }
}
