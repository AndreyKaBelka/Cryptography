package com.andreyka.crypto;

import com.andreyka.crypto.service.*;
import org.junit.Before;

public class BaseTest {
    protected CryptoService cryptoService;
    protected KeyPairService keyPairService;
    protected ECCService eccService;
    protected AESService aesService;
    protected ECDSAService ecdsaService;

    @Before
    public void setUp() throws Exception {
        ECPointMathService ecPointMathService = new ECPointMathServiceImpl();
        WordService wordService = new WordServiceImpl();
        MD5Service md5Service = new MD5ServiceImpl();
        Base64Service base64Service = new Base64ServiceImpl();
        aesService = new AESServiceImpl(wordService);
        keyPairService = new KeyPairServiceImpl(ecPointMathService);
        eccService = new ECCServiceImpl(ecPointMathService);
        ecdsaService = new ECDSAServiceImpl(keyPairService, ecPointMathService);
        cryptoService = new CryptoServiceImpl(base64Service, md5Service, ecdsaService, aesService);
    }
}
