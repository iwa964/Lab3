package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final List<String> countries = new ArrayList<>();
    private final List<List<String>> languages = new ArrayList<>();
    private final List<List<String>> translations = new ArrayList<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                var obj = jsonArray.getJSONObject(i);
                String country = obj.getString("country");
                JSONArray countryLanguages = obj.getJSONArray("languages");
                JSONArray translationsArray = obj.getJSONArray("translations");

                List<String> languageList = new ArrayList<>();
                List<String> translationList = new ArrayList<>();

                for (int j = 0; j < countryLanguages.length(); j++) {
                    languageList.add(countryLanguages.getString(j));
                    translationList.add(translationsArray.getString(j));
                }
                countries.add(country);
                languages.add(languageList);
                translations.add(translationList);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        int index = countries.indexOf(country);
        if (index != -1) {
            return languages.get(index);
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(countries);
    }

    @Override
    public String translate(String country, String language) {
        int countryIndex = countries.indexOf(country);
        if (countryIndex != -1) {
            List<String> languageList = languages.get(countryIndex);
            int languageIndex = languageList.indexOf(language);
            if (languageIndex != -1) {
                return translations.get(countryIndex).get(languageIndex);
            }
        }
        return null;
    }
}
