package dev.ted.tddgame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TimerWebSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimerWebSocketHandler.class);

    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(240);
    private ScheduledExecutorSecondsTicker timer;

    public TimerWebSocketHandler() {
        timer = new ScheduledExecutorSecondsTicker();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("Websocket connection established, session ID: {}, session remote address: {}", session.getId(), session.getRemoteAddress());
        timer.startTimer(this::sendTimerToAll);
        sessionMap.put(session.getId(), session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        LOGGER.info("Websocket Text Message received from session: {}, with message: {}", session.toString(), message.toString());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessionMap.remove(session.getId());
        if (sessionMap.isEmpty()) {
            timer.stopTimer();
        }
    }

    public void sendTimerToAll() {
        LOGGER.info("Sending timer {} to {} sessions", counter.get(), sessionMap.size());
        sessionMap.values().forEach(this::sendTimerUpdateTo);
    }

    static class ScheduledExecutorSecondsTicker {

        private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        private ScheduledFuture<?> countdownHandle;

        public void startTimer(Runnable task) {
            countdownHandle = scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        }

        public void stopTimer() {
            countdownHandle.cancel(false);
        }

    }

    private void sendTimerUpdateTo(WebSocketSession session) {
        try {
            double percentRemaining = (counter.decrementAndGet() * 100.0) / 240;
            int minutes = counter.get() / 60;
            int seconds = counter.get() % 60;
            session.sendMessage(new TextMessage(
                    """
                            <div id="timer-container"
                                 class="circle"
                                 style="background: conic-gradient(lightgreen 0%% %f%%, black %f%% 100%%);">
                                <svg class="progress-ring">
                                    <circle class="progress-circle"/>
                                </svg>
                                <div class="timer-circle">
                                   <div id="timer" class="font-bold">%d:%02d</div>
                                </div>
                            </div>
                            """.formatted(percentRemaining, percentRemaining, minutes, seconds)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
