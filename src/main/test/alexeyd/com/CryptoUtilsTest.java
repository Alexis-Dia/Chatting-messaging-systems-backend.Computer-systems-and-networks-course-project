package alexeyd.com;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Alexey Druzik on 03.05.2021
 */
public  class CryptoUtilsTest {

  CryptoUtils cryptoUtils = new CryptoUtils();

  @Test
  public void XOR_Ok() {
    /*Initialization*/
    String ar1 = "10000010";
    String ar2 = "10100100";
    String rightAnswer = "00100110";

    /*Action*/
    String result = cryptoUtils.XOR(ar1, ar2);

    /*Assert*/
    assertEquals(rightAnswer, result);
  }

  @Test
  public void getIndex_Ok() {
    /*Initialization*/
    int[] expected = {0, 3};
    String l = "0110";

    /*Action*/
    int[] res = cryptoUtils.getIndex(l);

    /*Assert*/
    assertEquals(res[0], 0);
    assertEquals(res[1], 3);
  }

  @Test
  public void getSR_11_Ok() throws Exception {
    /*Initialization*/
    String expected = "11";
    int[] indexes = {0, 3};

    /*Action*/
    String res = cryptoUtils.getSR(indexes);

    /*Assert*/
    assertEquals(expected, res);
  }

  @Test
  public void getSR_01_Ok() throws Exception {
    /*Initialization*/
    String expected = "01";
    int[] indexes = {0, 1};

    /*Action*/
    String res = cryptoUtils.getSR(indexes);

    /*Assert*/
    assertEquals(expected, res);
  }

  /**
   * 102 is 'f'
   * 212 is decrypted value of 'f'
   * @throws Exception
   */
  @Test
  public void encode_642and102_212() throws Exception {
    /*Initialization*/
    String expectedBin = "11010100";
    int expectedDec = 212;
    String key = cryptoUtils.toBinary(642);
    String[] keys = cryptoUtils.getKeys(key);
    String textCode = cryptoUtils.toBinary('f', 8);

    /*Action*/
    String res = cryptoUtils.encode(textCode, keys);

    /*Assert*/
    assertEquals(expectedBin, res);
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }

  /**
   * 102 is 'f'
   * 212 is decrypted value of 'f'
   * @throws Exception
   */
  @Test
  public void decode_642and212_102() throws Exception {
    /*Initialization*/
    int expectedDec = 102;
    String key = cryptoUtils.toBinary(642);
    String[] keys = cryptoUtils.getKeys(key);
    String textCode = cryptoUtils.toBinary(212, 8);

    /*Action*/
    String res = cryptoUtils.decode(textCode, keys);

    /*Assert*/
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }

  /**
   * 116 is 't'
   * 142 is decrypted value of 't'
   * @throws Exception
   */
  @Test
  public void encode_642and116_142() throws Exception {
    /*Initialization*/
     int expectedDec = 142;
    String key = cryptoUtils.toBinary(642);
    String[] keys = cryptoUtils.getKeys(key);
    String textCode = cryptoUtils.toBinary('t', 8);

    /*Action*/
    String res = cryptoUtils.encode(textCode, keys);

    /*Assert*/
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }

  /**
   * 116 is 't'
   * 142 is decrypted value of 't'
   * @throws Exception
   */
  @Test
  public void decode_642and142_116() throws Exception {
    /*Initialization*/
    int expectedDec = 116;
    String key = cryptoUtils.toBinary(642);
    String[] keys = cryptoUtils.getKeys(key);
    String textCode = cryptoUtils.toBinary(142, 8);

    /*Action*/
    String res = cryptoUtils.decode(textCode, keys);

    /*Assert*/
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }
}