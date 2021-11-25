package com.ziroom.tech.demeterapi.open.common.controller;

import com.ziroom.tech.demeterapi.open.common.enums.ResponseEnum;
import com.ziroom.tech.demeterapi.open.common.model.ModelResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author xuzeyu
 **/
@Slf4j
public class BaseController {

    /**
     * validate params
     */
    public ModelResponse validParams(BindingResult bindingResult) {
        ModelResponse response = new ModelResponse();
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            String code = processBindingError(fieldError);
            response.setResultCode(code);
            response.setSuccess(false);
            response.setResultMessage(fieldError.getDefaultMessage());
        }
        return response;
    }

    /**
     * 统一
     */
    private String processBindingError(FieldError fieldError) {
        String code = fieldError.getCode();
        switch (code) {
            case "NotEmpty":
                return ResponseEnum.PARAM_EMPTY_CODE.getCode();
            case "NotBlank":
                return ResponseEnum.PARAM_EMPTY_CODE.getCode();
            case "NotNull":
                return ResponseEnum.PARAM_EMPTY_CODE.getCode();
            case "Pattern":
                return ResponseEnum.PARAM_PATTERN_ERROR_CODE.getCode();
            case "Min":
                return ResponseEnum.PARAM_RANGE_CODE.getCode();
            case "Max":
                return ResponseEnum.PARAM_RANGE_CODE.getCode();
            case "Length":
                return ResponseEnum.PARAM_LENGTH_LIMIT_CODE.getCode();
            case "Range":
                return ResponseEnum.PARAM_LENGTH_LIMIT_CODE.getCode();
            case "Email":
                return ResponseEnum.PARAM_PATTERN_ERROR_CODE.getCode();
            case "DecimalMin":
                return ResponseEnum.PARAM_LENGTH_LIMIT_CODE.getCode();
            case "DecimalMax":
                return ResponseEnum.PARAM_LENGTH_LIMIT_CODE.getCode();
            case "Size":
                return ResponseEnum.PARAM_LENGTH_LIMIT_CODE.getCode();
            case "URL":
                return ResponseEnum.PARAM_PATTERN_ERROR_CODE.getCode();
            default:
                return ResponseEnum.PARAM_ERROR_CODE.getCode();
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<T, ?> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
