package QuizManagement;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The class QuizManagement is used to manage quiz
 *
 * @author Dang Minh Tuan SE150430
 */
public class QuizManagement {

    private static AnswerManagement am;
    private static QuestionManagement qm;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            am = new AnswerManagement("answers.txt");           //Loads answer bank
            am.loadAnswer();

            qm = new QuestionManagement("questions.txt", am);   //Loads question bank
            qm.loadQuestions();

            Scanner cin = new Scanner(System.in);   //Creates a scanner
            int func;                               //The function that selected by user
            do {
                //Shows menu
                System.out.println("\n---------QUIZ MANAGEMENT----------");
                System.out.println("1. Add question.");
                System.out.println("2. Show question bank.");
                System.out.println("3. Create quiz.");
                System.out.println("4. Quit.\n");

                //Gets function that selected by user
                System.out.print("   Please select a function: ");
                func = cin.nextInt();
                cin.nextLine();

                String strUserEntered = "";
                switch (func) {
                    case 1:
                        System.out.println("-----QUIZ MANAGEMENT [AND NEW QUESTION]------");
                        String qContent = "";
                        double qMark = 0.0;

                        //Gets content of question
                        do {
                            System.out.print("Please enter content of question: ");
                            qContent = cin.nextLine();
                            if (qContent.equals("")) {
                                System.out.println("Error: Question content can't be empty!");
                            }
                        } while (qContent.equals(""));

                        //Gets the mark of question
                        do {
                            System.out.println("Please enter mark of question: ");
                            qMark = cin.nextDouble();
                            cin.nextLine();
                            if (qMark < 0 || qMark > 10) {
                                System.out.println("Error: Question mark must be from 0 to 10!");
                            }
                        } while (qMark < 0 || qMark > 10);

                        //Creates new question and get it's id
                        int qId = qm.addQuestion(qMark, qContent);

                        System.out.println("Your question is created!");
                        System.out.println("+++ [ADD ANSWER FOR QUESTION] +++");

                        //Adds answer for this question
                        int aNo = 0;
                        do {
                            aNo++;
                            System.out.println("... Answer " + aNo + " ...");
                            String aContent = "";
                            boolean aStatus = false;

                            //Gets content of answer
                            do {
                                System.out.print("Please enter content of answer " + aNo + ": ");
                                aContent = cin.nextLine();
                                if (aContent.equals("")) {
                                    System.out.println("Error: Answer content can't be empty!");
                                }
                            } while (aContent.equals(""));

                            //Gets status of answer
                            do {
                                System.out.println("Is this answer True or False? (True/False) ");
                                strUserEntered = cin.nextLine();
                                if (strUserEntered.equals("True")) {
                                    aStatus = true;
                                } else if (strUserEntered.equals("False")) {
                                    aStatus = false;
                                } else {
                                    System.out.println("Error: You must type 'True' or 'False'!");
                                }
                            } while (!(strUserEntered.equals("True") || strUserEntered.equals("False")));

                            am.addAnswer(aContent, aStatus, qId);   //Creates new answer

                            do {
                                System.out.println("Do you want to add more answer? (Yes/No) ");
                                strUserEntered = cin.nextLine();
                                if ((!(strUserEntered.equals("Yes") || strUserEntered.equals("No")))) {
                                    System.out.println("Error: You must type 'Yes' or 'No'!");
                                }
                            } while (!(strUserEntered.equals("Yes") || strUserEntered.equals("No")));
                        } while (strUserEntered.equals("Yes"));
                        break;
                    case 2:
                        System.out.println("----- QUIZ MANAGEMENT [QUESTION BANK] (" + qm.getSize()
                                + " question) -----");
                        qm.showQuestionBank();
                        break;
                    case 3:
                        //examination
                        int totalQuestionNumbers = qm.getSize();
                        int qNumbers = 0;
                        boolean isRandom = false;
                        double mark = 0.0;
                        double totalMark = 0.0;
                        int correctCount = 0;

                        System.out.println("----- QUIZ MANAGEMENT [EXCAMNATION] (" + totalQuestionNumbers
                                + " question) -----");
                        //Gets number of question of the test
                        do {
                            System.out.print("How many question of the test: ");
                            qNumbers = cin.nextInt();
                            cin.nextLine();

                            if (qNumbers < 1 || totalQuestionNumbers < qNumbers) {
                                System.out.println("Number of question must be from 1 to "
                                        + totalQuestionNumbers);
                            }
                        } while (qNumbers < 1 || totalQuestionNumbers < qNumbers);

                        
                        //Turn on/off the random mode
                        do {
                            System.out.println("Do you want to shuffle the test? (True/False) ");
                            strUserEntered = cin.nextLine();
                            if (strUserEntered.equals("True")) {
                                isRandom = true;
                            } else if (strUserEntered.equals("False")) {
                                isRandom = false;
                            } else {
                                System.out.println("Error: You must type 'True' or 'False'");
                            }
                        } while (!(strUserEntered.equals("True") || strUserEntered.equals("False")));

                        //Generates the test
                        System.out.println("+++ The Test is generatimg...");
                        ArrayList<Question> qList = qm.getQuestionBank(qNumbers, isRandom);
                        System.out.println("Done! +++");
                        System.out.println("\n##################");
                        System.out.println("#      TESTING      #");

                        ArrayList<Answer> aList;
                        Question q;
                        int qNo = 1;
                        char ans,
                         last;
                        for (int i = 0; i < qList.size(); i++, qNo++) {
                            q = qList.get(i);
                            qId = q.getQId();
                            aList = am.getAnswers(qId, isRandom);

                            System.out.println("#####################");
                            System.out.println(qNo + ". " + qm.showQuestion(qId, aList));

                            do {
                                //Gets the answer of user
                                System.out.print("   >>>Please select answer: ");
                                ans = (cin.nextLine().charAt(0));
                                last = (char) ('a' + aList.size() - 1);

                                if (ans < 'a' || last < ans) {
                                    System.out.println("Error: Your answer must be from 'a' to '"
                                            + last + "'!");
                                } else {
                                    aList.get(ans - 'a').setASelected(true);
                                }
                            } while (ans < 'a' || last < ans);

                            boolean isUserCorrect = qm.isQuestionCorrect(qId, aList);

                            totalMark += q.getQMark();
                            if (isUserCorrect) {
                                mark += q.getQMark();
                                correctCount++;
                                System.out.println("   +++ Congratulation! Your answer is CORRECT!!!");
                            } else {
                                System.out.println("   --- So sad! Your answer is WRONG!!!");
                            }
                            cin.nextLine();
                        }
                        System.out.println("+++++++++++++++");
                        System.out.println("You are FINISH!!!");
                        System.out.println("Correct rate is " + correctCount + "/" + qList.size()
                                + " (" + String.format("%.2f", ((double) correctCount * 100 / qList.size())) + "%)");
                        System.out.println("Total mark is " + String.format("%.2f", mark) + "/"
                                + String.format("%.2f", totalMark)
                                + " (" + String.format("%.2f", ((double) mark * 100 / totalMark)) + "%");
                        cin.nextLine();
                        break;
                    case 4:
                        System.out.println("\n--------------------");
                        System.out.println("Thank for using our software!\n"
                                + "See you again!");
                        break;
                    default:
                        System.out.println("Error: The finction must be from 1 to 4!");
                }
            } while (func != 4);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                am.saveAnswer(); //Save answers
            } catch (Exception e) {
                System.out.println("Exception: Can't save answers!");
            }

            try {
                qm.saveQuestions(); //Save questions
            } catch (Exception e) {
                System.out.println("Exception: Can't save question!");
            }
        }
    }
}
