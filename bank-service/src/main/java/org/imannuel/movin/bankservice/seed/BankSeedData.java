package org.imannuel.movin.bankservice.seed;

import org.imannuel.movin.bankservice.entity.Bank;

import java.util.List;

public class BankSeedData {
    public static List<Bank> getBankSeedData() {
        return List.of(
                Bank.builder()
                        .code("bca")
                        .name("Bank Central Asia")
                        .fee(400)
                        .image("")
                        .status(true)
                        .build(),
                Bank.builder()
                        .code("bni")
                        .name("Bank Nasional Indonesia")
                        .fee(350)
                        .image("")
                        .status(true)
                        .build(),
                Bank.builder()
                        .code("bri")
                        .name("Bank Rakyat Indonesia")
                        .fee(300)
                        .image("")
                        .status(true)
                        .build(),
                Bank.builder()
                        .code("mandiri")
                        .name("Bank Mandiri")
                        .fee(450)
                        .image("")
                        .status(true)
                        .build()
        );
    }
}
