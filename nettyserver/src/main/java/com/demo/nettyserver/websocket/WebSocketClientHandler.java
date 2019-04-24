package com.demo.nettyserver.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private Logger log = Logger.getLogger(WebSocketClientHandler.class);
    private final WebSocketClientHandshaker handshaker;
    private WebSocketService service;
    private MoniterTask moniter;
    private ChannelPromise handshakeFuture;

    WebSocketClientHandler(WebSocketClientHandshaker handshaker, WebSocketService service, MoniterTask moniter) {
        this.handshaker = handshaker;
        this.service = service;
        this.moniter = moniter;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handshaker.handshake(ctx.channel());
        service.onConnected();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.error("channelInactive!");
        service.onDisConnected();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()){
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (moniter != null)
            moniter.updateTime();

        if (!handshaker.isHandshakeComplete()) {
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            log.info("WebSocket Client connected!");
            service.onConnected();
            handshakeFuture.setSuccess();
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content="
                    + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame){
            TextWebSocketFrame textFrame = (TextWebSocketFrame)frame;
            service.onReceive(textFrame.text());
        } else if (frame instanceof BinaryWebSocketFrame){
            BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) frame;
            service.onReceive(decodeByteBuff(binaryFrame.content()));
        } else if (frame instanceof PongWebSocketFrame) {
            log.info("WebSocket Client received pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            log.info("WebSocket Client received closing");
            ch.close();
        }else if(frame instanceof PingWebSocketFrame) {
            log.info("WebSocket Client received ping");
            ch.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            log.info("response pong");
        }
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    private String decodeByteBuff(ByteBuf buf) throws IOException, DataFormatException {
        byte[] temp = new byte[buf.readableBytes()];
        ByteBufInputStream bis = new ByteBufInputStream(buf);
        bis.read(temp);
        bis.close();
        Inflater decompresser = new Inflater(true);
        decompresser.setInput(temp, 0, temp.length);
        StringBuilder sb = new StringBuilder();
        byte[] result = new byte[1024];
        while (!decompresser.finished()) {
            int resultLength = decompresser.inflate(result);
            sb.append(new String(result, 0, resultLength, "UTF-8"));
        }
        decompresser.end();
        return sb.toString();
    }

}
