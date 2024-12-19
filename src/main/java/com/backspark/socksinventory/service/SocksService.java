package com.backspark.socksinventory.service;

import com.backspark.socksinventory.dto.SocksFilterRequest;
import com.backspark.socksinventory.dto.SocksRequest;
import com.backspark.socksinventory.dto.SocksResponse;
import com.backspark.socksinventory.entity.Socks;
import org.springframework.web.multipart.MultipartFile;

public interface SocksService {
    void addSocks(SocksRequest request);
    void removeSocks(SocksRequest request);

    int getSocksCount(SocksFilterRequest filter);

    SocksResponse updateSocks(Long id, SocksRequest request);

    void importSocksFromFile(MultipartFile file);

    Socks findById(Long id);
}
