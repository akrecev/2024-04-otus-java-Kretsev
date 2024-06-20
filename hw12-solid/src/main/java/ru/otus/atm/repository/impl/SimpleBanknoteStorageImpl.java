package ru.otus.atm.repository.impl;

import java.util.EnumMap;
import java.util.Map;
import ru.otus.atm.exception.NotEnoughBanknotesException;
import ru.otus.atm.model.Banknote;
import ru.otus.atm.repository.BanknoteStorage;

public class SimpleBanknoteStorageImpl implements BanknoteStorage {
    private final Map<Banknote, Integer> banknoteStorageMap = new EnumMap<>(Banknote.class);

    @Override
    public Map<Banknote, Integer> putTheMoney(Map<Banknote, Integer> sum) {
        for (Map.Entry<Banknote, Integer> banknotes : sum.entrySet()) {
            banknoteStorageMap.merge(banknotes.getKey(), banknotes.getValue(), Integer::sum);
        }

        return showMeTheMoney();
    }

    @Override
    public Map<Banknote, Integer> giveMeTheMoney(Map<Banknote, Integer> sum) {
        for (Map.Entry<Banknote, Integer> banknotes : sum.entrySet()) {
            if (banknoteStorageMap.get(banknotes.getKey()) - banknotes.getValue() < 0) {
                throw new NotEnoughBanknotesException("Not enough banknotes nominal " + banknotes.getKey().nominal);
            }
        }
        sum.forEach((k, v) -> banknoteStorageMap.put(k, banknoteStorageMap.get(k) - v));

        return sum;
    }

    @Override
    public Map<Banknote, Integer> showMeTheMoney() {
        return new EnumMap<>(banknoteStorageMap);
    }
}
