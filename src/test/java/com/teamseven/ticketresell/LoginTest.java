package com.teamseven.ticketresell;

import org.testng.Assert; // Library for assertions
import org.testng.annotations.DataProvider; // Library for providing data for tests
import org.testng.annotations.Test; // Library to mark methods as tests
import io.restassured.RestAssured; // Library for sending HTTP requests
import io.restassured.response.Response; // Library for handling responses

public class LoginTest {

    // URL của API đăng nhập
    private static final String LOGIN_URL = "http://localhost:8084/api/accounts/login";

    // Cung cấp dữ liệu cho các trường hợp kiểm thử
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
                // Tham số: identifier, mật khẩu, mã trạng thái mong đợi
                {"minhtri10504@gmail.com", "Aa@123456", 200}, // Đăng nhập thành công
                {"trinmse183033@fpt.edu.vn", "wrongpassword", 401}, // Mật khẩu không đúng
                {"", "password123", 400}, // Thiếu identifier
                {"user1@example.com", "", 400}, // Thiếu mật khẩu
                {"invalid@@@@Email", "password123", 400}, // Định dạng email không hợp lệ
                {"user1@example.com", "short", 400}, // Mật khẩu quá ngắn
                {"user3@example.com", "password123", 404}, // Tài khoản không tồn tại
                // Bạn có thể thêm các trường hợp kiểm thử khác ở đây
        };
    }

    // Phương thức kiểm thử cho chức năng đăng nhập
    @Test(dataProvider = "loginData")
    public void testLogin(String identifier, String password, int expectedStatusCode) {
        // Tạo body cho yêu cầu
        String requestBody = String.format("{\"identifier\": \"%s\", \"password\": \"%s\"}", identifier, password);

        // Gửi yêu cầu POST đến API
        Response response = RestAssured.given()
                .contentType("application/json") // Đặt kiểu nội dung là JSON
                .body(requestBody) // Thêm body vào yêu cầu
                .post(LOGIN_URL); // Gửi yêu cầu POST

        // In thông tin phản hồi để kiểm tra
        System.out.println("Response for identifier: " + identifier);
        System.out.println("Response body: " + response.asString());
        System.out.println("Response status code: " + response.getStatusCode());

        // Kiểm tra mã trạng thái HTTP
        try {
            Assert.assertEquals(response.getStatusCode(), expectedStatusCode,
                    String.format("Expected status code: %d, but received: %d for identifier: '%s' and password: '%s'. Response body: %s",
                            expectedStatusCode, response.getStatusCode(), identifier, password, response.asString()));
        } catch (AssertionError e) {
            // In thông điệp lỗi bổ sung nếu xác thực thất bại
            System.err.println("Assertion failed: " + e.getMessage());
            throw e; // Ném lại để TestNG có thể ghi nhận
        }
}

    }