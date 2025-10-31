package com.dodo.ai_trader.service.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class SHA256Util {

    public static String sha256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * 网页上输入框的SHA256加密
     * @param input
     * @return
     * @throws Exception
     */
    public static String sha256Hex(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes());

        // 转换为16进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 加盐SHA256哈希
     */
    public static String sha256WithSalt(String input, String salt) throws Exception {
        String saltedInput = input + salt;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(saltedInput.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * AES加密
     * @param data 待加密的数据
     * @param key 密钥（盐）
     * @return 加密后的数据(Base64编码)
     * @throws Exception 加密异常
     */
    public static String encryptAES(String data, String key) throws Exception {
        // 确保密钥长度为16字节（128位）
        if (key.length() > 16) {
            key = key.substring(0, 16);
        } else if (key.length() < 16) {
            // 如果密钥不足16位，则用'a'填充到16位
            key = String.format("%-16s", key).replace(' ', 'a');
        }

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * AES加密并返回十六进制字符串
     * @param data 待加密的数据
     * @param key 密钥（盐）
     * @return 加密后的数据(十六进制编码)
     * @throws Exception 加密异常
     */
    public static String encryptAESHax(String data, String key) throws Exception {
        // 确保密钥长度为16字节（128位）
        if (key.length() > 16) {
            key = key.substring(0, 16);
        } else if (key.length() < 16) {
            // 如果密钥不足16位，则用'a'填充到16位
            key = String.format("%-16s", key).replace(' ', 'a');
        }

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());

        // 转换为16进制字符串
        StringBuilder hexString = new StringBuilder();
        for (byte b : encryptedData) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * AES解密
     * @param encryptedData 待解密的数据(Base64编码)
     * @param key 密钥（盐）
     * @return 解密后的数据
     * @throws Exception 解密异常
     */
    public static String decryptAES(String encryptedData, String key) throws Exception {
        // 确保密钥长度为16字节（128位）
        if (key.length() > 16) {
            key = key.substring(0, 16);
        } else if (key.length() < 16) {
            // 如果密钥不足16位，则用'a'填充到16位
            key = String.format("%-16s", key).replace(' ', 'a');
        }

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }

    /**
     * 将十六进制字符串解密为原始数据
     * @param encryptedDataHex 待解密的数据(十六进制编码)
     * @param key 密钥（盐）
     * @return 解密后的数据
     * @throws Exception 解密异常
     */
    public static String decryptAESFromHex(String encryptedDataHex, String key) throws Exception {
        // 确保密钥长度为16字节（128位）
        if (key.length() > 16) {
            key = key.substring(0, 16);
        } else if (key.length() < 16) {
            // 如果密钥不足16位，则用'a'填充到16位
            key = String.format("%-16s", key).replace(' ', 'a');
        }

        // 将十六进制字符串转换为字节数组
        byte[] encryptedData = new byte[encryptedDataHex.length() / 2];
        for (int i = 0; i < encryptedDataHex.length(); i += 2) {
            encryptedData[i / 2] = (byte) ((Character.digit(encryptedDataHex.charAt(i), 16) << 4)
                    + Character.digit(encryptedDataHex.charAt(i+1), 16));
        }

        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData);
    }



    public static void main(String[] args) throws Exception {

        String input = "hello world111111111";
        String salt = "salt";
//        String hashed = encryptAES(input, salt);
//        System.out.println("hashed: " + hashed);
//        System.out.println("decrypted: " + decryptAES(hashed, salt));

        // 测试新的十六进制方法
        String hashedHex = encryptAESHax(input, salt);
        System.out.println("hashedHex: " + hashedHex);
        System.out.println("decrypted from hex: " + decryptAESFromHex(hashedHex, salt));
    }

}
