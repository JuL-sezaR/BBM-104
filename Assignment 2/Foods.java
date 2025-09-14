/**
 * This class represents the foods that our zoo can have in stock
 * Used abstract class for a better implementation of foods' own details
 * Methods consist of food type and amount getters and setters
 */

public abstract class Foods {
    private String type;
    private double amount;

    public Foods(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }
    public double getAmount() {
        return amount;
    }
    public double setAmount(double amount) {
        this.amount = amount;
        return amount;
    }
}

class Meat extends Foods {
    public Meat(double amount) {
        super("Meat", amount);
    }
}

class Plant extends Foods {
    public Plant(double amount) {
        super("Plant", amount);
    }
}

class Fish extends Foods {
    public Fish(double amount) {
        super("Fish", amount);
    }
}

