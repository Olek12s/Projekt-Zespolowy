package com.example.demo.security;
import com.example.demo.hibernate.HibernateConfig;
import org.hibernate.Session;

public class TestCreateTable {

    public static void main(String[] args) {

        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            System.out.println("Created table.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
