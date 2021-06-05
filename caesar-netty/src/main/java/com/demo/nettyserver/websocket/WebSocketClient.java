package com.demo.nettyserver.websocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public abstract class WebSocketClient {
    private Logger log = Logger.getLogger(WebSocketClient.class);

    protected WebSocketService service;
    private Timer timerTask = null;
    private MoniterTask moniter = null;
    protected EventLoopGroup group = null;
    private Bootstrap bootstrap = null;
    protected Channel channel = null;
    protected ChannelFuture future = null;
    private boolean isAlive = false;
    private String url = null;

    private boolean autoPing = true;
    private int moniterDelay = 5000;
    private int moniterPeriod = 5000;
    protected long kickTime;

    public void start() throws Exception {
        if (url == null){
            log.info("WebSocketClient start error  url can not be null");
            return;
        }

        if (service == null){
            log.info("WebSocketClient start error  WebSocketService can not be null");
            return;
        }

        moniter = new MoniterTask(this);
        this.connect();
        if (autoPing){
            if (isAlive){
                timerTask = new Timer();
                timerTask.schedule(moniter, moniterDelay, moniterPeriod);
            }
        }
    }

    public void close(){
        log.info("websocket close!");
        if (moniter != null){
            moniter.cancel();
        }
        if (group != null){
            group.shutdownGracefully();
        }
        setStatus(false);
    }
    
    public void reConnect() throws Exception {
    	log.info("websocket close!");
        if (moniter != null){
            moniter.cancel();
        }
        if (group != null){
            group.shutdownGracefully();
        }
        if (timerTask != null) {
			timerTask.cancel();
		}
        log.info("restart...");
		this.start();
	}

    protected void connect(){
        try {
            final URI uri = new URI(url);
            group = new NioEventLoopGroup(1);
            bootstrap = new Bootstrap();
            final SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
            final WebSocketClientHandler handler = new WebSocketClientHandler(WebSocketClientHandshakerFactory
                    .newHandshaker(uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders(), Integer.MAX_VALUE),
                    service, moniter);
            bootstrap.group(group).option(ChannelOption.TCP_NODELAY, true).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(sslCtx.newHandler(ch.alloc(), uri.getHost(), uri.getPort()));
                            p.addLast(new HttpClientCodec(), new HttpObjectAggregator(8192), handler);
                        }
                    });
            future = bootstrap.connect(uri.getHost(), uri.getPort()).sync();
            future.addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    final EventLoop loop = future.channel().eventLoop();
                    loop.schedule(() -> {
                        log.error("连接：{" + uri.getHost() + "}失败");
                        connect();
                    }, 1L, TimeUnit.SECONDS);
                } else {
                    log.info("连接：{" + uri.getHost() + "}成功");
                }
            });
            channel = future.sync().channel();

            handler.handshakeFuture().sync();
        } catch (InterruptedException | SSLException | URISyntaxException e) {
            e.printStackTrace();
        }

    }

    protected void sendMessage(String msg){
        if (!isAlive || channel == null){
            log.info("WebSocket is not Alive addChannel error");
            return;
        }
        channel.writeAndFlush(new TextWebSocketFrame(msg));
    }

    public void setStatus(boolean flag){
        this.isAlive = flag;
    }

    public boolean isAlive(){
        return this.isAlive;
    }

    protected void updateKickTime(long time) {
        kickTime = time;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrl(String url, String path){
        this.url = String.format(url, path);
    }

    public void setDelay(int delay) {
        this.moniterDelay = delay;
    }

    public void setPeriod(int period) {
        this.moniterPeriod = period;
    }

    public void setAutoPing(boolean autoPing) {
        this.autoPing = autoPing;
    }

    public abstract void sentPing();

    public abstract boolean isTimeOut();

}
