package org.example.dailyjournal;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity // 标记为数据库表
public class SpecialDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // 名称，比如：女朋友生日
    private LocalDate date;     // 日期，比如：2026-10-01
    private String description; // 描述，比如：记得买花！
    private boolean needReminder; // 是否需要提醒

    // 无参构造
    public SpecialDay() {}

    // Getter 和 Setter (记得用 Alt+Insert 生成，或者手动写)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isNeedReminder() { return needReminder; }
    public void setNeedReminder(boolean needReminder) { this.needReminder = needReminder; }
}
