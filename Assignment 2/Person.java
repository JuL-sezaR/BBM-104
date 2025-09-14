/**
 * This class represents Persons that can enter the zoo
 * Used abstract class and methods for better implementation of Persons' data
 * Methods consist of getters of people's ids and names
 */

public abstract class Person {
    private String name;
    private int id;

    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }

    public abstract String getType();
}

class Personnel extends Person {
    Personnel(String name, int id){
        super(name, id);
    }

    public String getType(){
        return "Personnel";
    }
}

class Visitor extends Person {
    Visitor(String name, int id){
        super(name, id);
    }
    public String getType(){
        return "Visitor";
    }

}
