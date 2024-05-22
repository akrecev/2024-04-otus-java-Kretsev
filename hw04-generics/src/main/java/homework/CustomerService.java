package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings({"java:S1186", "java:S1135", "java:S1172"}) // при выполнении ДЗ эту аннотацию надо удалить
public class CustomerService {

    // todo: 3. надо реализовать методы этого класса
    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final TreeMap<Customer, String> customerData = new TreeMap<>(Comparator.comparing(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {

        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return customerData.firstEntry() == null
                ? null
                : getEntry(customerData.firstEntry()); // это "заглушка, чтобы скомилировать"
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return customerData.higherEntry(customer) == null
                ? null
                : getEntry(customerData.higherEntry(customer)); // это "заглушка, чтобы скомилировать"
    }

    public void add(Customer customer, String data) {
        customerData.put(customer, data);
    }

    private Map.Entry<Customer, String> getEntry(Map.Entry<Customer, String> entry) {
        return Map.entry(
                new Customer(
                        entry.getKey().getId(),
                        entry.getKey().getName(),
                        entry.getKey().getScores()),
                entry.getValue());
    }
}
