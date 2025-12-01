# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
# -verbose

# Optimization is turned off by default. Dex does not like aggressive optimizations.
# We turn on cautious optimizations instead.
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

# Note that if you want to enable optimization, you cannot just
# include optimization flags in this configuration. Instead, you will
# need to rebuild your jar files excluding the constants *.class
# files, or set the bootclasspath if you are using JDK.

-dontpreverify
# Note: if you want to enable PreVerification, there are two alternatives; see below.
-verbose

-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
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

-keepclassmembers class **.R$* {
    public static <fields>;
}

# Preserve line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep ExoPlayer
-keep class com.google.android.exoplayer** { *; }
-keep class androidx.media3.** { *; }

# Keep Glide
-keep public class * implements com.bumptech.glide.load.ImageTranscoder {
 *;
}
-keep public class * implements com.bumptech.glide.load.ResourceDecoder {
 *;
}
-keep public class * implements com.bumptech.glide.load.ResourceEncoder {
 *;
}
-keep public class * implements com.bumptech.glide.load.Encoder {
 *;
}
-keep public class * implements com.bumptech.glide.load.ResourceLoader {
 *;
}
-keep public class * implements com.bumptech.glide.load.DataSource {
 *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder {
 *;
}

# Keep our code
-keep class com.muzic.** { *; }
