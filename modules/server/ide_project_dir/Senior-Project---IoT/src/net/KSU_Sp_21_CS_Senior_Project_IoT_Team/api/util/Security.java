package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.util;

import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.AccountLoginRecord;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.oauth.models.Token;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class Security {
    private final SecretKeyFactory sha256;
    private final Cipher aesEncryption;
    private final Cipher aesDecryption;
    private final long tokenLifetime;
    private final int shaIterations;

    private final SecureRandom random;

    public Security(long tokenLifetime, int shaIterations, String dbAESKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
        this.tokenLifetime = tokenLifetime;
        this.shaIterations = shaIterations;

        random = SecureRandom.getInstance("SHA1PRNG");
        sha256 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        aesEncryption = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesDecryption = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final byte[] salt = dbAESKey.getBytes(StandardCharsets.US_ASCII);
        final Key temp = sha256.generateSecret(new PBEKeySpec(dbAESKey.toCharArray(), salt, 100000, 256));
        final IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(temp.getEncoded(), 0, 16));
        final SecretKeySpec aesKeySpec = new SecretKeySpec(temp.getEncoded(), "AES");
        aesEncryption.init(Cipher.ENCRYPT_MODE, aesKeySpec, iv);
        aesDecryption.init(Cipher.DECRYPT_MODE, aesKeySpec, iv);
    }

    /**
     * record format is iterations:salt:password
     * @param recordPassword in Base64
     * @param claimPassword
     * @return
     */
    public boolean comparePassword(String recordPassword, String claimPassword) {
        try {
            final String[] parts = new String(
                    aesDecryption.doFinal(Base64.getDecoder().decode(recordPassword)),
                    StandardCharsets.US_ASCII
            ).split(":");

            final int iterations = Integer.parseInt(parts[0]);
            final byte[] salt = Utils.hexToBytes(parts[1]);
            final byte[] recordHash = Utils.hexToBytes(parts[2]);

            final byte[] credHash = sha256.generateSecret(
                    new PBEKeySpec(
                            claimPassword.toCharArray(), salt, iterations, recordHash.length * 8
                    )
            ).getEncoded();

            return Arrays.compare(recordHash, credHash) == 0;
        } catch (Exception e) {
            e.printStackTrace(); // TODO: proper logging
            return false;
        }
    }


    public AccountLoginRecord createLoginRecord(String password) {
        try {
            // get a new account id
            final String accountID = UUID.randomUUID().toString();

            // encrypt the password hash with salt and iteration count
            final byte[] salt = new byte[16];
            random.nextBytes(salt);
            final String recordPassword = Base64.getEncoder().encodeToString(
                    aesEncryption.doFinal(
                            ("" + shaIterations + ":"
                            + Utils.bytesToHex(salt) + ":"
                            + Utils.bytesToHex(
                                    sha256.generateSecret(new PBEKeySpec(
                                            password.toCharArray(), salt, shaIterations, 64 * 8
                                    )).getEncoded()
                            )).getBytes(StandardCharsets.US_ASCII)
                    )
            );

            return new AccountLoginRecord(accountID, recordPassword);
        } catch (Exception e) {
            return null;
        }
    }

    public Token createToken(String accountID) {
        return new Token(UUID.randomUUID().toString(), accountID, System.currentTimeMillis() + tokenLifetime);
    }
}
