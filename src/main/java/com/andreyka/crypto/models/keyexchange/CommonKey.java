package com.andreyka.crypto.models.keyexchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonKey {
    private int userId;
    private BigInteger commonKey = BigInteger.ZERO;
}
