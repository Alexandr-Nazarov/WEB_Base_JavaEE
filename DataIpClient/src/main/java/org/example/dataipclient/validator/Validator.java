package org.example.dataipclient.validator;

import java.util.regex.Pattern;

public class Validator {
    private static final Pattern RUSSIAN_PATTERN = Pattern.compile("^[а-яА-ЯёЁ\\-\\\"\\,\\.\\s]+$");
    private static final Pattern IP_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    private static final Pattern MAC_PATTERN = Pattern.compile("^[0-9a-fA-F]{2}(:[0-9a-fA-F]{2}){5}$");

    public static boolean isValidClientName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 100 &&
                RUSSIAN_PATTERN.matcher(name).matches();
    }

    public static boolean isValidType(String type) {
        return "Corporate".equals(type) || "Individual".equals(type);
    }

    public static boolean isValidIP(String ip) {
        return ip != null && !ip.trim().isEmpty() && ip.length() <= 25 && IP_PATTERN.matcher(ip).matches();
    }

    public static boolean isValidMAC(String mac) {
        return mac != null && !mac.trim().isEmpty() && mac.length() <= 20 && MAC_PATTERN.matcher(mac).matches();
    }

    public static boolean isValidModel(String model) {
        return model != null && !model.trim().isEmpty() && model.length() <= 100;
    }

    public static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty() && address.length() <= 200;
    }
}
