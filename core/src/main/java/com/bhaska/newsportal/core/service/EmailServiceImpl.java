package com.bhaska.newsportal.core.service;
import com.bhaska.newsportal.core.service.EmailService;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = EmailService.class)
public class EmailServiceImpl implements EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Reference
    private MessageGatewayService messageGatewayService;

    @Override
    public void sendWelcomeEmail(String to, String userName) {

        try {
            // 🔹 Get AEM Mail Gateway
            MessageGateway<HtmlEmail> gateway =
                    messageGatewayService.getGateway(HtmlEmail.class);

            if (gateway == null) {
                LOG.error("MessageGateway is null. Email cannot be sent.");
                throw new RuntimeException("Mail gateway not available");
            }

            // 🔹 Create Email
            HtmlEmail email = new HtmlEmail();

            email.addTo(to);
            email.setSubject("Welcome to News Portal");

            email.setHtmlMsg(
                    "<html>" +
                            "<body>" +
                            "<h2>Welcome " + userName + " 👋</h2>" +
                            "<p>Thank you for registering with us.</p>" +
                            "<p>We are glad to have you onboard!</p>" +
                            "</body>" +
                            "</html>"
            );

            // 🔹 Send Email
            gateway.send(email);

            LOG.info("Welcome email sent successfully to {}", to);

        } catch (EmailException e) {
            LOG.error("Error while sending email to {}", to, e);

            // 🔥 Important for Retry
            throw new RuntimeException("Email sending failed", e);
        }
    }
}