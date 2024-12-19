package com.backspark.socksinventory.service;

import com.backspark.socksinventory.dto.SocksFilterRequest;
import com.backspark.socksinventory.dto.SocksRequest;
import com.backspark.socksinventory.dto.SocksResponse;
import com.backspark.socksinventory.entity.Socks;
import com.backspark.socksinventory.exception.FileProcessingException;
import com.backspark.socksinventory.exception.NotEnoughSocksException;
import com.backspark.socksinventory.mapper.SocksMapper;
import com.backspark.socksinventory.repository.SocksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SocksServiceImpl implements SocksService {

    private final SocksRepository socksRepository;
    private final SocksMapper socksMapper;

    @Transactional
    @Override
    public void addSocks(SocksRequest request) {
        Socks socks = socksRepository.findByColorAndCottonPart(request.getColor(), request.getCottonPart())
                .orElseGet(() -> socksMapper.createEntityFromDto(request));

        socks.setQuantity(socks.getQuantity() + request.getQuantity());
        socksRepository.save(socks);
    }

    @Transactional
    @Override
    public void removeSocks(SocksRequest request) {
        Socks socks = socksRepository.findByColorAndCottonPart(request.getColor(), request.getCottonPart())
                .orElseThrow(() -> new NotEnoughSocksException("No socks found with color: "
                        + request.getColor() + " and cottonPart: " + request.getCottonPart()));

        if (socks.getQuantity() < request.getQuantity()) {
            throw new NotEnoughSocksException("Not enough socks in stock. Available: "
                    + socks.getQuantity() + ", requested: " + request.getQuantity());
        }

        socks.setQuantity(socks.getQuantity() - request.getQuantity());
        socksRepository.save(socks);
    }

    @Override
    public int getSocksCount(SocksFilterRequest filter) {
        List<Socks> filteredSocks = socksRepository.findWithFilters(filter);
        return filteredSocks.stream().mapToInt(Socks::getQuantity).sum();
    }

    @Transactional
    @Override
    public SocksResponse updateSocks(Long id, SocksRequest request) {
        Socks socks = socksRepository.findById(id)
                .orElseThrow(() -> new NotEnoughSocksException("Socks not found"));
        socksMapper.updateEntityFromDto(socks, request);
        socksRepository.save(socks);
        return socksMapper.mapSocksToResponse(socks);
    }

    @Transactional
    @Override
    public void importSocksFromFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(this::parseLineToDto)
                    .forEach(this::addSocks);
        } catch (Exception e) {
            throw new FileProcessingException("Error processing file", e);
        }
    }
    private SocksRequest parseLineToDto(String line) {
        String[] parts = line.split(",");
        if (parts.length != 3) {
            throw new FileProcessingException("Invalid file format line: " + line, null);
        }
        String color = parts[0].trim();
        int cotton;
        int quantity;
        try {
            cotton = Integer.parseInt(parts[1].trim());
            quantity = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new FileProcessingException("Invalid numeric values in line: " + line, e);
        }
        return new SocksRequest(color, cotton, quantity);
    }

    @Override
    public Socks findById(Long id) {
        return socksRepository.findById(id).orElseThrow(() -> new NotEnoughSocksException("Socks not found"));
    }
}
