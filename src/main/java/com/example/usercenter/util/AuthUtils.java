package com.example.usercenter.util;


import cn.hutool.crypto.KeyUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;

/**
 * JWT工具
 * @author _LJC
 */
public class AuthUtils {
    /**
     * 密钥
     */
    private static final String KEY = "token$*&23";

    /**
     * 生成token
     * @param userId 用户id
     * @return token
     */
    public static String createToken(Long userId) {
        String id = "rs256";
        // 设置过期时间按 10小时
        long expireTime = 1000 * 60 * 60 * 10;
        JWTSigner signer = JWTSignerUtil.createSigner(id, KeyUtil.generateKeyPair(AlgorithmUtil.getAlgorithm(id)));
        return JWT.create().setPayload("userId", userId).setPayload("expire", System.currentTimeMillis() + expireTime)
                .setSigner(signer).setKey(KEY.getBytes()).sign();
    }

    /**
     * 解析token，获取用户id
     * @param token token
     * @return 用户id
     */
    public static Long getUserId(String token) {
        JWT jwt = JWT.of(token);
        String userIdStr = String.valueOf(jwt.getPayload("userId"));
        System.out.println("用户Id：" + userIdStr);
        return Long.parseLong(userIdStr);
    }

}
