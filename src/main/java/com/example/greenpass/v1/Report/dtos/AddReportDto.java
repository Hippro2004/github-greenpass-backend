package com.example.greenpass.v1.Report.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddReportDto {

    @NotBlank(message = "กรุณากรอกหัวข้อรายงาน")
    @Size(min = 2, max = 25, message = "หัวข้อรายงานต้องมีตัวอักษรตั้งแต่ 2 - 25 ตัวอักษร")
    private String name;

    @NotBlank(message = "กรุณากรอกรายละเอียดรายงาน")
    @Size(min = 10, max = 255, message = "รายละเอียดรายงานต้องมีตัวอักษรตั้งแต่ 10 - 255 ตัวอักษร")
    private String description;

    @NotBlank(message = "กรุณาอัปโหลดรูปภาพ")
    private String image;

    @NotBlank(message = "กรุณากรอกประเภทรายงาน")
    private String typeName;

    @NotNull(message = "กรุณาเลือกอุทยาน")
    private int parkId;

}
