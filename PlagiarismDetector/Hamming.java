import java.util.*;
import java.util.stream.Collectors;

public class Hamming {

    /**
     * Construct map of synonyms 
     * @param inputString
     * @return
     */
    public Map<String, HashSet<String>> constructSynonymsMap(String inputString) {
        Map<String, HashSet<String>> synonymsMap = new HashMap<String, HashSet<String>>();

        if (inputString == null || inputString.length() == 0) {
            return synonymsMap;
        }

        List<String> synonymGroups =  Arrays.stream(inputString.split("\\|")).map(String::trim).collect(Collectors.toList());
        for (String synonymGroup : synonymGroups) {
            HashSet<String> synonymList = (HashSet<String>) Arrays.stream(synonymGroup.split(" ")).map(String::trim).collect(Collectors.toSet());
            for (String synonym : synonymList) {
                if (synonymsMap.containsKey(synonym)) {
                    HashSet<String> existingSynonyms = synonymsMap.get(synonym);
                    existingSynonyms.addAll(synonymList);
                    synonymsMap.put(synonym, existingSynonyms);
                } else {
                    synonymsMap.put(synonym, synonymList);
                }
            }
        }

        return synonymsMap;
    }

    /**
     *  Calculate plagiarism percentage using tuples
     * @param originalCorpus
     * @param suspectCorpus
     * @param synonymsMap
     * @return
     */
    private int getPlagiarismPercentage(Corpus originalCorpus, Corpus suspectCorpus, Map<String, HashSet<String>> synonymsMap) {
        List<Tuple> originalTupleList = originalCorpus.getTupleList();
        List<Tuple> suspectTupleList = suspectCorpus.getTupleList();

        if (originalTupleList.size() == 0 || suspectTupleList.size() == 0) {
            return 0;
        }
        double similarityCount = 0d;
        for (Tuple suspectTuple : suspectTupleList) {
            for (Tuple originalTuple : originalTupleList) {
                if (suspectTuple.equals(originalTuple, synonymsMap)) {
                    similarityCount += 1;
                    break;
                }
            }
        }
        return (int) Math.ceil((similarityCount / suspectTupleList.size()) * 100);
    }

    public int solution(String[] inputStrings, int tupleSize) throws IllegalArgumentException {
        if (inputStrings.length != 3) {
            throw new IllegalArgumentException("Size of Input Array should be 3");
        }
        Map<String, HashSet<String>> synonymsMap = constructSynonymsMap(inputStrings[0]);
        Corpus originalCorpus = new Corpus(inputStrings[1], tupleSize);
        Corpus suspectCorpus = new Corpus(inputStrings[2], tupleSize);
        return getPlagiarismPercentage(originalCorpus, suspectCorpus, synonymsMap);
    }

    public static void main(String[] args) {
        Hamming solution = new Hamming();
        String[] input = new String[3];
        input[0] = "run for meditation"; //to catch same one 
        input[2] = "speed|speeding"; //for length 
        input[1] = "run for medication|run for medication"; // similarity 
        System.out.println(solution.solution(input, 3));
    }
}
