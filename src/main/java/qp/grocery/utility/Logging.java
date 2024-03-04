package qp.grocery.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Logging {

    public static final Logger logMain = LogManager.getLogger("main");

    public static void getUrl() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request.getMethod().equals(HttpMethod.POST.toString()) || request.getMethod().equals(HttpMethod.PUT.toString())){
            getPostRequestUrl();
        }else{
            logMain.info(request.getMethod() + " " + request.getRequestURL() + "?" + request.getQueryString());
        }
    }

    public static void getPostRequestUrl(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            params.append(key).append("=");

            for (String value : values) {
                params.append(value);
            }

            params.append("&");

        }
        logMain.info(request.getMethod() + " " + request.getRequestURL() + "?" + params);
    }
}