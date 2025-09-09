package com.krainet.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

record dbInfo(String host,String port, String dbName){}

public class DbInit{

    @Value("${spring.datasource.url}")
    private static String dbUrl;

    @Value("${spring.datasource.username}")
    private static String dbUser;

    @Value("${spring.datasource.password}")
    private static String dbPassword;



    public static void run(ApplicationArguments args) throws Exception {
        var info = parseUrl(dbUrl);
        String postgresUrl = String.format("jdbc:postgresql://%s:%s/postgres", info.host(), info.port());

        try (Connection conn = DriverManager.getConnection(postgresUrl, dbUser, dbPassword)) {
            ResultSet resultSet = conn.createStatement()
                    .executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + info.dbName() + "'");
            if (!resultSet.next()) {
                conn.createStatement().execute("CREATE DATABASE " + info.dbName());
                System.out.println("db '" + info.dbName() + "' successfully created");
            } else {
                System.out.println("db '" + info.dbName() + "' already exists");
            }
        }
    }

    private static dbInfo parseUrl(String jdbcUrl) throws Exception {
        Pattern pattern = Pattern.compile("jdbc:postgresql://([^:/]+)(:(\\d+))?/([^?]+)");
        Matcher matcher = pattern.matcher(jdbcUrl);

        if (matcher.find()) {
            String host = matcher.group(1);
            String port = matcher.group(3) != null ? matcher.group(3) : "5432"; // по умолчанию
            String dbName = matcher.group(4);
            return new dbInfo(host,port, dbName);
        }
            throw new Exception("Unable to parse the connection string");

    }
}

