package com.example.pens.websocket;

import com.example.pens.domain.websocket.SignalMessage;
import com.example.pens.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class DrawingSocketHandler extends TextWebSocketHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DrawingSocketHandler.class);

    private static final String TYPE_INIT = "init";
    private static final String TYPE_LOGOUT = "logout";
    private static final String TYPE_LIST = "list";

    //세션 구조 -> Map[ roomId, Map[sessionId, sessionObject] ]
    private final Map<String, Map<String, WebSocketSession>> roomSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.info("[" + session.getId() + "] Connection established " + session.getId());


        //roomId 추출 from uri
        URI uri = session.getUri();
        MultiValueMap<String,String> parameters = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        String roomId = parameters.getFirst("roomId");
        String userId = parameters.getFirst("userId");
        session.getAttributes().put("roomId", roomId);
        session.getAttributes().put("userId", userId);

        if (!roomSessions.containsKey(roomId)) {
            roomSessions.put(roomId, new HashMap<>());
        }

        roomSessions.get(roomId).put(session.getId(), session);


        final SignalMessage newMenOnBoard = new SignalMessage();
        newMenOnBoard.setType(TYPE_INIT);
        newMenOnBoard.setSender(session.getId());
        newMenOnBoard.setRoomId(roomId);
        roomSessions.get(roomId).values().forEach(webSocketSession -> {
            try {
                if(!webSocketSession.equals(session)) {
                    LOG.info("[" + session.getId() + "] init Message broadcast.");
                    webSocketSession.sendMessage(new TextMessage(WebSocketUtil.getString(newMenOnBoard)));
                }
            } catch (Exception e) {
                LOG.warn("Error while message sending.", e);
            }
        });

        final SignalMessage userListOnBoard = new SignalMessage();
        userListOnBoard.setType(TYPE_LIST);
        userListOnBoard.setRoomId(roomId);

        StringBuffer listData = new StringBuffer("");

        roomSessions.get(roomId).values().forEach(webSocketSession -> {
           listData.append(webSocketSession.getAttributes().get("userId")+" ");
        });

        userListOnBoard.setData(listData.toString());

        roomSessions.get(roomId).values().forEach(webSocketSession -> {
            try {
                LOG.info("[" + session.getId() + "] list Message broadcast.");
                webSocketSession.sendMessage(new TextMessage(WebSocketUtil.getString(userListOnBoard)));
            } catch (Exception e) {
                LOG.warn("Error while message sending.", e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOG.info("[" + session.getId() + "] Connection closed " + session.getId() + " with status: " + status.getReason());
        removeUserAndSendLogout(session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOG.info("[" + session.getId() + "] Connection error " + session.getId() + " with status: " + exception.getLocalizedMessage());
        removeUserAndSendLogout(session.getId());
    }

    /*
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOG.info("handleTextMessage : {}", message.getPayload());

        SignalMessage signalMessage = WebSocketUtil.getObject(message.getPayload());
        // with the destinationUser find the targeted socket, if any
        //String destinationUser = signalMessage.getReceiver();
        String roomId = signalMessage.getRoomId();
        signalMessage.setSender(session.getId());

        // 같은 room에 모두 전송 (자신 제외)
        roomSessions.get(roomId).values().forEach(
                webSocketSession -> {
                    try {
                        if(!webSocketSession.equals(session)) {
                            webSocketSession.sendMessage(new TextMessage(WebSocketUtil.getString(signalMessage)));
                        }
                    } catch (Exception e) {
                        LOG.warn("Error while message sending.", e);
                    }
                }
        );
    }
    */

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        LOG.info("handleBinaryMessage from session: {}", session.getId());

        // Here you can access your binary data as ByteBuffer or byte array.
        ByteBuffer byteBuffer = message.getPayload();

        // Create a new BinaryMessage object using the received payload.
        BinaryMessage newMessage = new BinaryMessage(byteBuffer);

        // Retrieve the room ID from the session attributes or from the query string
        // (like you did in afterConnectionEstablished).
        String roomId = (String)session.getAttributes().get("roomId");

        roomSessions.get(roomId).values().forEach(
                webSocketSession -> {
                    try {
                        if(!webSocketSession.equals(session)) {
                            webSocketSession.sendMessage(newMessage);
                        }
                    } catch (Exception e) {
                        LOG.warn("Error while message sending.", e);
                    }
                }
        );
    }


    private void removeUserAndSendLogout(final String sessionId) {

        String roomToRemove = null;
        for (Map.Entry<String, Map<String, WebSocketSession>> roomEntry : roomSessions.entrySet()) {
            if (roomEntry.getValue().containsKey(sessionId)) {
                roomToRemove = roomEntry.getKey();
                break;
            }
        }

        if (roomToRemove != null) {
            roomSessions.get(roomToRemove).remove(sessionId);
        }
    }
}