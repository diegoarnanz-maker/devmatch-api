package com.devmatch.api.projectreview.domain.model.valueobject;

import java.util.Objects;

public class Comment {
    private final String value;
    private static final int MAX_LENGTH = 1000;

    public Comment(String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("El comentario no puede superar los " + MAX_LENGTH + " caracteres.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(value, comment.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
} 