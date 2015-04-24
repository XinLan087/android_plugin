package com.apkplugin.android.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.apkplugin.android.utils.HostClassLoader;

/**
 * 代理类的适配处理器，用于处理一些公共资源等
 * 
 * @author wangbx
 * 
 */
public class ClassLoaderAdapter {

	/**
	 * 取得插件ClassLoader
	 * 
	 * @param context
	 *            上下文
	 * @param superClassLoader
	 *            父类的ClassLoader
	 * @param dexPath
	 *            插件路径
	 * @return 生成一个新的ClassLoader
	 */
	public static ClassLoader getActivityClassLoader(final Context context,
			ClassLoader superClassLoader, final String dexPath) {
		final HostClassLoader mLocalClassLoader = HostClassLoader
				.getClassLoader(dexPath, context, superClassLoader);
		final String TAG = "ProxyActivityAdapter:getActivityClassLoader";
		ClassLoader classLoader = new ClassLoader(superClassLoader) {
			@Override
			public Class<?> loadClass(String className)
					throws ClassNotFoundException {
				Class<?> clazz = null;
				clazz = mLocalClassLoader.loadClass(className);
				Log.d(TAG, "load class:" + className + " " + clazz);
				if (clazz == null) {
					clazz = getParent().loadClass(className);
				}
				// still not found
				if (clazz == null) {
					throw new ClassNotFoundException(className);
				}
				return clazz;
			}
		};
		return classLoader;
	}

	/**
	 * 取得自定义的LayoutInflater实现
	 * 
	 * @param context
	 * @param customClassLoader
	 *            自定义的ClassLoader
	 * @return
	 */
	public static LayoutInflater getLayoutInflater(Context context,
			final ClassLoader customClassLoader) {
		LayoutInflater inflater = buildLayoutInflater(context);
		if (Build.VERSION.SDK_INT < 11) {
			setFactory(inflater, customClassLoader);
		} else {
			setFactory2(inflater, customClassLoader);
		}
		return inflater;
	}

	private static void setFactory(LayoutInflater inflater,
			final ClassLoader customClassLoader) {
		inflater.setFactory(new android.view.LayoutInflater.Factory() {
			public View onCreateView(String name, Context context,
					AttributeSet set) {
				View view = null;
				Class<?> cls;
				try {
					cls = getViewClass(customClassLoader, name);
					Constructor<?> constructor = cls.getConstructor(
							Context.class, AttributeSet.class);
					view = (View) constructor.newInstance(context, set);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

				return view;
			}
		});
	}

	private static void setFactory2(LayoutInflater inflater,
			final ClassLoader customClassLoader) {
		inflater.setFactory2(new android.view.LayoutInflater.Factory2() {
			public View onCreateView(View parent, String name, Context context,
					AttributeSet set) {
				View view = null;
				Class<?> cls;
				try {
					cls = getViewClass(customClassLoader, name);
					Constructor<?> constructor = cls.getConstructor(
							Context.class, AttributeSet.class);
					view = (View) constructor.newInstance(context, set);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

				return view;
			}

			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				return onCreateView(null, name, context, attrs);
			}
		});
	}

	/**
	 * 取得View的Class
	 * 
	 * @param customClassLoader
	 * @param name
	 *            类名，自定义的ClassLoader
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static Class<?> getViewClass(ClassLoader customClassLoader,
			String name) throws ClassNotFoundException {
		if (-1 == name.indexOf(".")) {
			try {
				Class s = View.class.getClassLoader().loadClass(
						"android.widget." + name);
				return s;
			} catch (Exception e) {
				Class s = View.class.getClassLoader().loadClass(
						"android.webkit." + name);
				return s;
			}
		} else {
			return customClassLoader.loadClass(name);
		}
	}

	/**
	 * 建立一个LayoutInflater
	 * 
	 * @param context
	 * @return
	 */
	private static LayoutInflater buildLayoutInflater(Context context) {
		return new LayoutInflater(LayoutInflater.from(context), context) {
			@Override
			public LayoutInflater cloneInContext(Context context) {
				return this;
			}
		};
	}

}
