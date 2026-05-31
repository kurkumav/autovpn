# Инструкция по сборке AutoVPN APK

## Проблема

Для сборки Android приложения требуется:
- ✅ Java JDK (у вас установлена Java 25)
- ❌ Android SDK (не установлен)
- ❌ Android Build Tools
- ❌ Gradle с правильной конфигурацией

## Решения

### Вариант 1: Установить Android Studio (рекомендуется)

1. **Скачайте Android Studio:**
   - https://developer.android.com/studio
   - Размер: ~1 GB

2. **Установите Android Studio:**
   - Запустите установщик
   - Выберите "Standard" установку
   - Дождитесь загрузки Android SDK

3. **Откройте проект:**
   - File → Open → выберите папку `C:\Users\yarte\AutoVPN`
   - Дождитесь синхронизации Gradle (5-10 минут)

4. **Соберите APK:**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Или: Build → Generate Signed Bundle / APK

5. **Найдите APK:**
   - `C:\Users\yarte\AutoVPN\app\build\outputs\apk\debug\app-debug.apk`

### Вариант 2: Использовать онлайн-сервис (быстро)

**GitHub Actions (бесплатно):**

1. Создайте репозиторий на GitHub
2. Загрузите папку AutoVPN
3. Создайте файл `.github/workflows/build.yml`:

\`\`\`yaml
name: Build APK

on:
  push:
    branches: [ main ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          
      - name: Setup Android SDK
        uses: android-actions/setup-android@v2
        
      - name: Build APK
        run: |
          chmod +x gradlew
          ./gradlew assembleDebug
          
      - name: Upload APK
        uses: actions/upload-artifact@v3
        with:
          name: app-debug
          path: app/build/outputs/apk/debug/app-debug.apk
\`\`\`

4. Запустите workflow
5. Скачайте APK из Artifacts

### Вариант 3: Использовать Docker (для продвинутых)

\`\`\`bash
# Создайте Dockerfile
docker run --rm -v "%cd%":/project -w /project mingc/android-build-box bash -c "chmod +x gradlew && ./gradlew assembleDebug"
\`\`\`

### Вариант 4: Попросить кого-то собрать

Отправьте папку AutoVPN другу с Android Studio, и он соберёт APK за 5 минут.

### Вариант 5: Установить только Android SDK (без Android Studio)

1. **Скачайте Command Line Tools:**
   - https://developer.android.com/studio#command-tools
   - Размер: ~150 MB

2. **Распакуйте в:**
   - `C:\Android\cmdline-tools\latest\`

3. **Установите необходимые компоненты:**
\`\`\`cmd
cd C:\Android\cmdline-tools\latest\bin
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
\`\`\`

4. **Установите переменные окружения:**
\`\`\`cmd
setx ANDROID_HOME "C:\Android"
setx PATH "%PATH%;C:\Android\platform-tools;C:\Android\cmdline-tools\latest\bin"
\`\`\`

5. **Соберите APK:**
\`\`\`cmd
cd C:\Users\yarte\AutoVPN
gradlew.bat assembleDebug
\`\`\`

## Что я рекомендую

**Для быстрого результата:** Вариант 2 (GitHub Actions) - загрузите проект на GitHub, и он соберёт APK автоматически.

**Для долгосрочной разработки:** Вариант 1 (Android Studio) - полноценная среда разработки.

## Альтернатива: Готовый APK

Если вам нужен APK прямо сейчас, я могу:
1. Подготовить проект для сборки на другой машине
2. Создать GitHub репозиторий с автоматической сборкой
3. Дать инструкции для сборки на виртуальной машине

## Размеры загрузок

- Android Studio: ~1 GB + ~3 GB SDK
- Command Line Tools: ~150 MB + ~2 GB SDK
- GitHub Actions: 0 MB (всё в облаке)

Какой вариант вам больше подходит?
