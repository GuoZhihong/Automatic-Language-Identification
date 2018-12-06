import sun.font.FontRunIterator;

import java.io.*;
import java.util.ArrayList;

public class Classification {

    private ArrayList<String> sentences;
    private UnigramModel unigramModelEN;
    private UnigramModel unigramModelFR;
    private UnigramModel unigramModelOT;
    private BigramsModel bigramsModelEN;
    private BigramsModel bigramsModelFR;
    private BigramsModel bigramsModelOT;
    private UnigramModel unigramModelJP;
    private BigramsModel bigramsModelJP;
    private TrigramModel trigramModelEN;
    private TrigramModel trigramModelFR;
    private TrigramModel trigramModelOT;
    private TrigramModel trigramModelJP;

    public Classification(UnigramModel unigramModelEN, UnigramModel unigramModelFR, UnigramModel unigramModelOT, BigramsModel bigramsModelEN, BigramsModel bigramsModelFR, BigramsModel bigramsModelOT,UnigramModel unigramModelJP,BigramsModel bigramsModelJP,TrigramModel trigramModelEN,TrigramModel trigramModelFR,TrigramModel trigramModelOT,TrigramModel trigramModelJP) {
        this.unigramModelEN = unigramModelEN;
        this.unigramModelFR = unigramModelFR;
        this.unigramModelOT = unigramModelOT;
        this.bigramsModelEN = bigramsModelEN;
        this.bigramsModelFR = bigramsModelFR;
        this.bigramsModelOT = bigramsModelOT;
        this.sentences = new ArrayList<String>();

        this.unigramModelJP = unigramModelJP;
        this.bigramsModelJP = bigramsModelJP;

        this.trigramModelEN = trigramModelEN;
        this.trigramModelFR =trigramModelFR;
        this.trigramModelOT = trigramModelOT;
        this.trigramModelJP = trigramModelJP;
    }

