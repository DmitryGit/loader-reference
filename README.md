# Справочник погрузчиков

Веб-приложение на Spring Boot + Vue.js для учёта погрузчиков и их простоев.

## Требования
- Java 17+
- Maven 3.8+
- Node.js 18+ и npm
- PostgreSQL 14+

## Настройка базы данных
Создайте базу данных и пользователя:
```sql
CREATE DATABASE loader_db;
CREATE USER loader_user WITH PASSWORD 'secret';
GRANT ALL PRIVILEGES ON DATABASE loader_db TO loader_user;