package QuizManagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Dang Minh Tuan SE150430
 */
public class AnswerManagement {

    private String A_FILE;              //The URL of data file that stores all answers
    private int numberOfAnswer;         //Number of answers that stored in data file
    private ArrayList<Answer> answers;  //All instance of answers

    /**
     * Creates instance for answer management
     *
     * @param A_FILE
     * @throws quizmanagement.AnswerException
     */
    public AnswerManagement(String A_FILE) throws AnswerException {
        if (A_FILE.equals("")) {
            throw new AnswerException("The URL of answer data file can't be empty");
        } else {
            this.A_FILE = A_FILE;                   //Inits the URL of data file thats stores answer bank
            this.answers = new ArrayList<Answer>(); //Creates empty answer bank
            this.numberOfAnswer = 0;                //So, the number of answer is 0
        }
    }

    /**
     * Loads data of answers from data file and stored it into ArrayList
     *
     * @throws IOException
     * @throws AnswerException
     */
    public void loadAnswer() throws IOException, AnswerException {
        File aFile = new File(A_FILE);

        if (!aFile.exists()) {       //Checks is file created
            aFile.createNewFile();  //If not, creates new file
            System.out.println("The data file answer.txt is not exits. "
                    + "Creating new data file answer.txt..."
                    + "Done!");
            this.numberOfAnswer = 0;    //New data file with the number of answer is 0
        } else {
            //If file is existed, so loading this data file
            System.out.println("\nThe data file answer.txt is found. "
                    + "Data of answer is loading...");
            //Loads text file into buffer
            try (BufferedReader br = new BufferedReader(new FileReader(A_FILE))) {
                String qid, aId = null, aContent, aStatus;

                this.numberOfAnswer = Integer.parseInt(br.readLine()); //Reads number of answers

                for (int i = 0; i < this.numberOfAnswer; i++) {
                    //Reads answer's information 
                    aId = br.readLine();
                    aContent = br.readLine();
                    aStatus = br.readLine();
                    qid = br.readLine();

                    //Create new instance of Answer and adds to answer bank 
                    this.answers.add(new Answer(Integer.parseInt(aId),
                            aContent, Boolean.parseBoolean(aStatus), Integer.parseInt(qid)));
                }
            }
            System.out.println("Done! [" + this.numberOfAnswer + " answers)");
        }
    }

    /**
     * Adds new answer to answer bank
     *
     * @param aContent
     * @param aStatus
     * @param qid
     * @return
     * @throws AnswerException
     */
    public int addAnswer(String aContent, boolean aStatus, int qid) throws AnswerException {
        this.answers.add(new Answer(++this.numberOfAnswer, aContent, aStatus, qid));
        return this.numberOfAnswer; //answer id
    }

    /**
     * Finds answer by answer id and return the index of this answer
     *
     * @param aId
     * @return
     */
    public int findAnswer(int aId) {
        for (int i = 0; i < this.answers.size(); i++) {
            Answer a = this.answers.get(i);
            if (a.getAId() == aId) {
                return i;
            }
        }
        return - 1;
    }

    /**
     * Saves answer bank (ArrayList) into data file
     *
     * @throws IOException
     */
    public void saveAnswer() throws IOException {
        //Overwrite data file
        FileWriter fw = new FileWriter(new File(A_FILE), false);

        try {
            System.out.println("Answer is saving into data file answer.txt...");

            //Write number of answer
            fw.append(String.valueOf(this.numberOfAnswer) + "\n");

            for (int i = 0; i < this.numberOfAnswer; ++i) {
                //Inits answer's information
                int aId = this.answers.get(i).getAId();
                String aContent = this.answers.get(i).getAContent();
                boolean aStatus = this.answers.get(i).getAStatus();
                int qId = this.answers.get(i).getQId();

                //Writes answer's information into data file
                fw.append(String.valueOf(aId) + "\n");
                fw.append(aContent + "\n");
                fw.append(String.valueOf(aStatus) + "\n");
                fw.append(String.valueOf(qId) + "\n");
            }
        } finally {
            //Saves data file (from RAM into HDD)
            fw.close();
            System.out.println("Done! [" + this.numberOfAnswer + " answers]");
        }
    }

    /**
     * Gets all answer that belongs to question that identifies by question id
     *
     * @param qid
     * @return
     */
    public ArrayList<Answer> getAnswers(int qId, boolean isShuffle) {
        ArrayList<Answer> aList = new ArrayList<Answer>();

        for (int i = 0; i < this.answers.size(); i++) {
            Answer a = this.answers.get(i);
            if (a.getQId() == qId) {
                aList.add(a);
            }
        }

        //Inits the index of all answer
        int[] idx = new int[aList.size()];
        for (int i = 0; i < aList.size(); i++) {
            idx[i] = i;
        }

        if (isShuffle) { //if the random mode is turned on
            int newidx, tmp;
            Random ran = new Random();

            //Randomizes indexes of answer bank
            for (int i = 0; i < aList.size(); i++) {
                newidx = ran.nextInt(aList.size());
                tmp = idx[i];
                idx[i] = idx[newidx];
                idx[newidx] = tmp;
            }
        }

        ArrayList<Answer> result = new ArrayList<Answer>();
        for (int i = 0; i < aList.size(); i++) {
            result.add(aList.get(idx[i]));
        }
        return result;
    }
}
