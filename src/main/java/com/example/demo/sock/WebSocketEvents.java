import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

@Component
public class WebSocketEvents {

    @EventListener
    public void handleConnect(SessionConnectedEvent event) {
        System.out.println("CONNECTED: " + event.getUser());
    }

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        System.out.println("SUBSCRIBE: " + event.getUser());
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        System.out.println("DISCONNECTED: " + event.getSessionId());
    }
}