package com.backspark.socksinventory.repository;

import com.backspark.socksinventory.dto.SocksFilterRequest;
import com.backspark.socksinventory.entity.Socks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/testdata/socks_data.sql", config = @SqlConfig(commentPrefix = "--"))
class SocksCustomRepositoryImplTest extends AbstractTestContainersIT {

    @Autowired
    private SocksRepository socksRepository;

    @Autowired
    private EntityManager em;

    private SocksCustomRepositoryImpl customRepository;

    @BeforeEach
    void setUp() {
        customRepository = new SocksCustomRepositoryImpl();
        customRepository.setEntityManager(em);
    }

    @Test
    @DisplayName("Should filter by color")
    void testFilterByColor() {
        SocksFilterRequest filter = new SocksFilterRequest();
        filter.setColor("red");

        List<Socks> result = customRepository.findWithFilters(filter);
        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(s -> "red".equals(s.getColor()));
    }

    @Test
    @DisplayName("Should filter by operation moreThan")
    void testFilterByOperationMoreThan() {
        SocksFilterRequest filter = new SocksFilterRequest();
        filter.setOperation("moreThan");
        filter.setCottonPart(50);

        List<Socks> result = customRepository.findWithFilters(filter);
        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(s -> s.getCottonPart() > 50);
    }

    @Test
    @DisplayName("Should filter by operation lessThan")
    void testFilterByOperationLessThan() {
        SocksFilterRequest filter = new SocksFilterRequest();
        filter.setOperation("lessThan");
        filter.setCottonPart(30);

        List<Socks> result = customRepository.findWithFilters(filter);
        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(s -> s.getCottonPart() < 30);
    }

    @Test
    @DisplayName("Should filter by operation equal")
    void testFilterByOperationEqual() {
        SocksFilterRequest filter = new SocksFilterRequest();
        filter.setOperation("equal");
        filter.setCottonPart(70);

        List<Socks> result = customRepository.findWithFilters(filter);
        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(s -> s.getCottonPart() == 70);
    }

    @Test
    @DisplayName("Should filter by range of cottonPart")
    void testFilterByRange() {
        SocksFilterRequest filter = new SocksFilterRequest();
        filter.setCottonPartFrom(40);
        filter.setCottonPartTo(60);

        List<Socks> result = customRepository.findWithFilters(filter);
        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(s -> s.getCottonPart() >= 40 && s.getCottonPart() <= 60);
    }

    @Test
    @DisplayName("Should apply sorting by color ascending")
    void testSortingByColorAsc() {
        SocksFilterRequest filter = new SocksFilterRequest();
        filter.setSortBy("color");
        filter.setDirection("asc");

        List<Socks> result = customRepository.findWithFilters(filter);
        assertThat(result).isNotEmpty();

        for (int i = 1; i < result.size(); i++) {
            assertThat(result.get(i).getColor().compareTo(result.get(i - 1).getColor())).isGreaterThanOrEqualTo(0);
        }
    }

    @Test
    @DisplayName("Should apply sorting by cottonPart descending")
    void testSortingByCottonPartDesc() {
        SocksFilterRequest filter = new SocksFilterRequest();
        filter.setSortBy("cottonPart");
        filter.setDirection("desc");

        List<Socks> result = customRepository.findWithFilters(filter);
        assertThat(result).isNotEmpty();

        for (int i = 1; i < result.size(); i++) {
            assertThat(result.get(i).getCottonPart()).isLessThanOrEqualTo(result.get(i - 1).getCottonPart());
        }
    }

    @Test
    @DisplayName("Should filter by color and operation")
    void testColorAndOperation() {
        SocksFilterRequest filter = new SocksFilterRequest();
        filter.setColor("blue");
        filter.setOperation("moreThan");
        filter.setCottonPart(50);

        List<Socks> result = customRepository.findWithFilters(filter);
        assertThat(result).isNotEmpty();
        assertThat(result).allSatisfy(s -> {
            assertThat(s.getColor()).isEqualTo("blue");
            assertThat(s.getCottonPart()).isGreaterThan(50);
        });
    }}
