package com.vsii.enamecard.utils;

public final class Constant {

    private Constant(){

    }

    public static final String NAME_ROLE_ADMIN = "Admin";
    public static final String ROLE_NAME_AGENT = "Sales_Agents";
    public static final String PATTERN_FULL_NAME = "[`!@#$%^&*()_+\\-=\\[\\]{};':\\\\|,.<>\\/?~0-9]";
    public static final String PATTERN_PHONE = "(0[3|5|7|8|9])+([0-9]{8}$)|(84[3|5|7|8|9])+([0-9]{8}$)";
    public static final String PATTERN_EMAIL = "^([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,}$)";
}
