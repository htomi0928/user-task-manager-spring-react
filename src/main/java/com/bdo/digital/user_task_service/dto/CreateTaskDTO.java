package com.bdo.digital.user_task_service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object for creating a new task.
 */
@Getter
@Setter
public class CreateTaskDTO {
    private String title;
    private String description;
    private boolean deleted;
    private long userId;
}
