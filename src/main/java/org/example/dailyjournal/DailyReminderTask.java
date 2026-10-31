package org.example.dailyjournal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component // 标记为组件，让 Spring 管理
public class DailyReminderTask {

    @Autowired
    private SpecialDayRepository specialDayRepository;

    @Autowired
    private JavaMailSender mailSender;

    // cron 表达式：秒 分 时 日 月 周
    // 下面的意思是：每天 早上 8:00:00 执行一次
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkAndSendReminders() {
        System.out.println(">>> 开始执行每日提醒检查...");

        LocalDate today = LocalDate.now();
        // 查找今天所有需要提醒的日子
        List<SpecialDay> events = specialDayRepository.findByDateAndNeedReminderTrue(today);

        if (events.isEmpty()) {
            System.out.println(">>> 今天没有需要提醒的事项。");
            return;
        }

        for (SpecialDay event : events) {
            sendEmail(event);
        }
    }

    private void sendEmail(SpecialDay event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            String myEmail = "2461029874@qq.com"; // 记得改成你的邮箱

            message.setFrom(myEmail);
            message.setTo(myEmail);
            message.setSubject("【日计提醒】今天是：" + event.getName());
            message.setText("今天是特殊日子：" + event.getName() + "\n\n备注：" + event.getDescription());

            mailSender.send(message);
            System.out.println(">>> 提醒邮件已发送: " + event.getName());
        } catch (Exception e) {
            System.err.println(">>> 发送邮件失败: " + e.getMessage());
        }
    }
}
