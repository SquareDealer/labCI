package atm.project.exception;

/**
 * Исключение, выбрасываемое при попытке использовать некорректную сумму.
 */
public class InvalidAmountException extends Exception {
    /**
     * Конструктор исключения.
     * @param message Сообщение об ошибке.
     */
    public InvalidAmountException(String message) {
        super(message);
    }
}
