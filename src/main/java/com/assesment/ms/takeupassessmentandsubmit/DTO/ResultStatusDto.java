package com.assesment.ms.takeupassessmentandsubmit.DTO;

import com.assesment.ms.takeupassessmentandsubmit.entity.Result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultStatusDto {

    private  Long resultId;
    private Long userId;

    
    private String batchNo;
    private Result status;
    
    
    

    

}

