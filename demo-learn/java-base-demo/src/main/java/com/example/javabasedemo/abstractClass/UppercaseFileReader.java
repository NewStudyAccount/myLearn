package com.example.javabasedemo.abstractClass;

import java.nio.file.Path;

class UppercaseFileReader extends BaseFileReader {
    protected UppercaseFileReader(Path filePath) {
        super(filePath);
    }

    @Override
    public String test() {
        return "大写";
    }

    @Override
    protected String mapFileLine(String line) {
        return line.toUpperCase();
    }
}