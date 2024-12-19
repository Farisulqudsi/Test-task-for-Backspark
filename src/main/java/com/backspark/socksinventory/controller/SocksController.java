package com.backspark.socksinventory.controller;

import com.backspark.socksinventory.dto.SocksFilterRequest;
import com.backspark.socksinventory.dto.SocksRequest;
import com.backspark.socksinventory.dto.SocksResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequestMapping("/api/socks")
@Tag(name = "Socks API", description = "Операции с носками")
public interface SocksController {

    @GetMapping
    @Operation(summary = "Поиск носков", description = "Фильтрует носки по параметрам")
    @ApiResponse(responseCode = "200", description = "Количество найденных носков",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = "integer")))
    ResponseEntity<Integer> getSocksCountByFilter(@ModelAttribute SocksFilterRequest filter);

    @PostMapping("/income")
    @Operation(summary = "Приход носков", description = "Добавляет носки в систему")
    @ApiResponse(responseCode = "201", description = "Носки успешно добавлены")
    @ApiResponse(responseCode = "400", description = "Неверные входные данные",
            content = @Content)    ResponseEntity<Void> addSocks(@Valid @RequestBody SocksRequest request);

    @PostMapping("/outcome")
    @Operation(summary = "Расход носков", description = "Списывает носки из системы")
    @ApiResponse(responseCode = "200", description = "Носки успешно списаны")
    @ApiResponse(responseCode = "400", description = "Неверные входные данные или недостаточно носков",
            content = @Content)    ResponseEntity<Void> removeSocks(@Valid @RequestBody SocksRequest request);

    @PutMapping("/{id}")
    @Operation(summary = "Обновление носков", description = "Обновляет информацию о носках по ID")
    @ApiResponse(responseCode = "200", description = "Носки успешно обновлены",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SocksResponse.class)))
    @ApiResponse(responseCode = "404", description = "Носки с таким ID не найдены",
            content = @Content)
    ResponseEntity<SocksResponse> updateSocks(
            @Parameter(description = "ID носков для обновления") @PathVariable Long id,
            @RequestBody @Valid SocksRequest request);

    @PostMapping("/batch")
    @Operation(summary = "Импорт носков пакетно", description = "Импортирует носки из файла")
    @ApiResponse(responseCode = "200", description = "Файл успешно обработан и данные импортированы")
    @ApiResponse(responseCode = "400", description = "Неверный формат файла или данные",
            content = @Content)
    ResponseEntity<Void> importSocksFromFile(
            @Parameter(description = "CSV файл с данными носков")@RequestParam("file") MultipartFile file);
}
