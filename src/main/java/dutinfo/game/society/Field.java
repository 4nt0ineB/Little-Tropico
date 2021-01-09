package dutinfo.game.society;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dutinfo.game.GameUtils;

import java.util.*;

public class Field {

    private static Set<Field> listFields = new HashSet<>();
    private final int id;
    private final String name;
    private float exploitationPercentage;
    private float yieldPercentage;

    private Field(String name) {
        this.name = Objects.requireNonNull(name, "The field muste have a name");
        id = GameUtils.idByHashString(name);
        exploitationPercentage = 0;
        addField(this);
        yieldPercentage = 0;
    }

    public String getName() {
        return name;

    }

    public void setYieldPercentage(float yieldPercentage){
        this.yieldPercentage = yieldPercentage;
    }


    public float getExploitationPercentage() {
        return exploitationPercentage;
    }

    /**
     * Set the exploitationPercentage percentage rate
     *
     * @param exploitationPercentage Approbation percentage
     */
    public void setExploitationPercentage(float exploitationPercentage) {
        if(exploitationPercentage >= 100){
            this.exploitationPercentage = 100;
        }else if(exploitationPercentage <= 0){
            this.exploitationPercentage = 100;
        }else{
            this.exploitationPercentage = exploitationPercentage;
        }
    }

    public static List<Field> initField(String pathToFieldsFile) {
        try{
            List<Field> fields = new ArrayList<>();
            String jsonString = GameUtils.getResourceFileAsString(pathToFieldsFile);
            JsonObject convertedObject = new Gson().fromJson(jsonString, JsonObject.class);
            JsonArray array = convertedObject.getAsJsonArray("names");
            array.forEach(x -> {
                fields.add(new Field(x.getAsString()));
            });

            return fields;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    private static void addField(Field field) {
        listFields.add(field);
    }

    public int getId() {
        return id;
    }

    public static boolean exist(String n) {
        return listFields.stream().anyMatch(x -> x.getId() == GameUtils.idByHashString(n));
    }

    public int generateProfit(float capital){
        Integer profit = (int) (capital*yieldPercentage);
        return profit;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Field field = (Field) o;
        return id == field.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id);
    }
}
