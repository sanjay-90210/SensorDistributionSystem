package yourpackage;

public class HashFunctionFactory {
    public static HashFunction getHashFunction(String type) {
        if ("murmur".equalsIgnoreCase(type)) {
            return new MurmurHashFunction();
        }
        throw new IllegalArgumentException("Unsupported hash function type: " + type);
    }
}
