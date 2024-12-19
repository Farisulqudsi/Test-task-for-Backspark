package com.backspark.socksinventory.mapper;

import com.backspark.socksinventory.dto.SocksRequest;
import com.backspark.socksinventory.dto.SocksResponse;
import com.backspark.socksinventory.entity.Socks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SocksMapperTest {

    private SocksMapper socksMapper;

    @BeforeEach
    void setUp() {
        socksMapper = new SocksMapper();
    }

    @Test
    @DisplayName("mapSocksToResponse should map Socks entity to SocksResponse correctly")
    void testMapSocksToResponse() {
        Socks socks = new Socks();
        socks.setId(1L);
        socks.setColor("red");
        socks.setCottonPart(50);
        socks.setQuantity(10);

        SocksResponse response = socksMapper.mapSocksToResponse(socks);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("red", response.getColor());
        assertEquals(50, response.getCottonPart());
        assertEquals(10, response.getQuantity());
    }

    @Test
    @DisplayName("updateEntityFromDto should update Socks entity from SocksRequest DTO")
    void testUpdateEntityFromDto() {
        Socks socks = new Socks();
        socks.setId(2L);
        socks.setColor("blue");
        socks.setCottonPart(30);
        socks.setQuantity(5);

        SocksRequest request = new SocksRequest("green", 60, 20);

        socksMapper.updateEntityFromDto(socks, request);

        assertEquals(2L, socks.getId()); // id не меняется
        assertEquals("green", socks.getColor());
        assertEquals(60, socks.getCottonPart());
        assertEquals(20, socks.getQuantity());
    }

    @Test
    @DisplayName("createEntityFromDto should create new Socks entity with initial quantity 0")
    void testCreateEntityFromDto() {
        SocksRequest request = new SocksRequest("yellow", 80, 15);

        Socks socks = socksMapper.createEntityFromDto(request);

        assertNotNull(socks);
        assertNull(socks.getId());
        assertEquals("yellow", socks.getColor());
        assertEquals(80, socks.getCottonPart());
        assertEquals(0, socks.getQuantity());
    }
}