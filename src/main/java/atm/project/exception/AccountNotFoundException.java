package atm.project.exception;

/**
 * Исключение, выбрасываемое при отсутствии счета.
 */
public class AccountNotFoundException extends Exception {
    /**
     * Конструктор исключения.
     * @param message Сообщение об ошибке.
     */
    public AccountNotFoundException(String message) {
        super(message);
    }
}
