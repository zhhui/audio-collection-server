package com.ushow.tv.utils;

import java.util.UUID;

public final class IdGenerator {
	
	/**
	 * 生成32位字符串，UUID
	 * 
	 * @return
	 */
	public static final String getId(){
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "");
		return uuid;
	}
	
}
