package main;

import java.io.IOException;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class configurations {
	
    public static final String CONFIG_FILE_PATH = "BookData.json";
    public static String base_url;


    public static String POST_New_Book;
    public static String GET_All_Books;
    public static String GET_Book_ById;
    public static String PUT_Book_ById;
    public static String DELETE_Book;
    public static String Negative_Scenarios_URL;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(Paths.get(CONFIG_FILE_PATH).toFile());
            base_url = root.path("baseUrl").asText();

            POST_New_Book = base_url + "Books";
            GET_All_Books = base_url + "Books";
            GET_Book_ById = base_url + "Books/{id}";
            PUT_Book_ById = base_url + "Books/{id}";
            DELETE_Book = base_url + "Books/{id}";
            Negative_Scenarios_URL = base_url + "Books/";
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load base_url from " + CONFIG_FILE_PATH);
        }
    }
	

}
