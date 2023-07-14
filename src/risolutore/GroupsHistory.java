package risolutore;

import memento.Memento;

import java.util.*;

public class GroupsHistory { //CareTaker

    private final int capacity=100;
    private Deque<Memento> undoHistory;
    private Deque<Memento> redoHistory;
    private int currentIndex=-1;

    public GroupsHistory() {
        undoHistory = new ArrayDeque<>();
        redoHistory = new ArrayDeque<>();
    }

    public void save(Memento memento){
        undoHistory.push(memento);
        if(undoHistory.size()>capacity)
            undoHistory.pollLast();
        redoHistory.clear();
    }

    public boolean canUndo() {
        return !undoHistory.isEmpty();
    }

    public boolean canRedo(){
        return !redoHistory.isEmpty();
    }

    public void undo(KenkenGrid kenken) {
        if (canUndo()) {
            Memento memento=undoHistory.pop();
            redoHistory.push(kenken.getMemento());
            kenken.setMemento(memento);
        }
    }

    public void redo(KenkenGrid kenken) {
        if(canRedo()) {
            Memento memento = redoHistory.pop();
            undoHistory.push(kenken.getMemento());
            kenken.setMemento(memento);
        }
    }



}
