package org.example.dailyjournal;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

// JpaRepository<实体类, 主键类型>
public interface SpecialDayRepository extends JpaRepository<SpecialDay, Long> {

    // Spring Data JPA 的神奇之处：方法名即查询逻辑！
    // 这句话的意思是：查找所有 date等于today 且 needReminder等于true 的记录
    List<SpecialDay> findByDateAndNeedReminderTrue(LocalDate today);
}
