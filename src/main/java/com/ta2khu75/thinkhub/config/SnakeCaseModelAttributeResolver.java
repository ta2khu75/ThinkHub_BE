package com.ta2khu75.thinkhub.config;

import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ta2khu75.thinkhub.shared.anotation.SnakeCaseModelAttribute;


public class SnakeCaseModelAttributeResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SnakeCaseModelAttribute.class);}

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        Class<?> paramType = parameter.getParameterType();
        Object attribute = BeanUtils.instantiateClass(paramType);

        Map<String, String[]> paramMap = webRequest.getParameterMap();
        MutablePropertyValues mpv = new MutablePropertyValues();

        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            String camel = toCamelCase(entry.getKey());
            mpv.add(camel, entry.getValue());
        }

        WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, parameter.getParameterName());
        binder.bind(mpv);
        return attribute;
    }

    private String toCamelCase(String input) {
        StringBuilder sb = new StringBuilder();
        boolean upper = false;
        for (char c : input.toCharArray()) {
            if (c == '_') {
                upper = true;
            } else {
                sb.append(upper ? Character.toUpperCase(c) : Character.toLowerCase(c));
                upper = false;
            }
        }
        return sb.toString();
    }
}

