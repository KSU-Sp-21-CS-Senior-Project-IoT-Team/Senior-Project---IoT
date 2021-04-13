import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util.Utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class Security {
    public static void testUtils() {
        byte[] bytes = {-0x7f, 0x24, 0x56, 0x1c, 0x3A};
        String hex = Utils.bytesToHex(bytes);
        byte[] outBytes = Utils.hexToBytes(hex);
        System.out.println(Arrays.toString(bytes));
        System.out.println(hex);
        System.out.println(Arrays.toString(outBytes));

        System.out.println();

        String hardHex = "ABCDEF1234FFFF";
        System.out.println(hardHex);
        byte[] hardBytes = Utils.hexToBytes(hardHex);
        System.out.println(Arrays.toString(hardBytes));
        String outHardHex = Utils.bytesToHex(hardBytes);
        System.out.println(outHardHex);
        System.out.println();
    }

    public static void testHashing() {
        try {
            String password = "password123";
            byte[] salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);
            salt = Utils.hexToBytes("E23A7134D53FE5B91F738A3CA7448FB6");
            char[] pwdBytes = password.toCharArray();
            SecretKeyFactory sha256 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hashed = sha256.generateSecret(new PBEKeySpec(pwdBytes, salt, 100000, 64*8)).getEncoded();
            System.out.println(Utils.bytesToHex(salt));
            System.out.println(Utils.bytesToHex(hashed));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testEncryption() {

    }

    public static void main(String[] args) {
        testUtils();
        testHashing();
        testEncryption();
    }
}
