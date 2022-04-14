package com.andreyka.crypto.models;

import lombok.ToString;
import lombok.Value;

@Value
@ToString(includeFieldNames = false)
class Message {
    long sessionId;
    long userId;
    String message;
}
