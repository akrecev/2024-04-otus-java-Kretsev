package ru.otus.atm.service.impl;

import static ru.otus.atm.utility.Validator.*;

import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import ru.otus.atm.exception.NotEnoughBanknotesException;
import ru.otus.atm.model.Banknote;
import ru.otus.atm.repository.BanknoteStorage;
import ru.otus.atm.service.AtmService;

@RequiredArgsConstructor
public class SimpleAtmServiceImpl implements AtmService {
    private final BanknoteStorage storage;

    @Override
    public int depositMoney(Map<Banknote, Integer> banknotes) {
        return storage.putTheMoney(banknotes).entrySet().stream()
                .mapToInt(e -> e.getKey().nominal * e.getValue())
                .sum();
    }

    @Override
    public int getMoney(int sum) {
        validation(sum);

        Map<Banknote, Integer> receivedMoney = new EnumMap<>(Banknote.class);
        for (Map.Entry<Banknote, Integer> banknotes : storage.showMeTheMoney().entrySet()) {
            int requiredAmount = sum / banknotes.getKey().nominal;

            if (banknotes.getValue() < requiredAmount) {
                sum -= banknotes.getKey().nominal * banknotes.getValue();
                banknotes.setValue(banknotes.getValue());
            } else {
                sum -= banknotes.getKey().nominal * requiredAmount;
                banknotes.setValue(requiredAmount);
            }
            receivedMoney.put(banknotes.getKey(), banknotes.getValue());
        }

        if (sum > 0) {
            throw new NotEnoughBanknotesException("Not enough banknotes for this sum");
        }

        storage.giveMeTheMoney(receivedMoney);

        return sum;
    }

    @Override
    public int showMoney() {
        return storage.showMeTheMoney().entrySet().stream()
                .mapToInt(e -> e.getKey().nominal * e.getValue())
                .sum();
    }
}
