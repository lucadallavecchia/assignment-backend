package com.gler.assignment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TextRequestDTO {

    @NotNull
    @Size(min = 2)
    private String text;

    public TextRequestDTO() {
        //NO-OP
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
