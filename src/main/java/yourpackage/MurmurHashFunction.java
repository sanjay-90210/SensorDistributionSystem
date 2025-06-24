package yourpackage;
import java.nio.charset.StandardCharsets;

public class MurmurHashFunction implements Hashfunction {

    @Override
    public int hash(Object key) {
        byte[] data = key.toString().getBytes(StandardCharsets.UTF_8);
        int seed = 0x9747b28c;
        int m = 0x5bd1e995;
        int r = 24;

        int length = data.length;
        int h = seed ^ length;
        int len_4 = length >> 2;

        for (int i = 0; i < len_4; i++) {
            int i4 = i << 2;
            int k = (data[i4 + 0] & 0xff) |
                    ((data[i4 + 1] & 0xff) << 8) |
                    ((data[i4 + 2] & 0xff) << 16) |
                    ((data[i4 + 3] & 0xff) << 24);

            k *= m;
            k ^= k >>> r;
            k *= m;

            h *= m;
            h ^= k;
        }

        int len_m = len_4 << 2;
        int left = length - len_m;

        if (left != 0) {
            if (left >= 3) h ^= (data[length - 3] & 0xff) << 16;
            if (left >= 2) h ^= (data[length - 2] & 0xff) << 8;
            if (left >= 1) h ^= (data[length - 1] & 0xff);
            h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return h & 0x7fffffff;  // Ensure non-negative
    }
}
