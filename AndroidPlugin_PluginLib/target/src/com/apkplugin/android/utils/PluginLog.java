package com.apkplugin.android.utils;

/**   
 * @Title: Logger.java
 * @Package com.uucun.adsdk.utils
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Wang Baoxi 
 * @date 2011-9-13 上午11:04:47
 * @version V1.0   
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

/**
 * @ClassName: Logger
 * @author Wang Baoxi
 * @date 2011-9-13 上午11:04:47
 * 
 */
public class PluginLog {

	/**
	 * 是否开启 D，专门用于可以关闭的信息
	 */
	public static boolean LOG_ON_D = false;
	/**
	 * 是否开启 I
	 */
	public static boolean LOG_ON_I = false;

	/**
	 * 是否开启 W
	 */
	public static boolean LOG_ON_W = false;

	/**
	 * 是否开启 E
	 */
	public static boolean LOG_ON_E = false;

	/**
	 * 是否将LOG写入到文件中
	 */
	public static boolean TO_FILE = false;

	/**
	 * LOG 在内存中的最大缓存
	 */
	private static int LOG_MAX_BUFFER = 4 * 1024;

	/**
	 * LOG 緩存對象
	 */
	private static StringBuffer LOG_BUFFER = new StringBuffer();

	/**
	 * Logger的打印文件夹
	 */
	public static String LOG_DIR = "pluginlogs";

	public static void closeDebugInfo() {
		LOG_ON_D = false;
	}

	/**
	 * error message
	 * 
	 * @Title: e
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (msg == null)
			return;
		if (TO_FILE) {
			log2Buffer("E", tag, msg);
		}
		if (!LOG_ON_E)
			return;
		Log.e(tag, msg);
	}

	/**
	 * info message
	 * 
	 * @Title: i
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (msg == null)
			return;
		if (TO_FILE) {
			log2Buffer("I", tag, msg);
		}
		if (!LOG_ON_I)
			return;
		Log.i(tag, msg);
	}

	/**
	 * warn message
	 * 
	 * @Title: w
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag, String msg) {
		if (msg == null)
			return;
		if (TO_FILE) {
			log2Buffer("W", tag, msg);
		}
		if (!LOG_ON_W)
			return;
		Log.w(tag, msg);
	}

	/**
	 * debug message
	 * 
	 * @Title: d
	 * @param tag
	 *            tag
	 * @param msg
	 *            message to print
	 */
	public static void d(String tag, String msg) {
		if (msg == null)
			return;
		if (TO_FILE) {
			log2Buffer("D", tag, msg);
		}
		if (!LOG_ON_D)
			return;
		Log.d(tag, msg);
	}

