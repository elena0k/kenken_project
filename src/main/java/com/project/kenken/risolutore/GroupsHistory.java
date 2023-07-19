package com.project.kenken.risolutore;

import com.project.kenken.memento.Memento;

import java.util.ArrayDeque;
import java.util.Deque;

public class GroupsHistory { //CareTaker

    private final int capacity = 100;
    private Deque<Memento> undoHistory;
    private Deque<Memento> redoHistory;
    private KenkenGrid kenken;

    public GroupsHistory(KenkenGrid kenken) {
        undoHistory = new ArrayDeque<>();
        redoHistory = new ArrayDeque<>();
        this.kenken=kenken;
    }

    public void save(Memento memento) {
        undoHistory.push(memento);
        if (undoHistory.size() > capacity)
            undoHistory.pollLast();
        redoHistory.clear();
    }

    public boolean canUndo() {
        return !undoHistory.isEmpty();
    }

    public boolean canRedo() {
        return !redoHistory.isEmpty();
    }

    public void undo() {
        if (canUndo()) {
            Memento memento = undoHistory.pop();
            redoHistory.push(kenken.getMemento());
            kenken.setMemento(memento);
        }
    }

    public void redo() {
        if (canRedo()) {
            Memento memento = redoHistory.pop();
            undoHistory.push(kenken.getMemento());
            kenken.setMemento(memento);
        }
    }
}
