package ru.otus.atm.repository;

import java.util.Map;
import ru.otus.atm.model.Banknote;

public interface BanknoteStorage {
    Map<Banknote, Integer> putTheMoney(Map<Banknote, Integer> sum);

    Map<Banknote, Integer> giveMeTheMoney(Map<Banknote, Integer> sum);

    Map<Banknote, Integer> showMeTheMoney();
}
