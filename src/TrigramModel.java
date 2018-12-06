import java.io.*;
import java.util.*;

public class TrigramModel {
    private HashMap<String, Integer> wordsCounts = new HashMap<String, Integer>();
    private HashMap<Character,Integer> letterCounts = new HashMap<Character, Integer>();
    private HashMap<String,Double> results = new HashMap<String, Double>();
    private int totalNumCount = 0;
    String Message = null;


    public TrigramModel(String path,String path2) throws IOException {
        readFile(path);
        writeFile(path2);
    }

    public HashMap<String, Double> getResults() {
        return results;
    }

    private void readFile(String path) throws IOException {
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String nextLine;
        do {
            nextLine = bufferedReader.readLine();
            stringBuilder.append(nextLine);
        } while (nextLine != null);
        this.Message = stringBuilder.toString().toLowerCase();// ignore the difference of upper and lower case
        countLetters();
        iniWordsCounts();
    }
    private void countLetters(){
        for (int i = 0; i < Message.length(); i++) {
            Character key = Message.charAt(i);
            if(!(Message.charAt(i) >='a'&& Message.charAt(i) <='z')){
                continue;
            }
            if(letterCounts.containsKey(Message.charAt(i))){
                int count = letterCounts.get(key) + 1;
                letterCounts.replace(key,count);
                totalNumCount++;
                continue;
            }else {
                letterCounts.put(key,1);
                totalNumCount++;
            }
        }
    }

    private void iniWordsCounts(){
        for (int i = 0; i < this.Message.length() - 2; i++) {
                Character key = Message.charAt(i);
                Character followBy = Message.charAt(i + 1);
                Character followBy2 = Message.charAt(i + 2);
                if(!(key >='a'&& key <='z'&& followBy >='a'&& followBy <='z'&&followBy2 >='a'&&followBy2 <= 'z')){
                    continue;
                }
                String combKey = key.toString() + followBy.toString()+followBy2.toString();
                if(wordsCounts.containsKey(combKey)){
                    int count = wordsCounts.get(combKey) + 1;
                    wordsCounts.replace(combKey,count);
                    continue;
                }else{
                    wordsCounts.put(combKey,1);
                    continue;
                }
        }
        computeProb();
    }

    /*output training model*/
    private void writeFile(String filePath){
        File file = new File(filePath);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < letterCounts.size(); i++) {
                char key = (char)(i + 97);
                for (String s : results.keySet()) {
                    if(s.charAt(0) == key){
                        writer.write("("+s.charAt(1)+s.charAt(2)+"|"+key+")  =  "+results.get(s)+"\r\n");
                    }
                }
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void computeProb(){
        double delta = 0.5;
        for (int i = 0; i < 26; i++) {
            Character key = (char) (i + 97);
            double result;
            if(!letterCounts.containsKey(key)){//add smoothing factor when train data set does not have this letter
                for (int j = 0; j < 26; j++) {
                    Character key2 = (char) (j + 97);
                    for (int k = 0; k < 26; k++) {
                        Character key3 = (char) (k + 97);
                        String s = key.toString()+key2.toString()+key3.toString();
                        result = (delta / (delta * 26));
                        results.put(s, result);
                    }
                }
            }else {
                for (int j = 0; j < 26; j++) {
                    Character key2 = (char) (j + 97);
                    for (int k = 0; k < 26; k++) {
                        Character key3 = (char) (k + 97);
                        String s = key.toString()+key2.toString()+key3.toString();
                        if(!wordsCounts.containsKey(s)){//add smoothing factor when train data set does not have this letter
                            result = (delta / (delta * 26));
                            results.put(s, result);
                        }else {
                            int baseNum = letterCounts.get(key);
                            result = ((wordsCounts.get(s) + delta) / (baseNum + delta * 26));
                            results.put(s, result);
                        }
                    }
                }
            }
        }
    }

    public void findHighWordCounts(){
        for (String s:wordsCounts.keySet()) {
            if(wordsCounts.get(s) >= 500){
                System.out.println("word  "+s+"  appears "+wordsCounts.get(s)+"  times");
            }
        }
    }

}

