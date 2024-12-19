package com.backspark.socksinventory.mapper;

import com.backspark.socksinventory.dto.SocksRequest;
import com.backspark.socksinventory.dto.SocksResponse;
import com.backspark.socksinventory.entity.Socks;
import org.springframework.stereotype.Component;

@Component
public class SocksMapper {

    public SocksResponse mapSocksToResponse(Socks socks) {
        SocksResponse dto = new SocksResponse();

        dto.setId(socks.getId());
        dto.setColor(socks.getColor());
        dto.setCottonPart(socks.getCottonPart());
        dto.setQuantity(socks.getQuantity());

        return dto;
    }

    public void updateEntityFromDto(Socks socks, SocksRequest dto) {
        socks.setColor(dto.getColor());
        socks.setCottonPart(dto.getCottonPart());
        socks.setQuantity(dto.getQuantity());
    }

    public Socks createEntityFromDto(SocksRequest dto) {
        Socks socks = new Socks();
        socks.setColor(dto.getColor());
        socks.setCottonPart(dto.getCottonPart());
        socks.setQuantity(0);
        return socks;
    }



}
