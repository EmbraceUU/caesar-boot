package com.demo.nettyserver.websocket;

import org.apache.log4j.Logger;
import java.util.TimerTask;

public class MoniterTask extends TimerTask {
    private Logger log = Logger.getLogger(MoniterTask.class);
    private WebSocketClient client;
    private long startTime = System.currentTimeMillis();
    private int checkTime = 300000;

    public MoniterTask(WebSocketClient client){
        this.client = client;
    }

    @Override
    public void run() {
        log.info("sendping");
        client.sentPing();
        if (System.currentTimeMillis() - startTime > checkTime) {
            log.info("reconnect happens");
            this.cancel();
            client.setStatus(false);
            try {
                client.reConnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateTime() {
        startTime = System.currentTimeMillis();
    }
}
