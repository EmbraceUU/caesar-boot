package com.demo.commonserver.api.curlapi.curlokex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public class CurlOkex {
    static final String HOST = "https://www.okex.com";
    public static final String PATH_POSITION_SPOT = "/api/spot/v3/accounts/xrp";
    public static final String PATH_POSITION_MARGIN = "/api/margin/v3/accounts";
    public static final String PATH_ACCOUNT_SPOT = "/api/spot/v3/accounts";
    public static final String PATH_ACCOUNT_FU = "/api/futures/v3/position";
    public static final String PATH_ACCOUNT_SP = "/api/swap/v3/BTC-USD-SWAP/position";
    public static final String PATH_ORDER_FU = "/api/futures/v3/order";
    public static final String PATH_ORDER_SPOT = "/api/spot/v3/orders";
    public static final String PATH_ORDER_SWAP = "/api/swap/v3/order";
    public static final String PATH_ORDER_MARGIN = "/api/margin/v3/orders";
    static final String PATH_CANCELORDER_SPOT = "/api/spot/v3/cancel_orders/%s";
    public static final String PATH_CANCELORDER_FU = "/api/futures/v3/cancel_orders/%s";
    public static final String PATH_CANCELORDER_SP = "/api/swap/v3/cancel_order/%s/%s";
    public static final String PATH_CANCELORDER_MARGIN = "/api/margin/v3/cancel_orders/%s";
    public static final String PATH_LEVERAGE_FU = "/api/futures/v3/accounts/%s/leverage";
    public static final String PATH_LEVERAGE_SP = "/api/swap/v3/accounts/%s/leverage";
    public static final String PATH_SETTING_SP = "/api/swap/v3/accounts/%s/settings";
    public static final String PATH_ASSETS_FU = "/api/futures/v3/accounts";

    public static final String MEHTODGET = "GET";
    static final String MEHTODPOST = "POST";

    String apiKey;
    String secretKey;
    String passPhrase;
    String method;
    String timestamp;
    String url;
    String sign;
    String body;

    String sign(String timestamp, String method,
                String requestPath, String body, String secretKey) {
        String signStr = null;
        body = body == null ? "" : body;
        method = method.toUpperCase();
        String preHash = timestamp + method + requestPath + body;
        byte[] secretKeyBytes;
        SecretKeySpec secretKeySpec;
        Mac mac;
        try {
            secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
            mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            signStr = Base64.getEncoder().encodeToString(
                    mac.doFinal(preHash.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalStateException e) {
            e.printStackTrace();
        }
        return signStr;
    }

    public void init(){
        apiKey = "";
        secretKey = "";
        passPhrase = "";
        timestamp = Instant.now().toString();
    }

    public String assembleBody(){
        return "";
    }
}
