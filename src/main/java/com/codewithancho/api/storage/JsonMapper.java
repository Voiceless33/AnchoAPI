package com.codewithancho.api.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class JsonMapper<T> {

    private final File file;
    private final Class<T> clazz;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private T data;

    public JsonMapper(File file, Class<T> clazz) {
        this.file = file;
        this.clazz = clazz;
    }

    public void load() throws IOException {
        if(!file.exists()){
            file.getParentFile().mkdirs();
            data = createEmptyInstance();
            save();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            data = gson.fromJson(reader, clazz);
        }
    }

    public void save() throws IOException {
        try (FileWriter fw = new FileWriter(file)){
            fw.write(data.toString());
        }
    }

    private T createEmptyInstance(){
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                 NoSuchMethodException |
                 IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
