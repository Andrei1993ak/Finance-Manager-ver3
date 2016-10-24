package com.gmail.a93ak.andrei19.finance30.model.pojos;


public class Currency {

    private long id;
    private String code;
    private String name;

    public Currency() {
    }

    public Currency(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
