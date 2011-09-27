package com.lti.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.io.ByteToCharConverter;
import sun.io.ByteToCharUTF8;

public class EncodingUtil {
	public static String unicodeToBinary(String content) {
		String hexStr = "";
		try {
			char contentBuffer[] = content.toCharArray();
			for (int i = 0; i < content.length(); i++) {
				int n = contentBuffer[i];
				String s = Integer.toHexString(n);
				if (s.length() > 4)
					s = s.substring(0, 4);
				else
					s = "0000".substring(0, 4 - s.length()) + s;
				hexStr = hexStr + "&#x" + s + ";";
			}

		} catch (Exception ex) {
			hexStr = "";
		}
		return hexStr;
	}

	public static String unicode(String instring) {
		String syscode = System.getProperty("file.encoding");

		String regEx = "(<)(.+?)(>)";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(instring);
		instring = m.replaceAll("");
		// return instring;

		if (syscode.compareTo("GBK") == 0) {
			String text = instring;
			byte b[] = null;
			try {
				b = text.getBytes("UTF-16LE");
			} catch (UnsupportedEncodingException e1) {
				return "";
			}
			String utf82 = "";
			int i = 0;
			int j = 0;
			int textlen = 0;
			byte[] newbie = text.getBytes();
			for (i = 0; i < b.length; i++) {
				utf82 += "|" + Integer.toHexString(b[i]) + "|";
				if ((i + 2) % 2 == 0) {
					newbie[j] = b[i];
					if (b[i] > 0) {
						textlen++;
					} else if (b[i] < 0 && ((i + 4) % 4 == 0)) {
						textlen++;
					}
					j++;
				}
			}
			// out.println (textlen);
			char c[] = null;
			String utf81 = "";
			try {
				ByteToCharConverter converter = ByteToCharUTF8.getConverter(syscode);
				// ByteToCharConverter converter = ByteToCharUTF8.getDefault();
				c = converter.convertAll(newbie);
				for (i = 0; i < textlen; i++) {
					utf81 += "&#x" + Integer.toHexString(c[i]) + ";";
				}
			} catch (Exception e) {
				return instring;
			}

			// catch (MalformedInputException e) {
			// return "1";
			// catch (UnsupportedEncodingException e1) {
			// return "2";
			// }

			return utf81;
		} else {
			int i = 0;
			char c[] = null;
			String utf81 = "";
			try {
				int len = instring.length();
				byte b[] = instring.getBytes("utf-8");
				ByteToCharConverter converter = ByteToCharUTF8.getConverter("utf-8");
				c = converter.convertAll(b);
				int temp = 0;
				byte be[] = instring.getBytes();
				int ii = 0;
				int j = 0;
				for (ii = 0; ii < c.length; ii++) {
					temp = (int) c[ii];
					if (temp > 0 && temp < 128) {
						be[ii] = (byte) temp;
					} else {
						be[ii] = (byte) temp;
						ii++;
						temp = (int) c[ii];
						be[ii] = (byte) temp;
					}
					j = j + 1;
				}
				char ce[] = null;
				converter = ByteToCharUTF8.getConverter("GBK");
				ce = converter.convertAll(be);

				for (i = 0; i < j; i++) {
					utf81 += "&#x" + Integer.toHexString(ce[i]) + ";";
				}

				// utf81+=Integer.toHexString(ii);
			} catch (Exception e) {
				return "";
			}
			return utf81;
		}

	}
	private static String utf8ToGBK(String src)
    {
        String temp = "";
        StringBuilder consult = new StringBuilder();
        consult.append(src);
        
        while(consult.toString().contains("[")&&consult.toString().contains("]"))
        {
            if(consult.indexOf("[")>=0)
            {
                consult.deleteCharAt(consult.indexOf("["));
            }
            if(consult.indexOf("]")>=0)
            {
                consult.deleteCharAt(consult.indexOf("]"));
            }
        }
        
        for(int i = 0 ; i < consult.length()/6;)
        {
            int location = consult.indexOf("\\u");
            temp += loadConvert(consult.substring(location,location+6));
            consult.delete(location, location+6);
        }
        return temp;
    }
    public static String Decode(String s)
    {
        return utf8ToGBK(s);
    }
    
    private static String loadConvert(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                }
                else {
                    outBuffer.append("\\" + aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }


	public static void main(String[] args) throws Exception{
		URLDecoder.decode("中国");
	}

}