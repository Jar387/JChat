package com.jar36.jchat.packet;

import com.alibaba.fastjson.JSON;

public class JsonSerializer {
    public byte[] serialize(Object o) {
        return JSON.toJSONBytes(o);
    }

    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
