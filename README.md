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
			//Production			
			implementation "in.oneplay.sdk:gaming:1.0.0" // Game
			implementation "in.oneplay.sdk:vdi:1.0.0" // Desk
			//Qa
			implementation "in.oneplay.sdk:gaming.qa:1.0.0" // Game
			implementation "in.oneplay.sdk:vdi.qa:1.0.0" // Desk
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

     override fun sendEvent(onePlayResponseData: OnePlayResponseData) {
        when {
            onePlayResponseData.code == 404 -> handleError(onePlayResponseData.message) //Connection error
            onePlayResponseData.code == 1000 && onePlayResponseData.isProgressed == true ->
                handleProgress(onePlayResponseData.percentage)
            onePlayResponseData.code == 1001 -> handleGameConnected()
            onePlayResponseData.code == 1002 -> handleGameDisconnected()
            onePlayResponseData.code == 1003 -> handleGameTerminated()
            onePlayResponseData.code == 801 -> handleQueue(onePlayResponseData.percentage, onePlayResponseData.message) // percentage value is the queue count
        }
    }

    private fun handleGameTerminated() {
        runOnUiThread {
            Toast.makeText(this, "Game Terminated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleProgress(percentage: Int) {
        runOnUiThread {
            Toast.makeText(this, "Loading.. $percentage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleQueue(percentage: Any, message: Any) {
        runOnUiThread {
            Toast.makeText(this, "Queue ahead $percentage user", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleGameDisconnected() {
        runOnUiThread {
            Toast.makeText(this, "Game Disconnected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleGameConnected() {
        runOnUiThread {
            Toast.makeText(this, "Game Connected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleError(error: String) {
        runOnUiThread {
            Toast.makeText(this, "Game Error: $error", Toast.LENGTH_SHORT).show()
        }
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
        oPlayId = "ONEPLAY_GAME_ID" // Only for partner authentication
        
        // Or OnePlay authentication
        onePlayAuthToken = "ONEPLAY_AUTH_TOKEN"
        onePlayUserId = "ONEPLAY_USER_ID"
    }

    gameSession.setInputData(inputData)
}
```
Here is the termination method:

```
gameSession.terminateOnePlaySession()
```

Here is the different streamSettings:

```json
"data":[{"enabled":true,"key":"resolution","show_in":"main","text":"Resolution","type":"dslider","values":[{"default":true,"display":"1280x720","value":"1280x720"},{"default":false,"display":"1366x768","value":"1366x768"},{"default":false,"display":"1440x900","value":"1440x900"},{"default":false,"display":"1920x1080","value":"1920x1080"},{"default":false,"display":"1920x1200","value":"1920x1200"},{"default":false,"display":"2560x1440","value":"2560x1440"},{"default":false,"display":"2560x1600","value":"2560x1600"},{"default":false,"display":"3840x2160","value":"3840x2160"}]},

{"enabled":true,"key":"fps","show_in":"main","text":"FPS","type":"dslider","values":[{"default":false,"display":"30 FPS","value":30},{"default":true,"display":"60 FPS","value":60},{"default":false,"display":"120 FPS","value":120},{"default":false,"display":"240 FPS *(beta)","value":240}]},

{"default":10000,"enabled":true,"key":"bitrate","max":50000,"min":5000,"show_in":"main","text":"Bitrate","type":"slider"},

{"default":true,"enabled":true,"key":"is_vsync_enabled","show_in":"main","text":"VSYNC","type":"toggle"},

{"default":true,"enabled":true,"key":"frame_pacing","show_in":"main","text":"Frame Pacing","type":"toggle"},

{"default":false,"enabled":true,"key":"show_stats","show_in":"advance","text":"Show Stats","type":"toggle"},

{"default":true,"enabled":true,"key":"fullscreen","show_in":"advance","text":"Full Screen","type":"toggle"},

{"default":false,"enabled":true,"key":"onscreen_controls","show_in":"advance","text":"On-screen Controls","type":"toggle"},

{"enabled":true,"key":"audio_type","show_in":"advance","text":"Audio Type","type":"select","values":[{"default":true,"display":"Stereo","value":"stereo"},{"default":false,"display":"5.1","value":"5.1-surround"}]},

{"enabled":true,"key":"stream_codec","show_in":"advance","text":"Stream Codec","type":"select","values":[{"default":false,"display":"Auto","value":"auto"},{"default":false,"display":"HEVC","value":"forceh265"},{"default":true,"display":"H.264","value":"neverh265"},{"default":false,"display":"AV1","value":"av1"}]},

{"enabled":true,"key":"video_decoder_selection","show_in":"advance","text":"Video Decoder","type":"select","values":[{"default":true,"display":"Auto","value":"auto"},{"default":false,"display":"Software","value":"software"},{"default":false,"display":"Hardware","value":"hardware"}]},

{"enabled":true,"key":"server_type","show_in":"advance","text":"Server Selection","type":"drop-down","values":[{"default":true,"display":"Best","value":"best"},{"default":false,"display":"Any Available","value":"any"}]},

{"enabled":true,"key":"server_region","show_in":"advance","text":"Server Region","type":"drop-down","values":[{"default":true,"display":"Auto","value":"auto"},{"default":false,"display":"Mumbai-North","value":"ind_mumbai_y1"},{"default":false,"display":"Mumbai-South","value":"ind_mumbai_l1"},{"default":false,"display":"Noida","value":"ind_noida_w1"}]},{"default":false,"enabled":true,"key":"optimize_for_rdp","show_in":"advance","text":"Use Local Cursor","type":"toggle"},{"default":true,"enabled":true,"key":"pip_mode_android","show_in":"advance","text":"PIP Mode","type":"toggle"},{"default":false,"enabled":true,"key":"virtual_display","show_in":"advance","text":"Advance Display Mode","type":"toggle"},{"default":20,"enabled":true,"key":"fec_num","max":250,"min":0,"show_in":"advance","text":"FEC","type":"slider"},{"default":28,"enabled":true,"key":"qp_num","max":100,"min":0,"show_in":"advance","text":"QP","type":"slider"},

{"enabled":true,"key":"zoom_level","show_in":"advance","text":"Zoom Level","type":"select","values":[{"default":true,"display":"100%","value":100},{"default":false,"display":"125%","value":125},{"default":false,"display":"150%","value":150}]},

{"enabled":true,"key":"nvidia_preset","show_in":"advance","text":"Preset","type":"select","values":[{"default":true,"display":"P1 (Fastest)","value":"p1"},{"default":false,"display":"P2","value":"p2"},{"default":false,"display":"P3","value":"p3"},{"default":false,"display":"P4","value":"p4"},{"default":false,"display":"P5","value":"p5"},{"default":false,"display":"P6","value":"p6"},{"default":false,"display":"P7 (Slowest)","value":"p7"}]},

{"enabled":true,"key":"nvidia_twopass_mode","show_in":"advance","text":"Mode","type":"select","values":[{"default":true,"display":"Quarter Resolution (Faster)","value":"quarter_resolution"},{"default":false,"display":"Disabled (Fastest)","value":"disabled"},{"default":false,"display":"Full Resolution (Fastest)","value":"full_resolution"}]},{"default":true,"enabled":true,"key":"nvidia_use_cabac","show_in":"advance","text":"Use CABAC","type":"toggle"}]

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
-keep class * { @androidx.annotation.FontRes *; }


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

# Gson
-keep class in.oneplay.sdk.SessionRequest
-keep class in.oneplay.sdk.ResponseActiveSession
-keep class in.oneplay.sdk.Data
-keep class in.oneplay.sdk.Queue

#-keep class com.newrelic.** { *; }#
#-keep class io.invertase.** { *; }
#-keep class com.newrelic.agent.android.api.v2.** { *; }

```
