package com.mbl.zaikavendor.Model;

/**
 * Created by sachin on 1/14/2019.
 */

public class DataObject {
    private String name;
    public DataObject(){}
    public DataObject(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}