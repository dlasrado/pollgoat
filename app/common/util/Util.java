/**
* FaseUtil.java
*
* The class contains utility methods
*
* Copyright(c) 2014 Equinix, Inc.  All Rights Reserved.
* This software is the proprietary information of Equinix.
*
*/

package common.util;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.equinix.fase.fqa.util.FQAConstants;

import play.Logger;
import play.api.i18n.Lang;
import scala.collection.Seq;

public class Util {

	/**
	 * Checks is the given string is null or empty
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNullOrEmpty(String val) {
		if (val == null)
			return true;

		if (val.length() == 0)
			return true;

		return false;
	}

	/**
	 * Converts Document object to String
	 * 
	 * @param doc
	 * @return
	 */
	public static String getStringFromDocument(String uuid, Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			return writer.toString();
		} catch (TransformerException ex) {
			AppLogger.error(uuid, "Error transforming Document Object to String : "+ex.getMessage());
			return null;
		}
	}
	
	/**
	 * Returns a single entry from a Map of collections
	 * @param name
	 * @param headers
	 * @return
	 */
	public static String getSingleEntryFromMapofCollection(String name, Map<String, Collection<String>> map) {
		Collection<String> headerVals = map.get(name);
		if(headerVals.isEmpty()) {
			return null;
		}
		return headerVals.iterator().next();
	}
	
	/**
	 * Returns a single entry from a Map of collections
	 * @param name
	 * @param headers
	 * @return
	 */
	public static String getSingleEntryFromMapOfArray(String name, Map<String, String[]> map) {
		String[] array = map.get(name);
		if(array == null || array.length == 0) {
			return null;
		}
		return array[0].trim();
	}

	public static Lang getLanguage(Lang defaultLang,List<play.i18n.Lang> reqLangs,Seq<Lang> seqAvailableLangs ){
		
		if(reqLangs == null || reqLangs.size() == 0){
			return defaultLang;
		}
		else{
			scala.collection.Iterator<Lang> itrAvailableLangs = seqAvailableLangs.iterator();
			if(!itrAvailableLangs.hasNext()) return defaultLang;
			
			List<Lang> availableLangs = new ArrayList<Lang>();
			while(itrAvailableLangs.hasNext()){
				Lang availableLang = itrAvailableLangs.next();
				availableLangs.add(availableLang);
			}
			
			for(Lang reqLang:reqLangs)
				for(Lang availableLang:availableLangs)
					if(availableLang.code().equals(reqLang.code())){
						Logger.info("request locale matched with the configuration.");
						return reqLang;
					}
				
		}
		return defaultLang;
	}

	public static String getUniqueId(){
		SimpleDateFormat sd = new SimpleDateFormat();
		
		sd.applyPattern(AppConstants.TIME_STAMP_FORMAT);
		
		return sd.format(new Date());
	}

	
	public static String getUser(String pwdStr){
		
		StringTokenizer st = new StringTokenizer(pwdStr, ":");
		
		String usr = st.nextToken();
		
		return usr;
	}
	
	public static String getPassword(String pwdStr){
		
		StringTokenizer st = new StringTokenizer(pwdStr, ":");
		
		st.nextToken();
		String pwd = st.nextToken();
		
		return pwd;
	}
	
	public static String getStackTrace(Throwable e){
		
		String trace = "";
		StackTraceElement[] array =  e.getStackTrace();
		
		for(StackTraceElement element : array){
			trace = element.toString() + "\n";
		}
		
		return trace;
		
	}

	/**
	 * Replace Null values with a replace string
	 * @param val
	 * @return
	 */
	public static String replaceNull(String val, String replaceWith) {
		
		if(replaceWith==null) {
			
			replaceWith = FQAConstants.BLANK;
			
		}
		
		if(val != null && val.equalsIgnoreCase("null"))
			val = FQAConstants.BLANK;
		
		return (val==null) ? replaceWith : val.trim();
	}
	
    /**
     * Replace Null with empty string
     * @param val
     * @return
     */
	public static String replaceNull(String val) {
		
		return replaceNull(val, null);
		
	}
	
	
	

	
}
