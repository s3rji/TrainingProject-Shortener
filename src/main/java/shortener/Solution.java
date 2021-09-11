package shortener;

import shortener.strategy.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Solution {
    public static void main(String[] args) {
        HashMapStorageStrategy mapStrategy = new HashMapStorageStrategy();
        testStrategy(mapStrategy, 50000);

        OurHashMapStorageStrategy ourStrategy = new OurHashMapStorageStrategy();
        testStrategy(ourStrategy, 50000);

        FileStorageStrategy fileStrategy = new FileStorageStrategy();
        testStrategy(fileStrategy, 50);

        OurHashBiMapStorageStrategy ourBiMapStrategy = new OurHashBiMapStorageStrategy();
        testStrategy(ourBiMapStrategy, 50000);

        HashBiMapStorageStrategy biMapStrategy = new HashBiMapStorageStrategy();
        testStrategy(biMapStrategy, 50000);

        DualHashBidiMapStorageStrategy bidiMapStrategy = new DualHashBidiMapStorageStrategy();
        testStrategy(bidiMapStrategy, 50000);
    }

    public static Set<Long> getIds(Shortener shortener, Set<String> strings) {
        return strings.stream().map(shortener::getId).collect(Collectors.toSet());
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys) {
        return keys.stream().map(shortener::getString).collect(Collectors.toSet());
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber) {
        Helper.printMessage(strategy.getClass().getSimpleName());

        Set<String> generatedStrings = new HashSet<>();
        for (long i = 0; i < elementsNumber; i++) {
            generatedStrings.add(Helper.generateRandomString());
        }

        Shortener shortener = new Shortener(strategy);

        Date startGetIds = new Date();
        Set<Long> ids = getIds(shortener, generatedStrings);
        Date endGetIds = new Date();
        Helper.printMessage(String.valueOf(endGetIds.getTime() - startGetIds.getTime()));

        Date startGetStrings = new Date();
        Set<String> strings = getStrings(shortener, ids);
        Date endGetStrings = new Date();
        Helper.printMessage(String.valueOf(endGetStrings.getTime() - startGetStrings.getTime()));

        if (generatedStrings.size() == strings.size())
            Helper.printMessage("Тест пройден.");
        else
            Helper.printMessage("Тест не пройден.");

    }
}
