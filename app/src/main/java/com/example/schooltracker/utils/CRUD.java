package com.example.schooltracker.utils;

public interface CRUD<T> {
    void insert(T ob);
    void delete(T ob);
    void update(T ob);
}
