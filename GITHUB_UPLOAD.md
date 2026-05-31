# 🚀 Инструкция по загрузке на GitHub и получению APK

## Шаг 1: Создайте репозиторий на GitHub

1. Откройте https://github.com/new
2. Заполните:
   - **Repository name:** `AutoVPN`
   - **Description:** `Android app for auto-starting VPN when opening specific apps`
   - **Visibility:** Public (или Private, если хотите)
   - ❌ НЕ добавляйте README, .gitignore или LICENSE (они уже есть)
3. Нажмите **"Create repository"**

## Шаг 2: Загрузите код на GitHub

Откройте терминал в папке `C:\Users\yarte\AutoVPN` и выполните:

```bash
# Добавьте удалённый репозиторий (замените YOURUSERNAME на ваш GitHub username)
git remote add origin https://github.com/YOURUSERNAME/AutoVPN.git

# Переименуйте ветку в main (если нужно)
git branch -M main

# Загрузите код на GitHub
git push -u origin main
```

**Пример с реальным username:**
```bash
git remote add origin https://github.com/ivan123/AutoVPN.git
git branch -M main
git push -u origin main
```

## Шаг 3: Дождитесь сборки APK

1. После push перейдите на GitHub в ваш репозиторий
2. Откройте вкладку **"Actions"** (вверху)
3. Вы увидите запущенный workflow **"Build Android APK"**
4. Дождитесь завершения (обычно 3-5 минут)
5. ✅ Зелёная галочка = сборка успешна

## Шаг 4: Скачайте APK

1. Кликните на завершённый workflow
2. Прокрутите вниз до раздела **"Artifacts"**
3. Скачайте **"AutoVPN-debug"** (это ZIP архив)
4. Распакуйте ZIP → внутри будет `app-debug.apk`

## Шаг 5: Установите на телефон

**Способ 1: Через USB**
```bash
adb install app-debug.apk
```

**Способ 2: Вручную**
1. Скопируйте `app-debug.apk` на телефон (через USB, облако, мессенджер)
2. Откройте файл на телефоне
3. Разрешите установку из неизвестных источников (если попросит)
4. Нажмите "Установить"

## Шаг 6: Настройте приложение

1. Откройте **AutoVPN**
2. Включите **Accessibility Service** (приложение откроет настройки)
3. Выберите **VPN-клиент** (например, Hiddify или v2rayNG)
4. Добавьте **приложения** для мониторинга (YouTube, Telegram)
5. Готово! 🎉

## 📝 Полезные команды Git

```bash
# Проверить статус
git status

# Посмотреть удалённые репозитории
git remote -v

# Обновить README и загрузить изменения
git add README.md
git commit -m "Update README"
git push

# Создать новую версию (тег)
git tag v1.0.0
git push --tags
```

## 🔧 Если что-то пошло не так

### Ошибка: "remote origin already exists"
```bash
git remote remove origin
git remote add origin https://github.com/YOURUSERNAME/AutoVPN.git
```

### Ошибка: "failed to push"
```bash
git pull origin main --rebase
git push -u origin main
```

### GitHub Actions не запускается
- Проверьте, что файл `.github/workflows/build.yml` загружен
- Убедитесь, что Actions включены в настройках репозитория:
  - Settings → Actions → General → "Allow all actions"

### APK не собирается
- Откройте вкладку Actions
- Кликните на failed workflow
- Посмотрите логи ошибок
- Создайте Issue с описанием проблемы

## 🎯 Что дальше?

После успешной установки:
1. Прочитайте [USAGE.md](USAGE.md) для подробной инструкции
2. Настройте VPN-клиент (Hiddify, v2rayNG и т.д.)
3. Добавьте приложения для автозапуска VPN
4. Наслаждайтесь автоматизацией! 🚀

## 💡 Совет

Обновите README.md и замените `YOURUSERNAME` на ваш реальный GitHub username, чтобы badge работал корректно:

```bash
# Откройте README.md и замените YOURUSERNAME на ваш username
# Затем:
git add README.md
git commit -m "Update GitHub username in README"
git push
```

---

**Время сборки APK:** ~3-5 минут  
**Размер APK:** ~5-10 MB  
**Требования:** Android 7.0+
