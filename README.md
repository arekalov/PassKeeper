
### Стек
- Kotlin
- Android navigation
- Kotlin Coroutines
- Room
- Glide
- biometric (вход по отпечатку)
- tink <https://developers.google.com/tink> (шифрование)
- ssp, sdp <https://github.com/intuit/sdp> (лучшая адаптация приложения к разным размерам экрана)

### Архитектура
1. App (точка входа в приложение, presentation слой, stateHolders)
3. Data (Работа с бд, модель пароля, репозитории для работы с бд и шифрованием)

### Особенности реализации
- Clean Architecture, MVVM, Single Activity
- Навигация с помощью navigation components
- Возможность войти в приложение по отпечатку пальца
- Шифрование данных и пароля входа с помощью tink (aead, AES256_GCM) с использование секретных ключей PBKDF2withHmacSHA256 и дополнительно генерируемой соли
- Сохранение паролей в Room в зашифрованном виде
- Использование EncryptedSharedPreferences
- Возможность изменить пароль по старому паролю или отпечатку
- Блокировка скриншотов внутри приложения
- Темная тема
