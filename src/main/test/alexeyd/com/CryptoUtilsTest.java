package alexeyd.com;


import static org.junit.Assert.assertEquals;

import alexeyd.com.util.SDESCypherUtils;
import org.junit.Test;

/**
 * @author Alexey Druzik on 03.05.2021
 */
public  class CryptoUtilsTest {

  @Test
  public void XOR_Ok() {
    /*Initialization*/
    String ar1 = "10000010";
    String ar2 = "10100100";
    String rightAnswer = "00100110";

    /*Action*/
    String result = SDESCypherUtils.XOR(ar1, ar2);

    /*Assert*/
    assertEquals(rightAnswer, result);
  }

  @Test
  public void getIndex_Ok() {
    /*Initialization*/
    String l = "0110";

    /*Action*/
    int[] res = SDESCypherUtils.getIndex(l);

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
    String res = SDESCypherUtils.getSR(indexes);

    /*Assert*/
    assertEquals(expected, res);
  }

  @Test
  public void getSR_01_Ok() throws Exception {
    /*Initialization*/
    String expected = "01";
    int[] indexes = {0, 1};

    /*Action*/
    String res = SDESCypherUtils.getSR(indexes);

    /*Assert*/
    assertEquals(expected, res);
  }

  /**
   * 102 is 'f'
   * 212 is decrypted value of 'f'
   */
  @Test
  public void encode_642and102_212() throws Exception {
    /*Initialization*/
    String expectedBin = "11010100";
    int expectedDec = 212;
    String key = SDESCypherUtils.toBinary(642);
    String[] keys = SDESCypherUtils.getKeys(key);
    String textCode = SDESCypherUtils.toBinary('f', 8);

    /*Action*/
    String res = SDESCypherUtils.encode(textCode, keys);

    /*Assert*/
    assertEquals(expectedBin, res);
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }

  /**
   * 102 is 'f'
   * 212 is decrypted value of 'f'
   */
  @Test
  public void decode_642and212_102() throws Exception {
    /*Initialization*/
    int expectedDec = 102;
    String key = SDESCypherUtils.toBinary(642);
    String[] keys = SDESCypherUtils.getKeys(key);
    String textCode = SDESCypherUtils.toBinary(212, 8);

    /*Action*/
    String res = SDESCypherUtils.decode(textCode, keys);

    /*Assert*/
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }

  /**
   * 116 is 't'
   * 142 is decrypted value of 't'
   */
  @Test
  public void encode_642and116_142() throws Exception {
    /*Initialization*/
     int expectedDec = 142;
    String key = SDESCypherUtils.toBinary(642);
    String[] keys = SDESCypherUtils.getKeys(key);
    String textCode = SDESCypherUtils.toBinary('t', 8);

    /*Action*/
    String res = SDESCypherUtils.encode(textCode, keys);

    /*Assert*/
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }

  /**
   * 116 is 't'
   * 142 is decrypted value of 't'
   */
  @Test
  public void decode_642and142_116() throws Exception {
    /*Initialization*/
    int expectedDec = 116;
    String key = SDESCypherUtils.toBinary(642);
    String[] keys = SDESCypherUtils.getKeys(key);
    String textCode = SDESCypherUtils.toBinary(142, 8);

    /*Action*/
    String res = SDESCypherUtils.decode(textCode, keys);

    /*Assert*/
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }

  /**
   * 120 is 'x'
   * 142 is decrypted value of 't'
   */
  @Test
  public void encode_642and120_160() throws Exception {
    /*Initialization*/
    int expectedDec = 160;
    String key = SDESCypherUtils.toBinary(642);
    String[] keys = SDESCypherUtils.getKeys(key);
    String textCode = SDESCypherUtils.toBinary('x', 8);

    /*Action*/
    String res = SDESCypherUtils.encode(textCode, keys);

    /*Assert*/
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }

  /**
   * 120 is 'space'
   * 98 is decrypted value of 'space'
   */
  @Test
  public void encode_642and32_98() throws Exception {
    /*Initialization*/
    int expectedDec = 98;
    String key = SDESCypherUtils.toBinary(642);
    String[] keys = SDESCypherUtils.getKeys(key);
    String textCode = SDESCypherUtils.toBinary(' ', 8);

    /*Action*/
    String res = SDESCypherUtils.encode(textCode, keys);

    /*Assert*/
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }

  /**
   * 65 is 'A'
   * 21 is decrypted value of 'A'
   */
  @Test
  public void encode_642and65_21() throws Exception {
    /*Initialization*/
    int expectedDec = 21;
    String key = SDESCypherUtils.toBinary(642);
    String[] keys = SDESCypherUtils.getKeys(key);
    String textCode = SDESCypherUtils.toBinary('A', 8);

    /*Action*/
    String res = SDESCypherUtils.encode(textCode, keys);

    /*Assert*/
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }

  /**
   * 65 is 'A'
   * 21 is decrypted value of 'A'
   */
  @Test
  public void encode_642and21_65() throws Exception {
    /*Initialization*/
    int expectedDec = 65;
    String key = SDESCypherUtils.toBinary(642);
    String[] keys = SDESCypherUtils.getKeys(key);
    String textCode = SDESCypherUtils.toBinary(21, 8);

    /*Action*/
    String res = SDESCypherUtils.decode(textCode, keys);

    /*Assert*/
    assertEquals(expectedDec, Integer.parseInt(res, 2));
  }

  @Test
  public void encodeAndDecode_isOk() throws Exception {
    /*Initialization*/
    String phrase = "Alex hi!~/*-+1234567890";
    int key = 642;

    /*Action*/
    String encodedPhrase = SDESCypherUtils.encodePhrase(key, phrase);
    String decodedPhrase = SDESCypherUtils.decodePhrase(key, encodedPhrase);

    /*Assert*/
    assertEquals(phrase, decodedPhrase);
  }

  @Test
  public void encodeAndDecode_isOk_2() throws Exception {
    /*Initialization*/
    String phrase = "Channel was created by admin at 01/01/2021";
    int key = 642;

    /*Action*/
    String encodedPhrase = SDESCypherUtils.encodePhrase(key, phrase);
    String decodedPhrase = SDESCypherUtils.decodePhrase(key, encodedPhrase);

    /*Assert*/
    assertEquals(phrase, decodedPhrase);
  }
}