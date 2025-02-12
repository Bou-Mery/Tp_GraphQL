package ma.ensa.graph.controllers;

import lombok.AllArgsConstructor;
import ma.ensa.graph.entities.Compte;
import ma.ensa.graph.entities.Transaction;
import ma.ensa.graph.entities.TransactionRequest;
import ma.ensa.graph.entities.TypeTransaction;
import ma.ensa.graph.repositories.CompteRepository;
import ma.ensa.graph.repositories.TransactionRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CompteControllerGraphQL {

    private CompteRepository compteRepository;
    private TransactionRepository transactionRepository;

    @QueryMapping
    public List<Compte> allComptes() {
        return compteRepository.findAll();
    }

    @QueryMapping
    public Compte compteById(@Argument Long id) {
        Compte compte = compteRepository.findById(id).orElse(null);
        if ( compte == null ) throw new RuntimeException(String.format("Compte %s not found", id));
        else return compte;
    }

    @MutationMapping
    public Compte saveCompte(@Argument Compte compte) {
        return compteRepository.save(compte);
    }

    @QueryMapping
    public Map<String , Object > totalSolde(){
        long count = compteRepository.count();
        double sum = compteRepository.sumSoldes();
        double average =  count >0 ? sum/count : 0.0;

        return  Map.of(
                "count" , count ,
                "sum" , sum ,
                "average" , average
        );
    }


    @MutationMapping
    public Transaction addTransaction(@Argument TransactionRequest transactionRequest) {
        if (transactionRequest == null) {
            throw new IllegalArgumentException("Transaction request cannot be null");
        }

        Compte compte = compteRepository.findById(transactionRequest.getCompteId())
                .orElseThrow(() -> new RuntimeException("Compte not found"));

        Transaction transaction = new Transaction();
        transaction.setMontant(transactionRequest.getMontant());
        transaction.setDate(transactionRequest.getDate());
        transaction.setTypeTransaction(transactionRequest.getTypeTransaction());
        transaction.setCompte(compte);

        return transactionRepository.save(transaction);
    }


    @QueryMapping
    public List<Transaction> compteTransactions(@Argument Long id){
        Compte compte = compteRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("Compte %s not found", id))
        );
        return transactionRepository.findByCompte(compte);
    }

    @QueryMapping
    public Map<String , Object> transactionStats(){
        long count = transactionRepository.count();
        double sumDepots = transactionRepository.sumByType(TypeTransaction.DEPOT);
        double sumRetraits = transactionRepository.sumByType(TypeTransaction.RETRAIT);

        return  Map.of(
                "count" , count ,
                "sumDepots" , sumDepots,
                "sumRetraits" , sumRetraits
        );

    }

}
