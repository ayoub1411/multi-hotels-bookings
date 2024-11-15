package com.booking.service;

import com.booking.dto.ClientResponse;
import com.booking.entities.Client;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    ClientResponse fetchClientById(String id);

    List<ClientResponse> findClientByFnameOrEmailOrLname(String keyword, Pageable pageable);

    List<ClientResponse> findPassiveClient(Pageable pageable);


}
