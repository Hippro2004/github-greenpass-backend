package com.example.greenpass.v1.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.greenpass.v1.entities.Announcement;
import com.example.greenpass.v1.repositories.AnnouncementRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {
    private AnnouncementRepository announcementRepository;

    @Override
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    @Override
    public Announcement getAnnouncementById(Long id) {
        return announcementRepository.findById(id).orElse(null);
    }

}
