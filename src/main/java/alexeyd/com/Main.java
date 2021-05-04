package alexeyd.com;

/**
 * @author Alexey Druzik on 03.05.2021
 */
public class Main {

  public static void main(String[] args) throws Exception {

    CryptoUtils cryptoUtils = new CryptoUtils();

    String key = cryptoUtils.toBinary(642);
    String[] keys = cryptoUtils.getKeys(key);

    String textCode = cryptoUtils.toBinary('f', 8);

    String encoded = cryptoUtils.encode(textCode, keys);
    System.out.println("encode = " + Integer.parseInt(encoded, 2));
  }

}
