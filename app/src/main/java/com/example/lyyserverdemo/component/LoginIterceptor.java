package com.example.lyyserverdemo.component;

import androidx.annotation.NonNull;

import com.yanzhenjie.andserver.annotation.Interceptor;
import com.yanzhenjie.andserver.error.HttpException;
import com.yanzhenjie.andserver.framework.HandlerInterceptor;
import com.yanzhenjie.andserver.framework.handler.MethodHandler;
import com.yanzhenjie.andserver.framework.handler.RequestHandler;
import com.yanzhenjie.andserver.framework.mapping.Addition;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.session.Session;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 拦截器
 * @author Yingyong Lao
 * 创建时间 2022/5/14 23:06
 * @version 1.0
 */
@Interceptor
public class LoginIterceptor implements HandlerInterceptor {
    public static final String LOGIN_ATTRIBUTE = "USER.LOGIN.SIGN";
    @Override
    public boolean onIntercept(@NonNull HttpRequest request, @NonNull HttpResponse response, @NonNull RequestHandler handler) throws Exception {
        if (handler instanceof MethodHandler){
            MethodHandler methodHandler= (MethodHandler) handler;
            Addition addition = methodHandler.getAddition();
            if (!isLogin(request,addition)){
                throw new HttpException(401,"您尚未登录，无法访问！");
            }
        }
        return false;
    }

    private boolean isNeedLogin(Addition addition) {
        if (addition == null) {
            return false;
        }

        String[] stringType = addition.getStringType();
        if (ArrayUtils.isEmpty(stringType)) {
            return false;
        }

        boolean[] booleanType = addition.getBooleanType();
        if (ArrayUtils.isEmpty(booleanType)) {
            return false;
        }
        return stringType[0].equalsIgnoreCase("login") && booleanType[0];
    }

    private boolean isLogin(HttpRequest request, Addition addition) {
        if (isNeedLogin(addition)) {
            Session session = request.getSession();
            if (session != null) {
                Object o = session.getAttribute(LOGIN_ATTRIBUTE);
                return o instanceof Boolean && (boolean) o;
            }
            return false;
        }
        return true;
    }
}
