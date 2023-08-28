package com.brilworks.mockup.utils;

import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class DateUtils {
    public Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
}
