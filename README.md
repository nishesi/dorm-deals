# DormDeals - маркетплейс для студентов, где они могут размещать свои товары на продажу.
Далее описывается бэкенд приложения
## Фичи:
- Поиск товаров
- Создание магазина
- Размещение товаров
- Оформление заказа
- интерфейс для общения с продавцом
- Популярные товары
- Персональные рекомендации товаров
- Функциональная корзина

## Использованные технологии и описание:
- Spring Boot/MVC/Data/Security:
  - написан RestApi приложения, документация эндпоинтов описана с помощью Swagger
  - взаимодействие с базой данных происходит через Spring Data, используются как интерфейсы спринга, так и JPQL, SQL, CriteriaBuilder для большей гибкости и эффективности запросов.
  - реализована аутентификация и авторизация на основе JWT-токенов на базе Spring Security. Есть возможность выходить из аккаунта. Redis используется для хранения валидных и невалидных токенов.
  - использованы механизмы Spring scheduling для выполнения периодических задач.
- Elasticsearch:
  - реализован полнотекстовый поиск товаров для наилучшего совпадения и выявления опечаток в запросах.
- WebSocket:
  - реализован чат пользователя с продавцом товара для общения в реальном времени
- S3 Minio:
  - использован для хранения изображений товаров.
- Тестирование:
  - написаны unit-тесты с использованием технологий JUnit Jupiter, AssertJ
  - написаны интеграционные тесты с использованием технологий Testcontainers, Spring Test.
