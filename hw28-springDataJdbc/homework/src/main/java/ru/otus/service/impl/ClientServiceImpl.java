package ru.otus.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.otus.exception.DataNotFoundException;
import ru.otus.model.Client;
import ru.otus.repository.ClientRepository;
import ru.otus.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client getById(long id) {
        return clientRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Client not found, id:" + id));
    }

    @Override
    public List<Client> getByName(String name) {
        return clientRepository.findClientsByNameContainsIgnoreCase(name);
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public void delete(long id) {
        clientRepository.deleteById(id);
    }
}
