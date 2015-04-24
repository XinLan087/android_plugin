package com.apkplugin.android.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.res.XmlResourceParser;

/**
 * 基础插件组件
 * @author XinLan
 */
public abstract class BasePluginComponent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7252295727771411242L;
	/**
	 * 路径
	 */
	private String mApkPath = null;

	public void setApkPath(String mApkPath) {
		this.mApkPath = mApkPath;
	}

	public String getApkPath() {
		return mApkPath;
	}

	public String dump() {
		return dumpAllAttr();
	}

	/**
	 * Dump 出所有基本属性
	 * 
	 * @return
	 */
	protected String dumpAllAttr() {
		Field[] fields = getClass().getFields();
		StringBuffer b = new StringBuffer();
		b.append("(ClassName=").append(getClass().getSimpleName()).append(" ");
		for (int i = 0; i < fields.length; i++) {
			if (!isPrimitType(fields[i])) {
				continue;
			}
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName().substring(0, 1)
					.toUpperCase()
					+ fields[i].getName().substring(1);
			try {
				Method getMethod = getClass().getMethod("get" + fieldName,
						new Class[] {});
				b.append(" ").append(fieldName).append("=")
						.append(getMethod.invoke(this, new Object[] {}))
						.append(" ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		b.append(")");
		return b.toString();
	}

	/**
	 * 字段是否是基础类型
	 * 
	 * @param field
	 * @return
	 */
	public boolean isPrimitType(Field field) {
		String pname = field.getType().getName();
		if (pname.equals(String.class.getName())) {
			return true;
		} else if (pname.equals(Integer.class.getName()) || "int".equals(pname)) {
			return true;
		} else if (pname.equals(Boolean.class.getName())
				|| "boolean".equals(pname)) {
			return true;
		} else if (pname.equals(Float.class.getName()) || "float".equals(pname)) {
			return true;
		}
		return false;
	}

	/**
	 * 从XmlResourceParser解析出字段值，直接设置到对应的字段中去
	 * 
	 * @param prefix
	 * @param xmlParser
	 */
	public void addAttr(String prefix, XmlResourceParser xmlParser) {
		String tag = "BasePluginComponent:addAttr";
		Field[] f = getClass().getFields();
		int size = f.length;
		for (int i = 0; i < size; i++) {
			Field field = f[i];
			field.setAccessible(true);
			try {
				String fieldname = field.getName();
				Class type = field.getType();
				String pname = type.getName();
				Object value = null;
				// 判断数据类型，取得对应不同的方法，进行处理
				if (pname.equals(String.class.getName())) {
					value = xmlParser.getAttributeValue(prefix, fieldname);

				} else if (pname.equals(Integer.class.getName())
						|| "int".equals(pname)) {
					value = xmlParser
							.getAttributeIntValue(prefix, fieldname, 0);
				} else if (pname.equals(Boolean.class.getName())
						|| "boolean".equals(pname)) {
					value = xmlParser.getAttributeBooleanValue(prefix,
							fieldname, false);
				} else if (pname.equals(Float.class.getName())
						|| "float".equals(pname)) {
					value = xmlParser.getAttributeFloatValue(prefix, fieldname,
							0f);
				}

				if (value != null) {
					field.set(this, value);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
