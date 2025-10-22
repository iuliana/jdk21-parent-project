  #include <jni.h>
  #include "ArrayHandler.h"

JNIEXPORT jobjectArray JNICALL Java_ArrayHandler_returnArray (JNIEnv *env, jobject jobj) {

    jobjectArray ret;
    int i;

    char *message[5]= {"first",
	"second",
	"third",
	"fourth",
	"fifth"};

    ret= (jobjectArray)env->NewObjectArray(5,
         env->FindClass("java/lang/String"),
         env->NewStringUTF(""));

    for(i=0;i<5;i++) {
        env->SetObjectArrayElement(ret,i,env->NewStringUTF(message[i]));
    }
    return(ret);
}

/**
The gcc compilation process has four different steps:

The preprocessing : gcc -E jw.c
The compiling :  gcc -S jw.c
The assembling :  gcc -c jw.c
The linking :  gcc jw.c -o jw

All in one:  gcc -o jw jw.c

gcc -I /Users/iuliana/.sdkman/candidates/java/jdk-21/include -I /Users/iuliana/.sdkman/candidates/java/jdk-21/include/darwin -o jw jw.c
Run : ./jw
*/