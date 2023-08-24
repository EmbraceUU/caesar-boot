package com.demo.nettyserver.websocket.api;

import com.demo.nettyserver.util.JsonUtil;
import com.demo.nettyserver.websocket.WebSocketClient;
import com.demo.nettyserver.websocket.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HuobiWebSocketClient extends WebSocketClient {

    private Logger log = LoggerFactory.getLogger(HuobiWebSocketClient.class);
    private static String WSURL = "wss://api.huobi.pro/ws";

    public HuobiWebSocketClient() {
        this.service = new WebSocketService(){

            @Override
            public void onReceive(String msg) {
                handleMsg(msg);
            }

            @Override
            public void onConnected() {
                String[] symbols = new String[]{"btcusdt"};
                subscribe(symbols, 1);
            }

            @Override
            public void onDisConnected() {
                try {
                    HuobiWebSocketClient.this.reConnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        this.setUrl(WSURL);
    }

    @Override
    public void sentPing() {

    }

    @Override
    public boolean isTimeOut() {
        return false;
    }

    private void handleMsg(String msg){

    }

    private void subscribe(String[] symbols, int type) {

    }

    public void send(Object msg) {
        String returnMsg = JsonUtil.toJson(msg);
        log.info("send msg" + returnMsg);
        TextWebSocketFrame binaryWebSocketFrame = new TextWebSocketFrame(
                returnMsg);
        channel.writeAndFlush(binaryWebSocketFrame);
    }
}
