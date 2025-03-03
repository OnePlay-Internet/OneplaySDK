# Oneplay Android SDK Integration.

Oneplay Android SDK Integration Guide

There are two ways to integrate the Oneplay SDK:

### Option 1: Gradle Dependency (Recommended) - In progress

Add the following to your app-level build.gradle file & settings:

```jsx
 build.gradle
 compileOptions {
        coreLibraryDesugaringEnabled true
        
        }

dependencies {
            // Use the dependency as per your need either Gaming or Desk
			implementation "in.oneplay.sdk:gaming:1.0.0" // Game
			implementation "in.oneplay.sdk:desk:1.0.0" // Desk
    }
```

```jsx
settings.gradle
repositories {
        maven {
            url = uri("https://maven.pkg.github.com/OnePlay-Internet/OneplaySDK/")
            credentials {
                username = "GITHUB_USERNAME"
                password = "GITHUB_TOKEN"  // PAT with `read:packages` scope
            }
        }
     }
```

### Option 2: AAR Import

If you prefer to use the AAR file directly:

1. Download the oneplay-android-sdk.aar file from the developer portal
2. Copy the AAR file to your project's libs directory (create if it doesn't exist)
3. Add the following to your app-level build.gradle:

```jsx

compileOptions {
        coreLibraryDesugaringEnabled true
        
        }

dependencies {
    implementation files('libs/oneplay-android-sdk.aar')

		//Exterbal library dependency which were not bundled with the aar
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.3'
    implementation 'org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0'

    implementation 'androidx.core:core-ktx:1.9.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

    // non root qa library
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'org.bouncycastle:bcprov-jdk15on:1.70'
    implementation 'org.bouncycastle:bcpkix-jdk15on:1.70'
    implementation 'org.jcodec:jcodec:0.2.3'
    implementation 'com.squareup.okhttp3:okhttp:4.9.3'
    implementation 'com.squareup.okio:okio:2.8.0'
    implementation 'org.jmdns:jmdns:3.5.7'

    implementation 'org.jsoup:jsoup:1.14.3'
    implementation 'com.google.android.material:material:1.8.0'
    coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:1.1.5'
    implementation 'androidx.window:window:1.0.0'
    implementation 'androidx.window:window-java:1.0.0'
    implementation 'androidx.core:core-splashscreen:1.0.1'
    implementation 'androidx.appcompat:appcompat:1.0.0'
    implementation 'com.intuit.sdp:sdp-android:1.1.1'
    implementation 'androidx.legacy:legacy-preference-v14:1.0.0'
    implementation 'androidx.preference:preference-ktx:1.2.1'

}
```

After adding either dependency method, sync your project with Gradle files.

## 2. Initialize SDK

In your Activity or Fragment, implement the OneplayGameSessionListener interface:

```kotlin
class GameActivity : AppCompatActivity(), OneplayGameSessionListener {
    private lateinit var gameSession: GameLaunchSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        
        // Initialize the game session
        OneplayGameFactory.initialize(applicationContext)
        gameSession = OneplayGameFactory.createOnePlaySession(applicationContext)
        if (OneplayGameFactory.getSdkContext() != null) {
            setupGameLaunch()
        }
    }

    override fun sendEvent(onePlayResponseData: OnePlayResponseDatag) {
        // Handle events from the game session
        Log.d("GameActivity", "Event received: $msg")
    }
}
```

## 3. Configure Game Launch Parameters

Create the InputData object with the required parameters:

```kotlin
private fun setupGameLaunch() {
    val streamSettings = JSONObject().apply {
        put("resolution", "1280x720")
        put("is_vsync_enabled", true)
        put("fps", "60")
        put("bitrate", 10000)
        put("show_stats", false)
        put("fullscreen", true)
        put("onscreen_controls", true)
        put("audio_type", "stereo")
        put("stream_codec", "forceh265")
        put("video_decoder_selection", "auto")
        put("store", "steam")
    }

    val inputData = InputData(
        partnerId = "YOUR_PARTNER_ID",
        gameId = "GAME_ID",
        payload = streamSettings.toString(),
        coreAppVersion = "1.0.0",
        packageName = "your_pakageName"
    ).apply {
        // Set either partner authentication
        partnerUserAuthToken = "USER_AUTH_TOKEN"
        partnerUserIdPhoneNumber = "USER_PHONE"
        
        // Or OnePlay authentication
        onePlayAuthToken = "ONEPLAY_AUTH_TOKEN"
        onePlayUserId = "ONEPLAY_USER_ID"
    }

    gameSession.setInputData(inputData)
}
```

## 4. Manifest Configuration

Add required permissions to your AndroidManifest.xml:

```xml
<manifest>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
</manifest>
```

## 5. Error Handling
```kotlin
/* data class OnePlayResponseData (
    val code: Int, val message: String, val isProgressed: Boolean, val percentage: Int
) */ Reference OnePlayResponseData

override fun sendEvent(onePlayResponseData: OnePlayResponseData) {
    when {
        onePlayResponseData.code == 404 -> handleError(onePlayResponse.message) //Connection error
        onePlayResponseData.code == 1000 && oneplayResponseData.isProgressed == true -> 
        handleProgress(oneplayResponseData.percentage)
        onePlayResponseData.code == 1001 -> handleGameConnected()
        onePlayResponseData.code == 1002 -> handleGameDisconnected()
        onePlayResponseData.code == 801 -> handleQueue(oneplayResponseData.percentage, oneplayResponseData.message) // percentage value is the queue count
    }
}

private fun handleError(error: String) {
    Toast.makeText(this, "Game Error: $error", Toast.LENGTH_SHORT).show()
}
```

## 6. Shortcuts for stream

- **Select + X** → Opens settings (for non-touch-based devices)
- **Select + A** → Keyboard overlay (for non-touch-based devices)
- **Press and hold the Start button for 3 seconds** to enable or disable the mouse cursor
- **Press and hold the remote center button for 3 seconds** to enable or disable the mouse curs

## 7. Best Practices

- Always check network connectivity before starting a game session
- Implement proper error handling for all SDK callbacks
- Store sensitive credentials securely
- Monitor and handle game session lifecycle events
- Implement proper cleanup in onDestroy()

## 8. Testing

Before deploying to production, test the implementation with these scenarios:

- Different network conditions (WiFi, 4G, 5G)
- Various device types and Android versions
- Different game titles and configurations
- Error scenarios and recovery mechanisms

## 9. ProGaurd rules
```markdown
# Don't obfuscate code
-dontobfuscate

# Our code
-keep class in.oneplay.binding.input.evdev.* {*;}

# Moonlight common
-keep class in.oneplay.nvstream.jni.* {*;}

# Okio
-keep class sun.misc.Unsafe {*;}
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

# BouncyCastle
-keep class org.bouncycastle.** {*;}
-dontwarn javax.naming.**

# jMDNS
-dontwarn javax.jmdns.impl.DNSCache
-dontwarn org.slf4j.**

-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

-keepclassmembers class * extends in.oneplay.ui.keyboard.layouts.KeyboardLayout {
    public protected <init>(...);
}

-keep class in.oneplay.sdk.** { *; }
-dontwarn java.lang.invoke.StringConcatFactory

#-keep class com.newrelic.** { *; }#
#-keep class io.invertase.** { *; }
#-keep class com.newrelic.agent.android.api.v2.** { *; }

```
