package view.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import javax.swing.*;
import java.net.URI;

public class RealtimeClient {
    private WebSocketClient webSocketClient;
    private boolean isConnected = false;
    private MessageHandler messageHandler;

    public interface MessageHandler {
        void onMessage(String message);
    }

    public RealtimeClient(MessageHandler handler) {
        this.messageHandler = handler;
    }

    public void connect() {
        try {
            webSocketClient = new WebSocketClient(new URI("ws://localhost:8080")) {
                @Override
                public void onOpen(ServerHandshake handshake) {
                    isConnected = true;
                    System.out.println("WebSocket Connected");
                    sendMessage("CLIENT:DASHBOARD:" + System.currentTimeMillis());
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("WebSocket Received: " + message);
                    SwingUtilities.invokeLater(() -> {
                        if (messageHandler != null) {
                            messageHandler.onMessage(message);
                        }
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    isConnected = false;
                    System.out.println("WebSocket Disconnected: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("WebSocket Error: " + ex.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (Exception e) {
            System.err.println("Failed to connect WebSocket: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(message);
            System.out.println("WebSocket Sent: " + message);
        }
    }

    public void close() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}