	/**
	 * Log 到 String Buffer中
	 * 
	 * @Title: log2Buffer
	 * @param logType
	 * @param tag
	 * @param msg
	 */
	private static void log2Buffer(String logType, String tag, String msg) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stacks = new Throwable().getStackTrace();
		if (stacks == null || stacks.length < 3) {
			return;
		}
		sb.append(logType).append("|")
				.append(dateToString(new Date(), FORMAT_DATETIME)).append("|")
				.append(stacks[2].getClassName()).append("|")
				.append(stacks[2].getMethodName()).append("|")
				.append(stacks[2].getLineNumber());
		LOG_BUFFER.append(sb.toString()).append("|").append(msg).append("\n");
		sb = null;
		if (LOG_BUFFER.length() >= LOG_MAX_BUFFER) {
			new Thread() {
				public void run() {
					writeLog2File(false);
				}
			}.start();
		}
	}

	/**
	 * 写入到文件
	 * 
	 * @Title: write2File
	 * @param isForce
	 *            是否是強制寫入
	 */
	private synchronized static void writeLog2File(boolean isForce) {
		if (!isForce) {
			if (LOG_BUFFER.length() < LOG_MAX_BUFFER) {
				return;
			}
		}
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
		String fileName = dateToString(new Date(), FORMAT_DATE) + ".log";
		File fileDir = new File(Environment.getExternalStorageDirectory(),
				LOG_DIR);
		File logFile = new File(fileDir, fileName);
		String s = LOG_BUFFER.toString();
		LOG_BUFFER.delete(0, LOG_BUFFER.length());
		FileWriter fileOutputStream = null;
		try {
			if (!fileDir.exists()) {
				if (!fileDir.mkdirs()) {
					return;
				}
			}
			if (!logFile.exists()) {
				if (!logFile.createNewFile()) {
					return;
				}
			}

			fileOutputStream = new FileWriter(logFile, true);
			fileOutputStream.write(s);
			fileOutputStream.flush();
		} catch (Exception e) {
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
				}
			}
		}
		cleanLogFile(fileDir);
		fileName = null;
		fileOutputStream = null;
		fileDir = null;
		logFile = null;
	}

	/**
	 * 设置LOG状态
	 * 
	 * @Title: setLogState
	 * @param on
	 */
	public static void setLogState(boolean on) {
		PluginLog.LOG_ON_E = on;
		PluginLog.LOG_ON_I = on;
		PluginLog.LOG_ON_W = on;
		PluginLog.LOG_ON_D = on;
		PluginLog.TO_FILE = on;
		File file = new File(Environment.getExternalStorageDirectory(), "wb.l");
		if (file.exists()) {
			LOG_ON_E = true;
			LOG_ON_I = true;
			LOG_ON_W = true;
			LOG_ON_D = true;
			TO_FILE = true;
		}
	}

	/**
	 * Date 2 String
	 * 
	 * @Title: dateToString
	 * @param source
	 * @param format
	 * @return
	 */
	public static String dateToString(Date source, String format) {
		if (source == null) {
			return null;
		}
		String tmpString = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		try {
			tmpString = simpleDateFormat.format(source);
		} catch (Exception e) {
		}
		return tmpString;
	}

	/**
	 * 取得今天和前几天时间数组
	 * 
	 * @param x
	 *            前几天
	 * @return
	 */
	public static String[] getDateAndPrev(int x) {
		Calendar now = Calendar.getInstance();
		if (x <= 0) {
			return new String[] { dateToString(new Date(), FORMAT_DATE) };
		}
		String[] s = new String[x + 1];
		s[0] = dateToString(new Date(), FORMAT_DATE);
		for (int i = 0; i < x; i++) {
			now.roll(Calendar.DAY_OF_YEAR, -1);
			String sd = dateToString(now.getTime(), FORMAT_DATE);
			s[i + 1] = sd;
		}
		return s;
	}

	/**
	 * 清除多余的日志文件
	 * 
	 * @param fileDir
	 */
	private static void cleanLogFile(File fileDir) {
		// 删除多余的文件
		String[] dates = getDateAndPrev(1);
		if (dates != null && dates.length > 0) {
			File[] files = fileDir.listFiles();
			int dal = dates.length;
			int length = (files != null ? files.length : 0);
			for (int i = 0; i < length; i++) {
				File ff = files[i];
				boolean isDelete = true;
				String ffName = ff.getName();
				for (int j = 0; j < dal; j++) {
					String dd = dates[j];
					if (ffName.startsWith(dd) && ffName.endsWith("log")) {
						isDelete = false;
						break;
					}
				}
				if (isDelete) {
					boolean f = ff.delete();
				}
			}
		}
	}

	public final static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public final static String FORMAT_DATE = "yyyy-MM-dd";

	/**
	 * @Title: clear
	 */
	public static void clear() {
		new Thread() {
			public void run() {
				writeLog2File(true);
			}
		}.start();
	}

	/**
	 * 计算开始时间到调用此方法的时间差
	 * 
	 * @Title: countTime
	 * @param startTime
	 * @return
	 */
	public static float countTime(long startTime) {
		return (System.currentTimeMillis() - startTime) / 1000f;
	}
}
