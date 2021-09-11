package shortener.strategy;

public class FileStorageStrategy implements StorageStrategy {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final long DEFAULT_BUCKET_SIZE_LIMIT = 10000;

    private FileBucket[] table = new FileBucket[DEFAULT_INITIAL_CAPACITY];
    private int size;
    private long bucketSizeLimit = DEFAULT_BUCKET_SIZE_LIMIT;
    private long maxBucketSize;

    public long getBucketSizeLimit() {
        return bucketSizeLimit;
    }

    public void setBucketSizeLimit(long bucketSizeLimit) {
        this.bucketSizeLimit = bucketSizeLimit;
    }

    @Override
    public boolean containsKey(Long key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(String value) {
        for (FileBucket bucket : table) {
            if (bucket != null) {
                for (Entry e = bucket.getEntry(); e != null ; e = e.next) {
                    if (e.getValue().equals(value)) return true;
                }
            }
        }

        return false;
    }

    @Override
    public void put(Long key, String value) {
        int h = hash(key);
        int index = indexFor(h, table.length);

        if (table[index] != null) {
            Entry first = table[index].getEntry();
            for (Entry e = first; e != null; e = e.next) {
                if (e.getKey().equals(key)) {
                    e.value = value;
                    table[index].putEntry(first);
                    return;
                }
            }
        }

        addEntry(h, key, value, index);
    }

    @Override
    public Long getKey(String value) {
        for (FileBucket bucket : table) {
            if (bucket != null) {
                for (Entry e = bucket.getEntry(); e != null ; e = e.next) {
                    if (e.getValue().equals(value)) return e.getKey();
                }
            }
        }

        return null;
    }

    @Override
    public String getValue(Long key) {
        Entry k;
        return (k = getEntry(key)) == null ? null : k.getValue();
    }

    int hash(Long k) {
        int h;
        return (k == null) ? 0 : (h = k.hashCode()) ^ (h >>> 16);
    }

    int indexFor(int hash, int length) {
        return hash & length - 1;
    }

    Entry getEntry(Long key) {
        if (size == 0) {
            return null;
        }

        int hash = hash(key);
        int index = indexFor(hash, table.length);

        if (table[index] == null) return null;

        for (Entry entry = table[index].getEntry(); entry != null; entry = entry.next) {
            if (key.equals(entry.key)) {
                return entry;
            }
        }

        return null;
    }

    void resize(int newCapacity) {
        FileBucket[] newTable = new FileBucket[newCapacity];
        transfer(newTable);
        table = newTable;
    }

    void transfer(FileBucket[] newTable) {
        int newCapacity = newTable.length;
        for (FileBucket bucket : table) {
            Entry e = bucket.getEntry();
            while (e != null) {
                Entry next = e.next;
                int indexInNewTable = indexFor(hash(e.getKey()), newCapacity);
                if (newTable[indexInNewTable] != null) {
                    e.next = newTable[indexInNewTable].getEntry();
                } else {
                    newTable[indexInNewTable] = new FileBucket();
                }

                newTable[indexInNewTable].putEntry(e);
                e = next;
            }
            bucket.remove();
        }
    }

    void addEntry(int hash, Long key, String value, int bucketIndex) {
        for (FileBucket bucket : table) {
            if (bucket != null && (bucket.getFileSize() >= bucketSizeLimit)) {
                resize(2 * table.length);
                hash = hash(key);
                bucketIndex = indexFor(hash, table.length);
                break;
            }
        }

        createEntry(hash, key, value, bucketIndex);
    }

    void createEntry(int hash, Long key, String value, int bucketIndex) {
        if (table[bucketIndex] != null) {
            FileBucket bucket = table[bucketIndex];
            Entry oldEntry = bucket.getEntry();
            Entry newEntry = new Entry(hash, key, value, oldEntry);
            bucket.putEntry(newEntry);
        } else {
            FileBucket bucket = new FileBucket();
            bucket.putEntry(new Entry(hash, key, value, null));
            table[bucketIndex] = bucket;
        }

        size++;
    }
}
