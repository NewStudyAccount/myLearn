package com.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

//        String orgValue = "M99_89";
//        String orgValue = "M99_K89";
//        String orgValue = "K99_M89";
//        String orgValue = "99K/89M";
        String orgValue = "K9.9_M8.9";
//        String newOrgValue = orgValue
//                .replaceAll("(?i)M(\\d+(?:\\.\\d+)?)_(\\d+(?:\\.\\d+)?)", "$1M/$2M")
//                .replaceAll("(?i)K(\\d+(?:\\.\\d+)?)_(\\d+(?:\\.\\d+)?)", "$1K/$2K")
//                .replaceAll("(?i)M(\\d+(?:\\.\\d+)?)_K(\\d+(?:\\.\\d+)?)", "$1M/$2K")
//                .replaceAll("(?i)K(\\d+(?:\\.\\d+)?)_M(\\d+(?:\\.\\d+)?)", "$1K/$2M");
        String newOrgValue = orgValue
                .replaceAll("([A-Za-z])(\\d+(?:\\.\\d+)?)_(\\d+(?:\\.\\d+)?)", "$2$1/$3$1")
                .replaceAll("([A-Za-z])(\\d+(?:\\.\\d+)?)_([A-Za-z])(\\d+(?:\\.\\d+)?)", "$2$1/$4$3");

        System.out.println(newOrgValue);

    }
}
