package com.demo.commonserver.entity;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BitmexSymbol {
	
	public static final int DEFAULTTYPE = 2;
	public static final int PRICEPERCISION = -1;
	public static final int AMOUNTPRECISION = -1;
	public static final String EXCHANGE = "BITMEX";
	public static final double INDEXFUTURES = -1;
	public static final double ETHUSDXBt = 0.000001;
	public static final String SETTLED = "Settled";
	public static final String OPEN = "Open";
	public static final String MINLEVERAGE = "0.01";
	
	private String symbol;
	private String typ;
	private String listing; // 上线时间
	private String settle; // 结算时间
	private String underlying;
	private String quoteCurrency;
	private Integer lotSize; // 最小合约张数
	private String settlCurrency; // 结算货币
	private String initMargin; // 起始保证金
	private String maintMargin; // 维持保证金
	private BigDecimal fundingRate; // 资金费率
	private String interval; // 合约周期
	@SuppressWarnings("rawtypes")
	private List intervals;
	@SuppressWarnings("rawtypes")
	private List symbols;
	private String exchange;
	private int price_precision;
	private int amount_precision;
	private int type; 
	private BigDecimal markPrice;
	private String positionCurrency;
	private BigDecimal takerFee;
	private double scalingRatio;
	private String state;
	private String section;

	public BitmexSymbol(JSONObject obj) {
		this.setSymbol(getValue(obj, "symbol"));
		this.setTyp(getValue(obj, "typ"));
		this.setListing(getValue(obj, "listing"));
		this.setSettle(getValue(obj, "settle"));
		this.setUnderlying(getValue(obj, "underlying"));
		this.setQuoteCurrency(getValue(obj, "quoteCurrency"));
		this.setLotSize(getValue(obj, "lotSize"));
		this.setSettlCurrency(getValue(obj, "settlCurrency"));
		this.setInitMargin(getValue(obj, "initMargin").toString());
		this.setFundingRate(getValue(obj, "fundingRate"));
		this.setMarkPrice(getValue(obj, "markPrice"));
		this.setPositionCurrency(getValue(obj, "positionCurrency"));
		this.setMaintMargin(getValue(obj, "maintMargin").toString());
		this.setTakerFee(getValue(obj, "takerFee"));
		this.setScalingRatio(0);
		this.setState(getValue(obj, "state"));
		this.exchange = EXCHANGE;
		this.amount_precision = AMOUNTPRECISION;
		this.price_precision = PRICEPERCISION;
		this.type = DEFAULTTYPE;
	}
	
	public boolean isOpen(){
		return OPEN.equals(getState());
	}
	
	public BigDecimal getTakerFee() {
		return takerFee;
	}

	public void setTakerFee(BigDecimal takerFee) {
		this.takerFee = takerFee;
	} 

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getListing() throws Exception {
		return timeFormat(this.listing);
	}

	public void setListing(String listing) {
		this.listing = listing;
	}

	public String getSettle() throws Exception {
		return timeFormat(this.settle);
	}

	public void setSettle(String settle) {
		this.settle = settle;
	}

	public String getUnderlying() { 
		String baseMapping;
		String base = this.underlying.trim();
		switch(base){
		case "XBT":
			baseMapping = "BTC";
			break;
		default:
			baseMapping = base;
		}
		return baseMapping;
	}

	public void setUnderlying(String underlying) {
		this.underlying = underlying;
	}

	public String getQuoteCurrency() {
		String quoteMapping ;
		String quote = this.quoteCurrency.trim();
		switch(quote){
		case "XBT":
			quoteMapping = "BTC";
			break;
		default:
			quoteMapping = quote;
		}
		return quoteMapping; 
	}

	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}

	public Integer getLotSize() {
		return lotSize;
	}

	public void setLotSize(Integer lotSize) {
		this.lotSize = lotSize;
	}

	public String getSettlCurrency() {
		String settlMapping ;
		String settle = this.settlCurrency.trim();
		switch(settle){
		case "XBt":
			settlMapping = "BTC";
			break;
		default:
			settlMapping = settle;
		}
		return settlMapping; 
	}

	public void setSettlCurrency(String settlCurrency) {
		this.settlCurrency = settlCurrency;
	}

	public String getInitMargin() {
		return initMargin;
	}

	public void setInitMargin(String initMargin) {
		this.initMargin = initMargin;
	}

	public BigDecimal getFundingRate() {
		return fundingRate;
	}

	public void setFundingRate(BigDecimal fundingRate) {
		this.fundingRate = fundingRate;
	}
	
	public String getPositionCurrency() {
		String positionMapping ;
		String position = this.symbol.trim();
		switch(position){
		case "ETHUSD":
			positionMapping = "张";
			break;
		default:
			positionMapping = this.positionCurrency;
			break;
		}
		return positionMapping;  
	}

	public void setPositionCurrency(String positionCurrency) {
		this.positionCurrency = positionCurrency;
	}

	public String getInterval() {
		switch (interval) {
		case "daily":
			return "d";
		case "weekly":
			return "w";
		case "biweekly":
			return "bw";
		case "monthly":
			return "m";
		case "bimonthly":
			return "bm";
		case "quarterly":
			return "q";
		case "biquarterly":
			return "bq";
		case "perpetual":
			return "p";
		default:
			return "w";
		}
	}

	public void setInterval() throws Exception {
		for (int i = 0; i < this.symbols.size(); i++) {
			if (this.symbol.equals(this.symbols.get(i).toString())) {
				this.interval = this.intervals.get(i).toString().split(":")[1];
				return;
			}
		}
		this.interval = "null";
	}
	
	public BigDecimal getMarkPrice() {
		return markPrice;
	}

	public void setMarkPrice(BigDecimal markPrice) {
		this.markPrice = markPrice;
	}

	public double getMaxLeverage() {
		BigDecimal bddend = new BigDecimal("1");
		BigDecimal bdsor = new BigDecimal(this.initMargin);
		double result = 0.0;
		try {
			result = bddend.divide(bdsor, 2, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String getBaseDsp() {
		String baseMapping ;
		String base = this.underlying.trim();
		switch(base){
		case "XBT":
			baseMapping = "BTC";
			break;
		default:
			baseMapping = base;
		}
		return baseMapping;
	}
	
	public String getQuoteDsp() throws Exception {
		String settleDate = this.getSettle();
		if (StringUtils.isNotBlank(this.getSettle())) {
			String[] dateArr = settleDate.trim().split("-| ");
	    	String month = dateArr[1];
	    	String day = dateArr[2];
	        return String.format("%s%s", month, day);
		} 
    	return "-1";
	}
	
	public String getBaseValue() throws Exception {
		BigDecimal mp = getMarkPrice();
		BigDecimal div = new BigDecimal("1");
		switch(getSymbol()){
			case "XBTUSD":
				return div.divide(mp, 8, RoundingMode.FLOOR).toPlainString();
			case "ETHUSD":
				return mp.multiply(new BigDecimal("0.000001")).toPlainString();
			default :
				if ("USD".equals(getQuoteCurrency())) {
					return div.divide(mp, 8, RoundingMode.FLOOR).toPlainString();
				}else{
					return mp.toPlainString();
				}
		} 
	}
	
	public String getMaintMargin() {
		return maintMargin;
	}

	public void setMaintMargin(String maintMargin) {
		this.maintMargin = maintMargin;
	} 
	
	public double getScalingRatio() {
		String base = getUnderlying();
		if (!StringUtils.isEmpty(base) && "BTC".equals(base)) {
			return INDEXFUTURES;
		}
		String symbol = getSymbol();
		if (!StringUtils.isEmpty(symbol) && "ETHUSD".equals(symbol)) {
			return ETHUSDXBt;
		}
		return scalingRatio;
	}

	public void setScalingRatio(double scalingRatio) {
		this.scalingRatio = scalingRatio;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getSection() {
		String base = this.getUnderlying();
		switch(base){
		case "BTC":
			return "1,2,3,5,10,25,50,100";
		case "ADA":
		case "BCH":
		case "EOS":
		case "TRX":
		case "XRP":
			return "1,2,3,4,5,10,15,20";
		case "ETH":
			return "1,2,3,5,10,25,35,50";
		case "LTC":
			return "1,2,3,5,10,25,33.3";
		default:
			return "";
		}
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String timeFormat(String timestamp) throws Exception {
		String time;
		if (timestamp == null) {
			return null;
		}
		time = timestamp.substring(0, 19);
		String[] strs = time.split("T");
		time = strs[0] + " " + strs[1];
		time = dateToStamp(time);
		return time;
	}

	public void transforIntervals(JSONObject obj) {
		this.intervals = (List) obj.getOrDefault("intervals", null);
		this.symbols = (List) obj.getOrDefault("symbols", null);
	}

	// 组装参数
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap assembleParams(HashMap param) throws Exception {
		param.put("symbol", this.getSymbol());
		param.put("price", this.price_precision);
		param.put("amount", this.amount_precision);
		param.put("base", this.getUnderlying());
		param.put("quote", this.getQuoteCurrency());
		param.put("exchange", this.exchange);
		if (StringUtils.isNotBlank(this.getSettle())) {
			param.put("settle_date", this.getSettle());
		}
		param.put("listing_date", this.getListing());
		param.put("settle", this.getSettlCurrency());
		param.put("type", this.type);
		if (this.getFundingRate() != null) {
			param.put("funding_rate", this.getFundingRate());
		}
		param.put("maxLeverage", this.getMaxLeverage());
		param.put("minLeverage", MINLEVERAGE);
		param.put("section", this.getSection());
		param.put("lotSize", this.getLotSize());
		param.put("contract_period", getInterval());
		param.put("base_dsp", this.getBaseDsp());
		param.put("quote_dsp", this.getQuoteDsp());
		param.put("base_value", this.getBaseValue());
		param.put("position_currency", this.getPositionCurrency());
		if (!StringUtils.isEmpty(this.getInitMargin())) {
			param.put("initMargin", this.getInitMargin());
		}
		if (!StringUtils.isEmpty(this.getMaintMargin())) {
			param.put("maintMargin", this.getMaintMargin());
		}
		if (this.getTakerFee() != null) {
			param.put("takerFee", this.getTakerFee());
		}
		if (getScalingRatio() != 0) {
			param.put("scaling_ratio", this.getScalingRatio());
		}
		param.put("hidden", 1);
		return param;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getValue(JSONObject obj, String key) {
		if (obj.containsKey(key)) {
			return (T) obj.getOrDefault(key, null);
		} else {
			return null;
		}
	}

	// 转换时间戳 增加8小时
	public static String dateToStamp(String s) throws ParseException {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime() + (1000 * 60 * 60 * 8); 
		res = simpleDateFormat.format(new Date(ts));
		return res;
	}
}
