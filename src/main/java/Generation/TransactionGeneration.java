package Generation;

import com.datastax.oss.driver.api.core.CqlSession;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import entity.Transaction;
import repository.CassandraConnection;
import repository.TransactionRepository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class TransactionGeneration {
    private final static String transactionsFilePath = "data/transactions.csv";

    public static void generateAndSave(){
        List<Transaction> transactions = new ArrayList<>();
        try{
            Reader reader = new BufferedReader(new FileReader(transactionsFilePath));
            CsvToBean<Transaction> csvReader = new CsvToBeanBuilder<Transaction>(reader)
                    .withType(Transaction.class)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();
            transactions = csvReader.parse();
        }catch (FileNotFoundException e){
            System.out.println("File not found: " + transactionsFilePath);
        }

        CassandraConnection connection = new CassandraConnection();
        connection.connect("127.0.0.1", 9042, "cassandra");
        CqlSession session = connection.getSession();
        TransactionRepository transactionRepository = new TransactionRepository(session);
        transactionRepository.createTable();

        for (Transaction transaction : transactions){
            transactionRepository.insertOrUpdateTransaction(transaction);
        }
        connection.close();
    }
}
