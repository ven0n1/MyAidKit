package com.example.myaidkit;

public class Medicine {
    String name;
    String link;
    String form;

    public Medicine(String name, String link, String form) {
        this.name = name;
        this.link = link;
        this.form = form;
    }

    public String getName() {
        return name;
    }

    public String getForm() {
        return form;
    }

    public String getLink() {
        return link;
    }

}
