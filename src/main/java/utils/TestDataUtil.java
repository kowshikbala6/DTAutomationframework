package utils;

import java.util.HashMap;
import java.util.Map;

public class TestDataUtil {

    private static final Map<String, Object> testData = new HashMap<>();

    static {
        // Initialize test data
        testData.put("valid_username", "admin@banking.com");
        testData.put("valid_password", "Password123!");
        testData.put("invalid_password", "WrongPassword");
        testData.put("account_number", "1234567890");
        testData.put("account_type", "Savings");
        testData.put("beneficiary_account", "9876543210");
        testData.put("transfer_amount", "1000");
        testData.put("deposit_amount", "5000");
        testData.put("withdrawal_amount", "500");
    }

    public static Object get(String key) {
        return testData.get(key);
    }

    public static String getString(String key) {
        Object value = testData.get(key);
        return value != null ? value.toString() : "";
    }

    public static void set(String key, Object value) {
        testData.put(key, value);
    }

    public static Map<String, Object> getAllTestData() {
        return new HashMap<>(testData);
    }
}
