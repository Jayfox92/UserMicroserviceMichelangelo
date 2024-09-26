package com.michelangelo.usermicroservice.VO;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GenreVO {
    private Long id;
    private String name;
    @JsonIgnore
    private int count;

    public GenreVO() {
    }

    public GenreVO(Long id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
