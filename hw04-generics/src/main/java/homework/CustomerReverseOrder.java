package homework;

import java.util.*;

public class CustomerReverseOrder {
    private final Deque<Customer> customers = new LinkedList<>();

    public void add(Customer customer) {
        customers.addFirst(customer);
    }

    public Customer take() {
        return customers.pollFirst();
    }
}
