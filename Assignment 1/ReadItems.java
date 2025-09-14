import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is for reading item information from files provided and creates Item objects.
 */
public class ReadItems {

    /**
     * Reads item daya from provided text file and creates Item objects.
     *
     * @param filePath Path of the text file
     * @return List of Item objects created from the provided data file
     * @throws IOException If an error occurs during the reading process
     *
     * @see Book
     * @see DVD
     * @see Magazine
     *
     * Each line represents one item with comma seperated attributes:
     * - For Books: B,id,title,author,category,type
     * - For DVDs: D,id,title,director,category,runtime,type
     * - For Magazines: M,id,title,publisher,category,tyoe
     */
    public static List<Item> readItemsFromFile(String filePath) throws IOException {
        List<Item> items = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String classType = data[0].trim();
                String id = data[1].trim();
                String title = data[2].trim();
                String type = data[data.length - 1].trim();

                switch (classType.toUpperCase()) {
                    case "B": // Book
                        String author = data[3].trim();
                        String category = data[4].trim();
                        items.add(new Book(id, title, author, category, type));
                        break;

                    case "D": // DVD
                        String director = data[3].trim();
                        String dvdCategory = data[4].trim();
                        String runtime = data[5].trim();
                        items.add(new DVD(id, title, director, dvdCategory, runtime, type));
                        break;

                    case "M": // Magazine
                        String publisher = data[3].trim();
                        String magazineCategory = data[4].trim();
                        items.add(new Magazine(id, title, publisher, magazineCategory, type));
                        break;

                    default:
                        System.out.println("Unknown item type: " + classType);
                }
            }
        }

        return items;
    }
}