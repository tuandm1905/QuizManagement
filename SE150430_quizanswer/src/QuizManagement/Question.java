package QuizManagement;

/**
 *
 * @author Dang Minh Tuan SE150430
 */
public class Question {

    private int qId;        //Question ID
    private double qMark;   //Question mark
    private String qContent;//Question content
    
    /**
    * Sets id for question
    * @param qId question id must be a positive integer
    * @throws QuestionException
    */
    public void setQId(int qId) throws QuestionException {
        if (qId <= 0) {
            throw new QuestionException("Question ID must be a positive integer");
        } else {
            this.qId = qId;
        }
    }
    
    /**
    * Sets mark for question
    * @param qMark question mark is real number and must be from 0 to 10
    * @throws QuestionException
    */
    public void setQMark(double qMark) throws QuestionException {
        if (qMark < 0 || qMark > 10) {
            throw new QuestionException("Question mark must be from 0 to 10");
        } else {
            this.qMark = qMark;
        }
    }

    /**
    * Sets content for question
    * @param qContent question content
    * @throws QuestionException
    */
    public void setQContent(String qContent) throws QuestionException {
        if (qContent.equals("")) {
            throw new QuestionException("Question content can't be empty");
        } else {
            this.qContent = qContent;
        }
    }

    /**
    * Gets the id of question
    * @return
    */
    public int getQId() {
        return qId;
    }

    /**
    * Gets the mark of question
    * @return
    */
    public double getQMark() {
        return qMark;
    }

    /**
    * Gets the content of question
    * @return
    */
    public String getQContent() {
        return qContent;
    }

    /**
    * Creates new question
    * @param qId
    * @param qMark
    * @param qContent
    * @throws QuestionException
    */
    public Question(int qId, double qMark, String qContent) throws QuestionException {
        this.setQId(qId);
        this.setQMark(qMark);
        this.setQContent(qContent);
    }

    /**
    * Convert the question to string
    * @return
    */
    @Override
    public String toString() {
        return this.qContent + "\n";
    }
}
