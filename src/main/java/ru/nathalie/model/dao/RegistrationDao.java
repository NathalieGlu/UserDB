package ru.nathalie.model.dao;

public interface RegistrationDao {

    boolean createUser(String name, String phone, String mail);
}
