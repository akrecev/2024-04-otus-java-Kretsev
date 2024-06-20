package ru.otus.atm.model;

public enum Banknote {
    BN_5000(5000),
    BN_1000(1000),
    BN_500(500),
    BN_100(100),
    BN_50(50),
    BN_10(10);

    public final int nominal;

    Banknote(int nominal) {
        this.nominal = nominal;
    }
}
