package com.booking.web;


import com.booking.dto.ClientResponse;
import com.booking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@CrossOrigin("*")
public class ClientController {
    @Autowired
    ClientService clientService;


    @GetMapping("/admin")
//just for admins
    ResponseEntity<List<ClientResponse>> findAll(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                 @RequestParam(value = "items", defaultValue = "4") int items) {

        return ResponseEntity.ok(clientService.findClientByFnameOrEmailOrLname(keyword, PageRequest.of(page, items)));


    }

    @GetMapping("/{id}")
    ResponseEntity<ClientResponse> findById(@PathVariable("id") String id) {

        return ResponseEntity.ok(clientService.fetchClientById(id));

    }


}
