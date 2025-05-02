package ru.otus.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import ru.otus.model.Client;
import ru.otus.service.ClientService;

@RestController
@RequestMapping("/api/v1/client")
public class ClientRestController {
    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return clientService.getById(id);
    }

    @GetMapping
    public List<Client> getClientsByNameContains(@RequestParam(name = "name", required = false) String name) {
        if (name != null && !name.isEmpty()) {
            return clientService.getByNameContains(name);
        }
        return clientService.getAll();
    }

    @PostMapping
    public Client saveClient(@RequestBody Client client) {
        return clientService.save(client);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable(name = "id") long id) {
        clientService.delete(id);
    }
}
