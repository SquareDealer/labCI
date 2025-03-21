package atm.project.exception;

/**
 * Исключение, выбрасываемое при недостатке средств на счете.
 */
public class InsufficientFundsException extends Exception {
    /**
     * Конструктор исключения.
     * @param message Сообщение об ошибке.
     */
    public InsufficientFundsException(String message) {
        super(message);
    }
}
