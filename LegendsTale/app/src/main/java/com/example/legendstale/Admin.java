package com.example.legendstale;

public class Admin {

    private String email;
    private String password;


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }



    public Admin(){

    }

    public Admin(String mail,String pass){
        this.email = mail;
        this.password = pass;
    }

}
