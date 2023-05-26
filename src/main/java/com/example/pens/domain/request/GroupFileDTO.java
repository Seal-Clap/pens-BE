package com.example.pens.domain.request;

import com.example.pens.domain.Group;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class GroupFileDTO {
    private int fileId;
    private int groupId;
    private String fileName;

}
