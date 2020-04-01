package com.navercorp.pinpoint.web.alarm;

import com.navercorp.pinpoint.web.alarm.checker.AlarmChecker;
import com.navercorp.pinpoint.web.service.AlarmService;
import com.navercorp.pinpoint.web.service.UserGroupService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;


public class AlarmMessageSenderImpl
        implements AlarmMessageSender
{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private MailSender mailSender;

    @Autowired
    private WxCpService wxCpService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private AlarmService alarmService;

    public void setMailSender(MailSender mailSender)
    {
        this.mailSender = mailSender;
    }

    public void sendSms(AlarmChecker checker, int sequenceCount) {
        String error_summary = alarmService.selectErrorSummary(checker.getRule().getApplicationId());
        List<String> receivers = this.userGroupService.selectPhoneNumberOfMember(checker.getuserGroupId());
        WxCpMessage message = null;
        for (String receiver : receivers) {
            message = WxCpMessage.TEXT().agentId(1000019).toUser(StringUtils.trim(receiver)).content(String.format("%s\n\n%s", new Object[] { checker.getSmsMessage() + error_summary, checker.getRule().getNotes() })).build();
            try {
                wxCpService.messageSend(message);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendEmail(AlarmChecker checker, int sequenceCount)
    {
        List receivers = this.userGroupService.selectEmailOfMember(checker.getuserGroupId());

        if (receivers.size() == 0) {
            return;
        }

        this.logger.info("send email : {}", checker.getEmailMessage());
        try {
            String error_summary = alarmService.selectErrorSummary(checker.getRule().getApplicationId());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo((String[])receivers.toArray(new String[receivers.size()]));
            message.setSubject(String.format("[PINPOINT Alarm - %s]", new Object[] { checker.getRule().getApplicationId() }));
            message.setText(String.format("%s\n\n%s", new Object[] { checker.getEmailMessage() + error_summary, checker.getRule().getNotes() }));
            this.mailSender.send(message);
            System.out.println(message.getText());
        } catch (Exception e) {
            this.logger.error("send email error", e);
        }
    }


}