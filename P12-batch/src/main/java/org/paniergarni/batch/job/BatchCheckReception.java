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

import java.text.MessageFormat;
import java.util.List;

@Component
public class BatchCheckReception {

    private static final Logger logger = LoggerFactory.getLogger(BatchCheckReception.class);

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
    @Value("${mail.object.recall}")
    private String objectRecall;
    @Value("${mail.body.recall}")
    private String bodyRecall;

    /**
     *
     */
    @Scheduled(cron = "${cron.every.days}")
    public void checkRecallOrder() {

        try {

            List<Order> orders = orderProxy.getListReceptionOrder();

            for (Order order : orders) {
                User user = userProxy.findByUserName(order.getUserId());

                String[] tableau_email = {user.getMail().getEmail()};

                String object = MessageFormat.format(objectRecall, order.getReference());

                String body = MessageFormat.format(bodyRecall, order.getReference());

                sendMail.sendFromGMail(emailUsers, emailPassword,
                        tableau_email, object, body);

                logger.info("Send email to user ID" + user.getId() + " for reception of order ID " + order.getId());
            }

        }catch (Exception e){
            logger.error("Error send mail");
        }
        logger.info("Success complete batch reception order");
    }
}
