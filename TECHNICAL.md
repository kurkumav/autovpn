# Технические детали AutoVPN

## Архитектура

### Компоненты

1. **MainActivity** - главный экран с Jetpack Compose
2. **AppMonitorService** - AccessibilityService для отслеживания запуска приложений
3. **AppPreferences** - управление настройками через SharedPreferences
4. **VpnClient** - модели данных для VPN-клиентов

### Принцип работы

```
Пользователь открывает YouTube
         ↓
AccessibilityService получает событие TYPE_WINDOW_STATE_CHANGED
         ↓
Проверка: YouTube в списке отслеживаемых?
         ↓ (да)
Получение выбранного VPN-клиента из настроек
         ↓
Отправка Intent для запуска VPN
         ↓
VPN-клиент запускается/активируется
```

### Cooldown механизм

Чтобы избежать множественных запусков, реализован cooldown на 5 секунд:
- Если то же приложение запускается повторно в течение 5 секунд, VPN не перезапускается

## Разрешения

### Обязательные

- `INTERNET` - для работы VPN-клиентов
- `QUERY_ALL_PACKAGES` - для получения списка установленных приложений
- `PACKAGE_USAGE_STATS` - для мониторинга использования приложений
- `FOREGROUND_SERVICE` - для работы службы в фоне
- `POST_NOTIFICATIONS` - для уведомлений (Android 13+)

### AccessibilityService

Требует ручного включения пользователем в настройках системы.

**Используемые события:**
- `TYPE_WINDOW_STATE_CHANGED` - изменение активного окна

**Флаги:**
- `FLAG_REPORT_VIEW_IDS` - для получения ID элементов

## Интеграция с VPN-клиентами

### Метод 1: Intent с действием (предпочтительный)

```kotlin
Intent("com.hiddify.app.START_VPN").apply {
    setPackage("com.hiddify.app")
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}
```

Работает для:
- Hiddify
- v2rayNG
- Shadowsocks

### Метод 2: Launch Intent (запасной)

```kotlin
packageManager.getLaunchIntentForPackage("com.github.kr328.clash")?.apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
}
```

Работает для всех клиентов, но только открывает приложение.

## Добавление новых VPN-клиентов

Отредактируйте `VpnClient.kt`:

```kotlin
VpnClient(
    name = "Название клиента",
    packageName = "com.example.vpn",
    startAction = "com.example.vpn.START_VPN" // или null
)
```

## Хранение данных

### SharedPreferences

**Ключи:**
- `monitored_apps` - Set<String> с package names отслеживаемых приложений
- `selected_vpn` - String с package name выбранного VPN-клиента
- `service_enabled` - Boolean состояние службы

## Производительность

- Минимальное потребление батареи (только события AccessibilityService)
- Нет постоянных фоновых процессов
- Нет сетевых запросов
- Легковесный UI на Compose

## Безопасность

### Что приложение НЕ делает:

- ❌ Не читает содержимое экрана
- ❌ Не перехватывает ввод
- ❌ Не отправляет данные в интернет
- ❌ Не имеет доступа к файлам
- ❌ Не использует камеру/микрофон

### Что приложение делает:

- ✅ Отслеживает только package name активного приложения
- ✅ Запускает другие приложения по команде пользователя
- ✅ Хранит настройки локально

## Тестирование

### Ручное тестирование

1. Установите VPN-клиент (например, v2rayNG)
2. Настройте VPN-подключение в клиенте
3. Установите AutoVPN
4. Включите Accessibility Service
5. Выберите v2rayNG как VPN-клиент
6. Добавьте YouTube в список
7. Откройте YouTube
8. Проверьте, что v2rayNG запустился

### Логирование

Используйте `adb logcat` для отладки:

```bash
adb logcat | grep AppMonitorService
```

Логи:
- `Service connected` - служба запущена
- `Detected monitored app: X` - обнаружено отслеживаемое приложение
- `Started VPN client: X` - VPN-клиент запущен
- `Failed to start VPN client` - ошибка запуска

## Известные проблемы

### Android 12+

На Android 12+ некоторые производители ограничивают AccessibilityService. Решение:
1. Отключить оптимизацию батареи
2. Разрешить автозапуск
3. Заблокировать в списке недавних приложений

### Xiaomi MIUI

MIUI агрессивно убивает фоновые службы:
1. **Настройки** → **Батарея** → **Управление приложениями**
2. Найти AutoVPN → **Нет ограничений**
3. **Настройки** → **Приложения** → **Автозапуск** → Включить для AutoVPN

### Huawei EMUI

Аналогично MIUI, требует:
1. Отключение оптимизации батареи
2. Разрешение автозапуска
3. Блокировка в списке недавних

## Будущие улучшения

### Возможные функции:

1. **Встроенный VPN-клиент** - поддержка протоколов без внешних приложений
2. **Правила по времени** - автоматическое включение VPN в определённое время
3. **Геолокация** - включение VPN в определённых местах
4. **Статистика** - отслеживание использования VPN
5. **Профили** - разные VPN для разных приложений
6. **Автоотключение** - отключение VPN при закрытии приложения

## Сборка релиза

```bash
# Создать keystore
keytool -genkey -v -keystore autovpn.keystore -alias autovpn -keyalg RSA -keysize 2048 -validity 10000

# Собрать release APK
./gradlew assembleRelease

# Подписать APK
jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 -keystore autovpn.keystore app/build/outputs/apk/release/app-release-unsigned.apk autovpn

# Оптимизировать APK
zipalign -v 4 app/build/outputs/apk/release/app-release-unsigned.apk AutoVPN-release.apk
```

## Контакты

Для вопросов и предложений создавайте Issues в репозитории проекта.
