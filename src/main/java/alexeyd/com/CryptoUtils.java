package alexeyd.com;

/**
 * @author Alexey Druzik on 03.05.2021
 */
public class CryptoUtils {

  private final static int[][] SR = new int[][] {
      {1,1,2,3},
      {2,0,1,3},
      {3,0,1,0},
      {2,1,0,3}
  };
  private final static int[][] SL = new int[][] {
      {1,0,3,2},
      {3,2,1,0},
      {0,2,1,3},
      {3,1,3,1}
  };
  private static final int[] IP = { 2, 6, 3, 1, 4, 8, 5, 7 };
  private static final int[] I_P = { 4, 1, 3, 5, 7, 2, 8, 6 };
  private static final int[] P10 = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
  private static final int[] P8 = { 6, 3, 7, 4, 8, 5, 10, 9 };
  private static final int[] EP = { 4, 1, 2, 3, 2, 3, 4, 1 };
  private static final int[] P4 = { 2, 4, 3, 1 };
  private static final int FIRST_ELEMENT_INDEX = 0;
  public String[] keys;

  public String[] getKeys(String key) {
    keys = new String[2];
    key = shuffle(P10, key);
    keys[0] = shuffle(P8, move(1, key));
    keys[1] = shuffle(P8, move(3, key));
    return keys;
  }

  public String encode(String textCode, String[] keys) throws Exception {
    String ip = shuffle(IP,textCode);
    //label6.Text += "Ip = " + ip + '\n';

    for (int i = 0; i < 2; ++i) {
      String ep = XOR(shuffle(EP, getR(ip)), keys[i]);
      //label6.Text += "E/P(R) XOR K[" + i + "] = " + ep + '\n';
      String slsr = getSL(getIndex(getL(ep))) + getSR(getIndex(getR(ep)));
      slsr = XOR(getL(ip),shuffle(P4, slsr));
      ip = i == 0 ? SW(ip, slsr) : SR(ip, slsr);
     }

    return shuffle(I_P, ip);
  }

  public String decode(String textCode, String[] keys) throws Exception {
    swapKeys();
    String ip = shuffle(IP,textCode);
    //label6.Text += "Ip = " + ip + '\n';

    for (int i = 0; i < 2; ++i) {
      String ep = XOR(shuffle(EP, getR(ip)), keys[i]);
      //label6.Text += "E/P(R) XOR K[" + i + "] = " + ep + '\n';
      String slsr = getSL(getIndex(getL(ep))) + getSR(getIndex(getR(ep)));
      slsr = XOR(getL(ip),shuffle(P4, slsr));
      ip = i == 0 ? SW(ip, slsr) : SR(ip, slsr);
    }

    return shuffle(I_P, ip);
  }

  private String move (Integer n, String array) {
    String f_arr = array.substring(0, 5);
    String s_arr = array.substring(5, 10);
    for (int i = 0; i < n; ++i) {
      f_arr = f_arr.substring(1) + getFirst(f_arr);
      s_arr = s_arr.substring(1) + getFirst(s_arr);
    }
    return f_arr + s_arr;
  }

  private String shuffle(int[] p, String ar) {
    String result = "";
    int len = ar.length();
    for (int i = 0; i < p.length; ++i) {
      result += ar.charAt(p[i] - 1);
    }

    return result;
  }

  private char getFirst(String s) {
    return s.charAt(FIRST_ELEMENT_INDEX);
  }

  public String XOR(String ar1, String ar2) {

    String Res = "";
    for (int i = 0; i < ar1.length(); ++i) {
      int res = Integer.parseInt(String.valueOf(ar1.charAt(i))) ^ Integer.parseInt(String.valueOf(ar2.charAt(i)));
      Res += res;
    }

    return Res;
  }

  private String getR(String ip) {
    return ip.substring(4, 8);
  }

  public int[] getIndex(String l) {
    int[] res = new int[2];
    String first = String.valueOf(l.charAt(0)) + l.charAt(3);
    String second = String.valueOf(l.charAt(1)) + l.charAt(2);

    res[0] = Integer.parseInt(first, 2);
    res[1] =  Integer.parseInt(second, 2);

    return res;
  }

  public String getL(String ip) {
    return ip.substring(0, 4);
  }

  public String getSL(int[] indexes) throws Exception {
    int digit = SL[indexes[0]][indexes[1]];
    String digitInBin = toBinary(digit, 2);
    return digitInBin;
  }

  public String getSR(int[] indexes) throws Exception {
    int digit = SR[indexes[0]][indexes[1]];
    String digitInBin = toBinary(digit, 2);
    return digitInBin;
  }

  public String SW(String key, String R) {
    //label6.Text += "SW(R) = " + getR(key) + R + '\n';
    return getR(key) + R;
  }

  public String SR(String key, String L) {
    //label6.Text += "Before IP(-1) = " + L + getR(key) + '\n';
    return L + getR(key);
  }

  public String toBinary(Integer digit) {
    return Integer.toBinaryString(digit);
  }

  public String toBinary(int digit, int length) throws Exception {
    String digitInBin = toBinary(digit);
    int lengthOfDigitInBin = digitInBin.length();
    if (lengthOfDigitInBin == length) {
      return digitInBin;
    } else if (length - 1 ==  lengthOfDigitInBin) {
      digitInBin = "0" + digitInBin;
      return digitInBin;
    }
    throw new Exception();
  }

  public String[] swapKeys() {
    String buffer = keys[0];
    keys[0] = keys[1];
    keys[1] = buffer;
    return keys;
  }

}
