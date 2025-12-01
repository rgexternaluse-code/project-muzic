# 🎵 Muzic - Visual Guide & Feature Overview

**Status:** ✅ Complete & Ready to Build  
**Generated:** December 1, 2025  

---

## 📸 App Screens Overview

### Screen 1: Main List Screen (MainActivity)
```
┌─────────────────────────────┐
│      Muzic Player           │  ← App Bar
├─────────────────────────────┤
│  [Sort ▼]                   │  ← Sort Menu Button
├─────────────────────────────┤
│ ┌─────────────────────────┐ │
│ │ [CD] Song Title 1  3:45 │ │  ← Song Item (Card)
│ │     Artist Name         │ │
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │ [CD] Song Title 2  4:12 │ │  ← Song Item (Card)
│ │     Artist Name         │ │
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │ [CD] Song Title 3  3:28 │ │  ← Song Item (Card)
│ │     Artist Name         │ │
│ └─────────────────────────┘ │
│ ┌─────────────────────────┐ │
│ │ [CD] Song Title 4  4:05 │ │  ← Tap to Play
│ │     Artist Name         │ │
│ └─────────────────────────┘ │
│  ...more songs scrolling...  │
└─────────────────────────────┘
```

**Features on Main Screen:**
- RecyclerView with smooth scrolling
- Album art thumbnails (48x48dp)
- Song title, artist, duration
- Sort menu (A-Z, Folder, Artist)
- Material 3 dark theme
- Click any song to play

---

### Screen 2: Full Player Screen (PlayerActivity)
```
┌──────────────────────────────┐
│   ┌─────────────────────┐    │
│   │                     │    │
│   │    [Album Art]      │    │  ← Large: 300x300dp
│   │     or CD Icon      │    │  ← Fallback: spinning CD
│   │                     │    │  ← Gradient background
│   └─────────────────────┘    │
│                              │
│   Song Title Here            │  ← Current song
│   Artist Name                │
│                              │
├──────────────────────────────┤
│ ┌──────────────────────────┐ │
│ │ ▯  ▯  ▯  ▯  ▯ ▯ ▯      │ │  ← Visualizer bars
│ │ ▮ ▮ ▮ ▮ ▮ ▮ ▮ ▮ ▮     │ │  ← Animated, synced to audio
│ └──────────────────────────┘ │
│                              │
│ ║────◯─────────────────────║ │  ← Seekbar
│ 1:23            2:45        │  ← Time display
│                              │
├──────────────────────────────┤
│  [-10s] [⏮] [⏯] [⏭] [+10s]  │  ← Control buttons
│         Backward    Forward   │
└──────────────────────────────┘
```

**Features on Player Screen:**
- Large album art (300x300dp)
- CD placeholder if no artwork
- Gradient background (from album colors)
- Song title & artist display
- Animated visualizer (circular bar pattern)
- Seekbar for position control
- Current time & total duration
- 5 control buttons:
  - Backward (-10s)
  - Previous (⏮)
  - Play/Pause (⏯)
  - Next (⏭)
  - Forward (+10s)

---

## 🎨 Color Scheme (Material 3 Dark)

```
┌─────────────────────────────────┐
│ Primary: #6200EE (Purple)       │  ← Buttons, highlights
├─────────────────────────────────┤
│ Secondary: #03DAC6 (Teal)       │  ← Accents
├─────────────────────────────────┤
│ Background: #121212 (Very Dark) │  ← Main background
├─────────────────────────────────┤
│ Surface: #1E1E1E (Dark Gray)    │  ← Cards, surfaces
├─────────────────────────────────┤
│ Error: #CF6679 (Red)            │  ← Errors
├─────────────────────────────────┤
│ On Surface: #FFFFFF (White)     │  ← Text
└─────────────────────────────────┘
```

---

## 🎵 Data Flow Diagram

```
┌──────────────────────────────┐
│    User Interaction          │
│  (Tap song, press controls)  │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│    MainActivity / Activity   │  ← UI Layer
│   (Handles user input)       │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│    MusicService             │  ← Service Layer
│  (ExoPlayer playback)        │  (Background)
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│   Android MediaStore API    │  ← System Services
│   Audio files on device     │
└──────────────────────────────┘

┌──────────────────────────────┐
│   ViewModel (LiveData)      │  ← State Management
│  Observes state changes      │  (Reactive updates)
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│   UI Updates (RecyclerView)  │  ← Display Layer
│  Shows song list / player   │
└──────────────────────────────┘
```

---

## 📂 Data Structure

### Song Object
```kotlin
data class Song(
    val id: Long,                    // Database ID
    val title: String,               // Song name
    val artist: String,              // Artist name
    val album: String,               // Album name
    val duration: Long,              // Duration in ms
    val path: String,                // File path on device
    val albumArtPath: String?,       // Path to album art
    val albumArt: Bitmap?,           // Album artwork image
    val folder: String               // Folder path
)
```

