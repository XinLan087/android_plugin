package com.apkplugin.android.model;

import java.util.ArrayList;

/**
 * 基础的activity，service，receiver类
 * 
 * @author XinLan
 * 
 */
public abstract class BaseARSPluginComponent extends BasePluginComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6418042911197537845L;
	/**
	 * 对应节点:android:name class name
	 */
	public String name;
	/**
	 * 对应节点:android:label
	 */
	public String label;
	
	/**
	 * Filter集合,Activity,service,receiver,都可以含有多个filter
	 */
	public ArrayList<PluginIntentFilter2> filters2 = new ArrayList<PluginIntentFilter2>();

	public void addIntentFilter(PluginIntentFilter2 filter) {
		filters2.add(filter);
	}

	public ArrayList<PluginIntentFilter2> getIntentFilter2() {
		return filters2;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	@Override
	public String dump() {
		String currentFields = super.dump();
		StringBuffer sb = new StringBuffer();
		sb.append("{").append(getClass().getName()).append(currentFields);
		/*for (PluginIntentFilter f : filters) {
			sb.append(f.dump());
		}*/
		sb.append("};");
		return sb.toString();
	}

}
