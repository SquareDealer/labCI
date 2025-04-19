package atm.project;

import atm.project.models.Transaction;
import atm.project.service.ATMService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import atm.project.exception.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ATMServiceTests {
    private ATMService atm;

    @BeforeEach
    void setUp() {
        atm = new ATMService();
    }

    // Тесты для Account.deposit
    @Test
    void depositShouldThrowInvalidAmountExceptionForNegativeAmount() {
        atm.createAccount("12345");
        Exception exception = assertThrows(InvalidAmountException.class, () -> atm.deposit(new BigDecimal("-100.0")));
        assertEquals("Deposit amount must be positive", exception.getMessage());
    }

    @Test
    void depositShouldThrowInvalidAmountExceptionForZeroAmount() {
        atm.createAccount("12345");
        Exception exception = assertThrows(InvalidAmountException.class, () -> atm.deposit(BigDecimal.ZERO));
        assertEquals("Deposit amount must be positive", exception.getMessage());
    }

    @Test
    void depositShouldThrowAccountNotFoundExceptionWhenNoAccount() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> atm.deposit(new BigDecimal("100.0")));
        assertEquals("No account found. Please create an account first.", exception.getMessage());
    }

    @Test
    void depositShouldWorkWithValidAmount() throws AccountNotFoundException, InvalidAmountException {
        atm.createAccount("12345");
        atm.deposit(new BigDecimal("100.0"));
        assertEquals(new BigDecimal("100.0"), atm.checkBalance());
    }

    // Тесты для Account.withdraw
    @Test
    void withdrawShouldThrowInvalidAmountExceptionForNegativeAmount() {
        atm.createAccount("12345");
        Exception exception = assertThrows(InvalidAmountException.class, () -> atm.withdraw(new BigDecimal("-100.0")));
        assertEquals("Withdrawal amount must be positive", exception.getMessage());
    }

    @Test
    void withdrawShouldThrowInvalidAmountExceptionForZeroAmount() {
        atm.createAccount("12345");
        Exception exception = assertThrows(InvalidAmountException.class, () -> atm.withdraw(BigDecimal.ZERO));
        assertEquals("Withdrawal amount must be positive", exception.getMessage());
    }

    @Test
    void withdrawShouldThrowAccountNotFoundExceptionWhenNoAccount() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> atm.withdraw(new BigDecimal("100.0")));
        assertEquals("No account found. Please create an account first.", exception.getMessage());
    }

    @Test
    void withdrawShouldThrowInsufficientFundsExceptionWhenNotEnoughMoney()
            throws AccountNotFoundException, InvalidAmountException {
        atm.createAccount("12345");
        atm.deposit(new BigDecimal("50.0"));
        Exception exception = assertThrows(InsufficientFundsException.class, () -> atm.withdraw(new BigDecimal("100.0")));
        assertTrue(exception.getMessage().contains("Insufficient funds"));
    }

    @Test
    void withdrawShouldWorkWithValidAmount()
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        atm.createAccount("12345");
        atm.deposit(new BigDecimal("100.0"));
        atm.withdraw(new BigDecimal("50.0"));
        assertEquals(new BigDecimal("50.0"), atm.checkBalance());
    }

    // Тесты для checkBalance
    @Test
    void checkBalanceShouldThrowAccountNotFoundExceptionWhenNoAccount() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> atm.checkBalance());
        assertEquals("No account found. Please create an account first.", exception.getMessage());
    }

    @Test
    void checkBalanceShouldReturnZeroForNewAccount() throws AccountNotFoundException {
        atm.createAccount("12345");
        assertEquals(BigDecimal.ZERO, atm.checkBalance());
    }

    // Тесты для getTransactionHistory
    @Test
    void getTransactionHistoryShouldThrowAccountNotFoundExceptionWhenNoAccount() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> atm.getTransactionHistory());
        assertEquals("No account found. Please create an account first.", exception.getMessage());
    }

    @Test
    void getTransactionHistoryShouldReturnEmptyListForNewAccount() throws AccountNotFoundException {
        atm.createAccount("12345");
        List<Transaction> history = atm.getTransactionHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void getTransactionHistoryShouldReturnTransactionsAfterOperations()
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        atm.createAccount("12345");
        atm.deposit(new BigDecimal("100.0"));
        atm.withdraw(new BigDecimal("50.0"));
        List<Transaction> history = atm.getTransactionHistory();
        assertEquals(2, history.size());
        System.out.println(history);
        assertTrue(history.get(0).toString().toLowerCase().contains("deposit"));
        assertTrue(history.get(1).toString().toLowerCase().contains("withdrawal"));
    }
}
