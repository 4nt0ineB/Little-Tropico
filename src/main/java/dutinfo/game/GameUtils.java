package dutinfo.game;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GameUtils {

    /**
     * Return JsonObject from parsing json file without using resources
     *
     * @param path Path to json file
     */
    public static JsonObject jsonToObject(String path) {

        try{
            String json = "";
            InputStream inputStream = new DataInputStream(new FileInputStream(new File(path).getAbsolutePath()));
            if (inputStream == null) return null;

            try (InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
                 BufferedReader reader = new BufferedReader(isr)) {
                json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
            JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
            return convertedObject;
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return null;

    }

    /**
     * Gives an id from a string input
     *
     * @param string the string to hash
     */
    public static int idByHashString(String string) {
        char[] ascii = string.toCharArray();
        int i = 0;
        for (char c : ascii) {
            i += c;
        }
        return i;
    }

    /**
     * Return the list of json from a directory (File object)
     *
     * @param dir directory where to get the jsons
     */
    public static List<File> allJsonFromDir(File dir) {
        assert null != dir;
        return Arrays.stream(Objects.requireNonNull(dir.listFiles())).filter(x -> {
            String name = x.getName();
            int i = name.lastIndexOf('.');
            return (i > 0) && name.substring(i + 1).equals("json");
        }).collect(Collectors.toList());
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    public static String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

}
