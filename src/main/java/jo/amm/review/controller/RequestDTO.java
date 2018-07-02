package jo.amm.review.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RequestDTO {

    @NotBlank(message = "Request id can not be null")
    private String id;
    private String message;
    private Object body;
}
