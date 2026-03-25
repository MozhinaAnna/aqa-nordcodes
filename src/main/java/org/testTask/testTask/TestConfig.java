package org.testTask.testTask;

public class TestConfig {

    /**
     * Получает базовый URL для тестируемого сервиса из переменной окружения APP_BASE_URL.
     * Если переменная не установлена, используется значение по умолчанию "http://localhost".
     */
    public static final String APP_BASE_URL = getEnvOrDefault("APP_BASE_URL", "http://localhost");

    private static String getEnvOrDefault(String envName, String defaultValue) {
        String value = System.getenv(envName);
        if (value == null || value.isEmpty()) {
            System.out.printf("WARN: Environment variable '%s' not set. Using default value: '%s'%n", envName, defaultValue);
            return defaultValue;
        }
        System.out.printf("INFO: Using '%s' from environment variable '%s'%n", value, envName);
        return value;
    }
}
