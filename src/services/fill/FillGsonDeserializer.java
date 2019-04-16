package services.fill;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * loads vital information into arrays for FillService
 */
class FillGsonDeserializer {
    /**
     * Loads locations into the locations array for FillService
     *
     * @return Array of locations
     * @throws FileNotFoundException
     */
    ArrayList<Location> GetLocations() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader("json/locations.json"));

        JSONTokener tokener = new JSONTokener(bufferedReader);
        JSONObject rootObj = new JSONObject(tokener);

        JSONArray array = rootObj.getJSONArray("data");

        ArrayList<Location> locations = new ArrayList<>();

        for (int i = 0; i < array.length(); ++i) {
            JSONObject element = array.getJSONObject(i);
            String country = element.getString("country");
            String city = element.getString("city");
            Double lat = element.getDouble("latitude");

            Double lon = element.getDouble("longitude");
            locations.add(new Location(country, city, lat.toString(), lon.toString()));
        }

        //Location[] locations = gson.fromJson(bufferedReader, Location[].class);
        //return new ArrayList<>(Arrays.asList(locations));
        return locations;
    }

    /**
     * Loads Female names into the fnames array for FillService
     *
     * @return list of names (string)
     * @throws FileNotFoundException
     */
    ArrayList<String> GetFnames() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader("json/fnames.json"));
        JSONTokener tokener = new JSONTokener(bufferedReader);
        JSONObject rootObj = new JSONObject(tokener);

        JSONArray array = rootObj.getJSONArray("data");
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i) {
            names.add(array.getString(i));
        }
        return names;
    }

    /**
     * Loads male names into the mnames array for FillService
     *
     * @return list of names (string)
     * @throws FileNotFoundException
     */
    ArrayList<String> GetMnames() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader("json/mnames.json"));
        JSONTokener tokener = new JSONTokener(bufferedReader);
        JSONObject rootObj = new JSONObject(tokener);

        JSONArray array = rootObj.getJSONArray("data");
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i) {
            names.add(array.getString(i));
        }
        return names;
    }

    /**
     * Loads surnames names into the snames array for FillService
     *
     * @return list of names (string)
     * @throws FileNotFoundException
     */
    ArrayList<String> GetSnames() throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader("json/snames.json"));
        JSONTokener tokener = new JSONTokener(bufferedReader);
        JSONObject rootObj = new JSONObject(tokener);

        JSONArray array = rootObj.getJSONArray("data");
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < array.length(); ++i) {
            names.add(array.getString(i));
        }
        return names;
    }
}
