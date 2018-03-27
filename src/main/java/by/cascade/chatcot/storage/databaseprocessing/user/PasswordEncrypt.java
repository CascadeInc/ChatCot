package by.cascade.chatcot.storage.databaseprocessing.user;

public class PasswordEncrypt {
    private static final byte[] KEY = "xfc13asf56sfc6xw5rfg7x23st".getBytes();

    public String encrypt(String input) {
        byte[] bytes = input.getBytes();
        byte[] output = new byte[bytes.length];
        int current = 0;
        while (current < bytes.length) {
            output[current] = (byte)(bytes[current] ^ KEY[current % KEY.length]);
            current++;
        }
        return new String(output);
    }
}
