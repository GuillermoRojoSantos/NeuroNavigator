package com.main.neuronavigator;

import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class Libro {
    public ObjectId _id;
    public String nombre;
    public List<String> autor;

    public Libro(String nombre, List<String> autor) {
        this.nombre = nombre;
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "_id=" + _id +
                ", nombre='" + nombre + '\'' +
                ", autor=" + autor +
                '}';
    }
}
