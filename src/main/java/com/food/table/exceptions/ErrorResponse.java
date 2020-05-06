package com.food.table.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "error")
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;

    private List<String> details;

}
