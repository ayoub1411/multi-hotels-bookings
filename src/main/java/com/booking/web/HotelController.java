package com.booking.web;


import com.booking.dto.CityDto;
import com.booking.dto.HotelDto;
import com.booking.dto.HotelRequestDto;
import com.booking.service.HotelService;
import com.booking.storage.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    HotelService hotelService;
    @Autowired
    FileStorageService storageService;

    @GetMapping
    List<HotelDto> hotels(@RequestParam(value = "name", defaultValue = "") String name,
                          @RequestParam(value = "stars", defaultValue = "0") int stars,
                          @RequestParam(value = "city", defaultValue = "0") long city, Model model,
                          @RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "items", defaultValue = "4") int items) {


        return hotelService.findHotels(name, city, stars, PageRequest.of(page, items));


    }


    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotel(@PathVariable("id") Long id) throws Exception {


        return ResponseEntity.status(HttpStatus.OK).body(hotelService.getHotelById(id));

    }

    @GetMapping("/cities")
    public ResponseEntity<List<CityDto>> getCities() throws Exception {


        return ResponseEntity.status(HttpStatus.OK).body(hotelService.getAllCities());

    }

    @PostMapping
    HotelDto saveHotel(@RequestPart("hotel") @Valid HotelRequestDto requestDto,
                       @RequestPart(value = "image", required = false) MultipartFile image) {


        return hotelService.saveHotel(requestDto, image);

    }

    @PostMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HotelDto upload(@PathVariable("id") Long id, @RequestParam("image") MultipartFile file) {

        return hotelService.uploadHotelImage(id, file);

    }

    @PutMapping("/{id}")
    public HotelDto updateHotel(@PathVariable("id") Long idHotel, @Valid @RequestPart("hotel") HotelRequestDto dto, @RequestPart(value = "image", required = false) MultipartFile file) {


        return hotelService.updateHotel(idHotel, dto, file);


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {

        hotelService.deleteHotelById(id);

        return ResponseEntity.status(HttpStatus.OK).body("Delete succesfully !");
    }

    @GetMapping("/image/{image}")
    public ResponseEntity<byte[]> hotelImage(@PathVariable("image") String imageName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok().headers(headers).body(storageService.getHotelImage(imageName));

    }

    @PutMapping("/like")
    public ResponseEntity<String> addFavorite(@RequestParam("hotel") Long hotel, Authentication authentication) {
        hotelService.likeHotel(hotel, authentication);


        return new ResponseEntity<>("Hotel added to your favorite list !", HttpStatus.OK);


    }


    @GetMapping("/liked")
    public ResponseEntity<List<HotelDto>> findFavorites(Authentication authentication) {

        return ResponseEntity.ok(hotelService.favoriteHotels(authentication));


    }

}
