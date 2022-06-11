package com.example.lyyserverdemo.component;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lyyserverdemo.utils.JsonUtils;
import com.yanzhenjie.andserver.annotation.Converter;
import com.yanzhenjie.andserver.framework.MessageConverter;
import com.yanzhenjie.andserver.framework.body.JsonBody;
import com.yanzhenjie.andserver.http.ResponseBody;
import com.yanzhenjie.andserver.util.IOUtils;
import com.yanzhenjie.andserver.util.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/14 23:20
 * @version 1.0
 */
@Converter
public class AppMessageConverter implements MessageConverter {
    /**
     *服务端 -> 客户端
     * @param output
     * @param mediaType
     * @return
     */
    @Override
    public ResponseBody convert(@Nullable Object output, @Nullable MediaType mediaType) {
        return new JsonBody(JsonUtils.successfulJson(output));
    }

    /**
     * 客户端 -> 服务端
     * @param stream
     * @param mediaType
     * @param type
     * @param <T>
     * @return
     * @throws IOException
     */
    @Nullable
    @Override
    public <T> T convert(@NonNull InputStream stream, @Nullable MediaType mediaType, Type type) throws IOException {
        Charset charset = mediaType == null ? null : mediaType.getCharset();
        if (charset == null) {
            return JsonUtils.parseJson(IOUtils.toString(stream), type);
        }
        return JsonUtils.parseJson(IOUtils.toString(stream, charset), type);
    }
}
