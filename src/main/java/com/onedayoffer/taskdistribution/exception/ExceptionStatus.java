package com.onedayoffer.taskdistribution.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ExceptionStatus {

    // 1xxx - непредвиденные ошибки
    // 2xxx - бизнес ошибки
    // 3xxx - ошибки работы с БД

    INTERNAL_ERROR(1000, "Произошла внезапная ошибка на сервере"),
    VALIDATION_ERROR(2001, "Неправильно составлен запрос"),
    NOT_FOUND_EMPLOYEE(3001, "Не смог найти в БД нужного employee"),
    NOT_FOUND_TASK(3002, "Не смог найти в БД нужного Task")
    ;

    private final int code;

    private final String message;

    @Override
    public String toString() {
        return this.code + ": " + this.message;
    }
}