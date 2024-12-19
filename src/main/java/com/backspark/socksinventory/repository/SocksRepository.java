package com.backspark.socksinventory.repository;

import com.backspark.socksinventory.entity.Socks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Long>, SocksCustomRepository {

    Optional<Socks> findByColorAndCottonPart(String color, Integer cottonPart);
}
