package com.andreyka.crypto.constants;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.ECFieldFp;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidParameterSpecException;

@Slf4j
public class Inputs {

    static {
        try {
            AlgorithmParameters params = AlgorithmParameters.getInstance("EC", "SunEC");
            params.init(new ECGenParameterSpec("NIST P-256"));

            ECParameterSpec ecParameters = params.getParameterSpec(ECParameterSpec.class);

            System.setProperty("A_CONST", ecParameters.getCurve().getA().toString());
            System.setProperty("GX_CONST", ecParameters.getGenerator().getAffineX().toString());
            System.setProperty("GY_CONST", ecParameters.getGenerator().getAffineY().toString());
            System.setProperty("P_CONST", ((ECFieldFp) ecParameters.getCurve().getField()).getP().toString());
            System.setProperty("B_CONST", ecParameters.getCurve().getB().toString());
            System.setProperty("N_CONST", ecParameters.getOrder().toString());
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidParameterSpecException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    public static final Inputs A = new Inputs(System.getProperty("A_CONST"));
    public static final Inputs GX = new Inputs(System.getProperty("GX_CONST"));
    public static final Inputs GY = new Inputs(System.getProperty("GY_CONST"));
    public static final Inputs P = new Inputs(System.getProperty("P_CONST"));
    public static final Inputs B = new Inputs(System.getProperty("B_CONST"));
    public static final Inputs N = new Inputs(System.getProperty("N_CONST"));

    public final BigInteger value;

    private Inputs(String str) {
        this.value = new BigInteger(str);
    }
}
