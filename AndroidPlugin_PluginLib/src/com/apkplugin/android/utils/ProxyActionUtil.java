package com.apkplugin.android.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;

import com.apkplugin.android.constanst.PluginConst;
import com.apkplugin.android.proxy.BaseProxyActivity;
import com.apkplugin.android.proxy.BaseProxyFragmentActivity;
import com.apkplugin.android.proxy.BaseProxyListActivity;
import com.apkplugin.android.service.BaseProxyService;
import com.apkplugin.android.service.ProxyService1;
import com.apkplugin.android.service.ProxyService10;
import com.apkplugin.android.service.ProxyService11;
import com.apkplugin.android.service.ProxyService12;
import com.apkplugin.android.service.ProxyService2;
import com.apkplugin.android.service.ProxyService3;
import com.apkplugin.android.service.ProxyService4;
import com.apkplugin.android.service.ProxyService5;
import com.apkplugin.android.service.ProxyService6;
import com.apkplugin.android.service.ProxyService7;
import com.apkplugin.android.service.ProxyService8;
import com.apkplugin.android.service.ProxyService9;

/**
 * 代理工具，用于查找代理类和继承类之间的关系
 * 
 * @author XinLan
 * 
 */
public class ProxyActionUtil {
	private static ProxyActionUtil proxyUtil = null;
	/**
	 * ACTION 与启动代理模型对应表
	 */
	private HashMap<String, ProxyActionModel> PROXY_ACTION_VS_CLASSES_MAP = null;

	private Map<String, Service> RUNNING_PROXYSERVICE_LIST = null;

