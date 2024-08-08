package com.spellingbee.models;

public class ModelState {
    private final boolean isValid;
    private String message;

    public ModelState(String message, boolean isValid){
        this.isValid=isValid;
        this.message=message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isValid() {
        return isValid;
    }

    public static ModelState Validate(String value){
        value = value.toUpperCase();
        for (char c : value.toCharArray())
            if(!Character.isLetter(c))
                return  new ModelState("Hatalı değer girdiniz !",false);

        if (value.contains("Q") || value.contains("W") || value.contains("X"))
            return new ModelState("Türkçe karakter giriniz !",false);

        return new ModelState("",true);
    }

    public static ModelState Validate(String value,Boolean ignoreSameCharacter){
        value = value.toUpperCase();
        for (char c : value.toCharArray())
            if(!Character.isLetter(c))
                return  new ModelState("Hatalı değer girdiniz !",false);

        if (value.contains("Q") || value.contains("W") || value.contains("X"))
            return new ModelState("Türkçe karakter giriniz !",false);

        if(!ignoreSameCharacter){
            var charValue = value.toCharArray();
            for (int i = 0; i < charValue.length; i++) {
                for (int j = 0; j < charValue.length; j++) {
                    if(i!=j && charValue[i]==charValue[j])
                        return new ModelState("Birden fazla aynı karakter var !",false);
                }
            }
        }
        return new ModelState("",true);
    }
}
