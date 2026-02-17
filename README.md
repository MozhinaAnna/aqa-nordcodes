Фреймворк для интеграционного тестирования API, тестовое задание для компании Nord.Codes
Написан на Java с использованием Maven, TestNG и RestAssured.

Перед выполнением тестов фреймворк программно запускает тестируемый сервис из JAR-файла и все его внешние зависимости, которые мокируются с помощью WireMock.
Используется TestNG suites (`smoke.xml`, `regress.xml`) для гибкого запуска различных наборов тестов.
Интеграция с Allure Framework для генерации отчетов о выполнении тестов.
Применяется библиотеки Awaitility для ожидания готовности сервиса перед началом тестов.

Стек технологий

Язык: Java 21
Сборка: Apache Maven
Тестовый фреймворк: TestNG
API-клиент: RestAssured
Отчеты: Allure Framework
Мокирование: WireMock
Ассерты: AssertJ
Вспомогательные библиотеки: Lombok, JavaFaker, Awaitility

Запуск регресс сьюта: 
mvn clean test -DsuiteXmlFiles=src/test/resources/regres.xml
Запуск смоук сьюта:
mvn clean test -DsuiteXmlFiles=src/test/resources/smoke.xml
Генерация отчета в Allure:
mvn clean test allure:report allure:serve
