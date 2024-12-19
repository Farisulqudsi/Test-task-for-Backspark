package com.backspark.socksinventory.service;

import com.backspark.socksinventory.dto.SocksFilterRequest;
import com.backspark.socksinventory.dto.SocksRequest;
import com.backspark.socksinventory.dto.SocksResponse;
import com.backspark.socksinventory.entity.Socks;
import com.backspark.socksinventory.exception.FileProcessingException;
import com.backspark.socksinventory.exception.NotEnoughSocksException;
import com.backspark.socksinventory.mapper.SocksMapper;
import com.backspark.socksinventory.repository.SocksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocksServiceImplTest {

    @Mock
    private SocksRepository socksRepository;

    @Mock
    private SocksMapper socksMapper;

    @InjectMocks
    private SocksServiceImpl socksService;

    private SocksRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new SocksRequest("red", 50, 10);
    }

    @Test
    @DisplayName("income should create new socks if not found and set quantity")
    void testIncomeCreateNew() {
        when(socksRepository.findByColorAndCottonPart("red", 50)).thenReturn(Optional.empty());
        Socks newSocks = new Socks();
        newSocks.setColor("red");
        newSocks.setCottonPart(50);
        newSocks.setQuantity(0);
        when(socksMapper.createEntityFromDto(validRequest)).thenReturn(newSocks);

        socksService.addSocks(validRequest);

        verify(socksRepository).save(newSocks);
        assertThat(newSocks.getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("income should increase quantity if socks found")
    void testIncomeExisting() {
        Socks existing = new Socks();
        existing.setColor("red");
        existing.setCottonPart(50);
        existing.setQuantity(5);

        when(socksRepository.findByColorAndCottonPart("red", 50)).thenReturn(Optional.of(existing));

        socksService.addSocks(validRequest);

        verify(socksRepository).save(existing);
        assertThat(existing.getQuantity()).isEqualTo(15);
    }

    @Test
    @DisplayName("outcome should decrease quantity if enough socks exist")
    void testOutcomeEnough() {
        Socks existing = new Socks();
        existing.setColor("red");
        existing.setCottonPart(50);
        existing.setQuantity(20);

        when(socksRepository.findByColorAndCottonPart("red", 50)).thenReturn(Optional.of(existing));

        socksService.removeSocks(validRequest);

        verify(socksRepository).save(existing);
        assertThat(existing.getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("outcome should throw NotEnoughSocksException if not enough")
    void testOutcomeNotEnough() {
        Socks existing = new Socks();
        existing.setColor("red");
        existing.setCottonPart(50);
        existing.setQuantity(5);

        when(socksRepository.findByColorAndCottonPart("red", 50)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> socksService.removeSocks(validRequest))
                .isInstanceOf(NotEnoughSocksException.class)
                .hasMessageContaining("Not enough socks");
    }

    @Test
    @DisplayName("outcome should throw NotEnoughSocksException if no socks found")
    void testOutcomeNotFound() {
        when(socksRepository.findByColorAndCottonPart("red", 50)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> socksService.removeSocks(validRequest))
                .isInstanceOf(NotEnoughSocksException.class)
                .hasMessageContaining("No socks found");
    }

    @Test
    @DisplayName("getSocksCount should return sum of quantities from filtered socks")
    void testGetSocksCount() {
        SocksFilterRequest filter = new SocksFilterRequest();
        // имитируем репозиторский метод
        Socks s1 = new Socks();
        s1.setQuantity(10);
        Socks s2 = new Socks();
        s2.setQuantity(20);

        when(socksRepository.findWithFilters(filter)).thenReturn(List.of(s1, s2));

        int count = socksService.getSocksCount(filter);
        assertThat(count).isEqualTo(30);
    }

    @Test
    @DisplayName("updateSocks should update entity from dto and return response")
    void testUpdateSocks() {
        Socks existing = new Socks();
        existing.setId(1L);
        SocksResponse response = new SocksResponse();
        response.setId(1L);
        response.setColor("red");
        response.setCottonPart(50);
        response.setQuantity(10);

        when(socksRepository.findById(1L)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> {
            // имитируем обновление через mapper
            Socks socksArg = invocation.getArgument(0);
            SocksRequest dtoArg = invocation.getArgument(1);
            socksArg.setColor(dtoArg.getColor());
            socksArg.setCottonPart(dtoArg.getCottonPart());
            socksArg.setQuantity(dtoArg.getQuantity());
            return null;
        }).when(socksMapper).updateEntityFromDto(any(Socks.class), any(SocksRequest.class));

        when(socksMapper.mapSocksToResponse(existing)).thenReturn(response);

        SocksResponse result = socksService.updateSocks(1L, validRequest);
        verify(socksRepository).save(existing);
        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("updateSocks should throw NotEnoughSocksException if not found")
    void testUpdateSocksNotFound() {
        when(socksRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> socksService.updateSocks(1L, validRequest))
                .isInstanceOf(NotEnoughSocksException.class)
                .hasMessageContaining("Socks not found");
    }

    @Test
    @DisplayName("importSocksBatch should process file and call income for each line")
    void testImportSocksBatch() throws Exception {
        String fileContent = "red,70,10\nblue,50,20";
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent.getBytes()));

        Socks newSocks = new Socks();
        newSocks.setColor("red");
        newSocks.setCottonPart(70);
        newSocks.setQuantity(0);
        when(socksMapper.createEntityFromDto(any(SocksRequest.class))).thenReturn(newSocks);

        ArgumentCaptor<SocksRequest> captor = ArgumentCaptor.forClass(SocksRequest.class);

        socksService.importSocksFromFile(mockFile);

        verify(socksRepository, times(2)).findByColorAndCottonPart(anyString(), anyInt());
        verify(socksRepository, times(2)).save(any(Socks.class));
        verify(socksMapper, times(2)).createEntityFromDto(any(SocksRequest.class));

        // Можно дополнительно проверить аргументы вызовов income.
        // Но т.к. income — внутренний метод сервиса, мы можем проверить через репозиторий/mapper вызовы.
        // Или использовать doAnswer на income, но это сложнее, так как мы тестируем сам сервис.
    }

    @Test
    @DisplayName("importSocksBatch should throw FileProcessingException on invalid format")
    void testImportSocksBatchInvalidFormat() throws Exception {
        String fileContent = "invalid_line";
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent.getBytes()));

        assertThatThrownBy(() -> socksService.importSocksFromFile(mockFile))
                .isInstanceOf(FileProcessingException.class)
                .hasMessageContaining("Error processing file");
    }

    @Test
    @DisplayName("findById should return Socks if found")
    void testFindById() {
        Socks s = new Socks();
        s.setId(10L);
        when(socksRepository.findById(10L)).thenReturn(Optional.of(s));

        Socks result = socksService.findById(10L);
        assertThat(result).isSameAs(s);
    }

    @Test
    @DisplayName("findById should throw NotEnoughSocksException if not found")
    void testFindByIdNotFound() {
        when(socksRepository.findById(10L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> socksService.findById(10L))
                .isInstanceOf(NotEnoughSocksException.class)
                .hasMessageContaining("Socks not found");
    }
}
