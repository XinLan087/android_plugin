package com.apkplugin.android.utils;

import java.lang.reflect.Method;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.os.Bundle;

import com.apkplugin.android.constanst.PluginConst;
import com.apkplugin.android.proxy.BaseProxyActivity;
import com.apkplugin.android.proxy.BaseProxyFragmentActivity;
import com.apkplugin.android.proxy.BaseProxyListActivity;
import com.apkplugin.android.service.BaseProxyService;

/**
 * 代理工具，用于查找代理类和继承类之间的关系
 * 
 * @author wangbx
 * 
 */
public class ProxyActionUtil {
	private static ProxyActionUtil proxyUtil = null;
	/**
	 * ACTION 与启动代理模型对应表
	 */
	private HashMap<String, ProxyActionModel> PROXY_ACTION_VS_CLASSES_MAP = null;
	/**
	 * 上下文对象
	 */
	private Context context;

	/**
	 * 插件代理类模型，包含了代理启动类路径和action name
	 * 
	 * @author wangbx
	 * 
	 */
	public static class ProxyActionModel {
		/**
		 * 类名
		 */
		public String proxyClassName;
		/**
		 * ACTION 名称
		 */
		public String proxyActionName;

		public ProxyActionModel() {
		}

		public ProxyActionModel(String proxyActionName, String proxyClassName) {
			this.proxyClassName = proxyClassName;
			this.proxyActionName = proxyActionName;
		}
	}

	private ProxyActionUtil(Context context) {
		this.context = context;
		PROXY_ACTION_VS_CLASSES_MAP = new HashMap<String, ProxyActionModel>();
	}

	/**
	 * 取得ProxyUtil实例对象
	 * 
	 * @param context
	 * @return
	 */
	public static ProxyActionUtil getInstance(Context context) {
		if (proxyUtil == null) {
			proxyUtil = new ProxyActionUtil(context);
		}
		return proxyUtil;
	}

	/**
	 * 注册actionname 与代理 类名关系
	 * 
	 * @param proxyActionName
	 * @param proxyClassName
	 */
	public void registerProxyActionAndClass(String proxyActionName,
			String proxyClassName) {
		String TAG = "ProxyUtil:registerProxyActionAndClass";
		PluginLog.d(TAG, "register proxyActionName :" + proxyActionName
				+ " proxyClassName:" + proxyClassName);
		PROXY_ACTION_VS_CLASSES_MAP.put(proxyActionName, new ProxyActionModel(
				proxyActionName, proxyClassName));
	}

	/**
	 * 注册代理关系模型
	 * 
	 * @param proxyAction
	 */
	public void registerProxyModel(ProxyActionModel proxyAction) {
		String TAG = "ProxyUtil:registerProxyModel";
		if (proxyAction != null && proxyAction.proxyActionName != null) {
			PluginLog.d(TAG, "register proxyActionName :"
					+ proxyAction.proxyActionName + " proxyClassName:"
					+ proxyAction.proxyClassName);
			PROXY_ACTION_VS_CLASSES_MAP.put(proxyAction.proxyActionName,
					proxyAction);
		}
	}

	/**
	 * 通过类名找到对应的启动ACTION
	 * 
	 * @param context
	 * @param apkPath
	 *            插件的路径
	 * @param classPath
	 *            插件中的某个类路径
	 * @return
	 */
	public String getProxyClassAction(String apkPath, String classPath) {
		HostClassLoader mLocalClassLoader = HostClassLoader.getClassLoader(
				apkPath, context, context.getClassLoader());
		try {
			Class<?> localClass = mLocalClassLoader.loadClass(classPath);
			if (localClass == null) {
				return null;
			}
			// 反射类名实例，调用getProxyAction,取得代理类的action名称
			Object object = localClass.newInstance();
			Method method = localClass.getMethod("getProxyAction",
					new Class[] {});
			if (method != null) {
				Object result = method.invoke(object, new Object[] {});
				if (result != null) {
					object = null;
					method = null;
					return result.toString();
				}
			}
			object = null;
			method = null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
		}
		return null;
	}

	/**
	 * 取得要启动的className对应的Intent
	 * 
	 * @param mProxyActivity
	 *            代理Activity
	 * @param thisActivity
	 *            实际的Activity
	 * @param mDexPath
	 *            apk路径
	 * @param className
	 *            类名
	 * @param bundle
	 *            参数bundle
	 * @return
	 */
	public Intent getStartActivityIntent(Activity mProxyActivity,
			Activity thisActivity, String mDexPath, String className,
			Bundle bundle) {
		if (mProxyActivity == thisActivity) {
			Intent intent = new Intent();
			if (bundle != null) {
				intent.putExtras(bundle);
			}
			intent.setClassName(thisActivity, className);
			return intent;
		} else {
			Intent intent = getProxyIntent(mDexPath, className);
			if (bundle != null) {
				intent.putExtras(bundle);
			}
			return intent;
		}

	}

