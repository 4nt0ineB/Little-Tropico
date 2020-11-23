package dut.info.console;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
            e.printStackTrace();
        }
        return null;
    }
}
