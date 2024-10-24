import java.util.ArrayList;

public class EcbDesLikeCipher extends DesLikeCipher{

    public static void main(String[] args) {
        
        EcbDesLikeCipher cipher = new EcbDesLikeCipher();

        String plainText = "World!";
        int key = 421; //0x1a5
        String cipherText = cipher.ecbEncrypt(plainText, key);
        
        if (cipherText == "0x9f279835b7b4") {
            System.out.println("Life's good!");
        } else {
            System.out.println("Life's NOT good!");
        }
        
        //0x9f279835b7b4 - Expected (maybe)
        //0xe68f6cf98b86 - Output
        //1111010010110001100101001101011111111010111111110000101011110000110
        System.out.println("DES-Like encryption with ECB mode");
        System.out.println("=================================");
        System.out.println(" Plain Text: " + plainText);
        System.out.println("Cipher Text: " + cipherText);  //0x9f279835b7b4

    }

    private String ecbEncrypt(String plainText, int key) {
        String returnCipher = "";
        int cipherBlock;
        String binaryPlainText = getBinaryPlainText(plainText);
        
        //Split plainText into blocks
        ArrayList<Integer> inputBlocks = splitPlainText(binaryPlainText);
        ArrayList<Integer> outputBlocks = new ArrayList<>();
        
        for(Integer input : inputBlocks) {
            cipherBlock = encrypt(input, key);
            outputBlocks.add(cipherBlock);
        }
        
        returnCipher = assembleCipherText(outputBlocks);
        
        return returnCipher;
    }
    
    private String getBinaryPlainText(String plainText) {
        String binaryString = "";
        
        for(int i=0; i < plainText.length(); i++){
            int ascii = (int)plainText.charAt(i);
            String inBinary = Integer.toBinaryString(ascii);
            inBinary = String.format("%8s", inBinary).replace(' ', '0');
             binaryString += inBinary;    
         }
        
        return binaryString;
    }

    private String assembleCipherText(ArrayList<Integer> outputBlocks) {
        String returnCipher = "";
        long cipherText = 0;
        String cipherBlockStr = "";
        String cipherString = "";
        
        for(Integer cipherBlock : outputBlocks) {
            cipherBlockStr = Integer.toBinaryString(cipherBlock);
            cipherBlockStr = String.format("%12s", cipherBlockStr).replace(' ', '0');
            cipherString += cipherBlockStr;
        }
        
        cipherText = Long.valueOf(cipherString, 2);
        returnCipher = "0x" + Long.toHexString(cipherText);
        return returnCipher;
    }

    /**
     * splits the input plainText String into blocks of 12-bit strings and
     * converts them into integer values then adds them to an ArrayList.
     * 
     * @param plainText binary representation of an input string
     * @return blocks ArrayList of integer blocks representing the 12-bit strings
     */
    private ArrayList<Integer> splitPlainText(String plainText) {
        ArrayList<Integer> blocks = new ArrayList<>();
        int blockSize = 12;
        
        //Assume the block sizes are 12 bit long and any remainder will be padded
        for(int i = 0; i < plainText.length(); i+=blockSize) {
            int block = Integer.valueOf(plainText.substring(i, (i + blockSize)), 2);
            blocks.add(block);
            System.out.println(block);
        }
        
        return blocks;
    }
    
    private ArrayList<Integer> splitPlainTextByChar(String plainText) {
        ArrayList<Integer> blocks = new ArrayList<>();
        int blockSize = 12;
        
        //Assume the block sizes are 12 bit long and any remainder will be padded
        for(int i = 0; i < plainText.length(); i+=blockSize) {
            int block = Integer.valueOf(plainText.substring(i, (i + blockSize)), 2);
            blocks.add(block);
        }
        
        return blocks;
    }

}
