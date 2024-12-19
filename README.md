# Socks Inventory Service

Это приложение управляет учётом носков на складе. Можно добавлять носки, списывать их, обновлять информацию и импортировать данные пакетно.

## Функциональность

- **Поиск носков (`GET /api/socks`)**: Получает количество носков, соответствующих заданным фильтрам.
- **Приход носков (`POST /api/socks/income`)**: Добавляет заданное количество носков указанного цвета и процентного содержания хлопка.
- **Расход носков (`POST /api/socks/outcome`)**: Списывает носки со склада.
- **Обновление носков (`PUT /api/socks/{id}`)**: Обновляет информацию о носках по ID.
- **Импорт носков пакетно (`POST /api/socks/batch`)**: Импортирует носки из файла.

## Документация

После запуска приложения Swagger UI будет доступен по адресу:  
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

OpenAPI спецификация в JSON:  
[http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Запуск локально

### Требования

- Java 17
- Gradle

### Шаги

1. **Сборка приложения:**

    ```bash
    ./gradlew clean build
    ```

2. **Запуск приложения:**

    ```bash
    java -jar build/libs/socks-inventory-0.0.1-SNAPSHOT.jar
    ```

3. **Приложение будет доступно по адресу:**  
   [http://localhost:8080](http://localhost:8080)

## Запуск в Docker

### Шаги

1. **Сборка Docker-образа:**

    ```bash
    docker build -t socks-inventory:latest .
    ```

2. **Запуск контейнера:**

    ```bash
    docker run -d -p 8080:8080 --name socks-inventory socks-inventory:latest
    ```

3. **Приложение будет доступно по адресу:**  
   [http://localhost:8080](http://localhost:8080)

## Пример использования API

### Поиск носков (GET)

**Запрос:**

```bash
curl "http://localhost:8080/api/socks?color=red&operation=moreThan&cottonPart=50"
