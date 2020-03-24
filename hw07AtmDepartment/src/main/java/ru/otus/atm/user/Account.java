package ru.otus.atm.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.atm.enums.CcyCode;

@Getter
@AllArgsConstructor
class Account {
    private final CcyCode ccyCode;
    @Setter
    private Double amount;
}
