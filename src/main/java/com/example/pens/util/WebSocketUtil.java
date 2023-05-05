package com.example.pens.util;

import com.example.pens.domain.websocket.SignalMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketUtil {
    // Jackson JSON converter
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private WebSocketUtil() {
    }

    public static SignalMessage getObject(final String message) throws Exception {
        return objectMapper.readValue(message, SignalMessage.class);
    }

    public static String getString(final SignalMessage message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }
}