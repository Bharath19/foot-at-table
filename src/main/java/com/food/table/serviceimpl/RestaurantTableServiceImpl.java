package com.food.table.serviceimpl;

import com.food.table.dto.Restaurant;
import com.food.table.dto.RestaurantTable;
import com.food.table.exceptions.RecordNotFoundException;
import com.food.table.model.RestaurantTableDetailsModel;
import com.food.table.model.RestaurantTableModel;
import com.food.table.repo.RestaurantRepository;
import com.food.table.repo.RestaurantTableRepository;
import com.food.table.service.FoodApiService;
import com.food.table.service.RestaurantTableService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestaurantTableServiceImpl implements RestaurantTableService {

    final RestaurantTableRepository restaurantTableRepository;

    final RestaurantRepository restaurantRepository;

    final FoodApiService foodApiService;

    @Override
    public RestaurantTableModel insertTable(RestaurantTableModel restaurantTableModel) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantTableModel.getRestaurantId());
        if (!restaurant.isPresent())
            throw new RecordNotFoundException("No Record found in Restaurant for id: " + restaurantTableModel.getRestaurantId());
        RestaurantTable restaurantTable = RestaurantTable.builder().restaurant(restaurant.get())
                .name(restaurantTableModel.getName())
                .build();
        RestaurantTable restaurantTableWithQR = createQRCode(restaurantTableRepository.save(restaurantTable), restaurant.get());
        return RestaurantTableModel.convertDtoToModel(restaurantTableRepository.save(restaurantTableWithQR));
    }

    @Override
    public RestaurantTableModel getById(int id) {
        return RestaurantTableModel.convertDtoToModel(getTablebyId(id));
    }

    @Override
    public List<RestaurantTableModel> getAllByRestaurantId(int restaurantId) {
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findTablesByRestaurantId(restaurantId);
        if (CollectionUtils.isEmpty(restaurantTableList))
            throw new RecordNotFoundException("No Records found in RestaurantTable ");
        return restaurantTableList.stream().map(RestaurantTableModel::convertDtoToModel).collect(Collectors.toList());
    }

    @Override
    public List<RestaurantTableModel> getAllTable() {
        return restaurantTableRepository.findAllTables().stream()
                .map(RestaurantTableModel::convertDtoToModel)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(int id) {
        RestaurantTable restaurantTable = getTablebyId(id);
        restaurantTable.setDeleteFlag(1);
        restaurantTable.setDeletionDate(Timestamp.valueOf(LocalDateTime.now()));
        restaurantTableRepository.save(restaurantTable);
        return true;
    }


    @Override
    public RestaurantTableModel updateById(int tableId, RestaurantTableModel restaurantTableModel) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantTableModel.getRestaurantId());
        if (!restaurant.isPresent())
            throw new RecordNotFoundException("No Record found in Restaurant for id: " + restaurantTableModel.getRestaurantId());
        RestaurantTable restaurantTable = getTablebyId(tableId);
        return RestaurantTableModel.convertDtoToModel(restaurantTableRepository.save(RestaurantTable.builder()
                .id(tableId).restaurant(restaurant.get()).name(restaurantTableModel.getName())
                .qrCode(restaurantTable.getQrCode()).build()));
    }

    @Override
    public byte[] generateQRCode(int tableId) {
        RestaurantTable restaurantTable = getTablebyId(tableId);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = qrCodeWriter.encode(restaurantTable.getQrCode(), BarcodeFormat.QR_CODE, 250, 250);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pngOutputStream.toByteArray();
    }

    @Override
    public RestaurantTableDetailsModel getTableDetailsByQRCode(String qrCode) {
        RestaurantTable restaurantTable = restaurantTableRepository.findTableByQRCode(qrCode);
        if (Objects.isNull(restaurantTable))
            throw new RecordNotFoundException("No Record found in RestaurantTable for qrCode: " + qrCode);
        return RestaurantTableDetailsModel.builder()
                .restaurantId(restaurantTable.getRestaurant().getId())
                .tableId(restaurantTable.getId())
                .build();
    }

    private RestaurantTable createQRCode(RestaurantTable restaurantTable, Restaurant restaurant) {
        StringBuilder qrCodeBuilder = new StringBuilder();
        qrCodeBuilder.append(restaurantTable.getRestaurant().getId()).append("-").append(restaurantTable.getId());
        return RestaurantTable.builder()
                .id(restaurantTable.getId())
                .restaurant(restaurant)
                .name(restaurantTable.getName())
                .qrCode(qrCodeBuilder.toString())
                .build();
    }

    private RestaurantTable getTablebyId(int tableId) {
        RestaurantTable restaurantTable = restaurantTableRepository.findTableById(tableId);
        if (Objects.isNull(restaurantTable))
            throw new RecordNotFoundException("No Record found in RestaurantTable for id: " + tableId);
        return restaurantTable;
    }


}
