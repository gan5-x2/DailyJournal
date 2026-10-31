package org.example.dailyjournal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 这个接口不需要写任何代码，Spring Boot 会自动实现它
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // JpaRepository<操作的实体类, 主键ID的类型>
}
