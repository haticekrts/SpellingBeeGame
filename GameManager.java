package com.spellingbee;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class GameManager {

    public GameManager(String dictionaryPath){
        this.dictionaryPath=dictionaryPath;
    }
    private String centerChar;
    private String otherChars;

    private String dictionaryPath;

    public void setDictionaryPath(String dictionaryPath) {
        this.dictionaryPath = dictionaryPath;
    }

    public String getDictionaryPath() {
        return dictionaryPath;
    }

    public String getCenterChar() {
        return centerChar;
    }

    public void setCenterChar(String centerChar) {
        this.centerChar = centerChar;
    }

    public String getOtherChars() {
        return otherChars;
    }

    public void setOtherChars(String otherChar) {
        this.otherChars = otherChar;
    }

    private List<String> readTxtFileToList(){
        try
        {
            InputStream inputStream= getClass().getResourceAsStream(dictionaryPath);
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            var listStr = new ArrayList<String>();
            String line;
            while((line=reader.readLine())!=null)
            {
                if(line.length()>3)
                    listStr.add(line);
            }
            streamReader.close();
            reader.close();
            Collections.shuffle(listStr);
            return listStr;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private List<Character> getUniqueChars(String str){
        HashSet<Character> uniqueChar = new HashSet<Character>();
        for (int i = 0; i < str.length(); i++) {
            uniqueChar.add(str.charAt(i));
        }
        return uniqueChar.stream().toList();
    }
    public List<Character> isAvailablePangramChars(){
        var dictionary = readTxtFileToList();
        for (var str:dictionary){
            if(str.length()>=7){
                List<Character> characters = getUniqueChars(str);
                if(characters.size()==7){
                    return characters;
                }
            }
        }
        return null;
    }

    public boolean isMacthedString(String str){
        var list = readTxtFileToList();
        return list.stream().anyMatch(t->t.equalsIgnoreCase(str));
    }

    public String validateSolutionAndCenterChar(List<Character> uniqueChars){
        var list = readTxtFileToList();
        Random rnd = new Random();
        int rndInt = rnd.nextInt(7);
        final char centerChar = uniqueChars.get(rndInt);
        int counter=0;
        for (var str:list){

            var isValid= str.chars().allMatch(c->{
                if(!str.chars().anyMatch(s->Character.toLowerCase(s)==Character.toLowerCase(centerChar)))
                    return false;
                if(!uniqueChars.stream().anyMatch(unique->Character.toLowerCase(c)==Character.toLowerCase(unique)))
                    return false;
                return true;
            });

            if(isValid){
                counter++;
            }


            if(counter>=20){
                return String.valueOf(centerChar);
            }
        }
        return null;
    }
}
