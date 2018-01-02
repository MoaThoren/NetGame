package integration;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

//
//
//

public class WordHandler {
    private String file = "C:\\Users\\ofalk\\Desktop\\EnglishNounsEasy";
    private int wordArrSize = 25;
    private String[] quickWordArr = new String[wordArrSize];

    public WordHandler(){
        try{
            //downloadSwedishWords();
        } catch (Exception e){
            System.out.println(e);
        }

    }

    private int listLength(){
        int i = 0;
        try{
            FileReader fileReader = new FileReader(new File( file + ".txt"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((bufferedReader.readLine()) != null) {;
                i++;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return i;
    }


    /*
    private void downloadSwedishWords() throws Exception {
        try{
            String ts = "";
            String[] ta;

            URL web = new URL("https://doon.se/ordtyp/Substantiv");
            URLConnection uc = web.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            uc.getInputStream()));
            String inputLine;

            PrintWriter out = new PrintWriter("SwedishWords.txt");

            while ((inputLine = in.readLine()) != null)
                if (inputLine.contains("<a href=\"/ordtyp/Substantiv\">Substantiv</a>")) {
                    ts = inputLine;
                    ta = ts.split("/");
                    out.println(ta[2]);
                }
            in.close();
        }catch(Exception e){
            System.out.println("Could not download words.");
        }
    }
    */

    private void fillQuickArr() throws IOException {
        int i = 0;
        String[] tempArr = new String[listLength()];
        FileReader fileReader = new FileReader(new File(file + ".txt"));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            tempArr[i] = line;
            i++;
        }
        for(int j = 0; j < wordArrSize; j++){
            quickWordArr[j] = tempArr[(int)(Math.random() * tempArr.length)];
        }
        fileReader.close();
    }

    private void printQuickArr(){
        for(int i = 0; i < quickWordArr.length; i++){
            System.out.print("[" + quickWordArr[i] + "]");
        }
        System.out.println();
    }

    public String getWord(){
        String tempWord = null;
        try {
            for (int i = 0; i <= quickWordArr.length; i++){
                if (i == quickWordArr.length){
                    fillQuickArr();
                    i = 0;
                }
                if(quickWordArr[i] == null){
                    continue;
                }
                tempWord = quickWordArr[i];
                quickWordArr[i] = null;
                break;

            }
        }catch (Exception e){
            System.out.println(e);
        }
        return tempWord;
    }
}