package step.learning.services.kdf;

/**
 * Key Derivation function services
 * By RFC 2898
 */

public interface KdfService {
    String derivedKey(String password, String key);
}
