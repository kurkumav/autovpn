# Быстрая загрузка на GitHub

## Команды для выполнения:

```bash
# 1. Добавьте ваш GitHub репозиторий (замените YOURUSERNAME)
git remote add origin https://github.com/YOURUSERNAME/AutoVPN.git

# 2. Переименуйте ветку в main
git branch -M main

# 3. Загрузите код
git push -u origin main
```

## Пример с реальным username:

Если ваш GitHub username: **ivan123**

```bash
git remote add origin https://github.com/ivan123/AutoVPN.git
git branch -M main
git push -u origin main
```

## После push:

1. Откройте https://github.com/YOURUSERNAME/AutoVPN
2. Перейдите во вкладку **Actions**
3. Увидите запущенную сборку "Build Android APK"
4. Через 3-5 минут сборка завершится
5. Кликните на завершённый workflow
6. Прокрутите вниз до **Artifacts**
7. Скачайте **AutoVPN-debug.zip**
8. Распакуйте → получите **app-debug.apk**
9. Скопируйте APK на телефон и установите

## Готово! 🎉

После установки:
- Включите Accessibility Service
- Выберите VPN-клиент
- Добавьте приложения (YouTube, Telegram)
- VPN будет запускаться автоматически!