	/**
	 * 集合
	 */
	private List<String> DEFAULT_PROXYSERVICE_LIST = null;

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
		DEFAULT_PROXYSERVICE_LIST = new ArrayList<String>();
		RUNNING_PROXYSERVICE_LIST = new HashMap<String, Service>();
		// TODO 初始化默认的service代理列表
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService1.class.getName());
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService2.class.getName());
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService3.class.getName());
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService4.class.getName());
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService5.class.getName());
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService6.class.getName());
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService7.class.getName());
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService8.class.getName());
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService9.class.getName());
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService10.class.getName());
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService11.class.getName());
		DEFAULT_PROXYSERVICE_LIST.add(ProxyService12.class.getName());
	}

	/**
	 * 增加一个代理service运行标识
	 * 
	 * @param path
	 *            插件的路径
	 * @param className
	 *            插件的类名
	 */
	public void addRuningProxyService(String path, String className,
			Service service) {
		RUNNING_PROXYSERVICE_LIST.put(path + className, service);
	}

	/**
	 * 某个服务是否已经运行了
	 * 
	 * @param path
	 * @param className
	 * @return
	 */
	public Service getRuningProxyService(String path, String className) {
		return RUNNING_PROXYSERVICE_LIST.get(path + className);
	}

	/**
	 * 减少一个代理service运行标识
	 * 
	 * @param path
	 *            插件的路径
	 * @param className
	 *            插件的类名
	 */
	public void removeRuningProxyService(String path, String className) {
		RUNNING_PROXYSERVICE_LIST.remove(path + className);
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
			PluginLog.w(TAG, "register proxyActionName :"
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
	 * @param mDexPath
	 *            apk路径
	 * @param className
	 *            类名
	 * @param bundle
	 *            参数bundle
	 * @return
	 */
	public Intent getStartActivityIntent(Context mProxyActivity,
			String mDexPath, String className, Bundle bundle) {
		// 如果自身与代理相同，说明启动的不是插件中的类，而是宿主中的类，这里要注意
		if (mDexPath == null) {
			Intent intent = new Intent();
			if (bundle != null) {
				intent.putExtras(bundle);
			}
			intent.setClassName(mProxyActivity, className);
			return intent;
		} else {
			Intent intent = getProxyActivityIntent(mDexPath, className);
			if (bundle != null) {
				intent.putExtras(bundle);
			}
			return intent;
		}
	}

	/**
	 * 取得service
	 * 
	 * @param context
	 * @param mDexPath
	 * @param className
	 * @param bundle
	 * @return
	 */
	public Intent getStartServiceIntent(Context context, String mDexPath,
			String className, Bundle bundle) {
		// 如果自身与代理相同，说明启动的不是插件中的类，而是宿主中的类，这里要注意
		if (mDexPath == null) {
			Intent intent = new Intent();
			if (bundle != null) {
				intent.putExtras(bundle);
			}
			intent.setClassName(context, className);
			return intent;
		} else {
			Intent intent = getProxyServiceIntent(mDexPath, className);
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
	public Intent getProxyActivityIntent(String apkPath, String classPath) {
		String TAG = "ProxyUtil:getProxyActivityIntent";
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
	 * 取得service的代理
	 * 
	 * @param apkPath
	 * @param classPath
	 * @return
	 */
	public Intent getProxyServiceIntent(String apkPath, String classPath) {
		String TAG = "ProxyUtil:getProxyServiceIntent";
		PluginLog.d(TAG, "Get Plugin Intent ,apk path:" + apkPath
				+ " class path:" + classPath);
		String action = getProxyClassAction(apkPath, classPath);
		PluginLog.d(TAG, "Get proxy action :" + action);
		// TODO 如果找不到action，我们就是用默认的action，执行proxyservice
		// proxyServiceNumber中的任意一个
		if (action == null || "".equals(action.trim())) {
			// 先检查一下是否已经启动过,如果已经启动过类，就直接返回那个类名
			Service vv = getRuningProxyService(apkPath, classPath);
			if (vv != null) {
				String serviceName = "";
				serviceName = vv.getClass().getName();
				Intent i = new Intent();
				i.setComponent(new ComponentName(context, serviceName));
				i.putExtra(PluginConst.EXTRA_DEX_PATH, apkPath);
				i.putExtra(PluginConst.EXTRA_CLASS, classPath);
				Log.i(TAG, "Run old service ,path :" + apkPath + " classPath:"
						+ classPath + " service Name:" + serviceName);
				return i;
			}

			// 未启动过
			Collection<Service> bb = RUNNING_PROXYSERVICE_LIST.values();
			Collection<String> bbx = new ArrayList<String>();

			if (bb != null && !bb.isEmpty()) {
				for (Service svr : bb) {
					bbx.add(svr.getClass().getName());
				}
			}

			// 集合减法运算
			ArrayList<String> ersult = (ArrayList<String>) CollectionUtils
					.subtract(DEFAULT_PROXYSERVICE_LIST, bbx);
			if (ersult != null && !ersult.isEmpty()) {
				String serviceName = ersult.get(0);
				Intent i = new Intent();
				i.setComponent(new ComponentName(context, serviceName));
				i.putExtra(PluginConst.EXTRA_DEX_PATH, apkPath);
				i.putExtra(PluginConst.EXTRA_CLASS, classPath);
				Log.i(TAG, "Run new service ,path :" + apkPath + " classPath:"
						+ classPath + " service Name:" + serviceName);
				return i;
			} else {
				Log.e(TAG, "Running service proxy size over "
						+ DEFAULT_PROXYSERVICE_LIST.size());
			}
			return null;
		}

		// TODO 通过action，取得代理service的 类路径
		ProxyActionModel pp = PROXY_ACTION_VS_CLASSES_MAP.get(action);
		if (pp == null) {
			PluginLog.d(TAG, "can not register " + action
					+ " relate class,return null ");
			return null;
		}

		Intent i = new Intent(action);
		i.setComponent(new ComponentName(context, pp.proxyClassName));
		i.putExtra(PluginConst.EXTRA_DEX_PATH, apkPath);
		i.putExtra(PluginConst.EXTRA_CLASS, classPath);
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
					if ("activity".equals(tagName) || "service".equals(tagName)) {
						String name = xmlParser.getAttributeValue(prefix,
								"name");
						if (isProxyClasses(name)) {
							actionModel = new ProxyActionModel();
							actionModel.proxyClassName = name;
						}
					} else if ("receiver".equals(tagName)) {

					}
					if ("action".equals(tagName)) {
						if (actionModel != null) {
							String action = xmlParser.getAttributeValue(prefix,
									"name");
							if (action != null && !"".equals(action)) {
								actionModel.proxyActionName = action;
							}
						}
					}
				} else if (xmlParser.getEventType() == XmlResourceParser.END_TAG) {
					if ("activity".equals(tagName) || "service".equals(tagName)) {
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
