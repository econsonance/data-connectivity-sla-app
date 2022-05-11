package org.trustrelay.data.connectivity.sla.app.enums;

import lombok.Getter;

@Getter
public enum OutputType {
    WEBHOOK("webhook"),
    STDOUT("stdout");

    private String value;

    OutputType(String value) {
        this.value = value;
    }
}
