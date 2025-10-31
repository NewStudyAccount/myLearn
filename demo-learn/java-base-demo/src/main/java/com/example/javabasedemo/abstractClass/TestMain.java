package com.example.javabasedemo.abstractClass;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestMain {

    public static void main(String[] args) throws URISyntaxException, IOException {
        URL location = TestMain.class.getClassLoader().getResource("helloworld.txt");
        Path path = Paths.get(location.toURI());
        BaseFileReader lowercaseFileReader = new LowercaseFileReader(path);
        BaseFileReader uppercaseFileReader = new UppercaseFileReader(path);

        System.out.println("Lowercase File Reader:"+lowercaseFileReader.test());
        System.out.println("Uppercase File Reader:"+uppercaseFileReader.test());
        System.out.println(lowercaseFileReader.readFile());
        System.out.println(uppercaseFileReader.readFile());


        StringBuilder sb = new StringBuilder();

    }
}
