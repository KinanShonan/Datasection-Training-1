/*
    Copyright (C) 2009, Cam-Tu Nguyen, Xuan-Hieu Phan
    
    Email:	ncamtu@gmail.com; pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package jvntagger.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.Vector;

public class StringUtils {
		
	public static int findFirstOf (String container, String chars, int begin){        
        int minIdx = -1;        
        for (int i = 0; i < chars.length() && i >= 0; ++i){
            int idx = container.indexOf(chars.charAt(i), begin);            
            if ( (idx < minIdx && idx != -1) || minIdx == -1){                    
                    minIdx = idx;
            }
        }
        return minIdx;
    }
	   
    public static int findLastOf (String container, String charSeq, int begin){        
		//find the last occurence of one of characters in charSeq from begin backward
        for (int i = begin; i < container.length() && i >= 0; --i){
            if (charSeq.contains("" + container.charAt(i)))
                return i;
        }
        return -1;        
    }
    
    public static int findFirstNotOf(String container, String chars, int begin){
		//find the first occurence of characters not in the charSeq	from begin foward	
		for (int i = begin; i < container.length() && i >=0; ++i) 
		   if (!chars.contains("" + container.charAt(i)))
				return i;
		return -1;
    }
    
    public static int findLastNotOf(String container, String charSeq, int end){
        for (int i = end; i < container.length() && i >= 0; --i){
            if (!charSeq.contains("" + container.charAt(i)))
                return i;        
        }
        return -1;
    } 
    
    //Syllable Features 
    public static boolean containNumber(String str) {
		for (int i = 0; i < str.length(); i++) {
		    if (Character.isDigit(str.charAt(i))) {
			return true;
		    }
		}		
		return false;
    }    
    
    public static boolean containLetter(String str) {
		for (int i = 0; i < str.length(); i++) {
		    if (Character.isLetter(str.charAt(i))) {
			return true;
		    }
		}
		
		return false;
    }
    
    public static boolean containLetterAndDigit(String str) {
    	return (containLetter(str) && containNumber(str));
    }
            
    public static boolean isAllNumber(String str) {
    	boolean hasNumber = false;
		for (int i = 0; i < str.length(); i++) {
		    if (!(Character.isDigit(str.charAt(i)) || 
				str.charAt(i) == '.' || str.charAt(i) == ',' || str.charAt(i) == '%'
				|| str.charAt(i) == '$' || str.charAt(i) == '_')) {
			return false;
		    }
		    else if (Character.isDigit(str.charAt(i)))
		    	hasNumber = true;
		}
		
		if (hasNumber == true)
			return true;
		else return false;		
    }
    
    public static boolean isFirstCap(String str) {
    	if (isAllCap(str)) return false;
    	
		if (str.length() > 0 && Character.isLetter(str.charAt(0)) &&
				Character.isUpperCase(str.charAt(0))) {
		    return true;
		}
		
		return false;
    }
    
    public static boolean isAllCap(String str) {
		if (str.length() <= 0) {
		    return false;
		}
		
		for (int i = 0; i < str.length(); i++) {
		    if (!Character.isLetter(str.charAt(i)) ||
		    		!Character.isUpperCase(str.charAt(i))) {
				    return false;				
		    }
		}
		
		return true;	
    }
    
    public static boolean isNotFirstCap(String str) {
    	return !isFirstCap(str);
    }    
    
    public static boolean endsWithSign(String str) {
		if (str.endsWith(".") || str.endsWith("?") || str.endsWith("!") ||
			    str.endsWith(",") || str.endsWith(":") || str.endsWith("\"") || 
			    str.endsWith("'") || str.endsWith("''") || str.endsWith(";")) {
		    return true;
		}
		
		return false;
    }

    public static boolean endsWithStop(String str) {
	if (str.endsWith(".") || str.endsWith("?") || str.endsWith("!")) {
	    return true;
	}
	
	return false;
    }
    
    public static int countStops(String str) {
		int count = 0;
	    
		for (int i = 0; i < str.length(); i++) {
		    if (str.charAt(i) == '.' || str.charAt(i) == '?' || str.charAt(i) == '!') {
			count++;
		    }
		}
		
		return count;
    }
    
    public static int countSigns(String str) {
		int count = 0;
	    
		for (int i = 0; i < str.length(); i++) {
		    if (str.charAt(i) == '.' || str.charAt(i) == '?' || str.charAt(i) == '!' ||
				str.charAt(i) == ',' || str.charAt(i) == ':' || str.charAt(i) == ';') {
			count++;
		    }
		}
		
		return count;
    }
    
    public static boolean isStop(String str) {
		if (str.compareTo(".") == 0) {
		    return true;
		}
	
		if (str.compareTo("?") == 0) {
		    return true;
		}
		
		if (str.compareTo("!") == 0) {
		    return true;
		}
		
		return false;
    }
    
    public static boolean isSign(String str) {
    	if (str == null) return false;
    	str = str.trim();
    	
    	for (int i = 0; i < str.length(); ++i){
    		char c = str.charAt(i);
    		if (Character.isDigit(c) || Character.isLetter(c)){
    			return false;
    		}
    	}
		return true;
    }
    /**
	 * Join the <tt>String</tt> representations of an array of objects, with the specified
	 * separator.
	 * 
	 * @param  objects  array of objects to join.
	 * @param  sep      separator character.
	 * 	  
	 * @return  newly created <tt>String</tt>.
	 */
	public static String join( Object[] objects, char sep )
	{
		if( objects.length == 0 )
		{
			return "";
		}
		StringBuffer buffer = new StringBuffer( objects[0].toString() );
		for (int i = 1; i < objects.length; i++)
		{
			buffer.append( sep );
			buffer.append( objects[i].toString() );
		}
		return buffer.toString();
	}
	
	/**
	 * Join the <tt>String</tt> representations of a collection of objects, with the specified
	 * separator.
	 * 
	 * @param  col    collection of objects to join.
	 * @param  sep    separator character.
	 * 	  
	 * @return  newly created <tt>String</tt>.
	 */
	public static String join( Collection col, char sep )
	{
		if( col.isEmpty() )
		{
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		boolean first = true; 
		for (Object o : col)
		{
			if( first )
			{
				first = false;
			}
			else
			{
				buffer.append( sep );
			}
			buffer.append( o.toString() );
		}
		return buffer.toString();
	}
	
	// ---------------------------------------------------------
	// String Manipulation
	// ---------------------------------------------------------
	
	/**
	 * Capitalises the first letter of a given string.
	 *  
	 * @param s  the input string
	 * 
	 * @return   the capitalized string
	 */
	public static String capitalizeWord( String s )
	{
		// validate
		if( (s == null) || (s.length() == 0) )
		{
			return s;
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
	
	/** 
	 * Encloses the specified <tt>String</tt> in single quotes.
	 * 
	 * @param s  the input string
	 * 
	 * @return the quoted String
	 */
	public static String quote( String s )
	{
		return '\'' + s + '\''; 
	}

	/** 
	 * Encloses the specified <tt>String</tt> in double quotes.
	 * 
	 * @param s  the input string
	 * 
	 * @return the quoted String
	 */
	public static String doubleQuote( String s )
	{
		return '"' + s + '"'; 
	}

	/**
	 * Pad the specified <tt>String</tt> with spaces on the right-hand side.
	 * 
	 * @param s       String to add spaces
	 * @param length  Desired length of string after padding
	 * 
	 * @return padded string.
	 */
	public static String pad( String s, int length )
	{
		// Trim if longer...
		if( s.length() > length )
		{
			return s.substring( 0, length );
		}
		StringBuffer buffer = new StringBuffer(s);
		int spaces = length - s.length();
		while( spaces-- > 0 )
		{
			buffer.append(' ');
		}
		return buffer.toString();
	}
	
	/**
	 * Sorts the characters in the specified string.
	 * 
	 * @param s   input String to sort.
	 * 
	 * @return  output String, containing sorted characters.
	 */
	public static String sort( String s )
	{
		char[] chars = s.toCharArray();
		Arrays.sort( chars );
		return new String( chars );
	}

	  
	// ---------------------------------------------------------
	// String Matching
	// ---------------------------------------------------------
	
   /**
    * Checks whether a String is whitespace, empty or null.
    * 
    * @param s   the <tt>String</tt> to analyze.
    * 
    * @return <tt>true</tt> if <tt>s</tt> is whitespace/empty/null, <tt>false</tt> otherwise.
    */
	public static boolean isBlank( String s )
	{
		if (s == null)
		{
			return true;
		}
		int sLen = s.length();
		for (int i = 0; i < sLen; i++)
		{
			if (!Character.isWhitespace(s.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
   
   /**
    * Checks whether a <tt>String</tt> is composed entirely of whitespace characters.
    * 
    * @param s   the <tt>String</tt> to analyze.
    * 
    * @return <tt>true</tt> if <tt>s</tt> is all whitespace, <tt>false</tt> otherwise.
    */
	public static boolean isWhitespace( String s )
	{
		if( s == null )
		{
			return false;
		}
		int sLen = s.length();
		for (int i = 0; i < sLen; i++)
		{
			if (!Character.isWhitespace(s.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	// ---------------------------------------------------------
	// Search-related
	// ---------------------------------------------------------
   
	/**
	 * Counts the number of occurrences of a character in the specified <tt>String</tt>.
	 * 
	 * @param s   the <tt>String</tt> to analyze.
	 * @param c   the character to search for.
	 * 
	 * @return number of occurrences found.
	 */
	public static int countOccurrences( String s, char c )
	{
		int count = 0;
		int index = 0;
		while( true )
		{
			index = s.indexOf( c, index );
			if( index == -1 )
			{
				break;
			}
			count++;
		}
		return count;
	}
	
	/**
	 * Indicates whether the specified array of <tt>String</tt>s contains
	 * a given <tt>String</tt>.
	 * 
	 * @param  array   the array to search.
	 * @param  s       the <tt>String</tt> to search for.
	 * 
	 * @return <tt>true</tt> if <tt>s</tt> was found, <tt>false</tt> otherwise.
	 */
	public static boolean isContained( String[] array, String s )
	{
		for (String string : array)
		{
			if( string.equals( s ) )
			{
				return true;
			}
		}
		return false;
	}
	
	// ---------------------------------------------------------
	// Array/Collection conversion
	// ---------------------------------------------------------
	
	/**
	 * Returns the index of the first occurrence of the specified <tt>String</tt>
	 * in an array of <tt>String</tt>s.
	 * 
	 * @param array  array of <tt>String</tt>s to search.
	 * @param s      the <tt>String</tt> to search for.
	 * 
	 * @return the index of the first occurrence of the argument in this list, 
	 *         or -1 if the string is not found.
	 */
	public static int indexOf( String[] array, String s )
	{
		for (int index = 0; index < array.length; index++)
		{
			if( s.equals( array[index] ) )
			{
				return index;
			}
		}
		return -1;
	}
	
	/**
	 * Creates a new <tt>ArrayList</tt> collection from the specified array of <tt>String</tt>s.
	 * 
	 * @param  array of <tt>String</tt>s.
	 * 
	 * @return  newly created <tt>ArrayList</tt>.
	 */
	public static ArrayList<String> toList( String[] array )
	{
		if( array == null )
		{
			return new ArrayList<String>( 0 );
		}
		ArrayList<String> list = new ArrayList<String>( array.length );
		for (String s : array)
		{
			list.add( s );
		}
		return list;
	}
	
	/**
	 * Creates a new <tt>Vector</tt> collection from the specified array of <tt>String</tt>s.
	 * 
	 * @param  array of <tt>String</tt>s.
	 * 
	 * @return  newly created <tt>Vector</tt>.
	 */
	public static Vector<String> toVector( String[] array )
	{
		if( array == null )
		{
			return new Vector<String>( 0 );
		}
		Vector<String> v = new Vector<String>( array.length );
		v.copyInto( array );
		return v;
	}
	
	/**
	 * Creates a new <tt>ArrayList</tt> collection from the specified <tt>Set</tt> of <tt>String</tt>s.
	 * 
	 * @param set   a set of <tt>String</tt>s.
	 * 
	 * @return newly created <tt>ArrayList</tt>.
	 */
	public static ArrayList<String> toList( Set<String> set )
	{
		int n = set.size();
		ArrayList<String> list = new ArrayList<String>( n );
		for (String string : set)
		{
			list.add(string);
		}
		return list;
	}

    
}
