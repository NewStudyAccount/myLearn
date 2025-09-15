package com.example.javabasedemo.abstractClass;

import java.nio.file.Path;

class LowercaseFileReader extends BaseFileReader {
    protected LowercaseFileReader(Path filePath) {
        super(filePath);
    }

    @Override
    public String test() {
        return "小写";
    }

    @Override
    protected String mapFileLine(String line){
        return line.toLowerCase();
    }
}