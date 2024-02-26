package com.wenthor.service;

import com.wenthor.model.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(Notification notification){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true, "UTF-8");
            mimeMessageHelper.setTo(notification.getUserMail());
            mimeMessageHelper.setSubject(notification.getUserFullName().concat(" Digitopia Hesabını Onayla!"));
            mimeMessageHelper.setText(mailBodyGenerate(notification),true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            //..
        }

    }
    private String mailBodyGenerate(Notification notification){
        StringBuilder sb = new StringBuilder();
        sb.append("<html>")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<title>Hesabınızı Onaylayın</title>")
                .append("<style>")
                .append("body {")
                .append("font-family: Arial, sans-serif;")
                .append("background-color: #f4f4f4;")
                .append("margin: 0;")
                .append("padding: 0;")
                .append("}")
                .append(".container {")
                .append("max-width: 600px;")
                .append("margin: 20px auto;")
                .append("padding: 20px;")
                .append("background-color: #fff;")
                .append("border-radius: 5px;")
                .append("box-shadow: 0 2px 4px rgba(0,0,0,0.1);")
                .append("}")
                .append("h1 {")
                .append("color: #333;")
                .append("}")
                .append("p {")
                .append("margin-bottom: 20px;")
                .append("color: #666;")
                .append("}")
                .append("a {")
                .append("color: #007bff;")
                .append("text-decoration: none;")
                .append("}")
                .append(".footer {")
                .append("margin-top: 20px;")
                .append("border-top: 1px solid #ccc;")
                .append("padding-top: 10px;")
                .append("font-size: 12px;")
                .append("color: #666;")
                .append("}")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<div class=\"container\">")
                .append("<h1>Hesabınızı Onaylayın</h1>")
                .append("<p>Merhaba ").append(notification.getUserFullName()).append(",</p>")
                .append("<p>Hesabınızı aktifleştirmek için <a href=\"").append(notification.getVerificationURL()).append("\">buraya tıklayabilirsiniz</a>.</p>")
                .append("<p>Eğer linke tıklamakta sorun yaşıyorsanız, aşağıdaki linke kopyalayıp tarayıcınızın adres çubuğuna yapıştırabilirsiniz:</p>")
                .append("<p>").append(notification.getVerificationURL()).append("</p>")
                .append("<p>İyi günler dileriz,<br>").append("DIGITOPIA").append("</p>")
                .append("<div class=\"footer\">")
                .append("<p>Bu mail, ").append("DIGITOPIA").append(" tarafından gönderilmiştir. Herhangi bir sorunuz varsa lütfen <a href=\"mailto:me@").append("emrecelen").append(".com.tr\">mail@").append("emrecelen").append(".com.tr</a> adresine mail atınız.</p>")
                .append("</div>")
                .append("</div>")
                .append("</body>")
                .append("</html>");
        return sb.toString();
    }
}
