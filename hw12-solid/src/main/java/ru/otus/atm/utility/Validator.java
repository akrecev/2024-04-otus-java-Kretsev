package ru.otus.atm.utility;

import java.util.Arrays;
import lombok.experimental.UtilityClass;
import ru.otus.atm.exception.BadRequestException;
import ru.otus.atm.model.Banknote;

@UtilityClass
public class Validator {
    public static void validation(int sum) {
        checkNotNegative(sum);
        checkMultipleMinNominal(sum);
    }

    private static void checkMultipleMinNominal(int sum) {
        int minBanknoteValue = Arrays.stream(Banknote.values())
                .map(b -> b.nominal)
                .min(Integer::compareTo)
                .orElseThrow();
        if (sum % minBanknoteValue != 0) {
            throw new BadRequestException("Required sum multiple " + minBanknoteValue);
        }
    }

    private static void checkNotNegative(int sum) {
        if (sum < 0) {
            throw new BadRequestException("Required not negative sum");
        }
    }
}
