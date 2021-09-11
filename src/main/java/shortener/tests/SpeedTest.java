package shortener.tests;

import org.junit.Assert;
import org.junit.Test;
import shortener.Helper;
import shortener.Shortener;
import shortener.strategy.HashBiMapStorageStrategy;
import shortener.strategy.HashMapStorageStrategy;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SpeedTest {
    public long getTimeToGetIds(Shortener shortener, Set<String> strings, Set<Long> ids) {
        Date start = new Date();
        ids = strings.stream().map(shortener::getId).collect(Collectors.toSet());
        Date end = new Date();

        return end.getTime() - start.getTime();
    }

    public long getTimeToGetStrings(Shortener shortener, Set<Long> ids, Set<String> strings) {
        Date start = new Date();
        strings = ids.stream().map(shortener::getString).collect(Collectors.toSet());
        Date end = new Date();

        return end.getTime() - start.getTime();
    }

    @Test
    public void testHashMapStorage() {
        Shortener shortener1 = new Shortener(new HashMapStorageStrategy());
        Shortener shortener2 = new Shortener(new HashBiMapStorageStrategy());

        Set<String> origStrings = new HashSet<>();
        for (long i = 0; i < 10000; i++) {
            origStrings.add(Helper.generateRandomString());
        }

        Set<Long> ids1 = new HashSet<>();
        Set<Long> ids2 = new HashSet<>();

        long time1 = getTimeToGetIds(shortener1, origStrings, ids1);
        long time2 = getTimeToGetIds(shortener2, origStrings, ids2);

        Assert.assertTrue(time1 > time2);

        time1 = getTimeToGetStrings(shortener1, ids1, new HashSet<>());
        time2 = getTimeToGetStrings(shortener2, ids2, new HashSet<>());

        Assert.assertEquals(time1, time2, 30);
    }
}
