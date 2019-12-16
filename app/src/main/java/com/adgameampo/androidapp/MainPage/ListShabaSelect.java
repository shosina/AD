package com.adgameampo.androidapp.MainPage;

public class ListShabaSelect {
    public long id;
    public String title;
    public String number;
    public boolean IsSelect;

    public ListShabaSelect(long id, String title, String number, boolean isSelect) {
        this.id = id;
        this.title = title;
        this.number = number;
        IsSelect = isSelect;
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

    public boolean isSelect() {
        return IsSelect;
    }

    public void setSelect(boolean select) {
        IsSelect = select;
    }
}
