package com.example.pens.websocket;

import com.example.pens.domain.websocket.SignalMessage;
import com.example.pens.service.VoiceChannelService;
import com.example.pens.util.WebSocketUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class SignalingSocketHandler extends TextWebSocketHandler {
    private static final Logger LOG = LoggerFactory.getLogger(SignalingSocketHandler.class);

    private static final String TYPE_INIT = "init";
    private static final String TYPE_LOGOUT = "logout";

    //세션 구조 -> Map[ roomId, Map[sessionId, sessionObject] ]
    private final Map<String, Map<String, WebSocketSession>> roomSessions = new HashMap<>();



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.info("[" + session.getId() + "] Connection established " + session.getId());

        //roomId 추출 from uri
        URI uri = session.getUri();
        MultiValueMap<String,String> parameters = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        String roomId = parameters.getFirst("roomId");
        if (!roomSessions.containsKey(roomId)) {
            roomSessions.put(roomId, new HashMap<>());
        }
        String userId = parameters.getFirst("userId");
        // TODO: roomId, userId -> service에 만든 함수 이용, 음성 채널 접속 시 redis에 삽입


        final SignalMessage newMenOnBoard = new SignalMessage();
        newMenOnBoard.setType(TYPE_INIT);
        newMenOnBoard.setSender(session.getId());
        newMenOnBoard.setRoomId(roomId);

        roomSessions.get(roomId).values().forEach(webSocketSession -> {
            try {
                if(!webSocketSession.equals(session))
                    webSocketSession.sendMessage(new TextMessage(WebSocketUtil.getString(newMenOnBoard)));
            } catch (Exception e) {
                LOG.warn("Error while message sending.", e);
            }
        });

        roomSessions.get(roomId).put(session.getId(), session);
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

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        LOG.info("handleTextMessage : {}", message.getPayload());

        SignalMessage signalMessage = WebSocketUtil.getObject(message.getPayload());
        // with the destinationUser find the targeted socket, if any
        String destinationUser = signalMessage.getReceiver();
        String roomId = signalMessage.getRoomId();
        signalMessage.setSender(session.getId());


        WebSocketSession destSocket = roomSessions.get(roomId).get(destinationUser);
        // if the socket exists and is open, we go on
        if (destSocket != null && destSocket.isOpen()) {
            // set the sender as current sessionId.
            final String resendingMessage = WebSocketUtil.getString(signalMessage);
            LOG.info("send message {} to {}", resendingMessage, destinationUser);
            destSocket.sendMessage(new TextMessage(resendingMessage));
        }

        /*
        // 같은 room에 모두 전송 (자신 제외)
        roomSessions.get(roomId).values().forEach(
                webSocketSession -> {
                    try {
                        if(!webSocketSession.equals(session))
                            webSocketSession.sendMessage(new TextMessage(WebSocketUtil.getString(signalMessage)));
                    } catch (Exception e) {
                        LOG.warn("Error while message sending.", e);
                    }
                }
        );
        */
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

        // send the message to all other peers, somebody(sessionId) leave.
        final SignalMessage menOut = new SignalMessage();
        menOut.setType(TYPE_LOGOUT);
        menOut.setSender(sessionId);

        roomSessions.get(roomToRemove).values().forEach(webSocket -> {
            try {
                webSocket.sendMessage(new TextMessage(WebSocketUtil.getString(menOut)));
            } catch (Exception e) {
                LOG.warn("Error while message sending.", e);
            }
        });
    }
}
