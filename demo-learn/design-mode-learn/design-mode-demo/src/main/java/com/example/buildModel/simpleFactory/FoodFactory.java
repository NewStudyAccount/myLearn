package com.example.buildModel.simpleFactory;


import com.example.buildModel.domain.Food;
import com.example.buildModel.domain.HuangMenChicken;
import com.example.buildModel.domain.Noodle;

public class FoodFactory {

    public static Food makeFood(String flag){
        if ("noodle".equals(flag)) {
            Food noodle = new Noodle();
            noodle.setName("面");
            return noodle;
        }else if ("chicken".equals(flag)){
            Food chicken = new HuangMenChicken();
            chicken.setName("黄焖鸡");
            return chicken;
        }else {
            return null;
        }

    }


}
