package com.ziroom.tech.demeterapi.open.utils;

import com.alibaba.fastjson.JSONObject;
import com.ziroom.tech.demeterapi.open.common.enums.ResponseEnum;
import com.ziroom.tech.demeterapi.open.common.model.ModelResult;
import com.ziroom.tech.demeterapi.open.common.utils.ModelResultUtil;
import com.ziroom.tech.demeterapi.open.login.model.JwtSubjectModel;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;

/**
 * @author xuzeyu
 */
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final String JWT_SECRET = "&%Sk0a01@#U^S";
    private static final String JWT_ID = "demeter-api";
    private static final long SOP_TTL = 1800000L;

    public JwtUtils() {
    }

    public static String createDemeterJWT(String subject) throws UnsupportedEncodingException {
        return createJWT("demeter-api", subject, "&%Sk0a01@#U^S", -1L);
    }

    public static ModelResult<JwtSubjectModel> parseJWT(String jwtStr) {
        return parseJWT("demeter-api", jwtStr, "&%Sk0a01@#U^S");
    }

    public static String createJWT(String id, String subject, String jwtSecret, long ttlMillis) throws UnsupportedEncodingException {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey(jwtSecret);
        JwtBuilder builder = Jwts.builder().setId(id).setSubject(subject).setIssuer("user").setIssuedAt(now).signWith(signatureAlgorithm, secretKey);
        if (ttlMillis >= 0L) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate);
        }

        return builder.compact();
    }

    public static ModelResult<JwtSubjectModel> parseJWT(String id, String jwtStr, String jwtSecret) {
        try {
            Claims claims = validateJWT(jwtStr, jwtSecret);
            if (!StringUtils.isBlank(claims.getSubject()) && claims.getId().equals(id)) {
                JwtSubjectModel jwtSubjectModel = (JwtSubjectModel) JSONObject.parseObject(claims.getSubject(), JwtSubjectModel.class);
                return jwtSubjectModel != null && !StringUtils.isBlank(jwtSubjectModel.getCode()) ? ModelResultUtil.success(jwtSubjectModel) : ModelResultUtil.error(ResponseEnum.FRONT_LOGIN_USER_TOKEN_WRONG.getCode(), ResponseEnum.FRONT_LOGIN_USER_TOKEN_WRONG.getMessage());
            } else {
                return ModelResultUtil.error(ResponseEnum.FRONT_LOGIN_USER_TOKEN_SIG_WRONG.getCode(), ResponseEnum.FRONT_LOGIN_USER_TOKEN_SIG_WRONG.getMessage());
            }
        } catch (ExpiredJwtException var5) {
            return ModelResultUtil.error(ResponseEnum.FRONT_LOGIN_USER_TOKEN_EXPIRE.getCode(), ResponseEnum.FRONT_LOGIN_USER_TOKEN_EXPIRE.getMessage());
        } catch (Exception var6) {
            return ModelResultUtil.error(ResponseEnum.FRONT_LOGIN_USER_TOKEN_WRONG.getCode(), ResponseEnum.FRONT_LOGIN_USER_TOKEN_WRONG.getMessage());
        }
    }

    private static SecretKey generalKey(String jwtSecret) throws UnsupportedEncodingException {
        try {
            byte[] encode = BASE64_ENCODER.encode(jwtSecret.getBytes("utf-8"));
            SecretKey key = new SecretKeySpec(encode, 0, encode.length, "AES");
            return key;
        } catch (Throwable var3) {
            throw var3;
        }
    }

    private static Claims validateJWT(String jwt, String jwtSecret) throws UnsupportedEncodingException {
        SecretKey secretKey = generalKey(jwtSecret);
        return (Claims)Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
    }
}
