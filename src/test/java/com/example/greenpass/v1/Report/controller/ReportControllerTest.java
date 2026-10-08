package com.example.greenpass.v1.Report.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.greenpass.dtos.ResponseObject;
import com.example.greenpass.v1.Park.entities.Park;
import com.example.greenpass.v1.Park.services.ParkService;
import com.example.greenpass.v1.Report.dtos.AddReportDto;
import com.example.greenpass.v1.Report.services.ReportService;
import com.example.greenpass.v1.Stamp.entities.Stamp;
import com.example.greenpass.v1.Stamp.services.StampService;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @Mock
    private StampService stampService;

    @Mock
    private ParkService parkService;

    @InjectMocks
    private ReportController reportController;

    @Test
    void testHasUserBeenStampedToday_WhenNotStamped_ShouldReturn404() {
        String username = "testuser";
        when(stampService.hasUserBeenStamped(username)).thenReturn(null);

        ResponseEntity<ResponseObject> response = reportController.hasUserBeenStampedToday(username);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("User has not been stamped today", response.getBody().getMessage());
    }

    @Test
    void testHasUserBeenStampedToday_WhenStamped_ShouldReturn200AndPark() {
        String username = "testuser";
        Park park = new Park();
        park.setParkId(10);
        park.setName("อุทยานแห่งชาติดอยอินทนนท์");

        Stamp stamp = new Stamp();
        stamp.setPark(park);

        when(stampService.hasUserBeenStamped(username)).thenReturn(stamp);
        when(parkService.getParkById(10)).thenReturn(park);

        ResponseEntity<ResponseObject> response = reportController.hasUserBeenStampedToday(username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals(park, response.getBody().getResult());
    }

    @Test
    void testAddReport_WhenUserNotStamped_ShouldReturn400AndNotCallReportService() {
        String username = "testuser";
        AddReportDto dto = new AddReportDto("ต้นไม้ล้ม", "ขวางถนน", null, "ปกติ", 10);

        when(stampService.hasUserBeenStamped(username)).thenReturn(null);

        ResponseEntity<ResponseObject> response = reportController.addReport(username, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("User has not been stamped for this park today", response.getBody().getMessage());

        verify(reportService, never()).addReport(any(), any());
    }

    @Test
    void testAddReport_WhenUserStampedForDifferentPark_ShouldReturn400() {
        String username = "testuser";
        AddReportDto dto = new AddReportDto("ต้นไม้ล้ม", "ขวางถนน", null, "ปกติ", 10);

        Park differentPark = new Park();
        differentPark.setParkId(99); // Stamped at park 99, but reporting for park 10

        Stamp stamp = new Stamp();
        stamp.setPark(differentPark);

        when(stampService.hasUserBeenStamped(username)).thenReturn(stamp);

        ResponseEntity<ResponseObject> response = reportController.addReport(username, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("User has not been stamped for this park today", response.getBody().getMessage());

        verify(reportService, never()).addReport(any(), any());
    }

    @Test
    void testAddReport_WhenUserStamped_ShouldReturn201AndCallReportService() {
        String username = "testuser";
        AddReportDto dto = new AddReportDto("ต้นไม้ล้ม", "ขวางถนน", null, "ปกติ", 10);

        Park park = new Park();
        park.setParkId(10);

        Stamp stamp = new Stamp();
        stamp.setPark(park);

        when(stampService.hasUserBeenStamped(username)).thenReturn(stamp);

        ResponseEntity<ResponseObject> response = reportController.addReport(username, dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Add Report Success", response.getBody().getMessage());

        verify(reportService).addReport(dto, username);
    }
}
