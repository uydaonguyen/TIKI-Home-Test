package com.thanhuy.tiki.hometest.util;

import com.google.gson.Gson;
/**
 *
 * author:uy.daonguyen@gmail.com
 */
public class GsonUtil {

	private static class Loader {
		private static Gson INSTANCE = new Gson();
	}

	public static Gson getInstance() {
		return GsonUtil.Loader.INSTANCE;
	}


}