### PlaybackState Object
```kotlin
data class PlaybackState(
    val currentSong: Song?,          // Now playing
    val isPlaying: Boolean,          // Play/pause state
    val currentPosition: Long,       // Current time in ms
    val duration: Long,              // Total duration in ms
    val isLoading: Boolean,          // Loading indicator
    val playlistIndex: Int,          // Current index
    val playlist: List<Song>         // All songs
)
```

---

## 🔄 User Journey Map

```
START
  │
  ├─► Grant Permissions
  │   ↓
  ├─► Main Screen Opens
  │   └─► [Scanning songs...]
  │       ↓
  ├─► Song List Displays
  │   ├─► Option 1: Sort songs
  │   │   ├─ A-Z (Alphabetical)
  │   │   ├─ Folder (By Folder)
  │   │   └─ Artist (By Artist)
  │   │
  │   └─► Option 2: Play song
  │       ├─► Tap song ──→ Player Screen
  │       │   ├─► ⏯ Play/Pause
  │       │   ├─► ⏮⏭ Skip songs
  │       │   ├─► ±10s Seek
  │       │   └─► Visualizer animates
  │       │
  │       └─► Minimize app
  │           ├─► Music continues playing
  │           └─► Notification shows controls
  │
  └─► END
```

---

## 🎼 Feature Matrix

| Feature | Main Screen | Player Screen | Background | Notification |
|---------|:-----------:|:-------------:|:----------:|:------------:|
| Song List | ✅ | - | - | - |
| Album Art | ✅ | ✅ | - | ✅ |
| Metadata | ✅ | ✅ | - | - |
| Sort Options | ✅ | - | - | - |
| Play/Pause | - | ✅ | - | ✅ |
| Next/Previous | - | ✅ | - | ✅ |
| Seek ±10s | - | ✅ | - | - |
| Seekbar | - | ✅ | - | - |
| Visualizer | - | ✅ | - | - |
| Gradient BG | - | ✅ | - | - |
| Time Display | - | ✅ | - | - |
| Continue Playing | - | - | ✅ | - |

---

## 🎯 Control Flow for Playing a Song

```
User taps song
    ↓
SongAdapter.onSongClick()
    ↓
MainActivity: Load playlist into service
    ↓
MusicService.loadPlaylist(songs)
    ↓
ExoPlayer: Create MediaItems from songs
    ↓
MusicService.seekToIndex(selected_index)
    ↓
MusicService.play()
    ↓
Start PlayerActivity
    ↓
PlayerActivity updates UI
    ├─ Display album art
    ├─ Extract & apply gradient
    ├─ Setup visualizer
    ├─ Start update timer
    └─ Listen to ExoPlayer state
        ├─ Update seekbar
        ├─ Update time display
        ├─ Animate visualizer
        └─ Update play/pause button
```

---

## 📊 File Organization Strategy

```
com.muzic/
│
├─ UI Layer (Views & Activities)
│  └─ ui/
│     ├─ activity/
│     │  ├─ MainActivity.kt (List)
│     │  └─ PlayerActivity.kt (Player)
│     └─ adapter/
│        └─ SongAdapter.kt (RecyclerView)
│
├─ ViewModel Layer (State Management)
│  └─ ui/viewmodel/
│     ├─ MusicListViewModel.kt
│     └─ PlayerViewModel.kt
│
├─ Service Layer (Background Work)
│  └─ service/
│     └─ MusicService.kt (ExoPlayer)
│
├─ Data Layer (Access & Logic)
│  └─ data/
│     ├─ models/
│     │  └─ Models.kt (Data classes)
│     └─ repository/
│        └─ MusicRepository.kt (Data access)
│
└─ Utility Layer (Helpers)
   └─ utils/
      ├─ VisualizerView.kt (Custom view)
      └─ TimeFormatter.kt (Utilities)
```

---

## ⚙️ Permission Flow

```
App Launch
    ↓
Check Android Version
    ├─ Android 13+
    │  └─ Request: READ_MEDIA_AUDIO
    └─ Android < 13
       └─ Request: READ_EXTERNAL_STORAGE
       
    ↓
User Grant/Deny
    ├─ ✅ Granted
    │  └─ Load songs
    └─ ❌ Denied
       └─ Show message, ask again
```

---

## 🎨 Visualizer Algorithm

```
Real-time Audio Data
    ↓
Get FFT (Fast Fourier Transform) data
from Android Visualizer API
    ↓
Every 500ms:
├─ Read 256 bytes of FFT data
├─ Map to circular pattern
│  └─ 360° divided by frequencies
├─ Draw bars radiating outward
│  └─ Height = audio intensity
└─ Post invalidate (redraw)

Result: Spinning circular bars following music beat
```

---

## 🎯 Memory Management

```
Low Memory
    ↓
─────────────────────────
✅ What's Managed:
- Bitmap image scaling (Glide)
- RecyclerView recycling
- Visualizer cleanup
- Service lifecycle
- LiveData subscription cleanup

❌ What to Avoid:
- Holding references indefinitely
- Creating bitmaps > device RAM
- Starting multiple services
```

---

## 🔐 Security & Permissions

