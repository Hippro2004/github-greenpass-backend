package com.example.greenpass.v1.ReplyReport.services;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.greenpass.v1.ReplyReport.dtos.ReplyReportResponse;
import com.example.greenpass.v1.ReplyReport.entities.ReplyReport;
import com.example.greenpass.v1.ReplyReport.repositories.ReplyReporyRepository;
import com.example.greenpass.v1.Report.entities.Report;
import com.example.greenpass.utils.FileUtils;

import lombok.RequiredArgsConstructor;

/**
 * คลาสให้บริการจัดการข้อมูลการตอบกลับ/อัปเดตความคืบหน้าของรายงานเหตุการณ์
 * (ReplyReport Service)
 * ทำหน้าที่บันทึกประวัติการอัปเดต, กระจายการแจ้งเตือน Real-time ผ่าน WebSocket
 * และดึงข้อมูลประวัติย้อนหลัง
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyReportServiceImpl implements ReplyReportService {

    // Repository สำหรับจัดการข้อมูล ReplyReport ในฐานข้อมูล MySQL
    private final ReplyReporyRepository replyReporyRepository;

    // เครื่องมือสำหรับส่งข้อความแจ้งเตือน Real-time ไปยัง Client ผ่าน WebSocket
    // STOMP
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * เพิ่มข้อมูลการอัปเดตความคืบหน้าใหม่ (Add Reply Report)
     * พร้อมส่งข้อความแจ้งเตือนแบบ Real-time ไปยังผู้เกี่ยวข้องผ่าน WebSocket
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReplyReport addReplyReport(ReplyReport replyReport, Report report) {
        // 1. สร้างวัตถุ ReplyReport ใหม่ด้วยข้อมูลที่ส่งเข้ามา
        ReplyReport addReplyReport = ReplyReport.builder()
                .updateDate(replyReport.getUpdateDate())
                .updateTime(replyReport.getUpdateTime())
                .progress(replyReport.getProgress())
                .currentStatus(replyReport.getCurrentStatus())
                .image(FileUtils.extractFileName(replyReport.getImage(), "reports")) // จัดการตัดเอาเฉพาะชื่อไฟล์รูปภาพ
                .report(report)
                .parkRanger(replyReport.getParkRanger())
                .build();

        // 2. บันทึกลงฐานข้อมูล MySQL
        ReplyReport saved = replyReporyRepository.save(addReplyReport);

        // 3. แปลงเป็น DTO เพื่อเตรียมส่งแจ้งเตือนผ่าน WebSocket
        ReplyReportResponse responseDto = mapToResponse(saved);

        // 4. ส่งข้อความแจ้งเตือนผ่าน WebSocket ไปยัง Channels (Topics) ต่างๆ
        try {
            // 4.1 แจ้งเตือนไปยังฝั่ง Park (เจ้าหน้าที่ประจำอุทยานนั้นๆ)
            if (report != null && report.getPark() != null) {
                int parkId = report.getPark().getParkId();
                messagingTemplate.convertAndSend("/topic/park/" + parkId + "/reply-reports", responseDto);
            }

            // 4.2 แจ้งเตือนไปยังฝั่ง User (ประชาชนเจ้าของรายงานฉบับนี้)
            if (report != null && report.getUser() != null && report.getUser().getUsername() != null) {
                String uname = report.getUser().getUsername();
                messagingTemplate.convertAndSend("/topic/user/" + uname + "/reply-reports", responseDto);
                messagingTemplate.convertAndSend("/topic/user/" + uname.toLowerCase() + "/reply-reports", responseDto);
            }

            // 4.3 ส่งไปยัง Topic กลางสำหรับผู้ฟังระบบรวม
            messagingTemplate.convertAndSend("/topic/reply-reports", responseDto);
        } catch (Exception e) {
            System.err.println("Could not send WebSocket reply-report notification: " + e.getMessage());
        }

        return saved;
    }

    /**
     * ฟังก์ชันภายใน (Helper Method) สำหรับแปลง Entity (ReplyReport) ให้อยู่ในรูปแบบ
     * DTO (ReplyReportResponse)
     * เพื่อเตรียมส่งออกให้ Frontend
     * ใช้งานได้อย่างปลอดภัยและตรงตามโครงสร้างที่ต้องการ
     */
    @Override
    public List<ReplyReportResponse> findAllByReportReportId(Integer reportId) {
        return replyReporyRepository.findAllByReportReportId(reportId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ReplyReportResponse mapToResponse(ReplyReport e) {
        String rangerFullName = null;
        String rangerUsername = null;
        if (e.getParkRanger() != null) {
            rangerFullName = (e.getParkRanger().getFirstname() + " " + e.getParkRanger().getSurname()).trim();
            rangerUsername = e.getParkRanger().getUsername();
        }
        return ReplyReportResponse.builder()
                .replyReportId(e.getReplyReportId())
                .reportId(e.getReport() != null ? e.getReport().getReportId() : null)
                .updateDate(e.getUpdateDate())
                .updateTime(e.getUpdateTime())
                .progress(e.getProgress())
                .currentStatus(e.getCurrentStatus())
                .image(FileUtils.extractFileName(e.getImage(), "reports"))
                .parkRangerName(rangerFullName)
                .parkRangerUsername(rangerUsername)
                .build();
    }

    /**
     * ดึงประวัติการตอบกลับทั้งหมด ของรายงานฉบับใดฉบับหนึ่ง (ค้นหาตาม reportId)
     */
    @Override
    public List<ReplyReportResponse> getReplyReportByReportId(int reportId) {
        return replyReporyRepository.findAllByReportReportId(reportId).stream()
                .map(r -> new ReplyReportResponse(r.getReplyReportId(), r.getReport().getReportId(), r.getUpdateDate(),
                        r.getUpdateTime(), r.getProgress(), r.getCurrentStatus(),
                        FileUtils.extractFileName(r.getImage(), "reports"),
                        r.getParkRanger().getFirstname(), r.getParkRanger().getUsername(),
                        r.getReport().getType().getTypeName()))
                .toList();

    }

    /**
     * ดึงประวัติการตอบกลับทั้งหมด ของอุทยานแห่งชาตินั้นๆ (ค้นหาตาม parkId)
     * เรียงจากใหม่ไปเก่า
     */
    @Override
    public List<ReplyReportResponse> getReplyReportByParkId(int parkId) {
        return replyReporyRepository.findAllByReportParkParkIdOrderByReplyReportIdDesc(parkId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * ดึงประวัติการตอบกลับทั้งหมด ของผู้ใช้งานคนนั้นๆ (ค้นหาตาม username ประชาชน)
     * เรียงจากใหม่ไปเก่า
     */
    @Override
    public List<ReplyReportResponse> getReplyReportByUsername(String username) {
        return replyReporyRepository.findAllByReportUserUsernameOrderByReplyReportIdDesc(username).stream()
                .map(this::mapToResponse)
                .toList();
    }

}
