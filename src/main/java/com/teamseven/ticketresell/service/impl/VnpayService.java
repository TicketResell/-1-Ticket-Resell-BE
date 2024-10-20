package com.teamseven.ticketresell.service.impl;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnpayService {

    @Value("${vnpay.tmn_code}")
    private String vnpTmnCode;

    @Value("${vnpay.hash_secret}")
    private String vnpHashSecret;

    @Value("${vnpay.api_url}")
    private String vnpApiUrl;

    @Value("${vnpay.return_url}")
    private String vnpReturnUrl;

    public String createPaymentRequest(Long orderId, double amount) throws UnsupportedEncodingException {
        String vnpVersion = "2.1.0";
        String vnpCommand = "pay";
        String vnpCurrCode = "VND";
        String vnpLocale = "vn";
        String vnpOrderType = "billpayment";
        String vnpTxnRef = String.valueOf(orderId);  // Sử dụng orderId làm mã giao dịch
        String vnpIpAddr = "127.0.0.1";  // IP của client

        Map<String, String> vnpParams = new HashMap<>();
        String vnpBankCode = "NCB";  // Mã ngân hàng mặc định (NCB)
        vnpParams.put("vnp_Amount", String.valueOf((int)(amount * 100)));  // Số tiền tính theo VNĐ (nhân 100)
        vnpParams.put("vnp_BankCode", vnpBankCode);  // Thêm BankCode
        vnpParams.put("vnp_Command", vnpCommand);
        vnpParams.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        vnpParams.put("vnp_CurrCode", vnpCurrCode);
        vnpParams.put("vnp_IpAddr", vnpIpAddr);
        vnpParams.put("vnp_Locale", vnpLocale);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);
        vnpParams.put("vnp_OrderType", vnpOrderType);
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl);
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_Version", vnpVersion);
        System.out.println("Parameters before encoding: " + vnpParams);

        // Xây dựng query string
        String query = buildQueryString(vnpParams);
        System.out.println(query);
        // Tạo HMAC SHA512
        String vnpSecureHash = hmacSHA512(vnpHashSecret, query);
        System.out.println("HMAC SHA512 Hash: " + vnpSecureHash);

        query += "&vnp_SecureHash=" + vnpSecureHash;

        // Ghi log URL đã tạo
        System.out.println("Generated VNPay URL: " + vnpApiUrl + "?" + query);

        return vnpApiUrl + "?" + query;  // Trả về URL thanh toán
    }


    // Hàm tạo query string từ map tham số
    private String buildQueryString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder query = new StringBuilder();
        // Sắp xếp các key theo thứ tự bảng chữ cái
        Map<String, String> sortedParams = new TreeMap<>(params);
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()))
                    .append("&");
        }
        return query.substring(0, query.length() - 1);  // Bỏ dấu '&' cuối cùng
    }


    // Hàm tạo HMAC SHA512
    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] hashBytes = hmac.doFinal(data.getBytes());
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating HMAC SHA512", e);
        }
    }
}


