package org.paniergarni.batch.job;

import org.paniergarni.batch.object.Order;
import org.paniergarni.batch.object.User;
import org.paniergarni.batch.proxy.OrderProxy;
import org.paniergarni.batch.proxy.UserProxy;
import org.paniergarni.batch.service.SendMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *  Rappel de commande en retard
 *
 * @author PICHAT morgan
 */
@Component
public class BatchCheckLate {

    private static final Logger logger = LoggerFactory.getLogger(BatchCheckLate.class);

    @Autowired
    private SendMail sendMail;
    @Autowired
    private OrderProxy orderProxy;
    @Autowired
    private UserProxy userProxy;
    @Value("${mail.username}")
    private String emailUsers;
    @Value("${mail.password}")
    private String emailPassword;
    @Value("${mail.object.late}")
    private String objectLate;
    @Value("${mail.body.late}")
    private String bodyLate;

    /** Batch de rappel de commande en retard
     *
     */
    @Scheduled(cron = "${cron.every.days}")
    public void checkLateOrder() {

        try {



        List<Order> orders = orderProxy.getListLateOrder();

        for (Order order:orders) {
            User user = userProxy.findByUserName(order.getUserId());

            String[] tableau_email = { user.getMail().getEmail() };

            String object = MessageFormat.format(objectLate, order.getReference());

            String pattern = "dd/MM/yyyy";
            DateFormat df = new SimpleDateFormat(pattern);

            String body = MessageFormat.format(bodyLate, order.getReference(), df.format(order.getReception()));

            sendMail.sendFromGMail(emailUsers, emailPassword,
                    tableau_email, object, body);

            logger.info("Send email to user ID" + user.getId() + " for late of order ID " + order.getId());
        }

        }catch (Exception e){
            logger.error("Error send mail");
        }

        logger.info("Success complete batch late order");
    }
}