	/**
	 * 取得某个插件类对应的 启动代理类的Intent
	 * 
	 * @param context
	 * @param apkPath
	 *            插件路径
	 * @param classPath
	 *            插件类路径
	 * @return
	 */
	public Intent getProxyIntent(String apkPath, String classPath) {
		String TAG = "ProxyUtil:getProxyIntent";
		PluginLog.d(TAG, "Get Plugin Intent ,apk path:" + apkPath
				+ " class path:" + classPath);
		String action = getProxyClassAction(apkPath, classPath);
		PluginLog.d(TAG, "Get proxy action :" + action);
		if (action == null) {
			return null;
		}
		// 取得对应的代理模型
		ProxyActionModel pp = PROXY_ACTION_VS_CLASSES_MAP.get(action);
		if (pp == null) {
			PluginLog.d(TAG, "can not register " + action
					+ " relate class,return null ");
			return null;
		}

		Intent i = new Intent(action);
		i.addCategory(Intent.ACTION_DEFAULT);
		i.setComponent(new ComponentName(context, pp.proxyClassName));
		i.putExtra(PluginConst.EXTRA_DEX_PATH, apkPath);
		i.putExtra(PluginConst.EXTRA_CLASS, classPath);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return i;
	}

	/**
	 * 判断某个类是否是我们的代理类
	 * 
	 * @param className
	 *            类路径
	 * @return
	 */
	private boolean isProxyClasses(String className) {
		if (className == null) {
			return false;
		}
		if (className.startsWith(".")) {
			className = context.getPackageName() + className;
		}
		try {

			Class class1 = Class.forName(className);
			class1 = class1.getSuperclass();
			if (class1 == null) {
				return false;
			}
			String sucls = class1.getName();
			// BaseProxyActivity 的子类
			if (sucls.equals(BaseProxyActivity.class.getName())) {
				return true;
			} else if (sucls.equals(BaseProxyFragmentActivity.class.getName())) {
				// BaseProxyFragmentActivity 的子类
				return true;
			} else if (sucls.equals(BaseProxyListActivity.class.getName())) {
				// BaseProxyListActivity 的子类
				return true;
			} else if (sucls.equals(BaseProxyService.class.getName())) {
				// BaseProxyService 的子类
				return true;
			}
			return isProxyClasses(sucls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 初始化 宿主程序中间的，代理类和Action对应关系,此方法解析的是宿主的android manifest.xml
	 * 
	 * @param context
	 */
	public void initHostClassInAndroidManifest() {
		// 如果已经初始化过了，就不处理
		if (PROXY_ACTION_VS_CLASSES_MAP != null
				&& !PROXY_ACTION_VS_CLASSES_MAP.isEmpty()) {
			return;
		}
		XmlResourceParser xmlParser = null;
		String prefix = "http://schemas.android.com/apk/res/android";
		ProxyActionModel actionModel = null;
		try {
			Method openXmlResourceParser = AssetManager.class
					.getDeclaredMethod("openXmlResourceParser", new Class[] {
							int.class, String.class });
			xmlParser = (XmlResourceParser) openXmlResourceParser.invoke(
					context.getAssets(), new Object[] { 0,
							"AndroidManifest.xml" });
			while (xmlParser.getEventType() != XmlResourceParser.END_DOCUMENT) {
				String tagName = xmlParser.getName();
				// 开始标签
				if (xmlParser.getEventType() == XmlResourceParser.START_TAG) {
					// 处理一级节点
					if ("activity".equals(tagName)) {
						String name = xmlParser.getAttributeValue(prefix,
								"name");
						if (isProxyClasses(name)) {
							actionModel = new ProxyActionModel();
							actionModel.proxyClassName = name;
						}

					} else if ("receiver".equals(tagName)) {

					}
					if ("service".equals(tagName)) {

					}
					if ("action".equals(tagName)) {
						if (actionModel != null) {
							actionModel.proxyActionName = xmlParser
									.getAttributeValue(prefix, "name");
						}
					}
				} else if (xmlParser.getEventType() == XmlResourceParser.END_TAG) {
					if ("activity".equals(tagName)) {
						if (actionModel != null) {
							registerProxyModel(actionModel);
							actionModel = null;
						}
					}
				}
				xmlParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (xmlParser != null) {
				xmlParser.close();
			}

		}
	}

}
