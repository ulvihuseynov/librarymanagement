package com.librarymanagementsystem.fine.service.impl;

import com.librarymanagementsystem.fine.repository.FineRepository;
import com.librarymanagementsystem.fine.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;
}
