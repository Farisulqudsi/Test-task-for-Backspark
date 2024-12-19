package com.backspark.socksinventory.controller;

import com.backspark.socksinventory.dto.SocksFilterRequest;
import com.backspark.socksinventory.dto.SocksRequest;
import com.backspark.socksinventory.dto.SocksResponse;
import com.backspark.socksinventory.service.SocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class SocksControllerImpl implements SocksController {

    private final SocksService socksService;

    @Override
    public ResponseEntity<Integer> getSocksCountByFilter(SocksFilterRequest filter) {
        return ResponseEntity.ok(socksService.getSocksCount(filter));
    }

    @Override
    public ResponseEntity<Void> addSocks(SocksRequest request) {
        socksService.addSocks(request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removeSocks(SocksRequest request) {
        socksService.removeSocks(request);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<SocksResponse> updateSocks(Long id, SocksRequest request) {
        return ResponseEntity.ok(socksService.updateSocks(id, request));
    }

    @Override
    public ResponseEntity<Void> importSocksFromFile(MultipartFile file) {
        socksService.importSocksFromFile(file);
        return ResponseEntity.ok().build();
    }
}
