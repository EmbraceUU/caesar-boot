package com.demo.commonserver.jobs;

import com.mini.service.impl.BitmexSymbolService;
import com.mini.util.HttpUtil;
import org.apache.log4j.Logger;

public class BitMexSymbolJob implements Job {
	public static final Logger logger = Logger.getLogger(BitMexSymbolJob.class);
	public static final String ACTIVEURL = "https://www.bitmex.com/api/v1/instrument/active";
	public static final String ACTIVEINTERVALSURL = "https://www.bitmex.com/api/v1/instrument/activeIntervals";

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		logger.info("[Bitmex symbol update start !!!]");
		String symbolSource = "";
		String intervalSource = "";
		try{
			// 获得原始币对信息
			symbolSource = HttpUtil.get(ACTIVEURL); 
			// 获得原始币对周期信息
			intervalSource = HttpUtil.get(ACTIVEINTERVALSURL); 
			BitmexSymbolService.symbolMethod(symbolSource, intervalSource);
			logger.info("[Bitmex symbol update end !!!]");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[Bitmex symbol update error !!!]");
		}
	}

	@Override
	public void execute(String[] args) {
		
	}

}
