package com.vmg.scrum.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogDetailDTO {
    private String date_log;
    private String timeIn;
    private String timeOut;
}
