package com.limon.clubelo.clubelobrowser.ClubEloAPI.cache;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LifetimeDiskCache {
    private SimpleDiskCache simpleDiskCache;
    private static String LIFETIME_KEY = "lifetime";

    private LifetimeDiskCache(SimpleDiskCache simpleDiskCache) {
        this.simpleDiskCache = simpleDiskCache;
    }

    public static synchronized LifetimeDiskCache open(File dir, long maxSize) throws IOException {
        return new LifetimeDiskCache(SimpleDiskCache.open(dir, 0, maxSize));
    }

    public String getString(String key) throws IOException {
        SimpleDiskCache.StringEntry entry = simpleDiskCache.getString(key);

        if(entry == null) return null;
        else if(System.currentTimeMillis() > (Long) entry.getMetadata().get(LIFETIME_KEY)) {
            Log.d("Cache outdated", "Cache for resource '" + key + "' was outdated and deleted.");
            simpleDiskCache.remove(key);
            return null;
        } else {
            return entry.getString();
        }
    }

    public void putString(String key, String value, long lifetime) throws IOException {
        HashMap<String, Long> data = new HashMap<>();
        data.put(LIFETIME_KEY, lifetime);

        Log.d("Cached resource", "Resource '" + key + "' was cached for " + ((lifetime - System.currentTimeMillis()) / 3600000) + " hours");
        simpleDiskCache.put(key, value, data);
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !Environment.isExternalStorageRemovable() ?
                        context.getExternalCacheDir().getPath() :
                        context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }
}
