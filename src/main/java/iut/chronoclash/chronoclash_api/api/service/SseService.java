package iut.chronoclash.chronoclash_api.api.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    @Scheduled(fixedRate = 5000)
    public void sendEvents() {
        emitters.forEach(emitter -> {
            try {
                System.out.println("Sending event");
                emitter.send(String.format("Event %d", System.currentTimeMillis()));
            } catch (Exception e) {
                emitter.complete();
            }
        });
    }

    public void sendToParticularClient(SseEmitter emitter, String message) {
        try {
            emitter.send(message);
        } catch (Exception e) {
            emitter.complete();
        }
    }
}
