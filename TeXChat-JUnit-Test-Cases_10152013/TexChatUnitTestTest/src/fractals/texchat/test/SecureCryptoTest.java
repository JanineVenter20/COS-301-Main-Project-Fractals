package fractals.texchat.test;

import fractals.texchat.SecureCrypto;
import junit.framework.TestCase;

public class SecureCryptoTest extends TestCase {

	//PRIVATE MEMBERS
	private static final SecureCrypto sc = new SecureCrypto();
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	//TEST ENCRYPTION METHOD (breaks when the key is wrong etc)
	public void testEncrypt() {
		
		String testMessage = "";
		String input = "This a test input string.";
		boolean expected = true;
		boolean result = false;
		
		try
		{
			SecureCrypto.bytesToHex(sc.encrypt(input.trim()));
			result = true;
		}
		catch(Exception e)
		{
			testMessage = " ENCRYPTION METHOD FAILED ";
			result = false;
		}
		assertEquals(testMessage, expected, result);
		
	}

	//TEST DECRYPTION METHOD (breaks when input string cannot be decrypted - invalid input to decrypt)
	public void testDecrypt() {
		String testMessage = "";
		String toDecrypt = "f10646835b926dd020c87d56a0a54fd8050d5ba0072229434be3f5a22b4539dd";
		String dec_input = "";
		boolean expected = true;
		boolean result = false;
		
		try
		{
			dec_input = new String(sc.decrypt(toDecrypt));
			System.out.println("DECRYPTED TEST: " + dec_input);
			result = true;
		}
		catch(Exception e)
		{
			testMessage = " DECRYPTION METHOD FAILED ";
			result = false;
		}
		assertEquals(testMessage, expected, result);
	}
	
	//TEST BOTH (fails when input message and decrypted message is not the same or enc and dec method fails)
	public void testEncDec() {
		
		String testMessage = "";
	
		String input = "This a test input string.";
		String enc_input = "";
		String dec_input = "";
	
		boolean expected = true;
		boolean result = false;
		
		//ENCRYPT THE INPUT
		boolean encrypted = false;
		try {
			enc_input = SecureCrypto.bytesToHex(sc.encrypt(input.trim()));
			encrypted = true;
		}
		catch(Exception e) {
			testMessage += " ENCRYPTION METHOD FAILED ";
			encrypted = false;
		}
		
		
		//DECRYPT THE INPUT
		boolean decrypted = false;
		if(encrypted) {
			try {
				dec_input = new String(sc.decrypt(enc_input));
				decrypted = true;
			}
			catch(Exception e) {
				testMessage = " DECRYPTION METHOD FAILED ";
				decrypted = false;
			}
		}
		else //fail already
		{
			testMessage += " THE SECURITY FEATURE FAILED ";
			fail(testMessage);
		}
		
		
		//CHECK IF THEY ARE THE SAME BEFORE AND AFTER 
		if(!decrypted) //fail already
		{
			testMessage += " THE SECURITY FEATURE FAILED ";
			fail(testMessage);
		}
		
		if(input.equals(dec_input)) {
			result = true;
		}
		else {
			result = false;
			testMessage += " THE SECURITY FEATURE FAILED ";
		}
		assertEquals(testMessage, expected, result);
	}

}
