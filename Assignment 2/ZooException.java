/**
 * This class is mainly for the custom exceptions that may happen in the zoo
 */

// Base exception class
public class ZooException extends Exception {
    public ZooException(String message) {
        super(message);
    }
}

// Custom exception for food being not enough
class NotEnoughFood extends ZooException{
    public NotEnoughFood(String message) {
        super(message);
    }
}

// Custom exception for a person without an ID or with a wrong ID
class InvalidPersonID extends ZooException{
    public InvalidPersonID(String message) {
        super(message);
    }
}

// Custom exception for an invalid animal, it may be type or it may be just name
class InvalidAnimal extends ZooException{
    public InvalidAnimal(String message) {
        super(message);
    }
}

// Custom exception which is for the case if a visitor tries to feed an animal
class VisitorsCantFeed extends ZooException{
    public VisitorsCantFeed(String message) {
        super(message);
    }
}

// Custom exception which occurs when there is not enough food stock
class NotEnoughStock extends ZooException{
    public NotEnoughStock(String message) {
        super(message);
    }
}

// Custom exception for an invalid command
class InvalidCommandException extends ZooException{
    public InvalidCommandException(String message) {
        super(message);
    }
}
