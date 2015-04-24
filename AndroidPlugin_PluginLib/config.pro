-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity{*;}
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service{*;}
-keep public class * extends android.content.BroadcastReceiver{*;}
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.support.v4.app.Fragment{*;}

-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keep public class com.apkplugin.android.PluginManager{*;}
-keep public class com.apkplugin.android.callback.**{*;}
-keep public class com.apkplugin.android.model.**{*;}


-keep public class com.apkplugin.android.activity.IActivityLife{*;}
-keep public class com.apkplugin.android.fragment.IFragmentLife{*;}
-keep public class com.apkplugin.android.fragment.PluginFragmentPagerAdapter{*;}
-keep public class com.apkplugin.android.fragment.PluginFragmentPagerAdapter$**{*;}
-keep public class com.apkplugin.android.fragment.FragmentPagerAdapter{*;}
-keep public class com.apkplugin.android.fragment.OnExtraPageChangeListener{*;}
-keep public class com.apkplugin.android.fragment.FragmentPagerAdapter$**{*;}
-keep public class com.apkplugin.android.jar.BasePluginJar{*;}
-keep public class com.apkplugin.android.jar.JarContext{*;}
-keep public class com.apkplugin.android.receiver.IReceiverLife{*;}
-keep public class com.apkplugin.android.service.IServiceLife{*;}
-keep public class com.apkplugin.android.utils.PluginLog{*;}
-keep public class com.apkplugin.android.utils.DLUtils{*;}




-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
