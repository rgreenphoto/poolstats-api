package com.green.poolstatsapi.repositry;

import com.green.poolstatsapi.models.Format;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import com.green.poolstatsapi.repository.FormatRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FormatRepositoryTests {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private FormatRepository formatRepository;

    @Test
    public void whenGetById_thenReturnFormat() {
        Format apa = new Format();
        apa.setName("apa");
        entityManager.persist(apa);
        entityManager.flush();

        Format found = formatRepository.getOne(apa.getId());

        assertThat(found.getName())
            .isEqualTo(apa.getName());
    }
}
