package wrappers;

import io.github.bucket4j.Bucket;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BucketWrapper {

    @Setter
    private long lastUsedTimestamp;
    private final Bucket bucket;

    public BucketWrapper(long lastUsedTimestamp, Bucket bucket) {
        this.lastUsedTimestamp = lastUsedTimestamp;
        this.bucket = bucket;
    }
}