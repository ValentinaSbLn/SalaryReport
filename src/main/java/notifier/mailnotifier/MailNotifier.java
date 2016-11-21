package notifier.mailnotifier;
import notifier.Notifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class MailNotifier implements Notifier{

        private final String host;
        private JavaMailSenderImpl mailSender;

        public MailNotifier(String host) {
            this.host = host;
            mailSender = new JavaMailSenderImpl();
        }

        private MimeMessage genereteMessage(String recipients, String report) throws MessagingException {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(recipients);
            helper.setText(report, true);
            helper.setSubject("Monthly department salary report");
            return message;
        }
        @Override
        public void notifyReport(String recipients, String report)  {
            mailSender.setHost(host);
            MimeMessage message = null;
            try {
                message = genereteMessage(recipients, report);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            mailSender.send(message);
        }

}

