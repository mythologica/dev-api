package com.devtools.worker.tools.text;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextTools {
    public static String toCamelCase(String text){
        return toCamelCase(text,false);
    }

    public static String toCamelCase(String text, boolean isFirstUpperCase ){
        StringBuilder sb = new StringBuilder();
        int idx = 0;
        if(isFirstUpperCase) {
            for( String oneString : text.toLowerCase().split("_") ) {
                sb.append( oneString.substring(0,1).toUpperCase() );
                sb.append( oneString.substring(1).toLowerCase() );
            }
        } else {
            for( String oneString : text.toLowerCase().split("_") ) {
                if( idx == 0 ) sb.append( oneString.substring(0,1).toLowerCase() );
                else sb.append( oneString.substring(0,1).toUpperCase() );
                sb.append( oneString.substring(1).toLowerCase() );
                idx++;
            }
        }
        return sb.toString();
    }

    public static String toUnderScore(String text ) {
        return toUnderScore(text,false);
    }

    public static String toUnderScore(String text , boolean isChangeLowerCase) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        if( isChangeLowerCase ) {
            return text.replaceAll(regex, replacement).toLowerCase();
        } else {
            return text.replaceAll(regex, replacement).toUpperCase();
        }
    }

    public static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }
}
