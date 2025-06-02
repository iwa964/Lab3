package org.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    private static final String QUIT = "quit";

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {

        Translator translator = new JSONTranslator("sample.json");

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        org.translation.CountryCodeConverter cc = new CountryCodeConverter();
        org.translation.LanguageCodeConverter lc = new LanguageCodeConverter();
        while (true) {
            String countryCode = promptForCountry(translator);
            if (QUIT.equals(countryCode)) {
                break;
            }
            String languageCode = promptForLanguage(translator, countryCode);
            if (QUIT.equals(languageCode)) {
                break;
            }
            String countryName = cc.fromCountryCode(countryCode);
            String languageName = lc.fromLanguageCode(languageCode);

            System.out.println(countryName + " in " + languageName + " is "
                    + translator.translate(countryCode, languageCode));
            System.out.println("Press enter to continue or quit to exit.");

            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (QUIT.equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();

        List<String> countryNames = new ArrayList<>();
        List<String> countryCodes = translator.getCountries();
        for (String code : countryCodes) {
            countryNames.add(countryCodeConverter.fromCountryCode(code));
        }
        Collections.sort(countryNames);
        for (String name : countryNames) {
            System.out.println(name);
        }

        System.out.println("select a country from above:");
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();

        if (QUIT.equals(input)) {
            return QUIT;
        }

        return countryCodeConverter.fromCountry(input);
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country) {
        LanguageCodeConverter langCodeConverter = new LanguageCodeConverter();

        List<String> langNames = new ArrayList<>();
        List<String> langCode = translator.getCountryLanguages(country);
        // country is countrycode here

        for (String code : langCode) {
            langNames.add(langCodeConverter.fromLanguageCode(code));
        }
        Collections.sort(langNames);
        for (String name : langNames) {
            System.out.println(name);
        }

        System.out.println("select a language from above:");
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();

        if (QUIT.equals(input)) {
            return QUIT;
        }

        return langCodeConverter.fromLanguage(input);
    }
}
