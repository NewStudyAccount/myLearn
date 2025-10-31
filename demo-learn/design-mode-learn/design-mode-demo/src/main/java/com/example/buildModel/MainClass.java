package com.example.buildModel;

import com.example.buildModel.domain.Food;
import com.example.buildModel.factory.FoodFactoryInterface;
import com.example.buildModel.factory.HuangMenChickenFactory;
import com.example.buildModel.factory.NoodleFactory;
import com.example.buildModel.simpleFactory.FoodFactory;

public class MainClass {
    public static void main(String[] args) {
        System.out.println("简单工厂");
        Food noodle = FoodFactory.makeFood("noodle");
        System.out.println(noodle.getName());
        System.out.println("当添加新的产品时，需要修改工厂类，违反开闭原则");
        Food chicken = FoodFactory.makeFood("chicken");
        System.out.println(chicken.getName());


        System.out.println("-----------------------------------------------------");

        System.out.println("工厂模式");
        FoodFactoryInterface noodleFactory = new NoodleFactory();
        Food noodle1 = noodleFactory.makeFood("noodle");
        System.out.println(noodle1.getName());
        System.out.println("当添加新的产品时，不需要修改原工厂类，新增一个工厂，符合开闭原则");
        FoodFactoryInterface chickenFactory = new HuangMenChickenFactory();
        Food chicken1 = chickenFactory.makeFood("chicken");
        System.out.println(chicken1.getName());

    }
}
