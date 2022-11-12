package com.example.linventario;

public class SessionManager {

    private static SessionManager _instance = null;
    private static boolean logged = false;
    private int id;
    private String name;
    private String email;

    private SessionManager(){};
    public static SessionManager getInstance(){
        if (_instance == null)
            _instance = new SessionManager();
        return _instance;
    }


}
