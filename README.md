# Muzic - Music Player App

A feature-rich Android music player application built with Kotlin and Material Design 3.

## Features

✅ **Audio Scanning**
- Recursively scans device storage for audio files
- Supported formats: MP3, WAV, M4A, AAC, FLAC
- Extracts metadata: title, artist, duration, album, album art

✅ **Sorting Options**
- Alphabetical (A-Z)
- By Folder
- By Artist

✅ **Playback Engine (ExoPlayer)**
- Play/Pause controls
- Next/Previous navigation
- Forward/Backward seek (±10 seconds)
- Auto-play next song
- Background playback support
- Notification media controls

✅ **User Interface**
- Main list screen with RecyclerView
- Full player screen with large album art
- Animated visualizer bars
- Gradient background based on album colors
- Material 3 design system

✅ **Architecture**
- MVVM + Repository pattern
- ViewModel + LiveData for reactive UI
- MusicService (foreground service) for playback
- MediaSession + MediaStyle notifications

## Project Structure

```
Muzic/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/muzic/
│   │   │   ├── ui/
│   │   │   │   ├── activity/
│   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   └── PlayerActivity.kt
│   │   │   │   ├── adapter/
│   │   │   │   │   └── SongAdapter.kt
│   │   │   │   └── viewmodel/
│   │   │   │       ├── MusicListViewModel.kt
│   │   │   │       └── PlayerViewModel.kt
│   │   │   ├── service/
│   │   │   │   └── MusicService.kt
│   │   │   ├── data/
│   │   │   │   ├── models/
│   │   │   │   │   └── Models.kt
│   │   │   │   └── repository/
│   │   │   │       └── MusicRepository.kt
│   │   │   ├── utils/
│   │   │   │   ├── VisualizerView.kt
│   │   │   │   └── TimeFormatter.kt
│   │   │   └── MusicApplication.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── activity_player.xml
│   │   │   │   └── item_song.xml
│   │   │   ├── drawable/
│   │   │   │   ├── ic_cd_placeholder.xml
│   │   │   │   └── ic_launcher.xml
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   ├── dimens.xml
│   │   │   │   └── themes.xml
│   │   │   ├── menu/
│   │   │   │   └── sort_menu.xml
│   │   │   └── xml/
│   │   │       ├── data_extraction_rules.xml
│   │   │       └── backup_rules.xml
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
└── README.md
```

## Requirements

- Android 8.0+ (API 26+)
- Minimum SDK: 26
- Target SDK: 34

## Dependencies

- **androidx.core:core-ktx** - Android KTX extensions
- **androidx.appcompat:appcompat** - AppCompat support
- **com.google.android.material:material** - Material Design components
- **androidx.constraintlayout:constraintlayout** - Constraint Layout
- **androidx.lifecycle** - ViewModel, LiveData
- **androidx.media3** - Media3 (ExoPlayer integration)
- **com.github.bumptech.glide** - Image loading
- **androidx.palette** - Color palette extraction
- **org.jetbrains.kotlinx:kotlinx-coroutines** - Coroutines

## Building the APK

### Method 1: Android Studio

1. **Install Android Studio** (latest version)
2. **Open the project**: File → Open → Select Muzic folder
3. **Wait for Gradle sync** to complete
4. **Build APK**: Build → Build Bundle(s) / APK(s) → Build APK(s)
5. The APK will be generated in `app/build/outputs/apk/debug/`

### Method 2: Command Line (Gradle)

```bash
# Navigate to project directory
cd Muzic

# Build debug APK
./gradlew assembleDebug

# Build release APK (requires keystore)
./gradlew assembleRelease
```

### Method 3: AIDE (Android IDE)

1. **Install AIDE** from Google Play Store
2. **Import the project** into AIDE
3. **Tap Build** → **Build APK**
4. **Tap Install** to install on device

## Installation

### From Android Studio
1. Connect your Android device (USB debugging enabled)
2. Build → Build APK(s)
3. Once built, click "Install" in the notification

### Manual Installation
1. Transfer the APK file to your Android device
2. Open file manager and navigate to the APK
3. Tap the APK to install
4. Go to Settings → Security → Enable "Unknown Sources" if needed
5. Complete the installation

## Usage

1. **Launch the app**: Open Muzic
2. **Grant permissions**: Allow read audio file permissions
3. **View songs**: The app will scan and display your audio files
4. **Sort songs**: Tap the Sort button to sort by A-Z, Folder, or Artist
5. **Play music**: Tap a song to play it
6. **Full player**: Navigate to the player screen with visualizer and controls
7. **Controls**: 
   - Play/Pause: Center button
   - Next/Previous: Right/Left arrow buttons
   - Seek ±10s: Forward/Backward buttons
   - Drag seekbar to jump to specific time

## Features in Detail

### Audio Scanning
- Scans all device storage for supported audio formats
- Extracts complete metadata using MediaStore API
- Caches album artwork for quick display
- Shows fallback CD icon if no artwork available

### Playback Engine
- Uses ExoPlayer for stable, reliable playback
- Supports all major audio formats (MP3, WAV, M4A, AAC, FLAC)
- Background playback with notification controls
- Continues playing when app is minimized
- Automatic next track when current finishes

### Visualizer
- Real-time animated visualization of audio
- Uses Android Visualizer API for FFT data
- Circular bar pattern following the music
- Synced with playback engine

### UI/UX
- Material 3 design system
- Dark theme for easy viewing
- Gradient backgrounds extracted from album art
- Smooth animations and transitions
- Responsive RecyclerView with smooth scrolling

## Permissions Required

- `READ_MEDIA_AUDIO` (Android 13+) or `READ_EXTERNAL_STORAGE` (Android < 13)
- `FOREGROUND_SERVICE` - For background playback

## Architecture

### MVVM Pattern
- **Model**: Data models (Song, Album, PlaybackState)
- **View**: Activities and Adapters
- **ViewModel**: Manages UI state and business logic

### Repository Pattern
- `MusicRepository` - Handles all data operations
- Audio file scanning and metadata extraction
- Sorting and filtering logic
- Album art color extraction

### Service Architecture
- `MusicService` - Foreground service for playback
- ExoPlayer integration
- MediaSession management
- Notification controls

## Troubleshooting

### No songs found
- Check file permissions in app settings
- Ensure audio files are in standard locations (Music, Downloads, etc.)
- Verify file formats are supported (MP3, WAV, M4A, AAC, FLAC)

### Playback issues
- Restart the app
- Check if audio file is corrupted
- Ensure sufficient storage space

### Visualizer not showing
- Ensure app has audio focus
- Try playing a song first
- Some formats may not support visualization

## Future Enhancements

- [ ] Playlists creation and management
- [ ] Song search and filtering
- [ ] Equalizer controls
- [ ] Sleep timer
- [ ] Recently played history
- [ ] Favorites/Liked songs
- [ ] Theme customization
- [ ] Bluetooth controls

## License

This project is provided as-is for educational purposes.

## Support

For issues or questions, please refer to the Android documentation:
- [ExoPlayer Documentation](https://exoplayer.dev/)
- [Android Media3 Guide](https://developer.android.com/jetpack/androidx/releases/media3)
- [Android ViewModel Guide](https://developer.android.com/topic/libraries/architecture/viewmodel)

---

**Created**: December 2025
**Target API**: 34 (Android 14+)
**Min API**: 26 (Android 8.0+)
 
 