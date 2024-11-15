package com.booking.service;

import com.booking.dao.*;
import com.booking.dto.CityDto;
import com.booking.dto.HotelDto;
import com.booking.dto.HotelRequestDto;
import com.booking.entities.AppUser;
import com.booking.entities.City;
import com.booking.entities.Client;
import com.booking.entities.Hotel;
import com.booking.mappers.HotelMapper;
import com.booking.specifications.HotelSpecifications;
import com.booking.storage.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class HotelServiceImpl implements HotelService {

    private HotelRepository hotelRepository;
    private RoomImageRepository roomImageRepository;
    private FileStorageService storageService;
    private RoomRepository roomRepository;
    private CityRepository cityRepository;
    private UserRepository userRepository;
    @Value("${rooms.images.path}")
    String roomFolder;
    private HotelSpecifications specifications;
    private ReservationRepository reservationRepository;
    private HotelMapper mapper;

    public HotelServiceImpl(FileStorageService storageService, HotelRepository hotelRepository, RoomImageRepository roomImageRepository,
                            RoomRepository roomRepository, HotelMapper mapper, ReservationRepository reservationRepository, UserRepository userRepository, CityRepository cityRepository, HotelSpecifications specifications) {
        System.out.println(" creating session service !! ");
        this.cityRepository = cityRepository;
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
        this.roomImageRepository = roomImageRepository;
        this.userRepository = userRepository;
        this.specifications = specifications;
        this.mapper = mapper;

        this.storageService = storageService;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<CityDto> getAllCities() {
        return cityRepository.findAll().stream().map(mapper::fromCity).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public HotelDto saveHotel(HotelRequestDto requestDto, MultipartFile image) {


        City city = cityRepository.findById(requestDto.getCityId()).orElseThrow(() -> new EntityNotFoundException("City not found with id : " + requestDto.getCityId()));
        Hotel hotel = mapper.fromHotelRequest(requestDto);
        hotel.setCity(city);

        if (!image.isEmpty()) {
            hotel.setImageName(storageService.saveHotelImage(image));
            ;

        }

        hotel = hotelRepository.save(hotel);

        return mapper.fromHotel(hotel);


    }

    @Override
    public void deleteHotelById(Long id) {

        hotelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Hotel not exist with id " + id));


        hotelRepository.deleteById(id);


    }


    @Override
    public List<HotelDto> findHotels(String name, Long city, int stars, Pageable pageable) {


        Specification<Hotel> spec = Specification.where(null);

        //the name is not null by default is ""
        spec = spec.and(specifications.nameContains(name));

        if (stars > 0 && stars <= 5)
            spec = spec.and(specifications.starsEqual(stars));
        if (city > 0)
            spec = spec.and(specifications.cityIdIs(city));


        Page<Hotel> page = this.hotelRepository.findAll(spec, pageable);


        return page.getContent().stream()
                .map(hotel -> mapper.fromHotel(hotel))
                .collect(Collectors.toList());
    }


    @Override
    public HotelDto uploadHotelImage(Long hotelId, MultipartFile file) {


        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id : " + hotelId));
        String image = storageService.saveHotelImage(file);
        hotel.setImageName(image);

        hotel = hotelRepository.save(hotel);
        return mapper.fromHotel(hotel);

    }

    @Override
    @Transactional
    public void likeHotel(Long hotelId, Authentication authentication) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id : " + hotelId));

        AppUser user = (AppUser) authentication.getPrincipal();


        if (!(user instanceof Client))
            throw new RuntimeException("Not a client !");

        Client client = (Client) user;


        for (Hotel h : client.getFavorites()) {
            if (h.getId() == hotelId)
                return;
        }


        hotel.setNumberOfLike(hotel.getNumberOfLike() + 1);

        ((Client) user).getFavorites().add(hotel);

        userRepository.save(user);


    }

    @Override
    @Transactional
    public List<HotelDto> favoriteHotels(Authentication authentication) {

        AppUser user = (AppUser) authentication.getPrincipal();

        if (!(user instanceof Client)) {
            throw new RuntimeException("You're not a client !");

        }
        Client client = (Client) user;

        return client.getFavorites().stream().map(mapper::fromHotel).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public HotelDto updateHotel(Long hotelId, HotelRequestDto dto, MultipartFile image) {


        return hotelRepository.findById(hotelId)
                .map(hotel -> {

                    if (image != null) {
                        String imageName = storageService.saveHotelImage(image);

                        hotel.setImageName(imageName);
                    }

                    hotel.setStars(dto.getStars());
                    hotel.setName(dto.getName());
                    City city = cityRepository.findById(dto.getCityId())
                            .orElseThrow(() -> new EntityNotFoundException("City not found with id " + dto.getCityId()));
                    hotel.setCity(city);
                    hotel.setAddress(dto.getAddress());

                    if (hotel.getRooms() != null) hotel.getRooms()
                            .forEach(room -> System.out.println("room : " + room.getId() + " | price : " + room.getPrice()
                                    + " | surface : " + room.getSurface()));

                    return mapper.fromHotel(hotel);

                })
                .orElseThrow(() -> new EntityNotFoundException("Cannot find hotel with id " + hotelId));

    }


    @Override
    public HotelDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("cannot find an hotel with id  " + id));
        return mapper.fromHotel(hotel);

    }


}
