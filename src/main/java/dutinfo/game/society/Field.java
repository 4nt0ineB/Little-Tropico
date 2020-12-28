package dutinfo.game.society;

import dutinfo.game.GameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Field {

    private static Set<Field> listFields = new HashSet<>();
    private final int id;
    private final String name;
    private double exploitationPercentage;

    private Field(String name) {
        this.name = Objects.requireNonNull(name, "The field muste have a name");
        id = GameUtils.idByHashString(name);
        exploitationPercentage = 0.0d;
        addField(this);
    }

    public String getName() {
        return name;
    }

    public double getExploitationPercentage() {
        return exploitationPercentage;
    }

    /**
     * Set the exploitationPercentage percentage rate
     *
     * @param exploitationPercentage Approbation percentage
     */
    public void setExploitationPercentage(double exploitationPercentage) {
        this.exploitationPercentage = exploitationPercentage;
    }

    public static List<Field> initField(String pathToFieldsFile) {
        List<Field> fields = new ArrayList<>();
        Object jsonFields = GameUtils.jsonToObject(pathToFieldsFile);
        JSONArray fieldsAr = (JSONArray) jsonFields;
        assert fieldsAr != null;
        Object o1 = fieldsAr.get(0);
        JSONObject ev1 = (JSONObject) o1;
        JSONArray fieldNames = (JSONArray) ev1.get("names");
        fieldNames.forEach(x -> {
            fields.add(new Field((String) x));
        });
        return fields;
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
    public int hashCode() {
        return Objects.hash(id);
    }
}
