package ru.otus.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.exception.DataNotFoundException;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repository.ClientRepository;
import ru.otus.repository.PhoneRepository;
import ru.otus.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final PhoneRepository phoneRepository;

    public ClientServiceImpl(ClientRepository clientRepository, PhoneRepository phoneRepository) {
        this.clientRepository = clientRepository;
        this.phoneRepository = phoneRepository;
    }

    @Override
    @Transactional
    public Client save(Client client) {
        Client savedClient = clientRepository.save(new Client(null, client.name(), client.address(), null));

        if (!client.phones().isEmpty()) {
            List<Phone> phones = client.phones().stream()
                    .map(p -> new Phone(null, p.number(), savedClient.id()))
                    .toList();
            phoneRepository.saveAll(phones);
        }

        return clientRepository.findById(savedClient.id()).orElseThrow();
    }

    @Override
    public Client getById(long id) {
        return clientRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Client not found, id:" + id));
    }

    @Override
    public List<Client> getByNameContains(String name) {
        return clientRepository.findClientsByNameContainsIgnoreCase(name);
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(long id) {
        clientRepository.deleteById(id);
    }
}
