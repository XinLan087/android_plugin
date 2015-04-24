#include "com_example_testndk_Good.h"
#include <stdio.h>
#include "t/student.h"
jstring JNICALL Java_com_example_testndk_Good_testx(JNIEnv *env, jobject jobj) {

//调用静态方法
//查找到对应的class
	jclass Good = env->FindClass("com/example/testndk/Good");
//查找到对应的方法
	jmethodID stm = env->GetStaticMethodID(Good, "test2", "()V");
//调用静态方法
	env->CallStaticVoidMethod(Good, stm);

	//普通方法
	jmethodID construction_id = env->GetMethodID(Good, "<init>", "()V");
	//实例
	jobject mTestProvider = env->NewObject(Good, construction_id);
	jmethodID ctm = env->GetMethodID(Good, "testcommon",
			"()Ljava/lang/String;");
	env->CallObjectMethod(mTestProvider, ctm);
	Student d;
	return env->NewStringUTF(d.display());
}

