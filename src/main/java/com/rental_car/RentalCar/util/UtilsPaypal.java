package com.rental_car.RentalCar.util;

import jakarta.servlet.http.HttpServletRequest;

public class UtilsPaypal {
    public static String getBaseURL(HttpServletRequest request) {
        String schema = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuffer url = new StringBuffer();
        url.append(schema).append("://").append(serverName);
        if (serverPort!=80 && serverPort!=443) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        if (url.toString().endsWith("/")){
            url.append("/");
        }
        return url.toString();
    }
}
