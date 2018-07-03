/*******************************************************
 * Copyright (C) 2014 iQIYI.COM - All Rights Reserved
 * 
 * This file is part of Passport.
 * Unauthorized copy of this file, via any medium is strictly prohibited.
 * Proprietary and Confidential.
 * 
 * Author(s): Feifan Yin <yinfeifan@qiyi.com>
 * 
 *******************************************************/


package com.ushow.tv.utils;

import java.util.HashMap;
import java.util.Map;

public final class MapBuilder<K, V> {
	private HashMap<K, V> map = new HashMap<K,V>();
	
	public MapBuilder<K, V> put(K key, V value){
		map.put(key, value);
		return this;
	}
	
	public HashMap<K, V> build(){
		return map;
	}
	
	public MapBuilder<K, V> putAll(Map<K, V> map){
		map.putAll(map);
		return this;
	}
}
