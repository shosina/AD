package com.adcompany.androidapp;

public class EnglishNumberChange {
    
    public static String changeEnglish(String text)
    {
        if (text.length()==0)
        {
            return "";
        }
        String out;
        out=text;

        if (out.contains("۰"))
            out=out.replaceAll("۰","0");
        if (out.contains("۱"))
            out=out.replaceAll("۱","1");
        if (out.contains("۲"))
            out=out.replaceAll("۲","2");
        if (out.contains("۳"))
            out=out.replaceAll("۳","3");
        if (out.contains("۴"))
            out=out.replaceAll("۴","4");
        if (out.contains("۵"))
            out=out.replaceAll("۵","5");
        if (out.contains("۶"))
            out=out.replaceAll("۶","6");
        if (out.contains("۷"))
            out=out.replaceAll("۷","7");
        if (out.contains("۸"))
            out=out.replaceAll("۸","8");
        if (out.contains("۹"))
            out=out.replaceAll("۹","9");
        
        return out;
    }
}
