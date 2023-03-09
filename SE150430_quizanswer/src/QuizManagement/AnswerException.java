package QuizManagement;

/**
 *
 * @author Dang Minh Tuan SE150430
 */
public class AnswerException extends Exception {

    public AnswerException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "AnswerException: " + super.getMessage();
    }
}
