package com.demo.commonserver.jobs;

import com.mini.websocket.api.HuobiWebSocketClient;
import org.apache.log4j.Logger;

public class HuobiWsJob implements Job {

    public static final Logger logger = Logger.getLogger(HuobiWsJob.class);

    @Override
    public void execute() {
        HuobiWebSocketClient client = new HuobiWebSocketClient();
        try {
            client.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(String[] args) {

    }
}
