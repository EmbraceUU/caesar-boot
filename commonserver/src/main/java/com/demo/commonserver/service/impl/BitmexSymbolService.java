package com.demo.commonserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.commonserver.entity.BitmexSymbol;
import com.demo.commonserver.service.SymbolService;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;

public class BitmexSymbolService extends SymbolService {
	
	public static final String UPTYPE = "OCECCS";
	public static final String DOWNTYPE = "OPECCS";

	public static void symbolMethod(String symbolSource, String intervalSource) { 

		PrintWriter pwBaseInfo = null;
		StringBuilder symbolBuilder = new StringBuilder(symbolSource);
		StringBuilder intervalBuilder = new StringBuilder(intervalSource);
		String exchange = "BITMEX";

		try {
			// 生成输出文件 
			pwBaseInfo = new PrintWriter(new FileWriter(
					"/root/symbolJob/sql/bitmex.sql"), true);
			String plainSymbol = symbolBuilder.toString();
			JSONArray symbolArray = JSON.parseArray(plainSymbol);
			BitmexSymbol bs = null;
			JSONObject interMap = null;
			HashMap params = null;
			for (int i = 0; i < symbolArray.size(); i++) {
				JSONObject object = (JSONObject) symbolArray.get(i);
				String type = object.get("typ").toString();
				// 剔除期权
				if (UPTYPE.equals(type) || DOWNTYPE.equals(type)) {
					continue;
				}
				// 解析合约信息
				bs = new BitmexSymbol(object); 
				if (!bs.isOpen()) {
					continue;
				}
				interMap = JSON.parseObject(intervalBuilder.toString());
				// 解析区间列表
				bs.transforIntervals(interMap);
				// 配置区间
				bs.setInterval();
				params = bs.assembleParams(new HashMap());
				// 组装插入语句
				String updateSql = assembleInsertSql(params);
				pwBaseInfo.println(updateSql);
			}
			// 假设当前 数据库没有在更新数据 所以systime肯定会小于有更新和有新增的数据的时间 那么肯定会大于没有操作的数据的时间
			String systime = getSystime();
			String finalhandleSql = assembleUpdateSql(exchange, systime);
			pwBaseInfo.println(finalhandleSql);
			String querySql = assembleQuerySql(exchange, systime);
			pwBaseInfo.println(querySql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pwBaseInfo.close();
		}
	}
}
