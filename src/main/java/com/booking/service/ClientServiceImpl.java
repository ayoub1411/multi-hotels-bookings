package com.booking.service;

import com.booking.dao.ClientRepository;
import com.booking.dto.ClientResponse;
import com.booking.mappers.HotelMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ClientServiceImpl implements ClientService {

    ClientRepository clientRepository;
    HotelMapper mapper;

    public ClientServiceImpl(ClientRepository clientRepository, HotelMapper mapper) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public HotelMapper getMapper() {
        return mapper;
    }

    public void setMapper(HotelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ClientResponse fetchClientById(String id) {

        return clientRepository.findById(id).map(mapper::fromAppUser).orElseThrow(() -> new EntityNotFoundException("Cannot found a client with this id : " + id));

    }

    @Override
    public List<ClientResponse> findClientByFnameOrEmailOrLname(String keyword, Pageable pageable) {

        return clientRepository.findClientByLnameOrFnameOrEmail(keyword, pageable).getContent().stream().map(mapper::fromAppUser).collect(Collectors.toList());
    }

    @Override
    public List<ClientResponse> findPassiveClient(Pageable pageable) {
        return clientRepository.findPassiveClients(pageable).getContent().stream().map(mapper::fromAppUser).collect(Collectors.toList());
    }
}
