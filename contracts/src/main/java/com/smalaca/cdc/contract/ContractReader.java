package com.smalaca.cdc.contract;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContractReader {
    private final List<String> paths = ImmutableList.of("scenarios/eur-to-pln/", "scenarios/pln-to-usd/", "scenarios/unknown-from/");
    private final Gson gson = new Gson();

    public Input inputForScenario(int index) {
        return readInput(paths.get(index) + "input.json");
    }

    public Output outputForScenario(int index) {
        return readOutput(paths.get(index) + "output.json");
    }

    public Map<Input, Output> readAll() {
        Map<Input, Output> scenarios = new HashMap<>();
        paths.forEach(path -> {
            scenarios.put(readInput(path + "input.json"), readOutput(path + "output.json"));
        });

        return scenarios;
    }

    private Input readInput(String fileName) {
        JsonReader jsonReader = new JsonReader(readFile(fileName));

        return gson.fromJson(jsonReader, Input.class);
    }

    private Output readOutput(String fileName) {
        JsonReader jsonReader = new JsonReader(readFile(fileName));

        return gson.fromJson(jsonReader, Output.class);
    }

    private FileReader readFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            try {
                return new FileReader(new File(resource.getFile()));
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("could not read file!");
            }
        }

    }
}
