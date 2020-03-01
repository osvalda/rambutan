package com.github.osvalda.rambutan.uitest.framework;

import java.util.UUID;

public class RandomUtil {

    public static String getRandomEmailAddress() {
        return ("testEmail-"
                .concat(UUID.randomUUID().toString().replaceAll("-", "")).toLowerCase())
                .concat("@test.email.com");
    }

}
