package org.zhurko.blog.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertyUtil {

    private static final Properties properties = new Properties();

    private PropertyUtil() {
    }

    public static String getProperty(String name) {
        if (properties.isEmpty()) {
            try (InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream("database.properties")) {
                properties.load(in);
            } catch (IOException ex) {
                System.out.println("Error occurred during reading database properties file.");
                ex.printStackTrace();
                System.exit(1);
            }
        }

        return properties.getProperty(name);
    }
}
