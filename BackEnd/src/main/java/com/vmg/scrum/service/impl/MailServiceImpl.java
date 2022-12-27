package com.vmg.scrum.service.impl;


import com.vmg.scrum.model.User;
import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.repository.UserRepository;
import com.vmg.scrum.security.UserDetailsServiceImpl;
import com.vmg.scrum.security.jwt.HashOneWay;
import com.vmg.scrum.security.jwt.JwtUtils;
import com.vmg.scrum.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import java.util.Set;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Value("${application.domain.cors}")
    private String domain ;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    HashOneWay passwordEncoder;

    public MailServiceImpl(JavaMailSender mailSender, UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }
    private static String alphaNumericString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
@Override
    public Boolean sendEmail(String recipientEmail) {
    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("VMG@contact.com", "VMG");
        helper.setTo(recipientEmail);
        String subject = "Here's the link to reset your password";
        String emailToken = jwtUtils.generateJwtTokenEmail(recipientEmail);
        String resetPasswordLink = domain + "/reset_password-tokenLink?token=" +emailToken;
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + resetPasswordLink + "\">Click here to direct change your password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    } catch (Exception e){
        return false;
    }
    }
    
    @Override
    public void sendEmailAccountInfo(String recipientEmail, String rootPassword)
            throws MessagingException, UnsupportedEncodingException {
         MimeMessage message = mailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(message);

         helper.setFrom("VMG@mailnotifi", "VMG");
         helper.setTo(recipientEmail);
         String subject = "Here's the info account";
         String content = "<p>Hello,</p>"
                 + "<p>You have to logIn and change your password.</p>"
                 + "Here is account infomation :"
                 + "<p> Email:yourEmail </p>"
                 + "<br>"
                 + "<p> Password:" + rootPassword + "</p>";

         helper.setSubject(subject);

         helper.setText(content, true);

         mailSender.send(message);

    }

    @Override
    public void sendEmailFollowers(Set<String> recipientEmail, String title, String department, User fullName, Request totalsDay, LocalTime timeStart, LocalDate dateFrom, LocalTime timeEnd, LocalDate dateTo) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String requestLink = "http://192.168.45.50:9999" + "/managerequest";
        String[] multipleEmailIds = recipientEmail.toArray(new String[0]);
        helper.setFrom("VMG@mailnotifi", "VMG");
        helper.setTo(multipleEmailIds);
        String subject = "[Request]"+ fullName.getFullName() +" created a new request Đơn xin " + totalsDay.getCatergoryRequest().getName() + " that you followed";
        String content = "<p> Họ và tên: "+ fullName.getFullName() + "_Bộ phận: "+ department + "</p>"
                           +
                         "<p>Số ngày nghỉ: "+ totalsDay.getTotalDays() + " ngày từ: "+ timeStart + " ngày " + dateFrom + " đến " + timeEnd + " ngày " + dateTo + " " + "</p>"
                           +
                         "<p> Lý do: "+ title + " "+ "</p>"
                           +
                         "<p> Truy cập link: <a href=\"" + requestLink + "\">để xem chi tiết</a></p>"
                           +
                         "<table\n" +
                "  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"color: rgb(34, 34, 34); font-family: 'Times New Roman'; font-size: 9pt\"\n" +
                ">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td\n" +
                "        style=\"\n" +
                "          width: 652px;\n" +
                "          padding-top: 5px;\n" +
                "          padding-bottom: 8px;\n" +
                "          font-size: 9pt;\n" +
                "        \"\n" +
                "      >\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                "          <tbody>\n" +
                "            <tr>\n" +
                "              <td style=\"width: 100px; padding-left: 5px; padding-right: 5px\">\n" +
                "                <img\n" +
                "                  src=\"https://ci3.googleusercontent.com/proxy/7ODHXmr4KB__QBF5zf6OEIVvi0GqevmXzbOSE4Kr_Y05vTqtHhQ-e0dg0EDfkFL-c3chEl6WK0LwObV1KSyDCiA9sD7q-Qu45QT-TLy7ctWVe5zPJkSqimZOj8r-sZ2wBHHM32I_1SBOP0aJVUrRafq_4gMjwKHHO3f0wEnxqQETKgb-U0M7QtkK6kC10EXG3ynBXXJTahdM_hsMxw=s0-d-e1-ft#https://docs.google.com/uc?export=download&amp;id=109TlDyQtyW6COZ4_NOSQZAzhhwCgJGcs&amp;revid=0B8WbkXGKBxnQSVRqR3FHQlpIQ2tkdHBnRGU0OGRueUdtNjhzPQ\"\n" +
                "                  width=\"200\"\n" +
                "                  height=\"97\"\n" +
                "                  style=\"font-family: 'Times New Roman', serif; font-size: 16px\"\n" +
                "                  class=\"CToWUd\"\n" +
                "                  data-bit=\"iit\"\n" +
                "                />\n" +
                "              </td>\n" +
                "              <td></td>\n" +
                "              <td>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <br />\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 11.7px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >---------------------------</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <b\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-family: 'Times New Roman';\n" +
                "                        color: rgb(31, 73, 125);\n" +
                "                        font-size: 10pt;\n" +
                "                      \"\n" +
                "                      >VMG MEDIA GROUP</span\n" +
                "                    ></b\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >A : 6F, Peakview tower,&nbsp;36 Hoang Cau Street, Ha Noi,\n" +
                "                    Viet Nam</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >P : 84-4-35378820&nbsp;&nbsp;<b style=\"color: black\">|</b\n" +
                "                    >&nbsp; F:&nbsp;&nbsp;84-4-37726092&nbsp;&nbsp;<b\n" +
                "                      style=\"color: black\"\n" +
                "                      >|</b\n" +
                "                    >&nbsp;&nbsp;W: &nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"http://vmgmedia.vn/\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=http://vmgmedia.vn/&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw3-XufeUzUXBbaYLEwkO-hZ\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: 'times new roman';\n" +
                "                      \"\n" +
                "                      >vmgmedia.vn</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >FP:&nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"https://www.facebook.com/VMGMedia\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=https://www.facebook.com/VMGMedia&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw2O8jtVN6VNghigB6Yexax5\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: ' times new roman', ' serif';\n" +
                "                      \"\n" +
                "                      >https://www.facebook.com/<wbr />VMGMedia</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </tbody>\n" +
                "        </table>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; height: 1px\"></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; text-align: justify; padding-top: 5px\">\n" +
                "        <i\n" +
                "          ><span style=\"font-family: 'Times New Roman'\"\n" +
                "            >This email may contain confidential or proprietary information of\n" +
                "            VMG JSC. Its disclosure is strictly limited to the intended\n" +
                "            recipient by the sender. Any review, reliance or distribution by\n" +
                "            others, disclosure or forwarding without express permission from the\n" +
                "            sender is strictly prohibited. If you have received the message in\n" +
                "            error, please immediately contact the sender by reply email and\n" +
                "            delete the email and any attachments without reading or saving in\n" +
                "            any manner.</span\n" +
                "          ></i\n" +
                "        >\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }


    @Override
    public void sendEmailFollowersForget(Set<String> recipientEmail, String title, String department, Request totalsDay, User fullName, LocalDate dateForget) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String requestLink = "http://192.168.45.50:9999" + "/managerequest";
        String[] multipleEmailIds = recipientEmail.toArray(new String[0]);
        helper.setFrom("VMG@mailnotifi", "VMG");
        helper.setTo(multipleEmailIds);
        String subject = "[Request]"+ fullName.getFullName() +" created a new request Đơn xin " + totalsDay.getCatergoryRequest().getName() + " that you followed";
        String content = "<p> Họ và tên: "+ fullName.getFullName() + "_Bộ phận: "+ department + "</p>"
                +
                "<p>Ngày quên chấm công: "+ dateForget + "</p>"
                +
                "<p> Lý do: "+ title + " "+ "</p>"
                +
                "<p> Truy cập link: <a href=\"" + requestLink + "\">để xem chi tiết</a></p>"
                +
                "<table\n" +
                "  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"color: rgb(34, 34, 34); font-family: 'Times New Roman'; font-size: 9pt\"\n" +
                ">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td\n" +
                "        style=\"\n" +
                "          width: 652px;\n" +
                "          padding-top: 5px;\n" +
                "          padding-bottom: 8px;\n" +
                "          font-size: 9pt;\n" +
                "        \"\n" +
                "      >\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                "          <tbody>\n" +
                "            <tr>\n" +
                "              <td style=\"width: 100px; padding-left: 5px; padding-right: 5px\">\n" +
                "                <img\n" +
                "                  src=\"https://ci3.googleusercontent.com/proxy/7ODHXmr4KB__QBF5zf6OEIVvi0GqevmXzbOSE4Kr_Y05vTqtHhQ-e0dg0EDfkFL-c3chEl6WK0LwObV1KSyDCiA9sD7q-Qu45QT-TLy7ctWVe5zPJkSqimZOj8r-sZ2wBHHM32I_1SBOP0aJVUrRafq_4gMjwKHHO3f0wEnxqQETKgb-U0M7QtkK6kC10EXG3ynBXXJTahdM_hsMxw=s0-d-e1-ft#https://docs.google.com/uc?export=download&amp;id=109TlDyQtyW6COZ4_NOSQZAzhhwCgJGcs&amp;revid=0B8WbkXGKBxnQSVRqR3FHQlpIQ2tkdHBnRGU0OGRueUdtNjhzPQ\"\n" +
                "                  width=\"200\"\n" +
                "                  height=\"97\"\n" +
                "                  style=\"font-family: 'Times New Roman', serif; font-size: 16px\"\n" +
                "                  class=\"CToWUd\"\n" +
                "                  data-bit=\"iit\"\n" +
                "                />\n" +
                "              </td>\n" +
                "              <td></td>\n" +
                "              <td>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <br />\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 11.7px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >---------------------------</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <b\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-family: 'Times New Roman';\n" +
                "                        color: rgb(31, 73, 125);\n" +
                "                        font-size: 10pt;\n" +
                "                      \"\n" +
                "                      >VMG MEDIA GROUP</span\n" +
                "                    ></b\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >A : 6F, Peakview tower,&nbsp;36 Hoang Cau Street, Ha Noi,\n" +
                "                    Viet Nam</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >P : 84-4-35378820&nbsp;&nbsp;<b style=\"color: black\">|</b\n" +
                "                    >&nbsp; F:&nbsp;&nbsp;84-4-37726092&nbsp;&nbsp;<b\n" +
                "                      style=\"color: black\"\n" +
                "                      >|</b\n" +
                "                    >&nbsp;&nbsp;W: &nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"http://vmgmedia.vn/\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=http://vmgmedia.vn/&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw3-XufeUzUXBbaYLEwkO-hZ\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: 'times new roman';\n" +
                "                      \"\n" +
                "                      >vmgmedia.vn</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >FP:&nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"https://www.facebook.com/VMGMedia\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=https://www.facebook.com/VMGMedia&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw2O8jtVN6VNghigB6Yexax5\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: ' times new roman', ' serif';\n" +
                "                      \"\n" +
                "                      >https://www.facebook.com/<wbr />VMGMedia</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </tbody>\n" +
                "        </table>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; height: 1px\"></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; text-align: justify; padding-top: 5px\">\n" +
                "        <i\n" +
                "          ><span style=\"font-family: 'Times New Roman'\"\n" +
                "            >This email may contain confidential or proprietary information of\n" +
                "            VMG JSC. Its disclosure is strictly limited to the intended\n" +
                "            recipient by the sender. Any review, reliance or distribution by\n" +
                "            others, disclosure or forwarding without express permission from the\n" +
                "            sender is strictly prohibited. If you have received the message in\n" +
                "            error, please immediately contact the sender by reply email and\n" +
                "            delete the email and any attachments without reading or saving in\n" +
                "            any manner.</span\n" +
                "          ></i\n" +
                "        >\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public void sendEmailFollowersTCS(Set<String> recipientEmail, String title, String department,Request totalsDay, User fullName, LocalDate dateFrom, LocalDate dateTo) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String requestLink = "http://192.168.45.50:9999" + "/managerequest";
        String[] multipleEmailIds = recipientEmail.toArray(new String[0]);
        helper.setFrom("VMG@mailnotifi", "VMG");
        helper.setTo(multipleEmailIds);
        String subject = "[Request]"+ fullName.getFullName() +" created a new request Đơn xin " + totalsDay.getCatergoryRequest().getName() + " that you followed";
        String content = "<p> Họ và tên: "+ fullName.getFullName() + "_Bộ phận: "+ department + "</p>"
                +
                "<p>Nghỉ từ: "+ dateFrom + " đến " + dateTo + "</p>"
                +
                "<p> Lý do: "+ title + " "+ "</p>"
                +
                "<p> Truy cập link: <a href=\"" + requestLink + "\">để xem chi tiết</a></p>"
                +
                "<table\n" +
                "  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"color: rgb(34, 34, 34); font-family: 'Times New Roman'; font-size: 9pt\"\n" +
                ">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td\n" +
                "        style=\"\n" +
                "          width: 652px;\n" +
                "          padding-top: 5px;\n" +
                "          padding-bottom: 8px;\n" +
                "          font-size: 9pt;\n" +
                "        \"\n" +
                "      >\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                "          <tbody>\n" +
                "            <tr>\n" +
                "              <td style=\"width: 100px; padding-left: 5px; padding-right: 5px\">\n" +
                "                <img\n" +
                "                  src=\"https://ci3.googleusercontent.com/proxy/7ODHXmr4KB__QBF5zf6OEIVvi0GqevmXzbOSE4Kr_Y05vTqtHhQ-e0dg0EDfkFL-c3chEl6WK0LwObV1KSyDCiA9sD7q-Qu45QT-TLy7ctWVe5zPJkSqimZOj8r-sZ2wBHHM32I_1SBOP0aJVUrRafq_4gMjwKHHO3f0wEnxqQETKgb-U0M7QtkK6kC10EXG3ynBXXJTahdM_hsMxw=s0-d-e1-ft#https://docs.google.com/uc?export=download&amp;id=109TlDyQtyW6COZ4_NOSQZAzhhwCgJGcs&amp;revid=0B8WbkXGKBxnQSVRqR3FHQlpIQ2tkdHBnRGU0OGRueUdtNjhzPQ\"\n" +
                "                  width=\"200\"\n" +
                "                  height=\"97\"\n" +
                "                  style=\"font-family: 'Times New Roman', serif; font-size: 16px\"\n" +
                "                  class=\"CToWUd\"\n" +
                "                  data-bit=\"iit\"\n" +
                "                />\n" +
                "              </td>\n" +
                "              <td></td>\n" +
                "              <td>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <br />\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 11.7px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >---------------------------</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <b\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-family: 'Times New Roman';\n" +
                "                        color: rgb(31, 73, 125);\n" +
                "                        font-size: 10pt;\n" +
                "                      \"\n" +
                "                      >VMG MEDIA GROUP</span\n" +
                "                    ></b\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >A : 6F, Peakview tower,&nbsp;36 Hoang Cau Street, Ha Noi,\n" +
                "                    Viet Nam</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >P : 84-4-35378820&nbsp;&nbsp;<b style=\"color: black\">|</b\n" +
                "                    >&nbsp; F:&nbsp;&nbsp;84-4-37726092&nbsp;&nbsp;<b\n" +
                "                      style=\"color: black\"\n" +
                "                      >|</b\n" +
                "                    >&nbsp;&nbsp;W: &nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"http://vmgmedia.vn/\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=http://vmgmedia.vn/&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw3-XufeUzUXBbaYLEwkO-hZ\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: 'times new roman';\n" +
                "                      \"\n" +
                "                      >vmgmedia.vn</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >FP:&nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"https://www.facebook.com/VMGMedia\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=https://www.facebook.com/VMGMedia&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw2O8jtVN6VNghigB6Yexax5\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: ' times new roman', ' serif';\n" +
                "                      \"\n" +
                "                      >https://www.facebook.com/<wbr />VMGMedia</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </tbody>\n" +
                "        </table>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; height: 1px\"></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; text-align: justify; padding-top: 5px\">\n" +
                "        <i\n" +
                "          ><span style=\"font-family: 'Times New Roman'\"\n" +
                "            >This email may contain confidential or proprietary information of\n" +
                "            VMG JSC. Its disclosure is strictly limited to the intended\n" +
                "            recipient by the sender. Any review, reliance or distribution by\n" +
                "            others, disclosure or forwarding without express permission from the\n" +
                "            sender is strictly prohibited. If you have received the message in\n" +
                "            error, please immediately contact the sender by reply email and\n" +
                "            delete the email and any attachments without reading or saving in\n" +
                "            any manner.</span\n" +
                "          ></i\n" +
                "        >\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }



    @Override
    public void sendEmailApprovers(Set<String> recipientEmail, String title, String department, User fullName, Request totalsDay, LocalTime timeStart, LocalDate dateFrom, LocalTime timeEnd, LocalDate dateTo) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String requestLink = "http://192.168.45.50:9999" + "/managerequest";
        String[] multipleEmailIds = recipientEmail.toArray(new String[0]);
        helper.setFrom("VMG@mailnotifi", "VMG");
        helper.setTo(multipleEmailIds);
        String subject = "[Request]"+ fullName.getFullName() +" created a new request Đơn xin " + totalsDay.getCatergoryRequest().getName() + " that you approved";
        String content = "<p> Họ và tên: "+ fullName.getFullName() + "_Bộ phận: "+ department + "</p>"
                +
                "<p>Số ngày nghỉ: "+ totalsDay.getTotalDays() + " ngày từ: "+ timeStart + " ngày " + dateFrom + " đến " + timeEnd + " ngày " + dateTo + " " + "</p>"
                +
                "<p> Lý do: "+ title + " "+ "</p>"
                +
                "<p> Truy cập link: <a href=\"" + requestLink + "\">để xem chi tiết</a></p>"
                +
                "<table\n" +
                "  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"color: rgb(34, 34, 34); font-family: 'Times New Roman'; font-size: 9pt\"\n" +
                ">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td\n" +
                "        style=\"\n" +
                "          width: 652px;\n" +
                "          padding-top: 5px;\n" +
                "          padding-bottom: 8px;\n" +
                "          font-size: 9pt;\n" +
                "        \"\n" +
                "      >\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                "          <tbody>\n" +
                "            <tr>\n" +
                "              <td style=\"width: 100px; padding-left: 5px; padding-right: 5px\">\n" +
                "                <img\n" +
                "                  src=\"https://ci3.googleusercontent.com/proxy/7ODHXmr4KB__QBF5zf6OEIVvi0GqevmXzbOSE4Kr_Y05vTqtHhQ-e0dg0EDfkFL-c3chEl6WK0LwObV1KSyDCiA9sD7q-Qu45QT-TLy7ctWVe5zPJkSqimZOj8r-sZ2wBHHM32I_1SBOP0aJVUrRafq_4gMjwKHHO3f0wEnxqQETKgb-U0M7QtkK6kC10EXG3ynBXXJTahdM_hsMxw=s0-d-e1-ft#https://docs.google.com/uc?export=download&amp;id=109TlDyQtyW6COZ4_NOSQZAzhhwCgJGcs&amp;revid=0B8WbkXGKBxnQSVRqR3FHQlpIQ2tkdHBnRGU0OGRueUdtNjhzPQ\"\n" +
                "                  width=\"200\"\n" +
                "                  height=\"97\"\n" +
                "                  style=\"font-family: 'Times New Roman', serif; font-size: 16px\"\n" +
                "                  class=\"CToWUd\"\n" +
                "                  data-bit=\"iit\"\n" +
                "                />\n" +
                "              </td>\n" +
                "              <td></td>\n" +
                "              <td>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <br />\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 11.7px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >---------------------------</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <b\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-family: 'Times New Roman';\n" +
                "                        color: rgb(31, 73, 125);\n" +
                "                        font-size: 10pt;\n" +
                "                      \"\n" +
                "                      >VMG MEDIA GROUP</span\n" +
                "                    ></b\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >A : 6F, Peakview tower,&nbsp;36 Hoang Cau Street, Ha Noi,\n" +
                "                    Viet Nam</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >P : 84-4-35378820&nbsp;&nbsp;<b style=\"color: black\">|</b\n" +
                "                    >&nbsp; F:&nbsp;&nbsp;84-4-37726092&nbsp;&nbsp;<b\n" +
                "                      style=\"color: black\"\n" +
                "                      >|</b\n" +
                "                    >&nbsp;&nbsp;W: &nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"http://vmgmedia.vn/\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=http://vmgmedia.vn/&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw3-XufeUzUXBbaYLEwkO-hZ\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: 'times new roman';\n" +
                "                      \"\n" +
                "                      >vmgmedia.vn</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >FP:&nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"https://www.facebook.com/VMGMedia\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=https://www.facebook.com/VMGMedia&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw2O8jtVN6VNghigB6Yexax5\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: ' times new roman', ' serif';\n" +
                "                      \"\n" +
                "                      >https://www.facebook.com/<wbr />VMGMedia</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </tbody>\n" +
                "        </table>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; height: 1px\"></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; text-align: justify; padding-top: 5px\">\n" +
                "        <i\n" +
                "          ><span style=\"font-family: 'Times New Roman'\"\n" +
                "            >This email may contain confidential or proprietary information of\n" +
                "            VMG JSC. Its disclosure is strictly limited to the intended\n" +
                "            recipient by the sender. Any review, reliance or distribution by\n" +
                "            others, disclosure or forwarding without express permission from the\n" +
                "            sender is strictly prohibited. If you have received the message in\n" +
                "            error, please immediately contact the sender by reply email and\n" +
                "            delete the email and any attachments without reading or saving in\n" +
                "            any manner.</span\n" +
                "          ></i\n" +
                "        >\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public void sendEmailApproversForget(Set<String> recipientEmail, String title, String department, Request totalsDay, User fullName, LocalDate dateForget) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String requestLink = "http://192.168.45.50:9999" + "/managerequest";
        String[] multipleEmailIds = recipientEmail.toArray(new String[0]);
        helper.setFrom("VMG@mailnotifi", "VMG");
        helper.setTo(multipleEmailIds);
        String subject = "[Request]"+ fullName.getFullName() +" created a new request Đơn xin " + totalsDay.getCatergoryRequest().getName() + " that you approved";
        String content = "<p> Họ và tên: "+ fullName.getFullName() + "_Bộ phận: "+ department + "</p>"
                +
                "<p>Ngày quên chấm công: "+ dateForget + "</p>"
                +
                "<p> Lý do: "+ title + " "+ "</p>"
                +
                "<p> Truy cập link: <a href=\"" + requestLink + "\">để xem chi tiết</a></p>"
                +
                "<table\n" +
                "  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"color: rgb(34, 34, 34); font-family: 'Times New Roman'; font-size: 9pt\"\n" +
                ">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td\n" +
                "        style=\"\n" +
                "          width: 652px;\n" +
                "          padding-top: 5px;\n" +
                "          padding-bottom: 8px;\n" +
                "          font-size: 9pt;\n" +
                "        \"\n" +
                "      >\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                "          <tbody>\n" +
                "            <tr>\n" +
                "              <td style=\"width: 100px; padding-left: 5px; padding-right: 5px\">\n" +
                "                <img\n" +
                "                  src=\"https://ci3.googleusercontent.com/proxy/7ODHXmr4KB__QBF5zf6OEIVvi0GqevmXzbOSE4Kr_Y05vTqtHhQ-e0dg0EDfkFL-c3chEl6WK0LwObV1KSyDCiA9sD7q-Qu45QT-TLy7ctWVe5zPJkSqimZOj8r-sZ2wBHHM32I_1SBOP0aJVUrRafq_4gMjwKHHO3f0wEnxqQETKgb-U0M7QtkK6kC10EXG3ynBXXJTahdM_hsMxw=s0-d-e1-ft#https://docs.google.com/uc?export=download&amp;id=109TlDyQtyW6COZ4_NOSQZAzhhwCgJGcs&amp;revid=0B8WbkXGKBxnQSVRqR3FHQlpIQ2tkdHBnRGU0OGRueUdtNjhzPQ\"\n" +
                "                  width=\"200\"\n" +
                "                  height=\"97\"\n" +
                "                  style=\"font-family: 'Times New Roman', serif; font-size: 16px\"\n" +
                "                  class=\"CToWUd\"\n" +
                "                  data-bit=\"iit\"\n" +
                "                />\n" +
                "              </td>\n" +
                "              <td></td>\n" +
                "              <td>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <br />\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 11.7px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >---------------------------</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <b\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-family: 'Times New Roman';\n" +
                "                        color: rgb(31, 73, 125);\n" +
                "                        font-size: 10pt;\n" +
                "                      \"\n" +
                "                      >VMG MEDIA GROUP</span\n" +
                "                    ></b\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >A : 6F, Peakview tower,&nbsp;36 Hoang Cau Street, Ha Noi,\n" +
                "                    Viet Nam</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >P : 84-4-35378820&nbsp;&nbsp;<b style=\"color: black\">|</b\n" +
                "                    >&nbsp; F:&nbsp;&nbsp;84-4-37726092&nbsp;&nbsp;<b\n" +
                "                      style=\"color: black\"\n" +
                "                      >|</b\n" +
                "                    >&nbsp;&nbsp;W: &nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"http://vmgmedia.vn/\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=http://vmgmedia.vn/&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw3-XufeUzUXBbaYLEwkO-hZ\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: 'times new roman';\n" +
                "                      \"\n" +
                "                      >vmgmedia.vn</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >FP:&nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"https://www.facebook.com/VMGMedia\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=https://www.facebook.com/VMGMedia&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw2O8jtVN6VNghigB6Yexax5\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: ' times new roman', ' serif';\n" +
                "                      \"\n" +
                "                      >https://www.facebook.com/<wbr />VMGMedia</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </tbody>\n" +
                "        </table>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; height: 1px\"></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; text-align: justify; padding-top: 5px\">\n" +
                "        <i\n" +
                "          ><span style=\"font-family: 'Times New Roman'\"\n" +
                "            >This email may contain confidential or proprietary information of\n" +
                "            VMG JSC. Its disclosure is strictly limited to the intended\n" +
                "            recipient by the sender. Any review, reliance or distribution by\n" +
                "            others, disclosure or forwarding without express permission from the\n" +
                "            sender is strictly prohibited. If you have received the message in\n" +
                "            error, please immediately contact the sender by reply email and\n" +
                "            delete the email and any attachments without reading or saving in\n" +
                "            any manner.</span\n" +
                "          ></i\n" +
                "        >\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public void sendEmailApproversTCS(Set<String> recipientEmail, String title, String department, Request totalsDay, User fullName, LocalDate dateFrom, LocalDate dateTo) throws MessagingException, UnsupportedEncodingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        String requestLink = "http://192.168.45.50:9999" + "/managerequest";
        String[] multipleEmailIds = recipientEmail.toArray(new String[0]);
        helper.setFrom("VMG@mailnotifi", "VMG");
        helper.setTo(multipleEmailIds);
        String subject = "[Request]"+ fullName.getFullName() +" created a new request Đơn xin " + totalsDay.getCatergoryRequest().getName() + " that you approved";
        String content = "<p> Họ và tên: "+ fullName.getFullName() + "_Bộ phận: "+ department + "</p>"
                +
                "<p>Nghỉ từ: "+ dateFrom + " đến " + dateTo + "</p>"
                +
                "<p> Lý do: "+ title + " "+ "</p>"
                +
                "<p> Truy cập link: <a href=\"" + requestLink + "\">để xem chi tiết</a></p>"
                +
                "<table\n" +
                "  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"color: rgb(34, 34, 34); font-family: 'Times New Roman'; font-size: 9pt\"\n" +
                ">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td\n" +
                "        style=\"\n" +
                "          width: 652px;\n" +
                "          padding-top: 5px;\n" +
                "          padding-bottom: 8px;\n" +
                "          font-size: 9pt;\n" +
                "        \"\n" +
                "      >\n" +
                "        <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                "          <tbody>\n" +
                "            <tr>\n" +
                "              <td style=\"width: 100px; padding-left: 5px; padding-right: 5px\">\n" +
                "                <img\n" +
                "                  src=\"https://ci3.googleusercontent.com/proxy/7ODHXmr4KB__QBF5zf6OEIVvi0GqevmXzbOSE4Kr_Y05vTqtHhQ-e0dg0EDfkFL-c3chEl6WK0LwObV1KSyDCiA9sD7q-Qu45QT-TLy7ctWVe5zPJkSqimZOj8r-sZ2wBHHM32I_1SBOP0aJVUrRafq_4gMjwKHHO3f0wEnxqQETKgb-U0M7QtkK6kC10EXG3ynBXXJTahdM_hsMxw=s0-d-e1-ft#https://docs.google.com/uc?export=download&amp;id=109TlDyQtyW6COZ4_NOSQZAzhhwCgJGcs&amp;revid=0B8WbkXGKBxnQSVRqR3FHQlpIQ2tkdHBnRGU0OGRueUdtNjhzPQ\"\n" +
                "                  width=\"200\"\n" +
                "                  height=\"97\"\n" +
                "                  style=\"font-family: 'Times New Roman', serif; font-size: 16px\"\n" +
                "                  class=\"CToWUd\"\n" +
                "                  data-bit=\"iit\"\n" +
                "                />\n" +
                "              </td>\n" +
                "              <td></td>\n" +
                "              <td>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <br />\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 11.7px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >---------------------------</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <b\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-family: 'Times New Roman';\n" +
                "                        color: rgb(31, 73, 125);\n" +
                "                        font-size: 10pt;\n" +
                "                      \"\n" +
                "                      >VMG MEDIA GROUP</span\n" +
                "                    ></b\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >A : 6F, Peakview tower,&nbsp;36 Hoang Cau Street, Ha Noi,\n" +
                "                    Viet Nam</span\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >P : 84-4-35378820&nbsp;&nbsp;<b style=\"color: black\">|</b\n" +
                "                    >&nbsp; F:&nbsp;&nbsp;84-4-37726092&nbsp;&nbsp;<b\n" +
                "                      style=\"color: black\"\n" +
                "                      >|</b\n" +
                "                    >&nbsp;&nbsp;W: &nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"http://vmgmedia.vn/\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=http://vmgmedia.vn/&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw3-XufeUzUXBbaYLEwkO-hZ\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: 'times new roman';\n" +
                "                      \"\n" +
                "                      >vmgmedia.vn</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "                <p\n" +
                "                  style=\"\n" +
                "                    font-size: small;\n" +
                "                    margin: 0in 0in 0.0001pt 17.1pt;\n" +
                "                    line-height: 18.2px;\n" +
                "                  \"\n" +
                "                >\n" +
                "                  <span\n" +
                "                    style=\"\n" +
                "                      font-size: 9pt;\n" +
                "                      line-height: 16.8px;\n" +
                "                      font-family: 'Times New Roman';\n" +
                "                      color: rgb(31, 73, 125);\n" +
                "                    \"\n" +
                "                    >FP:&nbsp;</span\n" +
                "                  ><a\n" +
                "                    href=\"https://www.facebook.com/VMGMedia\"\n" +
                "                    style=\"color: rgb(31, 73, 125)\"\n" +
                "                    target=\"_blank\"\n" +
                "                    data-saferedirecturl=\"https://www.google.com/url?q=https://www.facebook.com/VMGMedia&amp;source=gmail&amp;ust=1671777795462000&amp;usg=AOvVaw2O8jtVN6VNghigB6Yexax5\"\n" +
                "                    ><span\n" +
                "                      style=\"\n" +
                "                        font-size: 9pt;\n" +
                "                        line-height: 16.8px;\n" +
                "                        font-family: ' times new roman', ' serif';\n" +
                "                      \"\n" +
                "                      >https://www.facebook.com/<wbr />VMGMedia</span\n" +
                "                    ></a\n" +
                "                  >\n" +
                "                </p>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </tbody>\n" +
                "        </table>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; height: 1px\"></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"width: 652px; text-align: justify; padding-top: 5px\">\n" +
                "        <i\n" +
                "          ><span style=\"font-family: 'Times New Roman'\"\n" +
                "            >This email may contain confidential or proprietary information of\n" +
                "            VMG JSC. Its disclosure is strictly limited to the intended\n" +
                "            recipient by the sender. Any review, reliance or distribution by\n" +
                "            others, disclosure or forwarding without express permission from the\n" +
                "            sender is strictly prohibited. If you have received the message in\n" +
                "            error, please immediately contact the sender by reply email and\n" +
                "            delete the email and any attachments without reading or saving in\n" +
                "            any manner.</span\n" +
                "          ></i\n" +
                "        >\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n";
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public UserDetails resetPasswordToken(String token) {
         return  userDetailsService.loadUserByUsername(jwtUtils.getUserNameFromJwtToken(token));
    }

    @Override
    public Boolean resetPassword(String email) throws MessagingException, UnsupportedEncodingException {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            User user = userRepository.findByUsername(email).get();
            String rootPassword = alphaNumericString(8);
            user.setRootPassword(passwordEncoder.encode(rootPassword));
            user.setCheckRootDisable(false);
            userRepository.save(user);
            helper.setFrom("VMG@mailnotifi", "VMG");
            helper.setTo(email);
            String subject = "Here's the info account";
            String content = "<p>Hello,</p>"
                    + "<p>You password have been reset.</p>"
                    + "Here is account infomation :"
                    + "<p> Email:yourEmail </p>"
                    + "<br>"
                    + "<p> Password:" + rootPassword +"</p>";
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            return true;
        }
        catch (Exception e){
            return false;
        }

    }
}
