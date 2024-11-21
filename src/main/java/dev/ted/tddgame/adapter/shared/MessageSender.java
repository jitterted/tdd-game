package dev.ted.tddgame.adapter.shared;

public interface MessageSender {
    boolean isOpen();
    void sendMessage(String message);
}
