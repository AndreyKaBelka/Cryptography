package com.andreyka.crypto.service;

import com.andreyka.crypto.data.ECPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ECCServiceImpl implements ECCService {
    private final ECPointMathService ecPointMathService;

    @Autowired
    public ECCServiceImpl(ECPointMathService ecPointMathService) {
        this.ecPointMathService = ecPointMathService;
    }

    @Override
    public BigInteger getCommonKey(ECPoint publicKeyOtherUser, BigInteger yourPrivateKey) throws CloneNotSupportedException {
        return this.ecPointMathService.multiply(publicKeyOtherUser, yourPrivateKey).getX();
    }

}
