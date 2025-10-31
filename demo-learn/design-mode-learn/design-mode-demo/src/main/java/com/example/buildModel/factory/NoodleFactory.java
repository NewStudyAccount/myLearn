package com.example.buildModel.factory;

import com.example.buildModel.domain.Food;
import com.example.buildModel.domain.Noodle;

public class NoodleFactory implements FoodFactoryInterface {
    @Override
    public Food makeFood(String flag) {
        Food noodle = new Noodle();
        noodle.setName("面"+flag);
        return noodle;
    }
}
