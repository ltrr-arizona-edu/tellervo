#include "edu_cornell_dendro_cpgdb_UUID.h"
#include <uuid/uuid.h>

#define UUID_STRING_LENGTH 36

/*
 * Class:     edu_cornell_dendro_cpgdb_UUID
 * Method:    generateUUID
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_edu_cornell_dendro_cpgdb_UUID_generateUUID
  (JNIEnv *env, jclass c) 
{
    uuid_t u;
    char buf[UUID_STRING_LENGTH + 1];

    // generate and return the UUID
    uuid_generate(u);
    uuid_unparse(u, buf);

    return (*env)->NewStringUTF(env, buf);
}

