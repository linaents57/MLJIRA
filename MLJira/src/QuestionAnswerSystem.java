import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.similarity.CosineSimilarity;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class QuestionAnswerSystem {

    static class QAEntry {
        public String question;
        public String answer;

        public QAEntry() {}

        public QAEntry(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }
    }

    public static void main(String[] args) {
        // Load the question-answer dataset
        List<QAEntry> qaDataset = loadDataset("src/qa_dataset.json"); // Make sure the path is correct

        if (qaDataset == null) {
            System.out.println("Failed to load the dataset.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the CS Q&A System!");
        System.out.print("Please enter your question: ");
        String userQuestion = scanner.nextLine();

        // Find the most similar question and provide the answer
        String bestAnswer = findBestAnswer(userQuestion, qaDataset);
        System.out.println("Answer: " + bestAnswer);
    }

    public static List<QAEntry> loadDataset(String filepath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return Arrays.asList(mapper.readValue(new File(filepath), QAEntry[].class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String findBestAnswer(String userQuestion, List<QAEntry> qaDataset) {
        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        double highestSimilarity = -1.0;
        String bestAnswer = "I'm sorry, I don't know the answer to that.";

        for (QAEntry qaEntry : qaDataset) {
            Double similarity = cosineSimilarity.apply(userQuestion, qaEntry.question);
            if (similarity != null && similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestAnswer = qaEntry.answer;
            }
        }

        return bestAnswer;
    }
}
