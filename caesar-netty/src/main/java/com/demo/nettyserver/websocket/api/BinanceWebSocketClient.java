package com.demo.nettyserver.websocket.api;

import com.demo.nettyserver.websocket.WebSocketClient;
import com.demo.nettyserver.websocket.WebSocketService;
import org.apache.log4j.Logger;

public class BinanceWebSocketClient extends WebSocketClient {
	
	private Logger log = Logger.getLogger(BinanceWebSocketClient.class);
	private static String WSURL = "wss://stream.binance.com:9443/ws/%s";

	public BinanceWebSocketClient(String path) {
		
		this.service = new WebSocketService(){

			@Override
			public void onReceive(String msg) {
				handleMsg(msg);
			}

			@Override
			public void onConnected() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onDisConnected() {
				// TODO Auto-generated method stub
				
			}
			
		};
		setUrl(WSURL, path);
	}

	@Override
	public void sentPing() {

	}

	@Override
	public void reConnect() throws Exception {
		
	}

	@Override
	public boolean isTimeOut() {
		return false;
	}

	private void handleMsg(String msg) {

	}

}
