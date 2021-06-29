package com.example.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

@Data
@JsonInclude(Include.NON_NULL) // JSON 으로 반환할 때 Null 이면 포함하지 않도록
public class ResponseUser {

    private String email;
    private String name;
    private String userId;

    private List<ResponseOrder> orders;


}
