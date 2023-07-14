package risolutore;

import memento.Memento;

import java.util.*;

public class GroupsHistory { //CareTaker

    private final int capacity=100;
    private Deque<Memento> undoHistory;
    private int currentIndex=-1;

    public GroupsHistory()
    {
        undoHistory = new ArrayDeque<>();
    }

    public void save(Memento memento)
    {
        undoHistory.push(memento);
    }

    public boolean canUndo() {
        return undoHistory.size()>0;
    }

    public void undo(KenkenGrid kenken) {
        if (canUndo()) {
            if(undoHistory.size()>1)
                undoHistory.pop();
            kenken.setMemento(undoHistory.pop());
        }
    }



}
