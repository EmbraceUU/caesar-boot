package com.demo.nettyserver.util;

import java.io.IOException;
import java.util.Properties;

public class ConfigUtils extends Properties {
	
	private static final long serialVersionUID = -8983785422927674763L;
	
	public ConfigUtils(String properties){
		try {
			load(ConfigUtils.class.getClassLoader().getResourceAsStream(properties));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ConfigUtils(Properties defaults) {
        super(defaults);
    }
}
