package ru.otus.atm.rest;

import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.atm.exception.BadRequestException;
import ru.otus.atm.exception.NotEnoughBanknotesException;
import ru.otus.atm.model.Banknote;
import ru.otus.atm.repository.impl.SimpleBanknoteStorageImpl;
import ru.otus.atm.service.AtmService;
import ru.otus.atm.service.impl.SimpleAtmServiceImpl;

class AtmTest {

    private AtmService atmService;
    private Map<Banknote, Integer> banknotes;

    @BeforeEach
    void setUp() {
        atmService = new SimpleAtmServiceImpl(new SimpleBanknoteStorageImpl());
        banknotes = new EnumMap<>(Banknote.class);
        banknotes.put(Banknote.BN_1000, 2);
        banknotes.put(Banknote.BN_500, 2);
        banknotes.put(Banknote.BN_100, 20);
    }

    @Test
    @DisplayName("ATM balance should be zero")
    void shouldZeroBalance() {
        int expectedBalance = 0;
        int actualBalance = atmService.showMoney();
        Assertions.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    @DisplayName("ATM balance should be 5000 after deposit")
    void should4000Balance() {
        int expectedBalance = 5000;
        int actualBalance = atmService.depositMoney(banknotes);
        Assertions.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    @DisplayName("ATM balance should be 3200 after deposit 5000 and withdrawing 1800")
    void should2500Balance() {
        int expectedBalance = 3200;
        atmService.depositMoney(banknotes);
        atmService.getMoney(1800);
        int actualBalance = atmService.showMoney();
        Assertions.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    @DisplayName("Should be thrown NotEnoughBanknotesException")
    void shouldNotEnoughBanknotesException() {
        atmService.depositMoney(banknotes);
        Assertions.assertThrows(NotEnoughBanknotesException.class, () -> atmService.getMoney(10000));
    }

    @Test
    @DisplayName("Should be thrown BadRequestException")
    void shouldBadRequestException() {
        atmService.depositMoney(banknotes);
        Assertions.assertThrows(BadRequestException.class, () -> atmService.getMoney(101));
    }
}
