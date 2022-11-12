package com.example.linventario;

public class SessionManager {

    private static SessionManager _instance = null;
    private static boolean logged = false;
    private int id;
    private String name;
    private String email;

    private SessionManager(){}

    public static SessionManager getInstance(){
        if (_instance == null)
            _instance = new SessionManager();
        return _instance;
    }

    public void setSession(int id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
        this.logged = true;
    }

    public static boolean isLogged() {
        return logged;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
