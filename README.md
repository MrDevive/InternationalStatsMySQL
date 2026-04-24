InternationalStatsMySQL
MySQL-плагин для сбора и хранения статистики игроков на Minecraft сервере.

📋 Возможности
Сбор статистики игроков (время игры, убийства, смерти и т.д.)

Интеграция с MySQL для надёжного хранения данных

Поддержка LuckPerms для получения групп игроков

Автоматическое обновление статистики в реальном времени

🔧 Требования
Minecraft сервер версии 1.16+ (или ваша версия)

Java 17 или выше

MySQL сервер (5.7+ или 8.0+)

LuckPerms (опционально, для групп)

📥 Установка
Скачайте последнюю версию плагина из Releases

Поместите .jar файл в папку plugins вашего сервера

Перезапустите сервер

Настройте plugins/InternationalStatsMySQL/config.yml

⚙️ Конфигурация
yaml
# config.yml
mysql:
  host: localhost
  port: 3306
  database: stats_db
  username: root
  password: your_password

settings:
  update-interval: 60  # обновление каждые 60 секунд
  save-on-quit: true   # сохранять статистику при выходе
  
🛠️ Команды

Команда	Описание	Права
/stats	Показать свою статистику	stats.use
/stats <игрок>	Показать статистику игрока	stats.use
/stats reload	Перезагрузить конфиг	stats.admin

📄 Лицензия
MIT License — свободно используйте и модифицируйте.

👤 Автор
Devive
