# Быстрый старт AutoVPN

## Что это?

AutoVPN - Android приложение, которое автоматически запускает VPN при открытии выбранных приложений (YouTube, Telegram и др.). Аналог Apple Shortcuts для автоматизации VPN.

## Быстрая установка

### Требования

- Android 7.0+ (API 24)
- Установленный VPN-клиент (Hiddify, v2rayNG, Shadowsocks и др.)
- Android Studio или Gradle для сборки

### Шаг 1: Сборка

```bash
cd AutoVPN
chmod +x gradlew
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

### Шаг 2: Установка

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Шаг 3: Настройка (на устройстве)

1. Откройте AutoVPN
2. Включите **Accessibility Service** (приложение откроет настройки)
3. Выберите VPN-клиент
4. Добавьте приложения (YouTube, Telegram)
5. Готово!

## Как использовать

1. Откройте YouTube → VPN запустится автоматически
2. Откройте Telegram → VPN запустится автоматически
3. Другие приложения работают без VPN

## Поддерживаемые VPN

- ✅ Hiddify (рекомендуется)
- ✅ v2rayNG
- ✅ Shadowsocks
- ⚠️ Clash for Android (только открывает приложение)
- ⚠️ Surfshark (только открывает приложение)

## Структура проекта

```
AutoVPN/
├── app/
│   ├── src/main/
│   │   ├── java/com/autovpn/app/
│   │   │   ├── MainActivity.kt              # UI (Jetpack Compose)
│   │   │   ├── data/
│   │   │   │   ├── AppPreferences.kt        # Настройки
│   │   │   │   └── VpnClient.kt             # VPN клиенты
│   │   │   ├── service/
│   │   │   │   └── AppMonitorService.kt     # Мониторинг приложений
│   │   │   └── ui/theme/Theme.kt
│   │   ├── res/                             # Ресурсы
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── README.md                                # Основная документация
├── USAGE.md                                 # Инструкция пользователя
├── TECHNICAL.md                             # Технические детали
└── LICENSE                                  # MIT License
```

## Ключевые файлы

| Файл | Описание |
|------|----------|
| `MainActivity.kt` | Главный экран с UI |
| `AppMonitorService.kt` | Служба отслеживания приложений |
| `AppPreferences.kt` | Хранение настроек |
| `VpnClient.kt` | Список поддерживаемых VPN |
| `AndroidManifest.xml` | Разрешения и компоненты |

## Технологии

- **Язык:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Мониторинг:** AccessibilityService
- **Хранение:** SharedPreferences
- **Минимальная версия:** Android 7.0 (API 24)
- **Целевая версия:** Android 14 (API 34)

## Отладка

```bash
# Просмотр логов
adb logcat | grep AppMonitorService

# Проверка службы
adb shell dumpsys accessibility | grep AutoVPN

# Очистка данных
adb shell pm clear com.autovpn.app
```

## Частые вопросы

**Q: VPN не запускается автоматически?**  
A: Проверьте, что Accessibility Service включен в настройках системы.

**Q: Приложение не отслеживается?**  
A: Убедитесь, что оно добавлено в список и служба включена.

**Q: Работает ли на Xiaomi/Huawei?**  
A: Да, но нужно отключить оптимизацию батареи и разрешить автозапуск.

**Q: Можно ли добавить свой VPN-клиент?**  
A: Да, отредактируйте `VpnClient.kt` и добавьте новый клиент в список.

## Безопасность

- ✅ Открытый исходный код
- ✅ Нет сбора данных
- ✅ Нет интернет-соединений (кроме VPN)
- ✅ Минимальные разрешения
- ✅ Локальное хранение настроек

## Дальнейшее развитие

Возможные улучшения:
1. Встроенный VPN-клиент (WireGuard, Shadowsocks)
2. Автоотключение VPN при закрытии приложения
3. Разные VPN для разных приложений
4. Правила по времени и геолокации
5. Статистика использования

## Документация

- `README.md` - обзор проекта
- `USAGE.md` - подробная инструкция для пользователей
- `TECHNICAL.md` - технические детали для разработчиков
- `QUICKSTART.md` - этот файл

## Лицензия

MIT License - свободное использование и модификация.

---

**Создано:** 2026-05-31  
**Версия:** 1.0  
**Платформа:** Android 7.0+
