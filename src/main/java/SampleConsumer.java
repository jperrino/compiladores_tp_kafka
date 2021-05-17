import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class SampleConsumer {
    /*
    Class to connect to to Kafka server and receive messages
    */
    private Properties consumerProperties;
    private final String CONSUMER_PROPERTIES_FILE_PATH = "src/main/resources/consumer.properties";

    public SampleConsumer() {
        this.consumerProperties = new Properties();

        try (InputStream input = new FileInputStream(CONSUMER_PROPERTIES_FILE_PATH)) {
            consumerProperties.load(input);
        }
        catch(IOException e) {
            System.out.println("Unable to find " + CONSUMER_PROPERTIES_FILE_PATH);
        }

        KafkaConsumer<String,String> kafkaConsumer = new KafkaConsumer<String, String>(consumerProperties);

        /*
        In order to receive messages from the begginig of the Topic,
        first Kafka first gets a heartbeat from consumer by
        making the consumer call the poll method
         */
        kafkaConsumer.subscribe(Arrays.asList(consumerProperties.getProperty("topic.name")));
        /*
        At this point, there is no heartbeat from consumer so seekToBeinning() wont work
        so call poll()
         */
        kafkaConsumer.poll(0);
        /*
        Now there is heartbeat and consumer is "alive"
        Start from offset=0, the beginning
         */
        kafkaConsumer.seekToBeginning(kafkaConsumer.assignment());

        System.out.printf("Sample consumer started. Connected to Kafka Server on %s\n",consumerProperties.getProperty("bootstrap.servers"));
        System.out.printf("Subscribed to Topic: %s\n",consumerProperties.getProperty("topic.name"));

        while(true){
            ConsumerRecords<String,String> consumerRecords = kafkaConsumer.poll(100);
            for(ConsumerRecord<String,String> consumerRecord:consumerRecords){
                System.out.printf("Offset= %d, Key=%s, Value=%s\n",consumerRecord.offset(),consumerRecord.key(),consumerRecord.value());
            }
        }
    }

    public static void main(String[] args) {
        SampleConsumer sampleConsumer = new SampleConsumer();
    }
}
