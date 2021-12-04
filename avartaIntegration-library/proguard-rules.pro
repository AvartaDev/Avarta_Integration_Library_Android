-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-dontoptimize
-dontpreverify

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class android.support.v4.app.**
-keep public class android.support.v4.app.**


-keep class org.json.**
-keep class org.apache.**
-dontwarn org.apache.**
-dontwarn org.json.**

-dontwarn android.support.**

-keep class com.avarta.solusconnect.models.** { *; }

-keep class com.avarta.integrationlibrary.data.api.response.PendingTask { *; }

-keep class android.net.http.AndroidHttpClient.** { *; }

-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.**
-dontwarn com.android.volley.toolbox.**

-keep class com.avarta.integrationlibrary.data.api.IntegrationApiManager { *; }
-keepclassmembers class com.avarta.integrationlibrary.data.api.IntegrationApiManager { *; }

-keep class com.avarta.integrationlibrary.data.api.response.** {*;}
-keepclassmembers class com.avarta.integrationlibrary.data.api.response.** {*;}

-keep class com.avarta.integrationlibrary.data.models.** {*;}
-keepclassmembers class com.avarta.integrationlibrary.data.models.** {*;}

-keep class com.avarta.integrationlibrary.utils.Logger { *; }
-keep class com.avarta.integrationlibrary.enums.WorkflowType { *; }

-keepparameternames
-keepattributes Signature
-keep interface com.avarta.integrationlibrary.interfaces.** {*;}
-keep class com.avarta.integrationlibrary.exceptions.** {
    <methods>;
}
-keep class com.avarta.integrationlibrary.data.helpers.ApiCryptManager {
    <methods>;
}
-keep class com.avarta.integrationlibrary.data.helpers.EllipticCurveKeys {
    <methods>;
}


-keep public class javax.*
-keep public class org.*
-keep public class org.apache.*

-dontwarn org.slf4j.**
-dontwarn org.json.*
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**

-dontwarn javax.xml.**
-dontwarn javax.management.**
-dontwarn java.lang.management.**
-dontwarn android.support.**
-dontwarn com.google.code.**
-dontwarn oauth.signpost.**

-keep class com.google.inject.**

-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class com.google.android.gms.common.GooglePlayServicesUtil
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient

-keepattributes *Annotation*,EnclosingMethod,MethodParameters

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.google.android.gms.common.GooglePlayServicesUtil {
int isGooglePlayServicesAvailable (android.content.Context);
}

-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
static *;
}

-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info { *; }

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep public class com.google.android.gms.**
-keep class com.google.android.gms.**

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }
