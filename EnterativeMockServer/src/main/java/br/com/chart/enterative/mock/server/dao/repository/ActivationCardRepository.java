package br.com.chart.enterative.mock.server.dao.repository;

import br.com.chart.enterative.mock.server.entity.ActivationCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author William Leite
 */
public interface ActivationCardRepository extends JpaRepository<ActivationCard, Long> {

    public ActivationCard findByEanStartingWithAndCardNumber(String ean, String cardNumber);

    @Modifying
    @Transactional
    @Query("DELETE FROM ActivationCard WHERE id = :id")
    public void deleteByID(@Param("id") Long id);
}
