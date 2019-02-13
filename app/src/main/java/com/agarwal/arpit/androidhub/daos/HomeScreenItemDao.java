package com.agarwal.arpit.androidhub.daos;

public class HomeScreenItemDao {

    public HomeScreenItemDao(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    private String name ;
    private String desc ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
