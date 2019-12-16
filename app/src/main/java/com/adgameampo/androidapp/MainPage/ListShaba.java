package com.adgameampo.androidapp.MainPage;

public class ListShaba {
    public long id;
    public String title;
    public String number;

    public ListShaba(long id, String title, String number) {
        this.id = id;
        this.title = title;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
