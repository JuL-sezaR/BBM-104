/*
In this class I used inheritance for the items that can be borrowed in the library
 */

/**
 * Here I used abstract base class to represent items in the library.
 * Tracks availability status and borrowing information.
 * Inherited for different item types (Book, DVD and Magazine).
 */
public abstract class Item {
    private String title;
    private String id;
    private String type;
    private String category;
    private boolean isAvailable;
    private String borrowDate;
    private String borrowedBy;

    /*
    Constructs a new item with general attributes
     */

    public Item(String id, String title, String type, String category) {
        this.title = title;
        this.id = id;
        this.type = type;
        this.category = category;
        this.isAvailable = true;
        this.borrowDate = null;
        this.borrowedBy = null;
    }

    // Getter Methods
    public String getTitle() {
        return title;
    }
    public String getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public String getCategory() {
        return category;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public String getBorrowDate() {
        return borrowDate;
    }
    public String getBorrowedBy() {
        return borrowedBy;
    }

    // Methods for borrowing and returning the items
    public void borrow(String borrowDate, String borrowedBy) {
        this.isAvailable = false;
        this.borrowDate = borrowDate;
        this.borrowedBy = borrowedBy;
    }
    public void returnItem() {
        this.isAvailable = true;
        this.borrowDate = null;
        this.borrowedBy = null;
    }
    public abstract void displayItemDetails();
}

/**
 * Represents a Book item in the library
 */
class Book extends Item {
    private String author;

    /**
     * Creates a new Book item.
     *
     * @param id Book's ID
     * @param title Book's title
     * @param author Author's name
     * @param category Genre of the book
     * @param type Book's rarity type
     */

    public Book(String id, String title, String author, String category, String type) {
        super(id, title, type, category);
        this.author = author;
    }

    // Getter Method for Author
    public String getAuthor() {
        return author;
    }

    @Override
    public void displayItemDetails() {
        System.out.println("------ Item Information for " + getId() + " ------");
        System.out.println("ID: " + getId() + " Name: " + getTitle() + " Status: " + (isAvailable() ? "Available" : "Borrowed"));
        if (!isAvailable()) {
            System.out.println("Borrowed Date: " + ((Book) this).getBorrowDate() + "Borrowed By: " + ((Book) this).getBorrowedBy());
        }
        System.out.println("Author: " + getAuthor() + " Genre: " + getCategory());
        System.out.println();
    }
}

/**
 * Represents a DVD item in the library
 */
class DVD extends Item {
    private String director;
    private String runtime;

    /**
     * Creates a new DVD item.
     *
     * @param id DVD's ID
     * @param title DVD's title
     * @param director Director's name
     * @param category Genre of the DVD
     * @param runtime Running time
     * @param type DVD's rarity tyoe
     */

    public DVD(String id, String title, String director, String category, String runtime, String type) {
        super(id, title, type, category);
        this.director = director;
        this.runtime = runtime;
    }

    // Getter Methods
    public String getDirector() {
        return director;
    }
    public String getRuntime() {
        return runtime;
    }

    @Override
    public void displayItemDetails() {
        System.out.println("------ Item Information for " + getId() + " ------");
        System.out.println("ID: " + getId() + " Name: " + getTitle() + " Status: " + (isAvailable() ? "Available" : "Borrowed"));
        if (!isAvailable()) {
            System.out.println("Borrowed Date: " + ((DVD) this).getBorrowDate() + " Borrowed by: " + ((DVD) this).getBorrowedBy());
        }
        System.out.println("Director: " + getDirector() + " Runtime: " + getRuntime());
        System.out.println();
    }
}

/**
 * Represents a Magazine item in the library
 */
class Magazine extends Item {
    private String publisher;

    /**
     * Creates a Magazine item.
     *
     * @param id Magazine's ID
     * @param title Magazine's title
     * @param publisher Publisher's name
     * @param category Magazine's category
     * @param type Magazine's rarity type
     */

    public Magazine(String id, String title, String publisher, String category, String type) {
        super(id, title, type, category);
        this.publisher = publisher;
    }

    // Getter Method for Publisher
    public String getPublisher() {
        return publisher;
    }

    @Override
    public void displayItemDetails() {
        System.out.println("------ Item Information for " + getId() + " ------");
        System.out.println("ID: " + getId() + " Name: " + getTitle() + " Status: " + (isAvailable() ? "Available" : "Not Available"));
        if (!isAvailable()) {
            System.out.println("Borrowed Date: " + ((Magazine)this).getBorrowDate() + " Borrowed By: " + ((Magazine)this).getBorrowedBy());
        }
        System.out.println("Publisher: " + getPublisher() + " Category: " + getCategory());
        System.out.println();
    }
}