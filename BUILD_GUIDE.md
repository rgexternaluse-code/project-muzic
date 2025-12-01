# Muzic APK Build Guide

## Quick Start - Building and Installing APK

### Prerequisites
- Android Studio (latest version) OR AIDE app on Android phone
- USB cable (for Android device debugging)
- 2GB+ free disk space
- Android 8.0+ device for testing

---

## Option 1: Build with Android Studio (Recommended)

### Step 1: Install Android Studio
1. Download from https://developer.android.com/studio
2. Install and complete setup wizard
3. Install Android SDK (API 26-34)

### Step 2: Open Project
1. Launch Android Studio
2. Click "File" → "Open"
3. Navigate to the `Muzic` folder
4. Click "OK"
5. Wait for Gradle sync (may take 2-3 minutes)

### Step 3: Connect Device (Optional but Recommended)
1. Enable USB Debugging on your Android device:
   - Settings → About Phone → Tap "Build Number" 7 times
   - Go back → Developer Options → Enable "USB Debugging"
2. Connect device via USB cable
3. Accept permissions on device when prompted
4. Android Studio should show your device in the devices list

### Step 4: Build APK
**Method A: Debug APK (Quick build for testing)**
1. Click "Build" menu → "Build Bundle(s) / APK(s)" → "Build APK(s)"
2. Wait for build to complete (2-5 minutes)
3. A notification will appear: "APK(s) built successfully"
4. Click "Show in Explorer" (Windows) or "Show in Finder" (Mac)
5. The APK file is at: `Muzic\app\build\outputs\apk\debug\app-debug.apk`

**Method B: Release APK (For distribution)**
1. Click "Build" menu → "Build Bundle(s) / APK(s)" → "Build Bundle(s)"
2. Select release build
3. If prompted for keystore, create one:
   - Provide password and app info
   - Save keystore file in a safe location
4. Build completes with release APK

### Step 5: Install on Device
**Option A: Direct Install from Android Studio**
1. Click "Run" → "Run 'app'"
2. Select your device
3. Click "OK"
4. The app will build and install automatically
5. Wait 1-2 minutes for first launch

**Option B: Manual Install**
1. Copy `app-debug.apk` from build output
2. Transfer to Android device (USB/email/cloud)
3. On device: Open file manager → Find APK file
4. Tap APK → "Install"
5. If blocked: Settings → Apps & notifications → Advanced → Special app access → Install unknown apps → Enable for file manager

---

## Option 2: Build via Command Line (Gradle)

### Windows PowerShell

```powershell
# Navigate to project
cd "C:\Users\RamGopal\OneDrive - FALCON\Desktop\RG Singh\Extra\Muzic"

# Build debug APK (generates instantly)
.\gradlew assembleDebug

# Or build release APK (requires keystore)
.\gradlew assembleRelease

# APK location: app\build\outputs\apk\debug\app-debug.apk
```

### macOS/Linux Terminal

```bash
cd ~/path/to/Muzic

# Build debug APK
./gradlew assembleDebug

# APK location: app/build/outputs/apk/debug/app-debug.apk
```

---

## Option 3: Build with AIDE (Android IDE)

### On Your Android Phone
1. Install AIDE from Google Play Store
2. Open AIDE
3. Tap "New" → "Import Project"
4. Navigate to Muzic folder (via cloud sync or USB transfer)
5. Wait for project to load
6. Tap "Build" menu
7. Tap "Build APK"
8. After build completes, tap "Install"
9. Grant permissions
10. Launch app from home screen

---

## APK File Locations

After building, find your APK here:

**Debug APK:**
```
Muzic/app/build/outputs/apk/debug/app-debug.apk
```

**Release APK:**
```
Muzic/app/build/outputs/bundle/release/app.aab
```

---

## Installation on Device

### Method 1: USB Connection
1. Connect Android device via USB cable
2. Enable USB Debugging on device (Settings → Developer Options)
3. From Android Studio: Run → Run 'app' → Select device
4. App installs and launches automatically

### Method 2: Transfer APK Manually
1. Copy APK file to device (USB transfer, email, cloud drive)
2. On device: File Manager → Locate APK
3. Tap APK → "Install"
4. If blocked: Settings → Security → Unknown Sources → Enable
5. Tap "Install" again
6. Once installed, tap "Open"

### Method 3: ADB (Advanced)
```bash
# Connect device via USB
adb devices  # Verify device shows up

# Install APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Or uninstall and reinstall
adb uninstall com.muzic
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## First Run Setup

1. **Grant Permissions**
   - App requests permission to read audio files
   - Tap "Allow"

2. **Initial Scan**
   - App scans device storage for audio files
   - First scan may take 30-60 seconds
   - Progress spinner appears during scan

3. **View Music Library**
   - Songs appear in list view
   - Tap Sort button to change sort order

4. **Play Music**
   - Tap any song to play
   - Full player screen opens
   - Use controls to play, pause, skip

---

## Troubleshooting

### Build Fails: "Gradle sync failed"
**Solution:**
1. File → Sync Now
2. Check internet connection
3. Build → Clean Project
4. Build → Rebuild Project

### No Device Shows Up
**Solution:**
1. Enable USB Debugging: Settings → About Phone → Tap Build Number 7x
2. Go back → Developer Options → USB Debugging ON
3. Accept USB debugging prompt on device
4. Restart Android Studio
5. Reconnect USB cable

### Installation Fails: "Unknown Sources Blocked"
**Solution (Android 12+):**
1. Settings → Apps & notifications
2. Advanced → Special app access
3. Install unknown apps
4. Select your file manager → Allow

### APK File Not Found After Build
**Solution:**
1. Build → Build APK(s) again
2. Watch for notification "APK built successfully"
3. Check: Muzic/app/build/outputs/apk/debug/

### App Crashes on First Launch
**Solution:**
1. Uninstall app: adb uninstall com.muzic
2. Rebuild APK
3. Reinstall APK
4. Grant all permissions when prompted

### No Songs Appear in App
**Solution:**
1. Check if device has audio files in standard locations (Music, Downloads, etc.)
2. Grant file permissions: Settings → Apps → Muzic → Permissions
3. Reload app: Pull down notification area → Muzic → Swipe up to close
4. Reopen app

### Visualizer Not Showing
**Solution:**
1. Play a song first (visualizer needs audio session)
2. Grant audio focus permissions if prompted
3. Try different audio file

---

## File Sizes

- Debug APK: ~45-50 MB
- Release APK: ~35-40 MB (with ProGuard optimization)

## Signing for Release

To distribute on Google Play Store:

```bash
# Create keystore (one-time)
keytool -genkey -v -keystore release.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias musik

# Build signed release APK
./gradlew assembleRelease

# Or manually sign
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
  -keystore release.keystore app-release-unsigned.apk release
```

---

## Support

**Common Issues:**
- YouTube: Search "Android Studio setup tutorial"
- Android Docs: https://developer.android.com/docs
- ExoPlayer Docs: https://exoplayer.dev/

**Need Help?**
- Check Android Studio Logcat for error messages
- Verify all permissions in AndroidManifest.xml
- Ensure target Android version matches your device

---

## Next Steps After Installation

1. Open Muzic app
2. Grant file access permissions
3. Wait for song library to load
4. Browse and play your music!
5. Try different sort options
6. Play in background (minimize app)

---

**Build Time:** 2-5 minutes
**Installation Time:** 1-2 minutes
**First Launch:** 30-60 seconds (for music scan)

**Happy listening! 🎵**
