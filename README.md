# Phonemics Arabic Keyboard for Android

An Android input method (keyboard) that converts phonemic Arabic input into proper orthographic Arabic text, based on the original React TypeScript web application.

## Features

- **Phonemic to Orthographic Conversion**: Real-time transformation using 206+ Arabic orthographic rules
- **Two Keyboard Layouts**: 
  - Layout 1: Diacritics-focused (فتحة، ضمة، كسرة)
  - Layout 2: Full vowels (ا، و، ي)
- **Dot Cycling**: Press the dot key to cycle through character variants (ح → خ → ج)
- **Color-coded Keys**: Visual grouping by character type
- **Real-time Suggestions**: API integration for word suggestions and rhythm patterns
- **RTL Support**: Proper Arabic text rendering and direction

## Quick Start

### Prerequisites
- **Android Studio** OR **IntelliJ IDEA** with Android plugin
- Android SDK (API 21+)
- Android device or emulator for testing

### Build and Test

#### Option A: Using IntelliJ IDEA

1. **Setup Android SDK in IntelliJ:**
   - Go to IntelliJ IDEA → Preferences → Appearance & Behavior → System Settings → Android SDK
   - Set SDK location (e.g., `/Users/username/Library/Android/sdk`)
   - Install Android SDK Platform-Tools and at least one API level

2. **Add to your shell profile** (`~/.zshrc` or `~/.bash_profile`):
   ```bash
   export ANDROID_HOME=$HOME/Library/Android/sdk
   export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools/bin
   ```

3. **Open project in IntelliJ:**
   - File → Open → Select `phonemics_keyboard_android` folder
   - Wait for Gradle sync to complete
   - Use green play button (▶️) to build and run

#### Option B: Command Line

1. **Clone and build:**
   ```bash
   cd phonemics_keyboard_android
   ./gradlew assembleDebug
   ```

2. **Install on device:**
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

3. **Or use the automated script:**
   ```bash
   ./run_tests.sh
   ```

### Setup on Device

1. **Open the app** and follow the setup instructions
2. **Enable the keyboard:**
   - Settings > System > Languages & input > Virtual keyboard
   - Enable "Phonemics Arabic Keyboard"
3. **Select the keyboard:**
   - Use the keyboard selector or the app's "Select Keyboard" button
4. **Test the keyboard:**
   - Use the app's test screen or any text input field

## Testing the Keyboard

### Core Functionality Tests

1. **Transformation Rules:**
   - Type: `ضض` → Should transform to `الضّ`
   - Type: `لق` → Should transform to `الق`
   - Type: `هاذا` → Should transform to `هذا`

2. **Dot Cycling:**
   - Type `ح` then press dot (•) → `خ` → `ج` → `ح`
   - Type `ب` then press dot → `ت` → `ث` → `ٮ`

3. **Keyboard Switching:**
   - Press the keyboard icon (⌨) to switch between layouts
   - Layout 1: Diacritics version
   - Layout 2: Full vowels version

4. **Special Keys:**
   - Space: Triggers transformation and API call
   - Delete: Removes characters and applies transformations
   - Enter: Sends newline
   - Dot: Cycles character variants

### API Integration

The keyboard supports backend integration for:
- Word suggestions
- Rhythm patterns
- Text corrections

To enable API features, update the backend URL in `ApiClient.kt`:
```kotlin
.baseUrl("http://your-backend-url.com/")
```

## Project Structure

```
app/src/main/java/com/phonemics/keyboard/
├── service/
│   └── PhonemicsKeyboardService.kt    # Main keyboard service
├── data/
│   ├── TransformationEngine.kt        # Arabic transformation rules
│   └── KeyboardLayoutManager.kt       # Keyboard layouts
├── model/
│   └── Models.kt                      # Data models
├── network/
│   └── ApiClient.kt                   # Backend API integration
├── ui/
│   └── KeyboardView.kt                # Keyboard UI components
├── MainActivity.kt                    # Setup and instructions
├── TestActivity.kt                    # Keyboard testing
└── SettingsActivity.kt                # Keyboard settings
```

## Key Components

### TransformationEngine
- Ports all 206 Arabic orthographic rules from the original TypeScript
- Handles diacritics, hamza placement, prefix transformations
- Includes dot transformation mappings for character variants

### KeyboardLayoutManager
- Two keyboard layouts with color-coded key groups
- Hexagonal arrangement (4 rows × 6 keys)
- Special action keys (delete, space, enter, dot, switch)

### PhonemicsKeyboardService
- Main `InputMethodService` implementation
- Manages text input, transformation, and API calls
- Debounced API integration (250ms delay)
- Operation state tracking to prevent infinite loops

## Debugging

### Common Issues

1. **Keyboard not appearing:**
   - Check if enabled in Settings > Virtual keyboard
   - Verify the keyboard is selected as active input method

2. **Transformations not working:**
   - Check `TransformationEngine` logs
   - Verify operation state management

3. **API not working:**
   - Check network permissions in manifest
   - Verify backend URL in `ApiClient.kt`
   - Test with offline mode (suggestions disabled)

### Logs
```bash
adb logcat | grep "PhonemicsKeyboard"
```

## License

Based on the original Phonemics Keyboard web application. Ported to Android with the same core transformation logic and functionality.