package com.avarta.integrationlibrary.data.helpers;

import android.util.Base64;
import android.util.Log;

import com.avarta.integrationlibrary.utils.CrashLogging;

import java.security.SecureRandom;

import de.frank_durr.ecdh_curve25519.ECDHCurve25519;

public class EllipticCurveKeys {
    public static byte[] PUBLIC_KEY, SECRET_KEY;
    public static String publicBase64, secretBase64;

    static {
        try {
            System.loadLibrary("ecdhcurve25519");
            Log.e("ELLIPTIC", "Loaded ecdhcurve25519 library.");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Log.e("ELLIPTIC", "Error loading ecdhcurve25519 library: " + e.getMessage());
        }
    }

    public static void initializeKeys() {
        Log.e("CLEAR KEY ", "initialize key");
        PUBLIC_KEY = null;
        SECRET_KEY = null;
        publicBase64 = null;
        secretBase64 = null;
        try {
            SecureRandom random = new SecureRandom();
            SECRET_KEY = ECDHCurve25519.generate_secret_key(random);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < SECRET_KEY.length; i++) {
                sb.append(SECRET_KEY[i]).append(" ");
            }

            StringBuilder sb1 = new StringBuilder();
            PUBLIC_KEY = ECDHCurve25519.generate_public_key(SECRET_KEY);
            for (int i = 0; i < PUBLIC_KEY.length; i++) {
                sb1.append(PUBLIC_KEY[i]).append(" ");
            }
            publicBase64 = Base64.encodeToString(PUBLIC_KEY, Base64.NO_WRAP);
            secretBase64 = Base64.encodeToString(SECRET_KEY, Base64.NO_WRAP);
        } catch (Exception e) {
            CrashLogging.LogCrashException(e, "Error in initializing Ellitic Curve NDK");
            Log.e("INITIALIZATION", "FAil");
            e.printStackTrace();
        }
    }
}
