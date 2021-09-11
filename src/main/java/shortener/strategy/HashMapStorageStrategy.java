package shortener.strategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HashMapStorageStrategy implements StorageStrategy {
    private HashMap<Long, String> data = new HashMap<>();

    @Override
    public boolean containsKey(Long key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(String value) {
        return data.containsValue(value);
    }

    @Override
    public void put(Long key, String value) {
        data.put(key, value);
    }

    @Override
    public Long getKey(String value) {
        Optional<Long> key = data.entrySet().stream().filter(pair -> pair.getValue().equals(value)).
                            map(Map.Entry::getKey).findFirst();
        return key.orElse(null);
    }

    @Override
    public String getValue(Long key) {
        return data.get(key);
    }
}
