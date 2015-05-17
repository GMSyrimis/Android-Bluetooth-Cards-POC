package edu.uchicago.pkuprys.cards.messaging;

public interface Messenger {
    public void sendMessage(Object message);

    public Object getMessage();

    public void close();
}
