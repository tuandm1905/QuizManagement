package QuizManagement;

/**
 *
 * @author Dang Minh Tuan SE150430
 */
public class QuestionException extends Exception {

    /**
     * Creates new QuestionException
     *
     * @param message
     */
    public QuestionException(String message) {
        super(message);
    }

    /**
     * Get the exception message
     *
     * @return
     */
    @Override
    public String getMessage() {
        return "QuestionException: " + super.getMessage();
    }
}
