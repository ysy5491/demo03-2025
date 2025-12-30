package com.ll.demo03.standard.utill.utill;

import com.ll.demo03.global.AppConfig;
import lombok.SneakyThrows;

public class Ut {
    public static class str {
        public static boolean isBlank(String str) {
            return str == null || str.isBlank();
        }

        public static boolean hasLength(String str) {
            return !isBlank(str);
        }
    }

    public static class json {
        @SneakyThrows
        public static String toString(Object obj) {
            return AppConfig.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }
    }

    public static class cmd {

        public static void runAsync(String cmd) {
            new Thread(() -> {
                run(cmd);
            }).start();
        }

        public static void run(String cmd) {
            try {
                ProcessBuilder pb = new ProcessBuilder(
                        "bash", "-lc", cmd
                );
                pb.redirectErrorStream(true);

                Process process = pb.start();

                try (var reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream())
                )) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }

                int exitCode = process.waitFor();
                System.out.println("Command exit code: " + exitCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

