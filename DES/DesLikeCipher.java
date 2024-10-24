public class DesLikeCipher{
    private final static int[] expanderTable = {1,2,4,3,4,3,5,6};
    /**
     * Implements the expander operation from the algorithm. 
     * 
     * @param e 6-bits number
     * @return Expanded version of e
     */
    private static int expand(int e) {
        int expander = 0;
        String exp = "";
        String binE = "";
        binE = Integer.toBinaryString(e);
        binE = String.format("%6s", binE).replace(' ', '0');

        for(int i=0; i<expanderTable.length; i++){
            int pos = expanderTable[i];
            String newBit = binE.substring(pos-1, pos);
            exp += newBit; 
        }

        expander = Integer.valueOf(exp, 2);
        return expander;
    }

    /**
     * S-box one for the DES-like algorithm
     * 
     * @param s 4-bit input
     * @return 3-bit substitution value for the given x
     */
    private static int s1(int s) {
        int sbox1[][] = {
                {0b101, 0b010, 0b001, 0b110, 0b011, 0b100, 0b111, 0b000}, 
                {0b001, 0b100, 0b110, 0b010, 0b000, 0b111, 0b101, 0b011}
            };

        // ...
        String inputS = Integer.toString(s);
        inputS = String.format("%4s", inputS).replace(' ', '0');
        String str1 = inputS.substring(0, 1);
        String str2 = inputS.substring(1);
        int x1 = Integer.valueOf(str1, 2);
        int x2 = Integer.valueOf(str2, 2); 
        String s1Value = Integer.toBinaryString(sbox1[x1][x2]);
        String padding = String.format("%3s", s1Value).replace(' ', '0');
        int s1Return = Integer.valueOf(padding, 2);
        return s1Return;
    }

    /**
     * S-box two for the DES-like algorithm
     * 
     * @param s 4-bit input
     * @return 3-bit substitution value for the given x
     */
    private static int s2(int s) {
        int sbox2[][] = {
                {0b100, 0b000, 0b110, 0b101, 0b111, 0b001, 0b011, 0b010}, 
                {0b101, 0b011, 0b000, 0b111, 0b110, 0b010, 0b001, 0b100}
            };

        // ...
        String inputS = Integer.toString(s);
        inputS = String.format("%4s", inputS).replace(' ', '0');
        String str1 = inputS.substring(0, 1);
        String str2 = inputS.substring(1);
        int x1 = Integer.valueOf(str1, 2);
        int x2 = Integer.valueOf(str2, 2); 
        String s1Value = Integer.toBinaryString(sbox2[x1][x2]);
        String padding = String.format("%3s", s1Value).replace(' ', '0');
        int s2Return = Integer.valueOf(padding, 2);
        return s2Return;
    }

    /**
     * f-function for the DES-like algorithm
     * @param f plaintext to be encrypted  
     * @param k key to use for encryption
     * @return f-value replacement for given input
     */
    private int f(int f, int k) {
        int f_value = 0;
        int f_expanded = expand(f);

        int f_xor = f_expanded ^ k;

        int s_1, s_2;
        String str1, str2;
        String bin_f_xor = "";
        bin_f_xor = Integer.toBinaryString(f_xor);
        bin_f_xor = String.format("%8s", bin_f_xor).replace(' ', '0');
        str1 = bin_f_xor.substring(0, 4);
        str2 = bin_f_xor.substring(4, 8);

        s_1 = Integer.valueOf(str1);
        s_2 = Integer.valueOf(str2);

        int sub1, sub2;
        sub1 = s1(s_1);
        sub2 = s2(s_2);
        //f_value = sub1 + sub2;

        String f_value_str = "";
        String sub1_str = Integer.toBinaryString(sub1);
        sub1_str = String.format("%3s", sub1_str).replace(' ', '0');

        String sub2_str = Integer.toBinaryString(sub2);
        sub2_str = String.format("%3s", sub2_str).replace(' ', '0');

        f_value_str = sub1_str + sub2_str;
        f_value = Integer.valueOf(f_value_str, 2);

        return f_value;
    }

    /**
     * Subkey derives 8 bits from the 9 bits input by
     *     counting 8 bits starting at position rnd 
     * For example:
     *   master key = 110011010
     * k1 = 11001101
     * k2 = 10011010
     * k3 = 00110101 # 7 bits + 1 bit from front
     * k4 = 01101011 # 6 bits + 2 bits from front
     * k5 = 11010110 # 5 bits + 3 bits from front
     *
     * @param k 9-bit master key to derive from
     * @param rnd index of subkey
     * @return 8-bit subkey derived from master key (k)
     */
    private int subkey(int k, int rnd) {
        int subKeyValue = 0;
        String subKeyStr = "";
        String keyStr = "";

        if(k < 256) {
            keyStr = Integer.toBinaryString(k);
            keyStr = String.format("%9s", keyStr).replace(' ', '0');
        } else {
            keyStr = Integer.toBinaryString(k);
        }

        char[] keyArray = keyStr.toCharArray();
        int index1 = (rnd-1);  //Index is zero based
        int index2 = 0;
        for(int i=0; i<(keyArray.length - 1); i++){
            if(index1 < keyArray.length){
                subKeyStr += keyArray[index1++];
            } else {
                subKeyStr += keyArray[index2++];
            }
        }
        subKeyValue = Integer.valueOf(subKeyStr, 2);
        return subKeyValue;
    }

    /**
     * Performs one round of encryption
     * 
     * @param x 12-bit plaintext
     * @param k 9-bit key
     * @param rnd round to perform
     * @return 12-bit ciphertext for given round
     */
    private int round(int x, int k, int rnd) {
        int l0 = x >> 6;
        int r0 = x & 0b111111;

        int r1 = f(r0, subkey(k, rnd)) ^ l0;
        int l1 = r0;

        return l1 << 6 | r1;
    }

    /**
     * DES-like encryption cipher for homework
     * 
     * @param x 12-bit plaintext
     * @param k 9-bit key
     * @return 12-bit ciphertext
     */
    public int encrypt(int x, int k) {
        int y=x;
        for (int r=0; r<5; r++) {
            y=round(y, k, r+1);
        }

        return y;
    }  

    public static void main(String args[]) {
        DesLikeCipher cipher=new DesLikeCipher();
        //0x4d
        if (cipher.encrypt(0x726, 0x99) == 0x3f8) {
            System.out.println("Life's good!");
        } else {
            System.out.println("Life's NOT good!");
        }

    }
}
