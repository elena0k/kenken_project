package memento;

public interface Originator {
    Memento getMemento();

    void setMemento(Memento m);
}
