package com.example.buildModel.factory;

import com.example.buildModel.domain.Food;
import com.example.buildModel.domain.HuangMenChicken;

public class HuangMenChickenFactory implements FoodFactoryInterface {
    @Override
    public Food makeFood(String flag) {
        Food chicken = new HuangMenChicken();
        chicken.setName("黄焖鸡"+flag);
        return chicken;
    }
}
