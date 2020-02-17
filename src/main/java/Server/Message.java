package Server;

import AES.AES;
import Base64.Base64;
import ECC.ECPoint;
import ECDSA.ECDSA;
import MD5.MD5;

import java.io.Serializable;
import java.math.BigInteger;

public class Message implements Serializable, Cloneable {
    private MessageType msgType;
    private String text;
    private ECPoint public_key;
    private String username;
    private ECPoint signature;

    Message(MessageType msgType) {
        this.msgType = msgType;
        this.text = null;
    }

    public Message(MessageType msgType, String text) {
        this.msgType = msgType;
        this.text = text;
    }

    Message(MessageType msgType, ECPoint public_key) {
        this(msgType, null, public_key);
    }

    public Message(MessageType msgType, String username, ECPoint publicKey) {
        this.msgType = msgType;
        this.public_key = publicKey;
        this.username = username;
    }

    public void encrypt(BigInteger commonKey, BigInteger yourPrivateKey) {
        try {
            String base64EncodedMessage = Base64.encode(this.text);
            String hashedBase64EncodedMessage = MD5.getHash(base64EncodedMessage);

            ECPoint signatureForHashed = ECDSA.getSignature(hashedBase64EncodedMessage, yourPrivateKey);

            AES aesObject = new AES(commonKey);
            byte[] bytes = aesObject.encrypt(base64EncodedMessage);

            this.text = Base64.encode(bytes);
            this.signature = signatureForHashed;
        } catch (Exception e) {
            e.printStackTrace();
            this.text = "Ошибка отправки сообщения!";
            this.signature = new ECPoint();
        }
    }

    public void decrypt(BigInteger commonKey, ECPoint other_public_key) {
        try {
            byte[] base64DecodedEncryptedMessage = Base64.decode(this.text);
            AES aesObject = new AES(commonKey);
            byte[] decodedByteArray = aesObject.decrypt(base64DecodedEncryptedMessage);

            String hashedBase64DecodedMessage = MD5.getHash(new String(decodedByteArray));

            boolean isValidSignature = ECDSA.isValid(hashedBase64DecodedMessage, other_public_key, this.signature);

            if (isValidSignature) {
                this.text = new String(Base64.decode(new String(decodedByteArray)));
            } else {
                System.out.println("ПОДМЕНА!!!!");
            }
        } catch (Exception e) {
            System.out.println("Кто-то подменил сообщение!");
            e.printStackTrace();
        }
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public String getText() {
        return this.text;
    }

    public ECPoint getPublic_key() {
        return public_key;
    }

    String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s", text, signature);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
