package com.example.clsw;

public class SessionNameGenerator {
    public static String generateSessionName() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append((char) (Math.random() * 26 + 'A'));
        }

        return sb.toString();
    }
}
