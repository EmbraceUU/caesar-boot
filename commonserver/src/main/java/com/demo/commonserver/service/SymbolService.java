package com.demo.commonserver.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SymbolService {
	protected static Logger log = Logger.getLogger(SymbolService.class);
	public static final String OKEXFU = "OKEXFU";
	public static final String BITMEX = "BITMEX";

	/**
	 * 组装插入&更新SQL
	 * 
	 * @param param
	 * @return
	 */
	public static <K, V> String assembleInsertSql(Map<K, V> param) {
		List<String> insClmArray = new ArrayList<String>();
		List<String> insValArray = new ArrayList<String>();
		List<String> updArray = new ArrayList<String>();

		String symbol = (String) param.get("symbol");
		String exchange = (String) param.get("exchange");
		String symbolDb = exchange + "." + symbol;

		insClmArray.add("`status`");
	    insValArray.add("1");
		for (Map.Entry<K, V> paramEntry : param.entrySet()) {
			String key = (String) paramEntry.getKey();
			String value = String.valueOf(paramEntry.getValue());
			switch (key) {
				case "symbol":
					insClmArray.add("`ex_symbol`");
					insClmArray.add("`symbol`");
				    insValArray.add("'" + value + "'");
				    insValArray.add("'" + symbolDb + "'");
					break;
				case "price":
					insClmArray.add("`price_precision`");
				    insValArray.add(value);
					break;
				case "amount":
					insClmArray.add("`amount_precision`");
				    insValArray.add(value);
					break;
				case "type":
				case "hidden":
				case "scaling_ratio":
				case "lg_type":
				case "rank":
					insClmArray.add("`" + key + "`");
				    insValArray.add(value);
					break;
				case "base":
				case "quote":
				case "exchange":
				case "settle_date":
				case "settle":
				case "listing_date":
				case "funding_rate":
				case "contract_period":
				case "maxLeverage":
				case "minLeverage":
				case "section":
				case "lotSize":
				case "base_dsp":
				case "quote_dsp":
				case "base_value":
				case "position_currency":
				case "initMargin":
				case "maintMargin":
				case "takerFee":
					insClmArray.add("`" + key + "`");
				    insValArray.add("'" + value + "'");
				    break;
				default:
					break;
			}

			if ((Integer) param.get("type") == 2) {
				switch (key) {
					case "funding_rate":
						updArray.add("`" + key + "`='" + value + "'");
						break;
					case "contract_period":
						if (BITMEX.equals(exchange)) {
							break;
						}
						updArray.add("`" + key + "`='" + value + "'");
						break;
					case "rank":
						updArray.add("`" + key + "`=" + value);
						break;
					default:
						break;
				}
			} else {
				switch (key) {
					case "price":
					    updArray.add("`price_precision`=" + value + "");
					    break;
					case "amount":
						updArray.add("`amount_precision`=" + value + "");
						break;
					default:
						break;
				}
			}
		}
		updArray.add("`update_at`=now()");

		String insClmString = StringUtils.join(insClmArray, ", ");
		String insValString = StringUtils.join(insValArray, ", ");
		String updSring = StringUtils.join(updArray, ", ");
		String insertSql = "insert into tradingcenter.trd_symbols(";
		insertSql += insClmString;
		insertSql += ") values (";
		insertSql += insValString;
		insertSql += ")  on duplicate key update ";
		insertSql += updSring;
		insertSql += ";";

		return insertSql;
	}

	/**
	 * 更新废弃的币对
	 * 
	 * @param exchange
	 * @param sysdate
	 * @return
	 */
	public static String assembleUpdateSql(String exchange, String sysdate) {
		String updateSql = "update tradingcenter.trd_symbols set `status` = 0 where exchange = '"
				+ exchange + "' and update_at < '" + sysdate + "';";
		return updateSql;
	}
	
	/**
	 * 上线币对但是不发布 
	 * 过渡阶段
	 * @param exchange
	 * @return
	 */
	public static String assembleUpdateSql(String exchange) {
		String updateSql = "update tradingcenter.trd_symbols set status = 0 where exchange = '"+ exchange +"' and status = 1 ;";
		return updateSql;
	}

	/**
	 * 组装打印sql 输出到sqlout文件中
	 * 
	 * @param exchange
	 * @param sysdate
	 * @return
	 */
	public static String assembleQuerySql(String exchange, String sysdate) {
		String outToTxt = "pager cat >> /root/symbolJob/tools/sqlout;";
		String querySql = "SELECT "
				+ "	'"
				+ exchange
				+ "' AS exchange, "
				+ "	'"
				+ sysdate
				+ "' AS time, "
				+ "	( SELECT count( * ) FROM tradingcenter.trd_symbols WHERE create_at >= '"
				+ sysdate
				+ "' AND exchange = '"
				+ exchange
				+ "' AND `status` = 1) AS adds, "
				+ "	( SELECT count( * ) FROM tradingcenter.trd_symbols WHERE update_at >= '"
				+ sysdate
				+ "' AND exchange = '"
				+ exchange
				+ "' AND `status` = 0) AS deletes, "
				+ "	( SELECT count( * ) FROM tradingcenter.trd_symbols WHERE exchange = '"
				+ exchange + "' AND `status` = 1 ) AS total  " + "FROM "
				+ "DUAL;";
		String shoutUp = "nopager;";
//		String returnSql = outToTxt + querySql + shoutUp;
		String returnSql = querySql;
		return returnSql;
	}
	
	/**
	 * 废除sql
	 * @param exchange
	 * @param exSymbol
	 * @return
	 */
	public static String assembleAbolishSql(String exchange, String exSymbol){
		String oldSymbol = "o-" + exSymbol;
		String returnSql = "UPDATE `trd_symbols`  SET `status` = 0, `ex_symbol` = '" + oldSymbol + "' WHERE `exchange` = '" + exchange + "' AND `ex_symbol` = '" + exSymbol + "';";
		return returnSql;
	}
	
	/**
	 * 插入语句
	 * @param exchange
	 * @param exSymbol
	 * @return
	 */
	public static String assembleInsertSql(String exchange, String exSymbol){
		String columns = " `symbol`, `amount_precision`, `scaling_ratio`, `hidden`, `settle_date`, `settle`, `section`, `lg_type`, `type`, `maxLeverage`, `minLeverage`, `quote`, `base_value`, `price_precision`, `exchange`, `position_currency`, `base_dsp`, `listing_date`, `quote_dsp`, `base` ";
		String diffcolumns = " `status`, `contract_period`, `ex_symbol`, ";
		String diffvalues = " '0', 'q', concat( 'o-', `ex_symbol` ), ";
		String returnSql = "insert into tradingcenter.trd_symbols(" 
				+ diffcolumns
				+ columns
				+ ") select "
				+ diffvalues
				+ columns
				+ " from trd_symbols"
				+ " where exchange = '" + exchange + "' AND ex_symbol = '" + exSymbol + "';";
		return returnSql;
	}

	/**
	 * 获取当前时间 格式为:yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getSystime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String systime = df.format(new Date());
		return systime;
	}
}
