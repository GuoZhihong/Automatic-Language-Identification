import java.io.*;

public class Driver {
    public static void main(String[] args) throws IOException {
        UnigramModel unigramModelEN = new UnigramModel("trainEN.txt","unigramEN.txt");
        UnigramModel unigramModelFR = new UnigramModel("trainFR.txt","unigramFR.txt");
        UnigramModel unigramModelOT = new UnigramModel("trainOT.txt","unigramOT.txt");
        BigramsModel bigramsModelEN = new BigramsModel("trainEN.txt","bigramEN.txt");
        BigramsModel bigramsModelFR = new BigramsModel("trainFR.txt","bigramFR.txt");
        BigramsModel bigramsModelOT = new BigramsModel("trainOT.txt","bigramOT.txt");
        UnigramModel unigramModelJP = new UnigramModel("trainJP.txt","unigramJP.txt");
        BigramsModel bigramsModelJP = new BigramsModel("trainJP.txt","bigramJP.txt");
        TrigramModel trigramModelEN = new TrigramModel("trainEN.txt","trigramEN.txt");
        TrigramModel trigramModelFR = new TrigramModel("trainFR.txt","trigramFR.txt");
        TrigramModel trigramModelOT = new TrigramModel("trainOT.txt","trigramOT.txt");
        TrigramModel trigramModelJP = new TrigramModel("trainJP.txt","trigramJP.txt");
//        trigramModelEN.findHighWordCounts();
        Classification classification = new Classification(unigramModelEN, unigramModelFR, unigramModelOT, bigramsModelEN, bigramsModelFR,bigramsModelOT,unigramModelJP,bigramsModelJP,trigramModelEN,trigramModelFR,trigramModelOT,trigramModelJP);
        classification.readSentences();
        classification.findPro();
    }
}
