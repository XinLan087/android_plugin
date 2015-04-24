package huahua.viewpager;

import android.content.Context;
import android.content.Intent;

import com.apkplugin.android.PluginManager;
import com.apkplugin.android.receiver.BasePluginReceiver;

public class RemoveReceiver extends BasePluginReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		System.out.println("RemoveReceiver.onReceive()arg1  " +arg1.getAction());
		System.out.println("RemoveReceiver.onReceive()start activity");
		
		startActivity(arg0, new Intent(arg0, MainActivity.class));
	}

}
