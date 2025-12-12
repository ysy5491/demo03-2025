package com.ll.demo03.global.exceptionHandlers;

import com.ll.demo03.global.exceptions.GlobalException;
import com.ll.demo03.global.rq.Rq;
import com.ll.demo03.global.rsData.RsData;
import com.ll.demo03.standard.dto.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final Rq rq;

//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public String handleException(Exception e) {
//        // 어차피 이 서버(스프링부트)를 API서버로만 이용할 것이므로 이 코드는 필요없다.
//        // 그리고 isApi의 로직은 조금 더 보강을 해야한다.
//        // if (!rq.isApi()) throw e;
//        log.debug("handleException 1");
//        return e.getMessage();
//    }

    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    // ResponseEntity의 타입 지정은 바디에 들어가는 타입
    public ResponseEntity<RsData<Empty>> handleException(GlobalException e) {
        log.debug("handleException Started!");

        // 어차피 이 서버(스프링부트)를 API서버로만 이용할 것이므로 이 코드는 필요없다.
        // 그리고 isApi의 로직은 조금 더 보강을 해야한다.
        // if (!rq.isApi()) throw e;
        RsData<Empty> rsData = e.getRsData();

        return ResponseEntity
                .status(rsData.getStatusCode())
                .body(rsData);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    // ResponseEntity의 타입 지정은 바디에 들어가는 타입
    public ResponseEntity<RsData<Empty>> handleException(MethodArgumentNotValidException e) {
        String resultCode = "400-" + e.getBindingResult().getFieldError().getCode();
        String msg = e.getBindingResult().getFieldError().getField() + " : " + e.getBindingResult().getFieldError().getDefaultMessage();
        return handleException(
                new GlobalException(
                        resultCode, msg
                )
        );
    }

    // 자연스럽게 발생시킨 예외처리
    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleApiException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("resultCode", "500-1");
        body.put("statusCode", 500);
        body.put("msg", ex.getLocalizedMessage());

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        body.put("data", data);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        data.put("trace", sw.toString().replace("\t", "    ").split("\\r\\n"));

        String path = rq.getCurrentUrlPath();
        data.put("path", path);

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
