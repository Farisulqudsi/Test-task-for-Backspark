package com.backspark.socksinventory.repository;

import com.backspark.socksinventory.dto.SocksFilterRequest;
import com.backspark.socksinventory.entity.Socks;

import java.util.List;

public interface SocksCustomRepository {
    List<Socks> findWithFilters(SocksFilterRequest filter);
}
