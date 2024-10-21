package com.teamseven.ticketresell.controller;

import com.teamseven.ticketresell.service.impl.OrderService;
import com.teamseven.ticketresell.service.impl.VnpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.stream.Collectors;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;


@RestController
@RequestMapping("/api/vnpay")
public class VnpayController {

    @Autowired
    private VnpayService vnpayService;

    @Autowired
    private OrderService orderService;

    // Chuỗi ký bí mật từ file cấu hình
    @Value("${vnpay.hash_secret}")
    private String vnpHashSecret;

    @PostMapping("/create-payment")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> request) {
        try {
            Long orderId = Long.parseLong(request.get("orderId").toString());
            double amount = Double.parseDouble(request.get("amount").toString());

            // Gọi dịch vụ để tạo URL thanh toán với VNPay
            String paymentUrl = vnpayService.createPaymentRequest(orderId, amount);
            return ResponseEntity.ok(paymentUrl);  // Trả về URL để frontend chuyển hướng người dùng đến VNPay
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.badRequest().body("Error occurred: " + e.getMessage());
        }
    }

    // VNPay callback handler
    @GetMapping("/callback")
    public ResponseEntity<?> vnpayCallback(@RequestParam Map<String, String> vnpayResponse) {
        try {
            String vnpResponseCode = vnpayResponse.get("vnp_ResponseCode");
            String vnpTransactionNo = vnpayResponse.get("vnp_TransactionNo");
            String vnpSecureHash = vnpayResponse.get("vnp_SecureHash");
            // Kiểm tra chữ ký bảo mật
//            String rawData = buildRawData(vnpayResponse);
//            System.out.println("RawData: " + rawData);
//            String generatedSecureHash = hmacSHA512(vnpHashSecret, rawData);
//            System.out.println("Generated SecureHash: " + generatedSecureHash);
//            System.out.println("vnp_SecureHash: " + vnpSecureHash);
//            if (!hmacSHA512(vnpHashSecret, rawData).equals(vnpSecureHash)) {
//                throw new IllegalArgumentException("Invalid signature!");
//            }
            // Kiểm tra mã phản hồi của VNPay
            Long orderId = null;
            if ("00".equals(vnpResponseCode)) {
                // Thanh toán thành công, cập nhật trạng thái đơn hàng
                orderId = Long.parseLong(vnpayResponse.get("vnp_TxnRef"));
                orderService.updatePaymentStatus(
                        orderId, "paid", vnpResponseCode, vnpTransactionNo);
                return ResponseEntity.ok("Payment success!");
            } else {
                orderService.updatePaymentStatus(orderId, "failed",vnpResponseCode,vnpTransactionNo);
                return ResponseEntity.badRequest().body("Payment failed with code: " + vnpResponseCode);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }

    // Hàm tạo chuỗi rawData để kiểm tra chữ ký bảo mật
    private String buildRawData(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(e -> !e.getKey().equals("vnp_SecureHash"))  // Loại bỏ vnp_SecureHash ra khỏi raw data
                .sorted(Map.Entry.comparingByKey())                 // Sắp xếp theo key
                .map(e -> e.getKey() + "=" + e.getValue())          // Gán key=value
                .collect(Collectors.joining("&"));                  // Kết hợp lại bằng dấu &
    }

    // Hàm mã hóa HMAC SHA512
    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] hashBytes = hmac.doFinal(data.getBytes());  // Sử dụng mã hóa UTF-8
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));  // Chuyển đổi thành chuỗi hexa
            }
            return hashString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while generating HMAC SHA512", e);
        }
    }
}