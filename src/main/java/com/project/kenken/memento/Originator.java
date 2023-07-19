package com.project.kenken.memento;

public interface Originator {
    Memento getMemento();

    void setMemento(Memento m);
}
