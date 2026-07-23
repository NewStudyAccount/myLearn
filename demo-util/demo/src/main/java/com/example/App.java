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


        String attrValue1 = "Mb_111";
        String attrValue2 = "Gb_10";

        String newattrValue = attrValue2.replaceAll("([A-Za-z])([A-Za-z])_(\\d+(?:\\.\\d+)?)", "$3$1$2ps");
        System.out.println(newattrValue);

        String attrValue3 = "EQ_2";
        String attrValue4 = "No_122";
        String newattrValue3  = attrValue4.replaceAll("([A-Za-z])([A-Za-z])_(\\d+(?:\\.\\d+)?)", "$3");
        System.out.println(newattrValue3);


        String attrValue5 = "GB_2";
        String newOrgValue5 = attrValue5.replaceAll("([A-Za-z])([A-Za-z])_(\\d+(?:\\.\\d+)?)", "$3$1$2");
        System.out.println(newOrgValue5);




        String attrValue6 = "T1(v.11/V.35)/T1(V.11/V.35)";
        String newOrgValue6 = attrValue6
                .replaceAll("([A-Za-z])(\\d+(?:\\.\\d+)?)_(\\d+(?:\\.\\d+)?)", "$2$1/$3$1")
                .replaceAll("([A-Za-z])(\\d+(?:\\.\\d+)?)_([A-Za-z])(\\d+(?:\\.\\d+)?)", "$2$1/$4$3");
        System.out.println("newOrgValue6"+newOrgValue6);




    }



}