    public void readSentences() throws IOException {
        File file = new File("InputSentences.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String nextLine;
        do {
            nextLine = bufferedReader.readLine();
            if(nextLine == null){
                break;
            }
            sentences.add(nextLine);
        } while (true);
    }

    public void findPro() throws IOException {
        for (int i = 0; i < sentences.size(); i++) {
            String filePath = "out"+(i + 1)+".txt";
            File file = new File(filePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String sentence = sentences.get(i);
            System.out.println(sentence);
            System.out.println();
            writer.write(sentence+"\r\n"+"\r\n");
            sentence = sentence.toLowerCase();
            System.out.println();
            System.out.println("UNIGRAM MODEL:");
            writer.write("UNIGRAM MODEL:"+"\r\n");
            double resultEN = 0,resultFR = 0,resultOT = 0,resultJP = 0;
            for (int j = 0; j < sentence.length(); j++) {
                if(!(sentence.charAt(j) >= 'a'&& sentence.charAt(j) <= 'z')){
                    continue;
                }
                System.out.println("UNIGRAM: "+ sentence.charAt(j));
                writer.write("UNIGRAM: "+ sentence.charAt(j)+"\r\n");
                resultFR += unigramFR(sentence.charAt(j),writer);
                resultEN += unigramEN(sentence.charAt(j),writer);
                resultOT += unigramOT(sentence.charAt(j),writer);
                resultJP += unigramJP(sentence.charAt(j),writer);
                System.out.println();
                writer.write("\r\n");
            }
            System.out.println("Total log prob of whole sentence for French is "+resultFR);
            writer.write("Total log prob of whole sentence for French is "+resultFR+"\r\n");
            System.out.println("Total log prob of whole sentence for ENGLISH is "+resultEN);
            writer.write("Total log prob of whole sentence for ENGLISH is "+resultEN+"\r\n");
            System.out.println("Total log prob of whole sentence for OTHER is "+resultOT);
            writer.write("Total log prob of whole sentence for OTHER is "+resultOT+"\r\n");
            System.out.println("Total log prob of whole sentence for JAPANESE is "+resultJP);
            writer.write("Total log prob of whole sentence for JAPANESE is "+resultJP+"\r\n");
            if(resultEN > resultFR && resultEN > resultOT&& resultEN > resultJP){
                System.out.println("According to the unigram model, the sentence is in English");
                writer.write("According to the unigram model, the sentence is in English"+"\r\n");
            }else if(resultFR > resultEN && resultFR > resultOT && resultFR>resultJP){
                System.out.println("According to the unigram model, the sentence is in French");
                writer.write("According to the unigram model, the sentence is in French"+"\r\n");
            }else if(resultOT > resultEN && resultOT > resultFR && resultOT>resultJP){
                System.out.println("According to the unigram model, the sentence is in Italian");
                writer.write("According to the unigram model, the sentence is in Italian"+"\r\n");
            }else if(resultJP> resultEN && resultJP>resultFR&& resultJP>resultOT){
                System.out.println("According to the unigram model, the sentence is in Japanese");
                writer.write("According to the unigram model, the sentence is in Japanese"+"\r\n");
            }


            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("BIGRAM MODEL");
            writer.write("----------------------------------------------------------------------------------"+"\r\n");
            writer.write("BIGRAM MODEL"+"\r\n");
            resultEN = 0 ; resultFR = 0 ; resultOT = 0; resultJP = 0;
            for (int j = 0; j < sentence.length(); j++) {
                if(j != sentence.length() - 1){
                    Character char1 = sentence.charAt(j);
                    Character char2 = sentence.charAt(j + 1);
                    if(!(char1 >='a'&& char1 <='z'&& char2 >='a'&& char2 <='z')){
                        continue;
                    }
                    String comb = char1.toString() + char2.toString();
                    System.out.println("BIGRAM: "+ comb);
                    writer.write("BIGRAM: "+ comb+"\r\n");
                    resultFR += bigramFR(comb,writer);
                    resultEN += bigramEN(comb,writer);
                    resultOT += bigramOT(comb,writer);
                    resultJP += bigramJP(comb,writer);
                    System.out.println();
                    writer.write("\r\n");
                }
            }
            System.out.println("Total log prob of whole sentence for French is "+resultFR);
            writer.write("Total log prob of whole sentence for French is "+resultFR+"\r\n");
            System.out.println("Total log prob of whole sentence for ENGLISH is "+resultEN);
            writer.write("Total log prob of whole sentence for ENGLISH is "+resultEN+"\r\n");
            System.out.println("Total log prob of whole sentence for OTHER is "+resultOT);
            writer.write("Total log prob of whole sentence for OTHER is "+resultOT+"\r\n");
            System.out.println("Total log prob of whole sentence for JAPANESE is "+resultJP);
            writer.write("Total log prob of whole sentence for JAPANESE is "+resultJP+"\r\n");
            if(resultEN > resultFR && resultEN > resultOT&& resultEN > resultJP){
                System.out.println("According to the bigram model, the sentence is in English");
                writer.write("According to the bigram model, the sentence is in English"+"\r\n");
            }else if(resultFR > resultEN && resultFR > resultOT && resultFR>resultJP){
                System.out.println("According to the bigram model, the sentence is in French");
                writer.write("According to the bigram model, the sentence is in French"+"\r\n");
            }else if(resultOT > resultEN && resultOT > resultFR && resultOT>resultJP){
                System.out.println("According to the bigram model, the sentence is in Italian");
                writer.write("According to the bigram model, the sentence is in Italian"+"\r\n");
            }else if(resultJP> resultEN && resultJP>resultFR&& resultJP>resultOT){
                System.out.println("According to the bigram model, the sentence is in Japanese");
                writer.write("According to the bigram model, the sentence is in Japanese"+"\r\n");
            }



            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("TRIGRAM MODEL");
            writer.write("----------------------------------------------------------------------------------"+"\r\n");
            writer.write("TRIGRAM MODEL"+"\r\n");

            resultEN = 0 ; resultFR = 0 ; resultOT = 0; resultJP = 0;
            for (int j = 0; j < sentence.length() - 2; j++) {
                    Character char1 = sentence.charAt(j);
                    Character char2 = sentence.charAt(j + 1);
                    Character char3 = sentence.charAt(j + 2);
                    if(!(char1 >='a'&& char1 <='z'&& char2 >='a'&& char2 <='z'&& char3 >='a' && char3 <='z')){
                        continue;
                    }
                    String comb = char1.toString() + char2.toString() + char3.toString();
                    System.out.println("TRIGRAM: "+ comb);
                    writer.write("TRIGRAM: "+ comb+"\r\n");
                    resultFR += trigramFR(comb,writer);
                    resultEN += trigramEN(comb,writer);
                    resultOT += trigramOT(comb,writer);
                    resultJP += trigramJP(comb,writer);
                    System.out.println();
                    writer.write("\r\n");
            }
            System.out.println("Total log prob of whole sentence for French is "+resultFR);
            writer.write("Total log prob of whole sentence for French is "+resultFR+"\r\n");
            System.out.println("Total log prob of whole sentence for ENGLISH is "+resultEN);
            writer.write("Total log prob of whole sentence for ENGLISH is "+resultEN+"\r\n");
            System.out.println("Total log prob of whole sentence for OTHER is "+resultOT);
            writer.write("Total log prob of whole sentence for OTHER is "+resultOT+"\r\n");
            System.out.println("Total log prob of whole sentence for JAPANESE is "+resultJP);
            writer.write("Total log prob of whole sentence for JAPANESE is "+resultJP+"\r\n");
            if(resultEN > resultFR && resultEN > resultOT&& resultEN > resultJP){
                System.out.println("According to the trigram model, the sentence is in English");
                writer.write("According to the trigram model, the sentence is in English"+"\r\n");
            }else if(resultFR > resultEN && resultFR > resultOT && resultFR>resultJP){
                System.out.println("According to the trigram model, the sentence is in French");
                writer.write("According to the trigram model, the sentence is in French"+"\r\n");
            }else if(resultOT > resultEN && resultOT > resultFR && resultOT>resultJP){
                System.out.println("According to the trigram model, the sentence is in Italian");
                writer.write("According to the trigram model, the sentence is in Italian"+"\r\n");
            }else if(resultJP> resultEN && resultJP>resultFR&& resultJP>resultOT){
                System.out.println("According to the trigram model, the sentence is in Japanese");
                writer.write("According to the trigram model, the sentence is in Japanese"+"\r\n");
            }


            System.out.println("==============================================================");
            System.out.println();
            writer.write("=============================================================="+"\r\n");
            writer.write("\r\n");
            writer.close();
        }
    }

    /*compute log values of 6 model results*/
    private double unigramEN(char letter,BufferedWriter writer) throws IOException {
        double result = unigramModelEN.getResults().get(letter);
        double logValue = Math.log10(result);
        System.out.println("ENGLISH: P("+letter+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("ENGLISH: P("+letter+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }

    private double unigramFR(char letter,BufferedWriter writer) throws IOException {
        double result = unigramModelFR.getResults().get(letter);
        double logValue = Math.log10(result);
        System.out.println("FRENCH: P("+letter+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("FRENCH: P("+letter+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }

    private double unigramOT(char letter,BufferedWriter writer) throws IOException {
        double result = unigramModelOT.getResults().get(letter);
        double logValue = Math.log10(result);
        System.out.println("OTHER: P("+letter+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("OTHER: P("+letter+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }

    private double bigramEN(String comb,BufferedWriter writer) throws IOException {
        double result = bigramsModelEN.getResults().get(comb);
        double logValue = Math.log10(result);
        System.out.println("ENGLISH: P("+comb.charAt(1)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("ENGLISH: P("+comb.charAt(1)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }
    private double bigramFR(String comb,BufferedWriter writer) throws IOException {
        double result = bigramsModelFR.getResults().get(comb);
        double logValue = Math.log10(result);
        System.out.println("FRENCH: P("+comb.charAt(1)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("FRENCH: P("+comb.charAt(1)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }

    private double bigramOT(String comb,BufferedWriter writer) throws IOException {
        double result = bigramsModelOT.getResults().get(comb);
        double logValue = Math.log10(result);
        System.out.println("OTHER: P("+comb.charAt(1)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("OTHER: P("+comb.charAt(1)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }

    private double unigramJP(char letter,BufferedWriter writer) throws IOException {
        double result = unigramModelJP.getResults().get(letter);
        double logValue = Math.log10(result);
        System.out.println("JAPANESE: P("+letter+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("JAPANESE: P("+letter+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }
    private double bigramJP(String comb,BufferedWriter writer) throws IOException {
        double result = bigramsModelJP.getResults().get(comb);
        double logValue = Math.log10(result);
        System.out.println("JAPANESE: P("+comb.charAt(1)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("JAPANESE: P("+comb.charAt(1)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }

    private double trigramEN(String comb,BufferedWriter writer) throws IOException {
        double result = trigramModelEN.getResults().get(comb);
        double logValue = Math.log10(result);
        System.out.println("ENGLISH: P("+comb.charAt(1)+comb.charAt(2)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("ENGLISH: P("+comb.charAt(1)+comb.charAt(2)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }

    private double trigramFR(String comb,BufferedWriter writer) throws IOException {
        double result = trigramModelFR.getResults().get(comb);
        double logValue = Math.log10(result);
        System.out.println("FRENCH: P("+comb.charAt(1)+comb.charAt(2)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("FRENCH: P("+comb.charAt(1)+comb.charAt(2)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }

    private double trigramOT(String comb,BufferedWriter writer) throws IOException {
        double result = trigramModelOT.getResults().get(comb);
        double logValue = Math.log10(result);
        System.out.println("ITALIAN: P("+comb.charAt(1)+comb.charAt(2)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("ITALIAN: P("+comb.charAt(1)+comb.charAt(2)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }

    private double trigramJP(String comb,BufferedWriter writer) throws IOException {
        double result = trigramModelJP.getResults().get(comb);
        double logValue = Math.log10(result);
        System.out.println("JAPANESE: P("+comb.charAt(1)+comb.charAt(2)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue);
        writer.write("JAPANESE: P("+comb.charAt(1)+comb.charAt(2)+"|"+comb.charAt(0)+") = " + result+ "==> log prob of sentence so far:" + logValue+"\r\n");
        return logValue;
    }
}
