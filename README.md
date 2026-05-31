# AutoVPN 🚀

[![Build APK](https://github.com/YOURUSERNAME/AutoVPN/actions/workflows/build.yml/badge.svg)](https://github.com/YOURUSERNAME/AutoVPN/actions/workflows/build.yml)

Android приложение для автоматического запуска VPN при открытии определённых приложений. Аналог Apple Shortcuts для автоматизации VPN.

## 📱 Возможности

- ✅ Автоматический запуск VPN при открытии выбранных приложений (YouTube, Telegram и др.)
- ✅ Поддержка популярных VPN-клиентов:
  - Hiddify (рекомендуется)
  - v2rayNG
  - Shadowsocks
  - Clash for Android
  - Surfshark
- ✅ Простой интерфейс на Jetpack Compose с Material 3
- ✅ Возможность добавления/удаления приложений
- ✅ Работает в фоновом режиме через AccessibilityService
- ✅ Cooldown механизм для предотвращения множественных запусков

## 📥 Скачать APK

**Вариант 1: Готовый APK из GitHub Actions**
1. Перейдите во вкладку [Actions](../../actions)
2. Выберите последний успешный build
3. Скачайте `AutoVPN-debug.apk` из Artifacts

**Вариант 2: Releases**
- Скачайте последнюю версию из [Releases](../../releases)

## 🔧 Требования

- Android 7.0 (API 24) или выше
- Установленный VPN-клиент (один из поддерживаемых)

## 📖 Установка и настройка

1. **Скачайте и установите APK** на устройство
2. **Включите Accessibility Service:**
   - Настройки → Специальные возможности → AutoVPN → Включить
3. **Выберите VPN-клиент** в приложении
4. **Добавьте приложения** для мониторинга (YouTube, Telegram и др.)
5. **Готово!** Теперь VPN будет запускаться автоматически

Подробная инструкция: [USAGE.md](USAGE.md)

## 🛠️ Как работает

```
Пользователь открывает YouTube
         ↓
AccessibilityService получает событие
         ↓
Проверка: YouTube в списке отслеживаемых?
         ↓ (да)
Запуск выбранного VPN-клиента
         ↓
VPN активируется автоматически
```

## 🏗️ Сборка проекта

### Через Android Studio:
```bash
git clone https://github.com/YOURUSERNAME/AutoVPN.git
cd AutoVPN
# Откройте проект в Android Studio
# Build → Build Bundle(s) / APK(s) → Build APK(s)
```

### Через командную строку:
```bash
git clone https://github.com/YOURUSERNAME/AutoVPN.git
cd AutoVPN
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

### Через GitHub Actions:
- Просто сделайте push в репозиторий
- APK соберётся автоматически и будет доступен в Actions → Artifacts

## 📚 Документация

- [USAGE.md](USAGE.md) - Подробная инструкция для пользователей
- [TECHNICAL.md](TECHNICAL.md) - Технические детали для разработчиков
- [QUICKSTART.md](QUICKSTART.md) - Быстрый старт
- [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) - Инструкции по сборке

## 🔐 Безопасность

- ✅ Открытый исходный код
- ✅ Нет сбора данных
- ✅ Нет интернет-соединений (кроме VPN)
- ✅ Минимальные разрешения
- ✅ Локальное хранение настроек

## 🛣️ Roadmap

- [ ] Встроенный VPN-клиент (WireGuard, Shadowsocks)
- [ ] Автоотключение VPN при закрытии приложения
- [ ] Разные VPN для разных приложений (профили)
- [ ] Правила по времени и геолокации
- [ ] Статистика использования

## 🤝 Вклад в проект

Pull requests приветствуются! Для крупных изменений сначала откройте issue для обсуждения.

## 📄 Лицензия

[MIT](LICENSE)

## 💬 Поддержка

Если у вас возникли проблемы:
1. Проверьте [USAGE.md](USAGE.md) - там есть раздел "Устранение неполадок"
2. Создайте [Issue](../../issues) с описанием проблемы

---

**Создано:** 2026-05-31  
**Версия:** 1.0  
**Платформа:** Android 7.0+
