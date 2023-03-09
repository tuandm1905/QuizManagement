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
public class QuestionManagement {

    private String Q_FILE;                  //The URL of data file that stores all questions
    private int numberOfQuestion;           //Number of questions that stored in data file
    private ArrayList<Question> question;   //All instances of questions
    private AnswerManagement am;            //Instance of AnswerManagement

    /**
     * Creates instance for question management
     *
     * @param Q_FILE
     * @param am
     * @throws quizmanagement.QuestionException
     */
    public QuestionManagement(String Q_FILE, AnswerManagement am) throws QuestionException {
        if (Q_FILE.equals("")) {
            throw new QuestionException("The URL of answer data file can't be empty!");
        } else {
            this.Q_FILE = Q_FILE; //Inits the URL of data file thats stores question bank 

            this.question = new ArrayList<Question>(); //Creates empty question bank 

            this.numberOfQuestion = 0; //So, the number of question is 0

            this.am = am; //Inits the answer management
        }
    }

    /**
     * Loads data of questions from data file and stored it into ArrayList
     *
     * @throws IOException
     * @throws QuestionException
     */
    public void loadQuestions() throws IOException, QuestionException {
        File qFile = new File(Q_FILE);

        if (!qFile.exists()) { //Checks is file created 
            qFile.createNewFile(); //If not, creates new file
            System.out.println("The data file questions.txt is not exits. "
                    + "Creating new data file questions.txt..."
                    + "Done!");
            this.numberOfQuestion = 0; //New data file with the number of question is 0
        } else {
            //If file is existed, so loading this data file 
            System.out.print("\nThe data file questions.txt is found. "
                    + "Data of questions is loading...");

            BufferedReader br = new BufferedReader(new FileReader(Q_FILE)); //Loads text file into buffer
            try {
                String qId, qContent, qMark;
                this.numberOfQuestion = Integer.parseInt(br.readLine()); //Reads number of answers
                for (int i = 0; i < this.numberOfQuestion; i++) {
                    //Reads answer's information	
                    qId = br.readLine();

                    qContent = br.readLine();
                    qMark = br.readLine();

                    //Create new instance of Answer and adds to answer bank 
                    this.question.add(new Question(Integer.parseInt(qId),
                            Double.parseDouble(qMark),
                            qContent));
                }
            } finally {
                br.close();
            }
            System.out.println("Done! [" + this.numberOfQuestion + " questions]");
        }
    }

    /**
     * Gets number of questions
     *
     * @return
     */
    public int getSize() {
        return this.numberOfQuestion;
    }

    /**
     * Adds new question to question bank
     *
     * @param qMark
     * @param qContent
     * @return
     * @throws QuestionException
     */
    public int addQuestion(double qMark, String qContent) throws QuestionException {
        this.question.add(new Question(++this.numberOfQuestion, qMark, qContent));

        return this.numberOfQuestion;
    }

    /**
     * Finds question by question id and return the index of this question
     *
     * @param qId
     * @return
     */
    public int findQuestion(int qId) {
        for (int i = 0; i < this.question.size(); i++) {
            Question q = this.question.get(i);
            if (q.getQId() == qId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the question instance by question id
     *
     * @param qId
     * @return
     */
    public Question getQuestion(int qId) {
        int idx = this.findQuestion(qId);
        if (idx == -1) {
            return null;
        } else {
            return this.question.get(idx);
        }
    }

    /**
     * Saves question bank (ArrayList) into data file
     *
     * @throws IOException
     */
    public void saveQuestions() throws IOException {
        FileWriter fw = new FileWriter(new File(Q_FILE), false); //Overwrite data file

        try {
            System.out.print("\nQuestions is saving into data file questions.txt...");
            fw.append(String.valueOf(this.numberOfQuestion) + "\n"); //Writes number of question
            for (int i = 0; i < this.numberOfQuestion; i++) {
                //Inits question's information
                int qId = this.question.get(i).getQId();
                String qContent = this.question.get(i).getQContent();
                double qMark = this.question.get(i).getQMark();
                //Writes quesiton's information into data file 
                fw.append(String.valueOf(qId) + "\n");
                fw.append(qContent + "\n");
                fw.append(String.valueOf(qMark) + "\n");
            }
        } finally {
            fw.close(); //Saves data file (from RAM to HOD) 
            System.out.println("Done! [" + this.numberOfQuestion + " questions]");
        }
    }

    /**
     * Checks that the user's answer is correct or incorrect
     *
     * @param qId
     * @param answers
     * @return
     */
    public boolean isQuestionCorrect(int qId, ArrayList<Answer> answers) {
        boolean isCorrect = true;

        for (int i = 0; i < answers.size(); i++) {
            //the answer of user is correct even if the user's selected is the same with answer's status 
            isCorrect = isCorrect && answers.get(i).isCorrect();
        }

        return isCorrect;
    }

    /**
     * Gets the question formatted string that includes question content and all
     * answers that comes with random mode
     *
     * @param qId
     * @param isShuffle turn on/off random display answer mode
     * @return
     */
    public String showQuestion(int qId, boolean isShuffle) {
        Question q = getQuestion(qId);
        ArrayList<Answer> aList = am.getAnswers(qId, isShuffle);

        String str = "";

        str += q.toString();

        char aNo = 'a';

        for (int i = 0; i < aList.size(); i++, aNo++) {
            str += "    " + aNo + ". " + aList.get(i).toString();
        }

        return str;
    }

    /**
     * Gets the question formatted string that includes question content and all
     * answers that comes with a list of answers
     *
     * @param qId
     * @param aList
     * @return
     */
    public String showQuestion(int qId, ArrayList<Answer> aList) {
        Question q = getQuestion(qId);

        String str = " ";

        str += q.toString();

        char aNo = 'a';

        for (int i = 0; i < aList.size(); i++, aNo++) {
            str += "   " + aNo + ". " + aList.get(i).toString();
        }

        return str;
    }

    /**
     * Displays all question of question bank
     */
    public void showQuestionBank() {
        int qNo = 1;

        for (int i = 0; i < this.question.size(); i++, qNo++) {
            Question q = this.question.get(i);
            System.out.println(qNo + ". " + showQuestion(q.getQId(), false));
        }
    }

    /**
     * Gets the first qNumber of question bank
     *
     * @param qNumber
     * @param isShuffle
     * @return
     */
    public ArrayList<Question> getQuestionBank(int qNumber, boolean isShuffle) {
        ArrayList<Question> qList = new ArrayList<Question>();

        //Inits the index of all answer
        int[] idx = new int[question.size()];

        for (int i = 0; i <question.size(); i++) {
            idx[i] = i;
        }

        if (isShuffle) { //if the random mode is turned on
            int newidx, tmp;
            Random ran = new Random();

            //Randomizes indexes of answer bank
            for (int i = 0; i < question.size(); i++) {
                newidx = ran.nextInt(question.size());

                tmp = idx[i];
                idx[i] = idx[newidx];
                idx[newidx] = tmp;
            }
        }
        for (int i = 0; i < qNumber; i++) {
            qList.add(question.get(idx[i]));
        }
        return qList;
    }
}
