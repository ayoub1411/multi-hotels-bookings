package com.booking.service;

import com.booking.dao.HotelRepository;
import com.booking.dao.RoomImageRepository;
import com.booking.dao.RoomRepository;
import com.booking.dto.RoomDto;
import com.booking.dto.RoomRequest;
import com.booking.entities.Hotel;
import com.booking.entities.Room;
import com.booking.entities.RoomImage;
import com.booking.mappers.HotelMapper;
import com.booking.specifications.RoomSpecification;
import com.booking.storage.FileStorageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

RoomRepository roomRepository;

HotelRepository hotelRepository;

RoomImageRepository roomImageRepository;

RoomSpecification roomSpecification;

HotelMapper mapper;
FileStorageService storageService;
public RoomServiceImpl(RoomRepository roomRepository,HotelRepository hotelRepository,HotelMapper mapper
        ,RoomSpecification roomSpecification,RoomImageRepository roomImageRepository,FileStorageService storageService){

    this.roomSpecification=roomSpecification;
    this.roomRepository=roomRepository;
    this.hotelRepository=hotelRepository;
    this.mapper=mapper;
    this.storageService=storageService;
    this.roomImageRepository=roomImageRepository;

}

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public RoomDto save(RoomRequest request, Long hotelId, MultipartFile[] files) {


        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()->new EntityNotFoundException("Cannot find hotel with id "+hotelId));
        Room room=new Room();

        room.setNumberOfRooms(request.getNumberOfRooms());
        room.setPrice(request.getPrice());
        room.setSurface(request.getSurface());
        room.setHotel(hotel);
        roomRepository.save(room);

        if(files!=null && files.length!=0){

            for(MultipartFile image:files){

               String imageName= storageService.saveRoomImage(image);

                RoomImage roomImage=new RoomImage();
                roomImage.setName(imageName);

                roomImage.setRoom(room);

                roomImageRepository.save(roomImage);


            }


        }

        return mapper.fromRoom(room);
    }

    @Override
    public List<RoomDto> roomsByHotelId(Long id,double min, double max,int numberOfRooms,Pageable pageable) {
        Specification<Room>specification= Specification.where(null);
        System.out.println(min+" "+max+" "+numberOfRooms);
        //check if exist
        hotelRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Hotel not found with id  "+id));

        specification=specification.and(roomSpecification.hotelId(id));
        if(max>0)
            specification=specification.and(roomSpecification.priceBetween(min,max));
        if(numberOfRooms>0)
            specification=specification.and(roomSpecification.numberOfRooms(numberOfRooms));



        return roomRepository.findAll(specification,pageable)
                .getContent().stream()
                .map(mapper::fromRoom)
                .toList();
    }

    @Override
    public List<RoomDto> findAvailableRooms(Long hotelId, double minPrice, double maxPrice, LocalDate checkInDate, LocalDate checkOutDate,int numberOfRooms) {
        return roomRepository
                .findAvailableRooms(hotelId,minPrice,maxPrice,checkInDate,checkOutDate,numberOfRooms)
                .stream()
                .map(mapper::fromRoom)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public RoomDto updateRoom(Long roomId, RoomRequest request, MultipartFile[] newImages) {

    Room room=roomRepository.findById(roomId)
            .orElseThrow(()->new EntityNotFoundException("Room not found with id : "+roomId));

    if(request!=null){
    room.setNumberOfRooms(request.getNumberOfRooms());
    room.setSurface(request.getSurface());
    room.setPrice(request.getPrice());}
      System.out.println("size before adding new image"+room.getRoomImages().size());

    if(newImages!=null && newImages.length>0)
        for(MultipartFile file:newImages){

            String imageName=storageService.saveRoomImage(file);
            RoomImage roomImage=new RoomImage();
            roomImage.setName(imageName);
            roomImage.setRoom(room);
            roomImageRepository.save(roomImage);

        }

        System.out.println("size after adding new image"+room.getRoomImages().size());




    return mapper.fromRoom(room);


    }

    @Override
    public RoomDto getRoomById(Long id) {

        return roomRepository.findById(id)
                .map(mapper::fromRoom)
                .orElseThrow(()->new EntityNotFoundException("Room not found with id : "+id));
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Room with id "+id+" not exist for deleting"));

        roomRepository.deleteById(id);

    }

}
