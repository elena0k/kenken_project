package risolutore;

import componenti.Gruppo;
import memento.Memento;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GroupsHistory { //CareTaker

    private Stack<Memento> history;
    private int currentIndex=-1;

    public GroupsHistory()
    {
        history= new Stack<>();
    }

    public void save(Memento memento)
    {
        history.push(memento);
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }

    public void undo(KenkenGrid kenken) {
        if (canUndo()) {
            history.pop();
            kenken.setMemento(history.pop());
        }
    }



}
