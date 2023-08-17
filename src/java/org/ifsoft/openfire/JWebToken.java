package org.ifsoft.openfire;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jivesoftware.util.*;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import org.apache.commons.codec.binary.Base64;
import java.util.List;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import net.sf.json.*;

/**
 *
 * @author user
 */
public class JWebToken {
    private static final Logger Log = LogManager.getLogger(JWebToken.class);
    private static final String SECRET_KEY = JiveGlobals.getProperty("sparkweb.jwt.secret.key", "H7pCkktUl5KyPCZ7CKw09y1j460tfIv4dRcS1XstUKY"); 
    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private JSONObject payload = new JSONObject();
    private String signature;
    private String encodedHeader;

    private JWebToken() {
        encodedHeader = encode(new JSONObject(JWT_HEADER));
    }

    public JWebToken(JSONObject payload) {
        this();
		this.payload = payload;
        signature = hmacSha256(encodedHeader + "." + encode(payload), SECRET_KEY);		
    }

    @Override
    public String toString() {
        return encodedHeader + "." + encode(payload) + "." + signature;
    }
	
    private static String encode(JSONObject obj) {
        return encode(obj.toString().getBytes(StandardCharsets.UTF_8));
    }	

    private static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64URLSafe(bytes));		
    }

    /**
     * Sign with HMAC SHA256 (HS256)
     *
     * @param data
     * @return
     * @throws Exception
     */
    private String hmacSha256(String data, String secret) {
        try {
            byte[] hash = java.util.Base64.getUrlDecoder().decode(secret); //secret.getBytes(StandardCharsets.UTF_8);

            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);

            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return encode(signedBytes);
        } catch (Exception ex) {
            Log.error("hmacSha256", ex);
            return null;
        }
    }

}