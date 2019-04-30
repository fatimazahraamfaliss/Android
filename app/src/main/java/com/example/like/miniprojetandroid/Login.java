package com.example.like.miniprojetandroid;

public class Login {
    public String name;

    public String getName() {
        return name;
    }

    public Login() {
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String password;

    public Login(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
