import java.util.ArrayList;

public class CbcDesLikeCipher extends DesLikeCipher {
	
	//Take plaintext and IV input and generates cipher text
	public String cbcEncrypt(String plainText,int key, int iv) {
		String cipherText = "";
		//long long_plainText = getLongPlainText(plainText);
		String binaryPlainText = getBinaryPlainText(plainText);
		
		//Split plainText into blocks
		ArrayList<Integer> inputBlocks = splitPlainText(binaryPlainText);
		ArrayList<Integer> outputBlocks = new ArrayList<>();
		int xor_input = 0;
		int counter = 0;
		int feedback = 0;
		for(Integer input : inputBlocks) {
			if(counter == 0) {
				xor_input = input ^ iv;
				feedback = encrypt(xor_input, key);
				outputBlocks.add(feedback);
			} else { 
				xor_input = input ^ feedback;
				feedback = encrypt(xor_input, key);
				outputBlocks.add(feedback);
			}
			counter++;
		}
		
		cipherText = assembleCipherText(outputBlocks);
		
		return cipherText;
	}
/*
	private long getLongPlainText(String plainText) {
		long long_plainText = 0;
		String binaryString = "";
		
		for(int i=0; i < plainText.length(); i++){
			int ascii = (int)plainText.charAt(i);
			String inBinary = Integer.toBinaryString(ascii);
		    inBinary = String.format("%8s", inBinary).replace(' ', '0');
		 	binaryString += inBinary;    
		 }
		
		long_plainText = Long.valueOf(binaryString, 2);
		return long_plainText;
	}
*/	
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
		int remainder = plainText.length() % blockSize;
		String binaryInput = "";
		String remainderText = "";
		
		if(remainder != 0) {
			remainderText = plainText.substring((plainText.length()-remainder));
			//remainderText = String.format("%8s", remainderText).replace(' ', '0');
			binaryInput = plainText.substring(0, (plainText.length()-remainder));
			//binaryInput += remainderText;
		} else {
			binaryInput = plainText;
		}
		
		//Assume the block sizes are 12 bit long and any remainder will be padded
		for(int i = 0; i < binaryInput.length(); i+=blockSize) {
			int block = Integer.valueOf(binaryInput.substring(i, (i + blockSize)), 2);
			blocks.add(block);
		}
		if(remainder > 0) {
			int block = Integer.valueOf(remainderText);
			blocks.add(block);
		}
		return blocks;
	}

	public static void main(String[] args) {
		
		CbcDesLikeCipher cipher=new CbcDesLikeCipher();
		
		String plainText = "World!";
		int key = 421; //0x1a5
		int iv = 124; //0x7c
		String cipherText = cipher.cbcEncrypt(plainText, key, iv);
		
		if (cipherText == "0x4f3c5e86f7bd") {
			System.out.println("Life's good!");
		} else {
			System.out.println("Life's NOT good!");
		}
		
		System.out.println("DES-Like encryption with CBC mode");
		System.out.println("=================================");
		System.out.println(" Plain Text: " + plainText);
		System.out.println("Cipher Text: " + cipherText);
		
		//0x4f3c5e86f7bd - maybe
		//0x3dbf44e5da96
		//0x7b7e8965da96 - output
		//011110110111111010001001011001011101101010010110 - cipherText
		//System.out.println(Long.toBinaryString(0x9f279835b7b4));
		
	}

}
