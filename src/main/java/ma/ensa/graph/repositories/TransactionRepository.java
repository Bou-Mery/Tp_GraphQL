package ma.ensa.graph.repositories;

import ma.ensa.graph.entities.Compte;
import ma.ensa.graph.entities.Transaction;
import ma.ensa.graph.entities.TypeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.compte = :compte")
    List<Transaction> findByCompte(@Param("compte") Compte compte);


    @Query("SELECT SUM(t.montant) FROM Transaction t WHERE t.typeTransaction = :typeTransaction")
    Double sumByType(@Param("typeTransaction") TypeTransaction typeTransaction);
}
