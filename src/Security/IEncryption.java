package Security;

public interface IEncryption {

	boolean compareKeyToHash(String key, String hash);
	String convertKeyToHash(String key);
}