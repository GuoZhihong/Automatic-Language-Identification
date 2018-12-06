import java.io.*;
import java.util.HashMap;

public class UnigramModel {

    private HashMap<Character,Integer> letterCounts = new HashMap<Character, Integer>();
    private HashMap<Character,Double> results = new HashMap<Character, Double>();
    private int totalNumCount = 0;
    public UnigramModel(String path,String path2) throws IOException {
        readFile(path);
        computeProb();
        writeFile(path2);
    }

    public HashMap<Character, Double> getResults() {
        return results;
    }

    private void readFile(String path) throws IOException {
        File file = new File(path);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String nextLine;
        do {
            nextLine = bufferedReader.readLine();
            if(nextLine == null){
                break;
            }
            nextLine = nextLine.toLowerCase();// ignore the difference of upper and lower case
            char [] chars = nextLine.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                Character key = chars[i];
                if(!(chars[i] >='a'&& chars[i] <='z')){
                    continue;
                }
                if(letterCounts.containsKey(chars[i])){
                    int count = letterCounts.get(key) + 1;
                    letterCounts.replace(key,count);
                    continue;
                }else {
                    letterCounts.put(key,1);
                }
            }
        } while (true);
    }
    private void writeFile(String filePath){
        File file = new File(filePath);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            for (Character key:results.keySet()) {
                writer.write("("+key+")  =  "+results.get(key)+"\r\n");
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void computeProb(){
        for (Character key: letterCounts.keySet()) {
            this.totalNumCount += letterCounts.get(key);
        }

        double result;double delta =0.5;
        for (int i = 0; i < 26; i++) {
            char key = (char) (i + 97);
            if(!letterCounts.containsKey(key)){
                result = (0 + delta ) / (this.totalNumCount + delta*26);//add smoothing factor when train data set does not have this letter
                results.put(key,result);
            }else {
                result = (letterCounts.get(key) + delta ) / (this.totalNumCount + delta*26);
                results.put(key,result);
            }
        }
    }
}
