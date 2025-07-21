package com.devmatch.api.projectreview.domain.model.valueobject;

public class Rating {
    private final int value;

    public Rating(int value) {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("El rating debe estar entre 1 y 5.");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return value == rating.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
} 