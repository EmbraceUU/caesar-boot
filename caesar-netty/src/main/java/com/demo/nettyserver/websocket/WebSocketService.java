package com.demo.nettyserver.websocket;

public interface WebSocketService {
    void onReceive(String msg);
    void onConnected();
    void onDisConnected();
}
