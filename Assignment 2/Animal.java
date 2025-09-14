/**
 * This class represents the Animals that can be in our zoo
 * Used abstract class for methods to get animals' type, food type and lastly how much food they will need to get fed
 * Inherited all animals (Lion, Elephant, Penguin, Chimpanzee) from Animal base class
 * Overridden abstract methods for each animal separately
 */

public abstract class Animal {
    private String name;
    private int age;

    public Animal(String name, int age){
        this.name = name;
        this.age = age;
    }
    public abstract String getFoodType();

    public abstract String getType();

    public abstract double eat();

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

class Lion extends Animal {
    Lion(String name, int age) {
        super(name, age);
    }

    @Override
    public String getFoodType() {
        return "Meat";
    }

    @Override
    public String getType() {
        return "Lion";
    }

    @Override
    public double eat() {
        int baseAge = 5;
        double baseMealSize = 5.0;
        double adjustmentPerAge = 0.05;

        return baseMealSize + (getAge() - baseAge) * adjustmentPerAge;
    }
}

class Elephant extends Animal {
    Elephant(String name, int age) {
        super(name, age);
    }

    @Override
    public String getFoodType() {
        return "Plant";
    }

    @Override
    public String getType() {
        return "Elephant";
    }

    @Override
    public double eat() {
        int baseAge = 20;
        double baseMealSize = 10.0;
        double adjustmentPerAge = 0.015;

        return baseMealSize + (getAge() - baseAge) * adjustmentPerAge;
    }
}

class Penguin extends Animal {
    Penguin(String name, int age) {
        super(name, age);
    }

    @Override
    public String getFoodType() {
        return "Fish";
    }

    @Override
    public String getType() {
        return "Penguin";
    }

    @Override
    public double eat() {
        int baseAge = 4;
        double baseMealSize = 3.0;
        double adjustmentPerAge = 0.040;

        return baseMealSize + (getAge() - baseAge) * adjustmentPerAge;
    }
}

class Chimpanzee extends Animal {
    Chimpanzee(String name, int age) {
        super(name, age);
    }

    @Override
    public String getFoodType() {
        return "Meat and Plant";
    }

    @Override
    public String getType() {
        return "Chimpanzee";
    }

    @Override
    public double eat() {
        int baseAge = 10;
        double baseMealSize = 6.0;
        double adjustmentPerAge = 0.025;

        return (baseMealSize + (getAge() - baseAge) * adjustmentPerAge)/2;
    }
}
