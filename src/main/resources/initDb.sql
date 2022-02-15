CREATE TABLE IF NOT EXISTS public.common_keys
(
    userId    int primary key,
    commonKey varchar(255)
);

CREATE TABLE IF NOT EXISTS public.key_confirmations
(
    userId          int primary key,
    keyConfirmation varchar
);

CREATE TABLE IF NOT EXISTS public.info
(
    userId     int,
    publicKey  varchar,
    privateKey varchar
);

CREATE TABLE IF NOT EXISTS public.secret_shares
(
    chatId      int,
    userId      int,
    secretShare varchar,

    primary key (userId, chatId)
);

CREATE TABLE IF NOT EXISTS public.session_ids
(
    chatId    int primary key,
    sessionId varchar
);