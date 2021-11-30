package com.ziroom.tech.demeterapi.open.login.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xuzeyu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtSubjectModel implements Serializable {
    private String code;
    private Long cts;
}
