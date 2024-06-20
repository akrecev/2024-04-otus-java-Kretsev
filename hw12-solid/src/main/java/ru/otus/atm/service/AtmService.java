package ru.otus.atm.service;

import java.util.Map;
import ru.otus.atm.model.Banknote;

public interface AtmService {
    int depositMoney(Map<Banknote, Integer> banknotes);

    int getMoney(int sum);

    int showMoney();
}
