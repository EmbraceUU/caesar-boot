package com.demo.commonserver.jobs;

import com.mini.websocket.api.BinanceWebSocketClient;
import org.apache.log4j.Logger;

public class BinanceWsJob implements Job {

    public static final Logger logger = Logger.getLogger(BinanceWsJob.class);
    private static final String pathBar = "btcusdt@kline_1m";

    @Override
    public void execute() {
        BinanceWebSocketClient client = new BinanceWebSocketClient(pathBar);
        try {
            logger.info(String.format("%s job start !", this.getClass().getName()));
            client.start();
        } catch (Exception e) {
            logger.error("exception !");
            e.printStackTrace();
        }
    }

    @Override
    public void execute(String[] args) {

    }
}
