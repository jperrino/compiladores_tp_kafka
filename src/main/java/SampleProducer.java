import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import java.util.Properties;

public class SampleProducer {
    /*
    Class to connect to to Kafka server and send messages
    */
    private KafkaProducer kafkaProducer;
    private Properties producerProperties;
    private final String PRODUCER_PROPERTIES_FILE_PATH = "src/main/resources/producer.properties";

    public SampleProducer() {
        this.producerProperties = new Properties();

        try (InputStream input = new FileInputStream(PRODUCER_PROPERTIES_FILE_PATH)) {
            producerProperties.load(input);
        }
        catch(IOException e) {
            System.out.println("Unable to find " + PRODUCER_PROPERTIES_FILE_PATH);
        }
        this.kafkaProducer = new KafkaProducer(producerProperties);
    }

    public ProducerRecord createRecord(String record){
        String[] parts = record.split(",");
        String key = parts[0];
        String value = parts[1];
        //To send data to Kafka server, need to send it as a Producer Records object
        return new ProducerRecord(producerProperties.getProperty("topic.name"),key,value);
    }

    public KafkaProducer getProducer(){
        return this.kafkaProducer;
    }

    public Properties getProperties(){
        return this.producerProperties;
    }

    public static void main(String[] args) {
        SampleProducer sampleProducer = new SampleProducer();
        KafkaProducer producer = sampleProducer.getProducer();
        Scanner keyboard = new Scanner(System.in);
        System.out.printf("Sample producer started. Connected Kafka Server on %s\n",sampleProducer.getProperties().getProperty("bootstrap.servers"));
        System.out.printf("Enter key,value pairs to send to Topic: %s\n",sampleProducer.getProperties().getProperty("topic.name"));
        boolean flag = true;
        do{
            String record = keyboard.nextLine();
            if(!record.equals("exit")){
                producer.send(sampleProducer.createRecord(record));
            }
            else{
                flag = false;
            }
        }while(flag);
        producer.close();
    }
}
