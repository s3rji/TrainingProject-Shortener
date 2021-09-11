package shortener.tests;

import org.junit.Assert;
import org.junit.Test;
import shortener.Shortener;
import shortener.strategy.*;

public class FunctionalTest {

    public void testStorage(Shortener shortener) {
        String string1 = "WdwdadaDAWDdad123213";
        String string2 = "dadkjwhkhdajdfa821h2j";
        String string3 = "WdwdadaDAWDdad123213";

        Long id1 = shortener.getId(string1);
        Long id2 = shortener.getId(string2);
        Long id3 = shortener.getId(string3);

        Assert.assertNotEquals(id1, id2);
        Assert.assertNotEquals(id3, id2);

        Assert.assertEquals(id1, id3);

        String value1 = shortener.getString(id1);
        String value2 = shortener.getString(id2);
        String value3 = shortener.getString(id3);

        Assert.assertEquals(string1, value1);
        Assert.assertEquals(string2, value2);
        Assert.assertEquals(string3, value3);
    }

    @Test
    public void testHashMapStorageStrategy() {
        HashMapStorageStrategy mapStrategy = new HashMapStorageStrategy();
        Shortener shortener = new Shortener(mapStrategy);

        testStorage(shortener);
    }

    @Test
    public void testOurHashMapStorageStrategy() {
        OurHashMapStorageStrategy ourStrategy = new OurHashMapStorageStrategy();
        Shortener shortener = new Shortener(ourStrategy);

        testStorage(shortener);
    }

    @Test
    public void testFileStorageStrategy() {
        FileStorageStrategy fileStrategy = new FileStorageStrategy();
        Shortener shortener = new Shortener(fileStrategy);

        testStorage(shortener);
    }

    @Test
    public void testHashBiMapStorageStrategy() {
        HashBiMapStorageStrategy biMapStrategy = new HashBiMapStorageStrategy();
        Shortener shortener = new Shortener(biMapStrategy);

        testStorage(shortener);
    }

    @Test
    public void testDualHashBidiMapStorageStrategy() {
        DualHashBidiMapStorageStrategy bidiMapStrategy = new DualHashBidiMapStorageStrategy();
        Shortener shortener = new Shortener(bidiMapStrategy);

        testStorage(shortener);
    }

    @Test
    public void testOurHashBiMapStorageStrategy() {
        OurHashBiMapStorageStrategy ourBiMapStrategy = new OurHashBiMapStorageStrategy();
        Shortener shortener = new Shortener(ourBiMapStrategy);

        testStorage(shortener);
    }
}
