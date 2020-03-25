package com.example.legendstale;

public class Update {

    private String name;
    private String description;
    private String twiter;
    private String web;
    private String imgUrl;
    private String contact;



    Update(){

    }
    Update(String n,String d, String t, String w, String i, String c){

        if(n.trim().equals(""))
            n = "Not found";
        else if(d.trim().equals(""))
            d = "Not found";
        else if(t.trim().equals(""))
            t = "Not found";
        else if(w.trim().equals(""))
            w = "Not found";
        else if(i.trim().equals(""))
            i = "Not found";
        name = n;
        description = d;
        twiter = t;
        web = w;
        imgUrl = i;
        contact = c;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTwiter() {
        return twiter;
    }

    public String getWeb() {
        return web;
    }

    public String getImgUrl() {
        return imgUrl;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTwiter(String twiter) {
        this.twiter = twiter;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }



}
