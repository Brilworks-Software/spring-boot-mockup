package com.brilworks.mockup.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseListEntity<T> {

    private List<T> data;
    private long count;

    public ResponseListEntity(List<T> data, long count) {
        this.data = data;
        this.count = count;
    }
}