```
App Permissions:
├─ READ_MEDIA_AUDIO (Android 13+)
│  └─ Access audio files only
│
├─ READ_EXTERNAL_STORAGE (Android < 13)
│  └─ Access all external storage
│
├─ FOREGROUND_SERVICE
│  └─ Required for background playback
│
└─ INTERNET (future use)
   └─ For potential cloud features

Requested at:
- App launch (if not granted)
- User can grant/revoke anytime
- Settings → Apps → Muzic → Permissions
```

---

## 🎵 Supported Audio Formats

```
✅ SUPPORTED:
├─ MP3 (Moving Picture Experts Group Audio)
├─ WAV (Waveform Audio File)
├─ M4A (MPEG-4 Audio)
├─ AAC (Advanced Audio Coding)
└─ FLAC (Free Lossless Audio Codec)

Filtered by:
- File extension check
- Duration > 0 check
- isMusic flag in MediaStore

Fallback: CD placeholder icon
```

---

## 📱 Device Compatibility

```
Android 8.0 - 8.1 (API 26-27)
├─ ✅ Full support
├─ ⚠️  Runtime permissions required
└─ Note: Old devices may be slow

Android 9.0 - 9.1 (API 28-29)
├─ ✅ Full support
└─ ⚠️  Storage access requirements

Android 10 (API 29)
├─ ✅ Full support
├─ ⚠️  Scoped storage limitations
└─ Note: SD card access limited

Android 11 (API 30)
├─ ✅ Full support
└─ ⚠️  Scoped storage enforced

Android 12 (API 31)
├─ ✅ Full support
└─ Note: Most compatible

Android 13+ (API 33+)
├─ ✅ Full support
├─ ✅ READ_MEDIA_AUDIO specific permission
└─ Best experience
```

---

## 🚀 Performance Optimization

```
Initialization:
├─ Lazy load images (Glide caching)
├─ Background thread scanning
└─ Coroutines for async operations

Runtime:
├─ RecyclerView recycling (smooth scroll)
├─ ExoPlayer buffering (smooth playback)
├─ Visualizer updates 500ms (not every frame)
├─ LiveData batch updates
└─ ProGuard code optimization (Release APK)

Memory:
├─ Bitmap scaling (48dp thumb, 300dp player)
├─ Glide memory cache
├─ Song object pooling (RecyclerView)
└─ Visualizer cleanup on destroy
```

---

## 📞 Support & Troubleshooting Quick Map

```
Issue: No songs found
├─ Check: File permissions granted?
├─ Check: Music files in standard location?
└─ Fix: Reload app, restart device

Issue: App crashes
├─ Check: Android version compatible?
├─ Check: Sufficient storage?
└─ Fix: Uninstall, rebuild, reinstall

Issue: Visualizer not showing
├─ Check: Song playing?
├─ Check: Audio focus available?
└─ Fix: Try different song

Issue: Build fails
├─ Check: Gradle sync completed?
├─ Check: Internet connection?
└─ Fix: Clean project, rebuild

→ See BUILD_GUIDE.md for detailed troubleshooting
```

---

## 🎯 Architecture Layers Visualization

```
┌──────────────────────────────────┐
│    PRESENTATION LAYER            │
│  (Activities, Fragments, Views)  │
│  MainActivity ← → PlayerActivity  │
└────────────────┬─────────────────┘
                 │
        ╭────────┴────────╮
        ↓                 ↓
┌──────────────┐  ┌─────────────┐
│   VIEWMODEL  │  │   Adapter   │
│  (LiveData)  │  │ (RecyclerV) │
└──────────────┘  └─────────────┘
        │                 │
        ╰────────┬────────╯
                 ↓
┌──────────────────────────────────┐
│    BUSINESS LAYER                │
│  (Repository, ViewModel Logic)   │
│  MusicRepository                 │
└────────────────┬─────────────────┘
                 │
        ╭────────┴────────╮
        ↓                 ↓
┌──────────────┐  ┌─────────────┐
│   Service    │  │   Models    │
│  (ExoPlayer) │  │  (DataClass)│
└──────────────┘  └─────────────┘
        │                 │
        ╰────────┬────────╯
                 ↓
┌──────────────────────────────────┐
│    DATA LAYER                    │
│  (Device Storage, MediaStore)    │
│  Device Audio Files              │
└──────────────────────────────────┘
```

---

## ✨ Key Takeaways

1. **Architecture**: MVVM + Repository (Clean & Testable)
2. **Threading**: Coroutines for async, Main thread for UI
3. **State**: LiveData for reactive updates
4. **Playback**: ExoPlayer with MediaSession
5. **UI**: Material Design 3 dark theme
6. **Performance**: Optimized with ProGuard
7. **Permissions**: Android 13+ compatible
8. **Resources**: All defined in res/ folder
9. **Documentation**: Complete guides included
10. **Ready**: Build and install immediately!

---

**Your complete music player is ready to build and enjoy! 🎵**

For next steps:
→ **START_HERE.md** (Quick start)
→ **BUILD_GUIDE.md** (Detailed build)
→ **README.md** (Features)
