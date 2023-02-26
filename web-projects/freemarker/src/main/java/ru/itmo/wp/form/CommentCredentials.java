package ru.itmo.wp.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentCredentials {
    @NotNull
    @NotEmpty
    @Size(min = 2, max = 300)
    private String text;

    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }
}
