package com.hunter.owen.myethics;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class EthicGroup {
    @Expose
    private String name;
    @Expose
    private int id;
    @Expose
    private List<EthicTag> tags;

    public EthicGroup(String name, int id){
        this.name = name;
        this.id = id;
        this.tags = new ArrayList<EthicTag>();
    }

    public EthicGroup(String name, int id, List<EthicTag> tags){
        this.name = name;
        this.id = id;
        this.tags = tags;
    }

    public EthicGroup(EthicGroup ethicGroup) {
        this.name = ethicGroup.name;
        this.id = ethicGroup.id;
        this.tags = ethicGroup.tags;
    }

    public List<EthicTag> getTagsList() {
        return tags;
    }

    public void setTags(List<EthicTag> tags) {
        this.tags = tags;
    }

    public void addTag(EthicTag tag) {
        this.tags.add(tag);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
