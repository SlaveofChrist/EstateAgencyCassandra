package Generation;

import com.datastax.oss.driver.api.core.CqlSession;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import dao.TransactionDao;
import entity.Transaction;
import repository.AccommodationRepository;
import repository.CassandraConnection;
import repository.IndividualRepository;
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
        List<TransactionDao> transactions = new ArrayList<>();
        try{
            Reader reader = new BufferedReader(new FileReader(transactionsFilePath));
            CsvToBean<TransactionDao> csvReader = new CsvToBeanBuilder<TransactionDao>(reader)
                    .withType(TransactionDao.class)
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
        IndividualRepository individualRepository = new IndividualRepository(session);
        AccommodationRepository accommodationRepository = new AccommodationRepository(session);
        transactionRepository.createTable();

        for (TransactionDao transactionDao : transactions){
            Transaction transaction = new Transaction(
                    transactionDao.getCode(),
                    transactionDao.getDate(),
                    transactionDao.getCommission(),
                    transactionDao.getPrice(),
                    transactionDao.getType(),
                    individualRepository.selectByCode(transactionDao.getCustomerCode()),
                    individualRepository.selectByCode(transactionDao.getEstateAgentCode()),
                    individualRepository.selectByCode(transactionDao.getOwnerCode()),
                    accommodationRepository.selectByCode(transactionDao.getAccommodationCode())
            );
            transactionRepository.insertOrUpdateTransaction(transaction);
        }
        connection.close();
    }
}
