import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Main {
    @Test
    public void validateJsonUserFiles() throws IOException {
        File jsonFile_adminUsers = new File("src/admin_users.json");
        File jsonFile_regularUsers = new File("src/regular_users.json");
        File yamlFile_adminUsers = new File("src/admin_users.yaml");
        File yamlFile_regularUsers = new File("src/regular_users.yaml");

        // validate JSON file
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode1 = mapper.readTree(jsonFile_adminUsers);
        Assert.assertTrue(jsonNode1.isArray(), "JSON file should contain an array of users");
        for (JsonNode userNode : jsonNode1) {
            Assert.assertTrue(userNode.has("id"), "User should have an id field");
            Assert.assertTrue(userNode.has("name"), "User should have a name field");
            Assert.assertTrue(userNode.path("address").isMissingNode() || userNode.path("address").isObject(), "User may have an address field as object");
        }

        JsonNode jsonNode2 = mapper.readTree(jsonFile_regularUsers);

        Assert.assertTrue(jsonNode2.isArray(), "JSON file should contain an array of users");
        for (JsonNode userNode : jsonNode2) {
            Assert.assertTrue(userNode.has("id"), "User should have an id field");
            Assert.assertTrue(userNode.has("name"), "User should have a name field");
            Assert.assertTrue(userNode.path("address").isMissingNode() || userNode.path("address").isObject(), "User may have an address field as object");
        }
    }

    @Test
    public void validateYamlUserFiles() throws IOException {
        File jsonFile_adminUsers = new File("src/admin_users.json");
        File jsonFile_regularUsers = new File("src/regular_users.json");
        File yamlFile_adminUsers = new File("src/admin_users.yaml");
        File yamlFile_regularUsers = new File("src/regular_users.yaml");

//	        // validate YAML file
        Yaml yaml = new Yaml();
        InputStream yamlNode1 = new FileInputStream(yamlFile_adminUsers);
        Object obj1 = yaml.load(yamlNode1);
        InputStream yamlNode2 = new FileInputStream(yamlFile_regularUsers);
        Object obj2 = yaml.load(yamlNode2);

//	        Object obj = yaml.load(yamlFile);
        Assert.assertTrue(obj1 instanceof List, "YAML file should contain a list of users");
        List<Map<String, Object>> users1 = (List<Map<String, Object>>) obj1;
        for (Map<String, Object> user : users1) {
            Assert.assertTrue(user.containsKey("id"), "User should have an id field");
            Assert.assertTrue(user.containsKey("name"), "User should have a name field");
            Assert.assertTrue(!user.containsKey("address") || user.get("address") instanceof Map, "User may have an address field as object");
        }

        Assert.assertTrue(obj2 instanceof List, "YAML file should contain a list of users");
        List<Map<String, Object>> users2 = (List<Map<String, Object>>) obj2;
        for (Map<String, Object> user : users2) {
            Assert.assertTrue(user.containsKey("id"), "User should have an id field");
            Assert.assertTrue(user.containsKey("name"), "User should have a name field");
            Assert.assertTrue(!user.containsKey("address") || user.get("address") instanceof Map, "User may have an address field as object");
        }
    }
    //
    @Test
    public <JsonArray> void validateJsonWithYamlUserFiles() throws IOException {
        File jsonFile_adminUsers = new File("src/admin_users.json");
        File jsonFile_regularUsers = new File("src/regular_users.json");
        File yamlFile_adminUsers = new File("src/admin_users.yaml");
        File yamlFile_regularUsers = new File("src/regular_users.yaml");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode1 = mapper.readTree(jsonFile_adminUsers);
        JsonNode jsonNode2 = mapper.readTree(jsonFile_regularUsers);

        Yaml yaml = new Yaml();
        InputStream yamlNode1 = new FileInputStream(yamlFile_adminUsers);
        Object obj1 = yaml.load(yamlNode1);
        InputStream yamlNode2 = new FileInputStream(yamlFile_regularUsers);
        Object obj2 = yaml.load(yamlNode2);

//	        // compare the json and yaml files

        JsonNode yaml_admin = mapper.convertValue(obj1, JsonNode.class);
//	        Assert.assertEquals(jsonNode1, yaml_admin, "JSON and YAML files should be equivalent");


        JsonNode yaml_regular = mapper.convertValue(obj2, JsonNode.class);
//	        Assert.assertEquals(jsonNode2, yaml_regular, "JSON and YAML files should be equivalent");

        Set<Integer> ids1 = new HashSet<>();
        Set<Integer> ids2 = new HashSet<>();

        Iterator<JsonNode> elements1 = jsonNode1.elements();
        while (elements1.hasNext()) {
            JsonNode element = elements1.next();
            ids1.add(element.get("id").asInt());
        }

        Iterator<JsonNode> elements2 = yaml_admin.elements();
        while (elements2.hasNext()) {
            JsonNode element = elements2.next();
            ids2.add(element.get("id").asInt());
        }

//	        List<Integer> list1 = Arrays.stream(ids1).boxed().collect(Collectors.toList());
//	        List<Integer> list2 = Arrays.stream(ids2).boxed().collect(Collectors.toList());
//
//	        boolean result = list2.containsAll(list1);
//	        Assert.assertTrue(result, "Array2 doesn't contain all elements of Array1");
//	    }
//	        System.out.print(ids1);
//	        System.out.print(ids2);

        Assert.assertTrue(ids2.containsAll(ids1),"Elements with same id not found in YAML file");


        Set<Integer> ids11 = new HashSet<>();
        Set<Integer> ids22 = new HashSet<>();

        Iterator<JsonNode> elements11 = jsonNode2.elements();
        while (elements11.hasNext()) {
            JsonNode element = elements11.next();
            ids11.add(element.get("id").asInt());
        }

        Iterator<JsonNode> elements22 = yaml_regular.elements();
        while (elements22.hasNext()) {
            JsonNode element = elements22.next();
            ids22.add(element.get("id").asInt());
        }

//	        List<Integer> list1 = Arrays.stream(ids1).boxed().collect(Collectors.toList());
//	        List<Integer> list2 = Arrays.stream(ids2).boxed().collect(Collectors.toList());
//
//	        boolean result = list2.containsAll(list1);
//	        Assert.assertTrue(result, "Array2 doesn't contain all elements of Array1");
//	    }
//	        System.out.print(ids11);
//	        System.out.print(ids22);

        Assert.assertTrue(ids22.containsAll(ids11),"Elements with same id not found in YAML file");

    }

}