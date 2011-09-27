package com.lti.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import com.lti.service.UserManager;

public class EmailValidChecker {

	public static Map<Boolean,String> checkEmail(String email) {  
		Map<Boolean,String> message = new HashMap<Boolean,String>();
		if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {   
			//System.err.println("Format error");  
			message.put(false, "Format error");
			return message ;  
		}  
		StringBuffer log = new StringBuffer();  
		String host = ""; 
		String hostName = email.split("@")[1];  
		Record[] result = null;  
		SMTPClient client = new SMTPClient();  
		try {   
			// look for MX records  
			Lookup lookup = new Lookup(hostName, Type.MX);   
			lookup.run();   
			if (lookup.getResult() != Lookup.SUCCESSFUL) {    
				log.append("can't look for MX records\n");    
				message.put(false, log.toString());
				return message;   
			} 
			else {    
				result = lookup.getAnswers();   
			}      
			//connect to the server
			for (int i = 0; i < result.length; i++) {    
				host = result[i].getAdditionalName().toString();    
				client.connect(host);    
				if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {     
					client.disconnect();     
					continue;    
				} 
				else {     
					log.append("MX record about " + hostName + " exists.\n");     
					log.append("Connection succeeded to " + host + "\n");     
					break;   
				}   
			}   
			log.append(client.getReplyString());   
			// HELO www.myplaniq.com  
			client.login("70.38.112.178");   
			log.append(">HELO myplaniq.com\n");   
			log.append("=" + client.getReplyString());   
			// MAIL FROM: <support@myplaniq.com>   
			client.setSender("support@myplaniq.com");   
			log.append(">MAIL FROM: <support@myplaniq.com>\n");   
			log.append("=" + client.getReplyString());   
			// RCPT TO: <$email>   
			client.addRecipient(email);   
			log.append(">RCPT TO: <" + email + ">\n");   
			log.append("=" + client.getReplyString());      
			if (250 == client.getReplyCode()) {    
				message.put(true, log.toString());
				return message;    
			}  
		} catch (Exception e) {   
			e.printStackTrace();  
		} finally {   
			try {    
				client.disconnect();   
			} catch (IOException e) {   }   
			// print log   
			//System.err.println(log); 
		}  message.put(false, log.toString());
		return message;
	} 
	
	
	public static void main(String[] args) {  
		Map<Boolean,String> message = EmailValidChecker.checkEmail("suping9@gmail.com");
		if(message.containsKey(true))
			System.err.println("Outcome: "    +message.get(true)+"True"); 
		else
			System.err.println("Outcome: "    +message.get(false)+"False"); 
	}
}
