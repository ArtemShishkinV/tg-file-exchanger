package com.shishkin;

import org.hashids.Hashids;

public class CryptoTool {
    private final Hashids hashids;

    public CryptoTool(String salt) {
        var minHashLength = 10;
        this.hashids = new Hashids(salt, minHashLength);
    }

    public String hashOf(Long value) {
        return hashids.encode(value);
    }

    public Long idOf(String hash) {
        var ids = hashids.decode(hash);
        if (ids != null && ids.length > 0) {
            return ids[0];
        }
        return null;
    }
}
