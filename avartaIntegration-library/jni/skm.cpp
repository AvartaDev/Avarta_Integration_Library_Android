#include <jni.h>
#include <string>
#include <stdio.h>
#include <android/log.h>
#include <sstream>
#include "aes256.h"

#define MIN(a,b) (a>b?b:a)

using namespace std;

extern "C" {

void Java_com_solus_solusconnect_helpers_test(JNIEnv *env,
													jobject _this) {
	__android_log_print(ANDROID_LOG_DEBUG, "DEBUG", "INIT");
}

bool utf8_check_is_valid(const string& string) {
	int c, i, ix, n, j;
	for (i = 0, ix = string.length(); i < ix; i++) {
		c = (unsigned char) string[i];
		//if (c==0x09 || c==0x0a || c==0x0d || (0x20 <= c && c <= 0x7e) ) n = 0; // is_printable_ascii
		if (0x00 <= c && c <= 0x7f)
			n = 0; // 0bbbbbbb
		else if ((c & 0xE0) == 0xC0)
			n = 1; // 110bbbbb
		else if (c == 0xed && i < (ix - 1)
				&& ((unsigned char) string[i + 1] & 0xa0) == 0xa0)
			return false; //U+d800 to U+dfff
		else if ((c & 0xF0) == 0xE0)
			n = 2; // 1110bbbb
		else if ((c & 0xF8) == 0xF0)
			n = 3; // 11110bbb
		//else if (($c & 0xFC) == 0xF8) n=4; // 111110bb //byte 5, unnecessary in 4 byte UTF-8
		//else if (($c & 0xFE) == 0xFC) n=5; // 1111110b //byte 6, unnecessary in 4 byte UTF-8
		else
			return false;
		for (j = 0; j < n && i < ix; j++) { // n bytes matching 10bbbbbb follow ?
			if ((++i == ix) || (((unsigned char) string[i] & 0xC0) != 0x80))
				return false;
		}
	}
	return true;
}

jstring Java_com_avarta_integrationlibrary_data_helpers_ApiCryptManager_decrypt(JNIEnv *env, jobject obj,
		jbyteArray array) {

	try {
		int len = env->GetArrayLength(array);
		unsigned char* source = new unsigned char[len];
		env->GetByteArrayRegion(array, 0, len,
				reinterpret_cast<jbyte*>(source));


unsigned char charParam[33]={51,189,6,246,172,78,55,210,232,137,31,162,129,225,56,48,124,53,65,76,150,21,117,109,104,6,244,253,243,241,66,209,0};
charParam[0]=charParam[0]^0;
charParam[1]=charParam[1]^143;
charParam[2]=charParam[2]^49;
charParam[3]=charParam[3]^206;
charParam[4]=charParam[4]^149;
charParam[5]=charParam[5]^122;
charParam[6]=charParam[6]^89;
charParam[7]=charParam[7]^228;
charParam[8]=charParam[8]^209;
charParam[9]=charParam[9]^190;
charParam[10]=charParam[10]^44;
charParam[11]=charParam[11]^219;
charParam[12]=charParam[12]^181;
charParam[13]=charParam[13]^130;
charParam[14]=charParam[14]^77;
charParam[15]=charParam[15]^3;
charParam[16]=charParam[16]^23;
charParam[17]=charParam[17]^92;
charParam[18]=charParam[18]^37;
charParam[19]=charParam[19]^42;
charParam[20]=charParam[20]^252;
charParam[21]=charParam[21]^113;
charParam[22]=charParam[22]^30;
charParam[23]=charParam[23]^1;
charParam[24]=charParam[24]^2;
charParam[25]=charParam[25]^96;
charParam[26]=charParam[26]^135;
charParam[27]=charParam[27]^145;
charParam[28]=charParam[28]^153;
charParam[29]=charParam[29]^154;
charParam[30]=charParam[30]^42;
charParam[31]=charParam[31]^169;

		std::string sName = "";
        		aes256_context ctx;
        		aes256_init(&ctx, charParam);
        		std::string bufS;

        		unsigned char decodeBase[17];

        		for (int i = 0; i < len; i += 16) {
        			for (int k = 0; k < 16; k++) {
        				if (i + k < len)
        					decodeBase[k] = (source[k + i]);
        			}
        			aes256_init(&ctx, charParam);
        			aes256_decrypt_ecb(&ctx, decodeBase);
        			decodeBase[16] = '\0';

        			aes256_done(&ctx);

        			std::string bufS((const char*) decodeBase);
        			sName += bufS.substr(0, 16);
        		}

		if (utf8_check_is_valid(sName.c_str())) {
			return env->NewStringUTF(sName.c_str());
		} else
			return NULL;
	} catch (...) {
		return NULL;
	}
}

jbyteArray Java_com_avarta_integrationlibrary_data_helpers_ApiCryptManager_encrypt(JNIEnv *env, jobject obj,
		jbyteArray array) {

	try {
		int len = env->GetArrayLength(array);
		unsigned char* source = new unsigned char[len];
		env->GetByteArrayRegion(array, 0, len,
				reinterpret_cast<jbyte*>(source));


unsigned char charParam[33]={51,189,6,246,172,78,55,210,232,137,31,162,129,225,56,48,124,53,65,76,150,21,117,109,104,6,244,253,243,241,66,209,0};
charParam[0]=charParam[0]^0;
charParam[1]=charParam[1]^143;
charParam[2]=charParam[2]^49;
charParam[3]=charParam[3]^206;
charParam[4]=charParam[4]^149;
charParam[5]=charParam[5]^122;
charParam[6]=charParam[6]^89;
charParam[7]=charParam[7]^228;
charParam[8]=charParam[8]^209;
charParam[9]=charParam[9]^190;
charParam[10]=charParam[10]^44;
charParam[11]=charParam[11]^219;
charParam[12]=charParam[12]^181;
charParam[13]=charParam[13]^130;
charParam[14]=charParam[14]^77;
charParam[15]=charParam[15]^3;
charParam[16]=charParam[16]^23;
charParam[17]=charParam[17]^92;
charParam[18]=charParam[18]^37;
charParam[19]=charParam[19]^42;
charParam[20]=charParam[20]^252;
charParam[21]=charParam[21]^113;
charParam[22]=charParam[22]^30;
charParam[23]=charParam[23]^1;
charParam[24]=charParam[24]^2;
charParam[25]=charParam[25]^96;
charParam[26]=charParam[26]^135;
charParam[27]=charParam[27]^145;
charParam[28]=charParam[28]^153;
charParam[29]=charParam[29]^154;
charParam[30]=charParam[30]^42;
charParam[31]=charParam[31]^169;

		std::string sName = "";
        		aes256_context ctx;
        		aes256_init(&ctx, charParam);
        		std::string bufS;

        		unsigned char* encodeBase = new unsigned char[16];

                int size = 0;
                if (len % 16 == 0)
                    size = len;
                else
                    size = len + 16 - (len % 16);


        		unsigned char* encodedBytes = new unsigned char[size];

        		for (int i = 0; i < len; i += 16) {
        		    memset(encodeBase, 0, 16);
        			for (int k = 0; k < 16; k++) {
        				if (i + k < len) {
        					encodeBase[k] = (source[k + i]);
        				}
        			}

        			aes256_init(&ctx, charParam);
        			aes256_encrypt_ecb(&ctx, encodeBase);

        			aes256_done(&ctx);
                    memcpy(encodedBytes + i, encodeBase, 16);
                }


		jbyteArray encryptedBytes = env->NewByteArray(size);
        	env->SetByteArrayRegion(encryptedBytes, 0, size, reinterpret_cast<jbyte*>((unsigned char*) encodedBytes));

       free(encodeBase);
       free(encodedBytes);
       free(source);
return encryptedBytes;
	} catch (...) {
		return NULL;
	}
}

}

