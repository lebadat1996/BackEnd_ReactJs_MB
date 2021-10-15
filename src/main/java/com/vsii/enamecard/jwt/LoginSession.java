package com.vsii.enamecard.jwt;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class LoginSession  {

    private JedisPool jedisPool;

    @Autowired
    public LoginSession(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void saveLoginSession(String id, String token, Instant expireMoment) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(id, token);
            jedis.expireAt(id, expireMoment.toEpochMilli());
        }
    }

    public void revokeLoginSession(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(id);
        }
    }

    public void revokeAllLoginSession(List<Integer> userIdList) {
        if(userIdList.isEmpty()) return;
        userIdList.forEach(id -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.del(id.toString());
            }
        });
    }

    public String getToken(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jwtToken = jedis.get(id);
            return Strings.isEmpty(jwtToken) ? "" : jwtToken;
        }
    }
}
