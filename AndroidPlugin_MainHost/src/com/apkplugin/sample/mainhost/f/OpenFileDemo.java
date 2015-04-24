// filename: OpenFileDemo.java
package com.apkplugin.sample.mainhost.f;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apkplugin.android.PluginManager;
import com.apkplugin.android.callback.InstallPluginCallBack;
import com.apkplugin.android.callback.InstalledPluginInfo;
import com.apkplugin.android.callback.UnInstallPluginCallBack;
import com.apkplugin.android.utils.DLUtils;
import com.apkplugin.sample.mainhost.R;

public class OpenFileDemo extends Activity implements OnItemClickListener {

	static private int openfileDialogId = 0;
	private ArrayList<PluginItem> mPluginItems = new ArrayList<PluginItem>();
	private PluginAdapter mPluginAdapter;

	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_file_demo);
		PluginManager.setLogState(true);
		// 设置单击按钮时打开文件对话框
		findViewById(R.id.button_openfile).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						showDialog(openfileDialogId);
					}
				});
		initView();
		initData();
	}

	private void initView() {
		mPluginAdapter = new PluginAdapter();
		mListView = (ListView) findViewById(R.id.plugin_list);
	}

	private void initData() {
		mListView.setAdapter(mPluginAdapter);
		mListView.setOnItemClickListener(this);

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				PluginItem info = mPluginItems.get(position);
				PluginManager.uninstallPlugin(getApplicationContext(),
						info.packageInfo.packageName,
						new UnInstallPluginCallBack() {

							@Override
							public void onUnInstallSuccess(String packageName) {
								System.out
										.println(".new UnInstallPluginCallBack() {...}.onUnInstallSuccess() 卸载成功"
												+ packageName);
								setData();
							}

							@Override
							public void onUnInstallError(int errorCode,
									String errorMsg) {
								System.out
										.println(".new UnInstallPluginCallBack() {...}.onUnInstallError() 卸载失败"
												+ errorCode + "　" + errorMsg);
							}

							@Override
							public void onPreUnInstall() {
								System.out
										.println(".new UnInstallPluginCallBack() {...}.onPreUnInstallF()");

							}
						});
				return true;
			}
		});

		setData();
	}

	private void setData() {

		PluginManager.loadingAllPlugin(getApplicationContext(), null);
		List<InstalledPluginInfo> plugins = PluginManager
				.getAllInstalledPlugin(this);
		mPluginItems.clear();
		mPluginAdapter.notifyDataSetChanged();
		if (plugins == null) {
			new Thread() {
				public void run() {
					loadAssetsPlugin();
				}
			}.start();
			return;
		}

		for (InstalledPluginInfo plugin : plugins) {
			PluginItem item = new PluginItem();
			item.pluginPath = plugin.getPath();
			item.packageInfo = DLUtils.getPackageInfo(this, item.pluginPath);
			mPluginItems.add(item);
		}
		mPluginAdapter.notifyDataSetChanged();
	}

	public void loadAssetsPlugin() {
		String[] aa;
		try {
			aa = getAssets().list("");
			System.out.println("OpenFileDemo.loadAssetsPlugin() " + aa.length);
			for (int i = 0; i < aa.length; i++) {
				if (aa[i].endsWith("apk")) {
					final File destFile = new File(getFilesDir(), aa[i]);
					copyFile(getAssets().open(aa[i]), destFile);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							installPlugin(destFile.getAbsolutePath());		
						}
					});
					
				}
				System.out.println("OpenFileDemo.loadAssetsPlugin()" + aa[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void copyFile(InputStream inputStream, File destFile) {

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(destFile);
			byte[] bb = new byte[1024];
			int readl = 0;
			while ((readl = inputStream.read(bb)) != -1) {
				fileOutputStream.write(bb, 0, readl);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
			inputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void installPlugin(String path) {
		PluginManager.installPlugin(this, path, new InstallPluginCallBack() {

			@Override
			public void onPreInstall() {
				System.out
						.println("OpenFileDemo.installPlugin(...).new InstallPluginCallBack() {...}.onPreInstall()");
			}

			@Override
			public void onInstallSuccess(boolean isUpdate) {
				System.out
						.println("OpenFileDemo.installPlugin(...).new InstallPluginCallBack() {...}.onInstallSuccess() "
								+ isUpdate);
				setData();
			}

			@Override
			public void onInstallError(int errorCode, String errorMsg) {
				System.out
						.println("OpenFileDemo.installPlugin(...).new InstallPluginCallBack() {...}.onInstallError() "
								+ errorCode + " " + errorMsg);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == openfileDialogId) {
			Map<String, Integer> images = new HashMap<String, Integer>();
			// 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹
			images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root); // 根目录图标
			images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up); // 返回上一层的图标
			images.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder); // 文件夹图标
			images.put("wav", R.drawable.filedialog_wavfile); // wav文件图标
			images.put(OpenFileDialog.sEmpty, R.drawable.filedialog_root);
			Dialog dialog = OpenFileDialog.createDialog(id, this, "打开文件",
					new CallbackBundle() {
						@Override
						public void callback(Bundle bundle) {
							String filepath = bundle.getString("path");
							setTitle(filepath); // 把文件路径显示在标题上
							installPlugin(filepath);
						}
					}, ".wav;", images);
			return dialog;
		}
		return null;
	}

	private class PluginAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public PluginAdapter() {
			mInflater = OpenFileDemo.this.getLayoutInflater();
		}

		@Override
		public int getCount() {
			return mPluginItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mPluginItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.plugin_item, parent,
						false);
				holder = new ViewHolder();
				holder.appIcon = (ImageView) convertView
						.findViewById(R.id.app_icon);
				holder.appName = (TextView) convertView
						.findViewById(R.id.app_name);
				holder.apkName = (TextView) convertView
						.findViewById(R.id.apk_name);
				holder.packageName = (TextView) convertView
						.findViewById(R.id.package_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			PluginItem item = mPluginItems.get(position);
			PackageInfo packageInfo = item.packageInfo;

			holder.appIcon.setImageDrawable(DLUtils.getAppIcon(
					OpenFileDemo.this, item.pluginPath));
			holder.appName.setText(DLUtils.getAppLabel(OpenFileDemo.this,
					item.pluginPath));
			holder.apkName.setText(item.pluginPath.substring(item.pluginPath
					.lastIndexOf(File.separatorChar) + 1));
			holder.packageName.setText(packageInfo.applicationInfo.packageName);
			return convertView;
		}
	}

	private static class ViewHolder {
		public ImageView appIcon;
		public TextView appName;
		public TextView apkName;
		public TextView packageName;
	}

	public static class PluginItem {
		public PackageInfo packageInfo;
		public String pluginPath;

		public PluginItem() {
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		String pp = mPluginItems.get(position).pluginPath;
		boolean a = PluginManager.startPlugin(this, pp);
		if (!a) {
			Toast.makeText(this, "启动" + pp + "插件失败 ", Toast.LENGTH_LONG).show();
		}
	}
}
