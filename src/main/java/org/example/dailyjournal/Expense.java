package org.example.dailyjournal;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private LocalDate date;

    private String note;

    // 【新增】收支类型：expense(支出) 或 income(收入)
    private String type;

    // 【新增】分类：如 cat_food, inc_salary 等
    private String category;

    // --- Getters and Setters ---
    // ... 原有的代码 ...
    private String subCat;

    public String getSubCat() {
        return subCat;
    }

    public void setSubCat(String subCat) {
        this.subCat = subCat;
    }

    // 【新增】表示这条账单属于哪个用户
    private Long userId;

    // 【新增】Getter 和 Setter
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    // 【新增】Type 的 Getter/Setter
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    // 【新增】Category 的 Getter/Setter
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}