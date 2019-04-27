package com.demo.commonserver.api.curlapi.curlokex;

import com.alibaba.fastjson.JSONObject;

public class CancelOrderOkex extends CurlOkex {
    public void init() {
        super.init();
        method = MEHTODPOST;
        String reqeustPath = String.format(PATH_CANCELORDER_SPOT,"");
        url = HOST + reqeustPath;
        body = assembleBody();
        sign = sign(timestamp, method, reqeustPath, body, secreatKey);
    }

    public String assembleBody() {
        JSONObject obj = new JSONObject();
        obj.put("order_id", "");
        obj.put("instrument_id", "");
        return obj.toJSONString();
    }

    public void sysoCurl() {
        System.out
                .println(String
                        .format("curl -i -X %s -H 'Cookie:locale=zh_CN' -H 'OK-ACCESS-KEY:%s' -H 'OK-ACCESS-SIGN:%s' -H 'OK-ACCESS-TIMESTAMP:%s' -H 'OK-ACCESS-PASSPHRASE:%s' -H 'Accept: application/json' -H 'Content-Type: application/json; charset=UTF-8' -d '%s' %s",
                                MEHTODPOST, apiKey, sign, timestamp, passPhrase,
                                body, url));
    }

    public static void main(String[] args) {
        CancelOrderOkex ok = new CancelOrderOkex();
        ok.init();
        ok.sysoCurl();
    }
}
