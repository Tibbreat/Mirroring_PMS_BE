package sep490.g13.pms_be.utils;

public class MailingText {
    public static String htmlSendingCode(String code,String emailSender, String account) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Đặt lại mật khẩu</title>" +
                "    <style>" +
                "        body {" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;" +
                "            background-color: #f3f4f6;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "        }" +
                "        .container {" +
                "            max-width: 600px;" +
                "            margin: 30px auto;" +
                "            background-color: #ffffff;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);" +
                "            overflow: hidden;" +
                "        }" +
                "        .header {" +
                "            background-color: #007bff;" +
                "            color: #ffffff;" +
                "            text-align: center;" +
                "            padding: 30px 20px;" +
                "        }" +
                "        .header h1 {" +
                "            margin: 0;" +
                "            font-size: 24px;" +
                "        }" +
                "        .content {" +
                "            padding: 20px;" +
                "            text-align: center;" +
                "            color: #333333;" +
                "        }" +
                "        .content p {" +
                "            margin: 10px 0;" +
                "            font-size: 16px;" +
                "            line-height: 1.5;" +
                "        }" +
                "        .account {" +
                "            font-size: 20px;" +
                "            font-weight: bold;" +
                "            color: #007bff;" +
                "        }" +
                "        .code {" +
                "            display: inline-block;" +
                "            background-color: #f8f9fa;" +
                "            border: 1px dashed #007bff;" +
                "            color: #333333;" +
                "            font-size: 20px;" +
                "            font-weight: bold;" +
                "            padding: 15px 30px;" +
                "            margin: 20px 0;" +
                "            border-radius: 6px;" +
                "        }" +
                "        .footer {" +
                "            background-color: #f9f9f9;" +
                "            text-align: center;" +
                "            padding: 20px;" +
                "            color: #555555;" +
                "            font-size: 14px;" +
                "        }" +
                "        .footer a {" +
                "            color: #007bff;" +
                "            text-decoration: none;" +
                "        }" +
                "        .footer a:hover {" +
                "            text-decoration: underline;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <h1>Đặt Lại Mật Khẩu</h1>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <p>Xin chào,</p>" +
                "            <p class=\"account\">" + account + "</p>" +
                "            <p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu từ bạn. Vui lòng sử dụng mã bên dưới để tiếp tục:</p>" +
                "            <div class=\"code\">" + code + "</div>" +
                "            <p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.</p>" +
                "            <p>Nếu có bất kỳ thắc mắc nào, vui lòng <a href=\"mailto:" + emailSender + "\">liên hệ bộ phận hỗ trợ</a>.</p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>&copy; 2024 PMS, Trường Mầm non Đông Phương Yên.</p>" +
                "            <p><a href=\"http://157.66.27.65:3000/pms/auth/login\">Truy cập website của chúng tôi</a></p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}
