package dutinfo.game;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameUtils {

    /**
     * Return Object from parsing json file
     * @param path Path to json file
     * */
    public static Object jsonToObject(String path){

        try(FileReader filereader = new FileReader(new File(path).getAbsolutePath())) {
            JSONParser jsonTest = new JSONParser();
            Object jsonObject = jsonTest.parse(filereader);
            return jsonObject;
        }catch (IOException | ParseException e){
            //e.printStackTrace();
        }
        return null;

    }

    /**
     * Gives an id from a string input
     * @param string the string to hash
     * */
    public static int idByHashString(String string){
        char[] ascii = string.toCharArray();
        int i = 0;
        for(char c: ascii){ i+=c; }
        return i;
    }

    /** Return the list of json from a directory (File object)
     * @param dir directory where to get the jsons
     * */
    public static List<File> allJsonFromDir(File dir){
        assert null != dir;
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(x -> {
                String name = x.getName();
                int i = name.lastIndexOf('.');
                return (i > 0) && name.substring(i + 1).equals("json");
            }).collect(Collectors.toList());
    }




}
