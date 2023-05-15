package com.example.pens.domain.websocket;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignalMessage {
    private String type;
    private String sender;
    private String receiver;
    private String roomId;
    private String data;
}
