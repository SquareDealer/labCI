package atm.project;

import java.time.LocalDateTime;

/**
 * Представляет банковскую транзакцию.
 */
public class Transaction {
    private final double amount;
    private final LocalDateTime dateTime;
    private final String type;

    /**
     * Создает новую транзакцию.
     * @param amount Сумма транзакции.
     * @param type Тип транзакции (Deposit или Withdrawal).
     */
    public Transaction(double amount, String type) {
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
        this.type = type;
    }

    /**
     * Возвращает строковое представление транзакции.
     * @return Строка с информацией о транзакции.
     */
    @Override
    public String toString() {
        return String.format("%s: %s %.2f at %s",
                type, type.equals("Withdrawal") ? "-" : "+", amount, dateTime);
    }
}
