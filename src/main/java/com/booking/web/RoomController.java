package com.booking.web;


import com.booking.dto.RoomDto;
import com.booking.dto.RoomRequest;
import com.booking.service.RoomService;
import com.booking.storage.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/rooms")
public class RoomController {

    RoomService roomService;
    FileStorageService storageService;

    public RoomController(RoomService roomService, FileStorageService storageService) {


        this.roomService = roomService;
        this.storageService = storageService;
    }

    @PostMapping(value = "/hotel/{hotelId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //
    public ResponseEntity<RoomDto> save(@PathVariable("hotelId") Long hotelId,
                                        @Valid @RequestPart("room") RoomRequest request,
                                        @RequestPart(value = "images", required = false) MultipartFile[] files) {

        return ResponseEntity.status(HttpStatus.OK).body(roomService.save(request, hotelId, files));


    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable("roomId") Long roomId
            , @Valid @RequestPart(value = "room", required = false) RoomRequest request
            , @RequestPart(value = "images", required = false) MultipartFile[] files
    ) {


        return ResponseEntity.status(HttpStatus.OK).body(roomService.updateRoom(roomId, request, files));


    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RoomDto>> rooms(@PathVariable("hotelId") Long hotelId
            , @RequestParam(name = "min", defaultValue = "0") double min
            , @RequestParam(value = "max", defaultValue = "0") double max
            , @RequestParam(value = "rooms", defaultValue = "0") int numberfRooms
            , @RequestParam(value = "page", defaultValue = "0") int page,

                                               @RequestParam(value = "items", defaultValue = "4") int items) {
        return ResponseEntity.status(HttpStatus.OK)

                .body(roomService.roomsByHotelId(hotelId, min, max, numberfRooms, PageRequest.of(page, items)));

    }


    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable("id") Long id) {

        return ResponseEntity.ok(roomService.getRoomById(id));

    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteRoom(@PathVariable("id") Long id) {

        roomService.deleteRoom(id);

        return ResponseEntity.ok("Delete successfully");
    }

    @GetMapping("/hotel/{id}/available")
    public List<RoomDto> available(@PathVariable("id") Long hotelId
            , @RequestParam(value = "min", defaultValue = "0") double min,
                                   @RequestParam(value = "max", defaultValue = "1000") double max
            , @RequestParam(value = "rooms", defaultValue = "1") int numberOfRooms,
                                   @RequestParam("checkIn") LocalDate checkIn, @RequestParam("checkOut") LocalDate checkOut) {


        return roomService.findAvailableRooms(hotelId, min, max, checkIn, checkOut, numberOfRooms);


    }

    @GetMapping("/image/{image}")
    public ResponseEntity<byte[]> roomImage(@PathVariable("image") String imageName) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok().headers(headers).body(storageService.getRoomlImage(imageName));
    }


}
