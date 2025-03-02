package com.geupjo.koreantiger.entity;

import com.geupjo.koreantiger.common.jpa.BaseEntity;
import com.geupjo.koreantiger.enums.Authority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Authority authority;

    private Member(BaseEntity base, String name, Authority authority) {
        super(base.getId(), base.getCreatedAt(), base.getLastModifiedAt());
        this.name = name;
        this.authority = authority;
    }

    // MockData 생성용 이외에 사용 x
    public static Member getMockStudent() {
        BaseEntity mockBase = new BaseEntity(888L, LocalDateTime.now(), LocalDateTime.now());
        return new Member(mockBase, "김석배", Authority.STUDENT);
    }

    // MockData 생성용 이외에 사용 x
    public static Member getMockTeacher() {
        BaseEntity mockBase = new BaseEntity(999L, LocalDateTime.now(), LocalDateTime.now());
        return new Member(mockBase, "김철수", Authority.TEACHER);
    }
}
