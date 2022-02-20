package com.andreyka.crypto.npone;

import com.andreyka.crypto.models.keyexchange.GroupMessage;

public class VerifySessionKeyCommand implements NPOneCommand<Void, GroupMessage<String>> {
    @Override
    public Void execute(GroupMessage<String> val) {
        return null;
    }

    private void checkSessionKey(GroupMessage<String> sessionKeyMessage) {
        long userId = sessionKeyMessage.getUserId();

//        EncryptionUtils.verifySign();
    }
}
