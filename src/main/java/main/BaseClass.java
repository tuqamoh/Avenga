package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;

import com.google.gson.Gson;

import io.restassured.path.json.JsonPath;

public class BaseClass {
	
    protected static final String JSON_FILE_PATH = "BookData.json";

    protected List<Map<String, Object>> scenarios;
    protected Map<String, Object> currentScenario;
    protected String payload;
    protected JsonPath expectedJson;
    public int BookId;

    @BeforeClass
    public void init() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));
        JsonPath root = new JsonPath(json);
        scenarios = root.getList("scenarios");

        System.out.println("Loaded " + scenarios.size() + " scenarios.");
    }

    protected void setScenario(int index) {
        if (scenarios == null || scenarios.isEmpty()) {
            throw new IllegalStateException("Scenarios list is not initialized. Call init() first.");
        }

        currentScenario = scenarios.get(index);
        payload = new Gson().toJson(currentScenario);
        expectedJson = new JsonPath(payload);
    }

}
