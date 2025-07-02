package com.example.constant;

public class TokenConstant {


    // token加密密钥
    public static final String CLAIMS_KEY = "login_user_id";

    // token加密密钥
    public static final String SECRET = "QWERTYUIOP";

    // token过期时间毫秒  15分钟
    public static long EXPIRE_TIME = 1000 * 60 * 15;


    // RedisLoginKey
    public static String REDIS_LOGIN_KEY = "login_user_key";

    // RedisLoginKey 缓存时间 分钟
    public static int REDIS_LOGIN_KEY_EXPIRE_TIME = 15;


}
