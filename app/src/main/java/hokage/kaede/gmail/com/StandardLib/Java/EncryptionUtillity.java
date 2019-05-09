package hokage.kaede.gmail.com.StandardLib.Java;

/**
 * 指定の文字列を暗号化/複合化するクラス。
 */
public class EncryptionUtillity {

	public static String encode(String value, byte key) {
		String ret = "";
		byte[] pass_org = value.getBytes();
		int size = pass_org.length;
		
		for(int i=0; i<size; i++) {
			ret = ret + String.format("%02x", (byte)(pass_org[i] ^ key));
		}
		
		return ret;
	}
	
	public static String decode(String value, byte key) {
		String ret = "";
		int size = value.length();
		byte[] ret_buffer = new byte[(int)(size/2)];
		
		try {
			for(int i=0; i<size; i+=2) {
				String buf = value.substring(i, i+2);
				ret_buffer[(int)(i/2)] = (byte)(Byte.parseByte(buf, 16) ^ key);
			}
			
			ret = new String(ret_buffer);
			
		} catch(Exception e) {
			//e.printStackTrace();
			ret = "";
		}
	
		return ret;
	}
}
