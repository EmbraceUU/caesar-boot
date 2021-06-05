package com.demo.commonserver.jobs;

import com.demo.commonserver.service.impl.BitmexSymbolService;
import com.demo.commonserver.utils.HttpUtil;

public class BitMexSymbolJob implements Job {
	public static final String ACTIVEURL = "https://www.bitmex.com/api/v1/instrument/active";
	public static final String ACTIVEINTERVALSURL = "https://www.bitmex.com/api/v1/instrument/activeIntervals";

	@Override
	public void execute() {
		String symbolSource = "";
		String intervalSource = "";
		try{
			// 获得原始币对信息
			symbolSource = HttpUtil.get(ACTIVEURL);
			// 获得原始币对周期信息
			intervalSource = HttpUtil.get(ACTIVEINTERVALSURL); 
			BitmexSymbolService.symbolMethod(symbolSource, intervalSource);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void execute(String[] args) {
		
	}

}
