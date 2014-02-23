package com.hsbc.hsdc.javacomm.wechat.bean;

import java.util.List;

public class TestData<T> {
    
    private String identifier;
    private List<T> items;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}

