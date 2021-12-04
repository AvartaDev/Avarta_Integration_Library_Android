/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class de_frank_durr_ecdh_curve25519_ECDHCurve25519 */

#ifndef _Included_de_frank_durr_ecdh_curve25519_ECDHCurve25519
#define _Included_de_frank_durr_ecdh_curve25519_ECDHCurve25519
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_frank_durr_ecdh_curve25519_ECDHCurve25519
 * Method:    secret_key
 * Signature: ([B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_de_frank_1durr_ecdh_1curve25519_ECDHCurve25519_secret_1key
  (JNIEnv *, jclass, jbyteArray);

/*
 * Class:     de_frank_durr_ecdh_curve25519_ECDHCurve25519
 * Method:    public_key
 * Signature: ([B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_de_frank_1durr_ecdh_1curve25519_ECDHCurve25519_public_1key
  (JNIEnv *, jclass, jbyteArray);

/*
 * Class:     de_frank_durr_ecdh_curve25519_ECDHCurve25519
 * Method:    shared_secret
 * Signature: ([B[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_de_frank_1durr_ecdh_1curve25519_ECDHCurve25519_shared_1secret
  (JNIEnv *, jclass, jbyteArray, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif