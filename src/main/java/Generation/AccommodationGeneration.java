package Generation;

import com.datastax.oss.driver.api.core.CqlSession;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import entity.Accommodation;
import repository.AccommodationRepository;
import repository.CassandraConnection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

public class AccommodationGeneration {


    private CassandraConnection connection;
    private CqlSession session;
    private AccommodationRepository accommodationRepository;

    private final String accommodationsFilePath = "data/accommodations.csv";

    public AccommodationGeneration(){
        try{
            Reader reader = new BufferedReader(new FileReader(accommodationsFilePath));
            CsvToBean<Accommodation> csvReader = new CsvToBeanBuilder<Accommodation>(reader)
                    .withType(Accommodation.class)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();
            List<Accommodation> accommodations = csvReader.parse();
        }catch (FileNotFoundException e){
            System.out.println("File not found: " + accommodationsFilePath);
        }


//        connection = new CassandraConnection();
//        connection.connect();
//        session = connection.getSession();
//        accommodationRepository = new AccommodationRepository(session);
//
//        accommodationRepository.createTable();


    }

}
