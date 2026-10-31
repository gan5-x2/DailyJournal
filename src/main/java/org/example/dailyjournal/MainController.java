package org.example.dailyjournal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@CrossOrigin
public class MainController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpecialDayRepository specialDayRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    //@GetMapping("/")
    //public String hello() {
    //    return "恭喜！你的日计软件后端已经启动成功了！";
    //}


    @GetMapping("/send-test-mail")
    public String testMail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            String myEmail = "2461029874@qq.com";
            message.setFrom(myEmail);
            message.setTo(myEmail);
            message.setSubject("【日计软件】测试提醒");
            message.setText("这是一封测试邮件！");
            mailSender.send(message);
            return "邮件发送指令已发出！";
        } catch (Exception e) {
            return "发送失败: " + e.getMessage();
        }
    }

    @PostMapping("/api/feedback")
    public String receiveFeedback(@RequestBody Map<String, String> payload) {
        try {
            String content = payload.get("content");
            if (content == null || content.trim().isEmpty()) return "error: 内容为空";

            SimpleMailMessage message = new SimpleMailMessage();
            String myEmail = "2461029874@qq.com";
            message.setFrom(myEmail);
            message.setTo(myEmail);
            message.setSubject("【日计软件】用户反馈");
            message.setText(content);
            mailSender.send(message);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    // --- 【核心】保存记账记录 ---
    @PostMapping("/api/save-expense")
    public String saveExpense(@RequestBody Map<String, Object> params,
                              @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // 1. 从 Token 中解析出当前登录用户的 ID
            Long userId = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                userId = JwtUtil.getUserIdFromToken(token);
            }

            // 如果没拿到 userId，说明没登录或者 Token 无效，拒绝保存
            if (userId == null) return "error: 未登录或登录已过期";

            Expense expense = new Expense();
            expense.setAmount(new BigDecimal(params.get("amount").toString()));
            expense.setDate(LocalDate.parse(params.get("date").toString()));
            expense.setNote(params.get("note") != null ? params.get("note").toString() : "");
            expense.setType(params.get("type").toString());
            expense.setCategory(params.get("category").toString());

            // 【关键】把当前用户的 ID 存进去
            expense.setUserId(userId);

            if (params.containsKey("subCat")) {
                expense.setSubCat(params.get("subCat").toString());
            }

            expenseRepository.save(expense);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    // --- 【新增】更新记账记录 ---
    @PutMapping("/api/update-expense")
    public String updateExpense(@RequestBody Map<String, Object> params,
                                @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long userId = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                userId = JwtUtil.getUserIdFromToken(token);
            }
            if (userId == null) return "error: 未登录";

            Long id = Long.valueOf(params.get("id").toString());

            // 查找数据库中的旧记录
            Optional<Expense> optionalExpense = expenseRepository.findById(id);
            if (optionalExpense.isPresent()) {
                Expense expense = optionalExpense.get();

                // 安全检查：确保只能修改自己的数据
                if (!expense.getUserId().equals(userId)) {
                    return "error: 无权修改此记录";
                }

                // 更新字段
                if (params.containsKey("amount")) expense.setAmount(new BigDecimal(params.get("amount").toString()));
                if (params.containsKey("category")) expense.setCategory(params.get("category").toString());
                if (params.containsKey("subCat")) expense.setSubCat(params.get("subCat").toString());
                if (params.containsKey("note")) expense.setNote(params.get("note").toString());
                if (params.containsKey("type")) expense.setType(params.get("type").toString());

                expenseRepository.save(expense);
                return "success";
            }
            return "error: 记录不存在";
        } catch (Exception e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    // --- 【新增】删除记账记录 ---
    @DeleteMapping("/api/delete-expense")
    public String deleteExpense(@RequestParam Long id,
                                @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            Long userId = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                userId = JwtUtil.getUserIdFromToken(token);
            }
            if (userId == null) return "error: 未登录";

            Optional<Expense> optionalExpense = expenseRepository.findById(id);
            if (optionalExpense.isPresent()) {
                Expense expense = optionalExpense.get();

                // 安全检查：确保只能删除自己的数据
                if (!expense.getUserId().equals(userId)) {
                    return "error: 无权删除此记录";
                }

                expenseRepository.deleteById(id);
                return "success";
            }
            return "error: 记录不存在";
        } catch (Exception e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }

    // --- 【核心】查询所有记录 ---
    @GetMapping("/api/get-expenses")
    public List<Expense> getExpenses(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        // 1. 解析 Token 获取当前用户 ID
        Long userId = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            userId = JwtUtil.getUserIdFromToken(token);
        }

        // 如果没登录，返回空列表
        if (userId == null) return new ArrayList<>();

        // 2. 【关键】从数据库查出所有数据后，只保留属于当前用户的数据
        List<Expense> allExpenses = expenseRepository.findAll();
        List<Expense> myExpenses = new ArrayList<>();

        for (Expense e : allExpenses) {
            // 只有当账单的 userId 等于当前登录用户的 ID 时，才加进来
            if (e.getUserId() != null && e.getUserId().equals(userId)) {
                myExpenses.add(e);
            }
        }

        return myExpenses;
    }
}