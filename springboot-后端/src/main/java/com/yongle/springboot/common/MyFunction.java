package com.yongle.springboot.common;


import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MyFunction {
     public static boolean isCidr(String str) {
        String cidrRegex = "^([0-9]{1,3}\\.){3}[0-9]{1,3}/([0-9]|[1-2][0-9]|3[0-2])$";
        return str.matches(cidrRegex);
    }
    public static boolean isValidIPv4(String ip) {
        // 定义IPv4的正则表达式
        String pattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        // 判断ip是否符合IPv4格式
        return ip.matches(pattern);
    }
    public static boolean matchCIDR(String ip, String cidr) {
        String[] cidrParts = cidr.split("/");
        String cidrIp = cidrParts[0];
        int cidrBits = Integer.parseInt(cidrParts[1]);

        long ipLong = ipToLong(ip);
        long cidrIpLong = ipToLong(cidrIp);

        long mask = 0xFFFFFFFF << (32 - cidrBits);
        return (ipLong & mask) == (cidrIpLong & mask);
    }

    private static long ipToLong(String ip) {
        String[] ipParts = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result |= Long.parseLong(ipParts[i]) << (24 - 8 * i);
        }
        return result;
    }
    public static Integer findMissingNumber(List<Integer> numbers) {
        int missingNumber = -1;
        for (int i = 0; i < numbers.size(); i++) {
            int currentNumber = numbers.get(i);
            if (i > 0 && currentNumber - numbers.get(i - 1) > 1) {
                missingNumber = numbers.get(i - 1) + 1;
                break;
            }
        }
        if (missingNumber == -1) {
            missingNumber = numbers.get(numbers.size() - 1) + 1;
        }
        return missingNumber;
    }
}
