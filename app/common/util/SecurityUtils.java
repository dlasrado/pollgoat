package common.util; 

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class SecurityUtils {
	
	private  byte[] msgNumber = "bazwsovdcrfvtgby".getBytes();
	
	public byte[] decodeBase64(byte[] val) throws UnsupportedEncodingException{
		
		return Base64.decodeBase64(val);
	}
	
	public byte[] encodeBase64(byte[] val) throws UnsupportedEncodingException{
		
		return Base64.encodeBase64(val);
	}
	
	public byte[] encryptPassword(String pwd) 
			throws NoSuchPaddingException,NoSuchAlgorithmException,InvalidKeyException,InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException, UnsupportedEncodingException{
		
		Security.addProvider(new BouncyCastleProvider());
		String symmKey = "nitemoulcaulerwynitemoulcaulerwy";
		
		byte[] keyBytes = symmKey.getBytes();
		
		SecretKeySpec key = new SecretKeySpec(keyBytes,"AES");
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
		cipher.init(Cipher.ENCRYPT_MODE,key,new IvParameterSpec(msgNumber));
		
		
		
		byte[] encryptedData = cipher.doFinal(pwd.getBytes("UTF-8"));
		
		return encryptedData;
	}
	
	public byte[] decryptPassword(byte[] pwd) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException{
		Security.addProvider(new BouncyCastleProvider());
		String symmKey = "nitemoulcaulerwynitemoulcaulerwy";
		//byte[] msgNumber = new byte[] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		
		byte[] keyBytes = symmKey.getBytes();
		
		SecretKeySpec key = new SecretKeySpec(keyBytes,"AES");
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
		
		/*KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		
		SecretKey aesKey = kgen.generateKey();
		
		IvParameterSpec iv = new IvParameterSpec(aesKey.getEncoded());*/
		
		//IvParameterSpec iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class);
		
		IvParameterSpec iv = new IvParameterSpec(msgNumber);
		
		cipher.init(Cipher.DECRYPT_MODE,key,iv);
		
		byte[] decryptedData = cipher.doFinal(pwd);
		
		return decryptedData;
		
	}
		
}