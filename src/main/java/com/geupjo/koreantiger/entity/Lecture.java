package com.geupjo.koreantiger.entity;

import com.geupjo.koreantiger.common.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jdk.jfr.Description;
import lombok.Getter;

@Getter
@Entity
public class Lecture extends BaseEntity {

    @Description("강의 진행률")
    @Column
    private String name;

    @Description("강의 코드")
    @Column
    private String code;

    @Description("강의 진행률")
    @Min(0)
    @Max(100)
    @Column
    private int progress;

    @Column
    private long memberId;

    public boolean isComplete() {
        return this.progress == 100;
    }
}
