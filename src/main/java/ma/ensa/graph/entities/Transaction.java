package ma.ensa.graph.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double montant;

   // @Column(name = "date", columnDefinition = "DATE")
    private String date;

    @Enumerated(EnumType.STRING)
    private TypeTransaction typeTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_id")
    private Compte compte;
}
