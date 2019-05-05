package eu.lukks;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodePasswd {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String passwd="admin";
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPass = encoder.encode(passwd);
		
		System.out.println(hashedPass);
	}

}
