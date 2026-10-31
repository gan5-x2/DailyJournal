package org.example.dailyjournal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JavaMailSender mailSender; // 【新增】注入邮件发送器
    private String token;

    // 1. 发送验证码接口（改为发送邮件）
    @PostMapping("/send-code")
    public Map<String, String> sendCode(@RequestBody Map<String, String> params) {
        String phoneOrEmail = params.get("phone"); // 前端传过来的可能是手机号，也可能是邮箱
        Map<String, String> res = new HashMap<>();

        // 简单校验：如果是邮箱格式
        if (phoneOrEmail != null && phoneOrEmail.contains("@")) {
            // 生成6位随机验证码
            String code = String.format("%06d", new Random().nextInt(999999));

            // 存入 Redis，有效期5分钟
            redisTemplate.opsForValue().set("sms:" + phoneOrEmail, code, 5, TimeUnit.MINUTES);

            // 【真正发送邮件】
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("2461029874@qq.com"); // 你的发件箱
                message.setTo(phoneOrEmail);          // 收件人（用户填的邮箱）
                message.setSubject("【日计】登录验证码");
                message.setText("您的验证码是：" + code + "，5分钟内有效。如非本人操作，请忽略。");

                mailSender.send(message);

                res.put("msg", "验证码已发送至您的邮箱");
                return res;
            } catch (Exception e) {
                e.printStackTrace();
                res.put("msg", "邮件发送失败: " + e.getMessage());
                return res;
            }
        } else {
            res.put("msg", "请输入有效的邮箱地址");
            return res;
        }
    }

    // 2. 登录/注册接口（逻辑不变，只是现在支持邮箱作为账号）
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        String account = params.get("phone"); // 这里其实存的是邮箱
        String inputCode = params.get("code");
        Map<String, Object> res = new HashMap<>();

        // 从 Redis 取出真实验证码
        String realCode = redisTemplate.opsForValue().get("sms:" + account);

        if (realCode == null) {
            res.put("msg", "验证码已过期");
            res.put("success", false);
            return res;
        }

        if (!realCode.equals(inputCode)) {
            res.put("msg", "验证码错误");
            res.put("success", false);
            return res;
        }

        // 验证通过，删除验证码
        redisTemplate.delete("sms:" + account);

        // 查找用户（用邮箱查），不存在则自动注册
        // 注意：User表里的phone字段现在实际上存的是邮箱
        User user = userRepository.findByPhone(account).orElse(null);
        if (user == null) {
            user = new User();
            user.setPhone(account); // 把邮箱存在phone字段里
            user.setCreateTime(LocalDateTime.now());
            userRepository.save(user);
        }

        // 签发 Token
        String token = JwtUtil.generateToken(user.getId(), user.getPhone());

        res.put("success", true);
        res.put("token", token);
        res.put("userId", user.getId());
        res.put("phone", user.getPhone());
        return res;
    }
}