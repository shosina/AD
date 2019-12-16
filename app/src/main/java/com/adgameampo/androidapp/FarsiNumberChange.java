package com.adgameampo.androidapp;

public class FarsiNumberChange {

    static String[] farsinumber=new String[]{"۰","۱","۲","۳","۴","۵","۶","۷","۸","۹"};
    public static String changefarsi(String text)
    {
        if (text.length()==0)
        {
            return "";
        }
        StringBuilder out= new StringBuilder();
        for (int i=0;i<text.length();i++)
        {
            char c=text.charAt(i);
            if ('0'<=c && c<='9')
            {
                out.append(farsinumber[Integer.parseInt(String.valueOf(c))]);
            }
            else if (c==',' || c=='و')
            {
                out.append(",");
            }
            else
            {
                out.append(c);
            }
        }
        return out.toString();
    }
}
