package com.food.table.serviceimpl;

import com.food.table.constant.FoodStatusEnum;
import com.food.table.dto.Restaurant;
import com.food.table.dto.RestaurantTable;
import com.food.table.dto.UserAccount;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.RestaurantTableDetailsModel;
import com.food.table.model.RestaurantTableModel;
import com.food.table.repo.RestaurantRepository;
import com.food.table.repo.RestaurantTableRepository;
import com.food.table.service.FoodApiService;
import com.food.table.service.RestaurantTableService;
import com.food.table.util.AuthorityUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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

    final AuthorityUtil authorityUtil;

    @Override
    public RestaurantTableModel insertTable(RestaurantTableModel restaurantTableModel) {
        checkAuthority(restaurantTableModel.getRestaurantId());
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantTableModel.getRestaurantId());
        if (!restaurant.isPresent())
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_RESTAURANT_ID);
        if (Objects.nonNull(restaurantTableRepository.findByNameAndRestaurantId(restaurantTableModel.getName(), restaurantTableModel.getRestaurantId())))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_TABLE_NAME);
        RestaurantTable restaurantTable = RestaurantTable.builder().restaurant(restaurant.get())
                .name(restaurantTableModel.getName())
                .seats(restaurantTableModel.getSeats())
                .status(FoodStatusEnum.getValue(restaurantTableModel.getStatus()))
                .build();
        RestaurantTable savedRestaurant = restaurantTableRepository.save(restaurantTable);
        if (Objects.isNull(savedRestaurant))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.ADD_TABLE_FAILED);
        RestaurantTable restaurantTableWithQR = createQRCode(savedRestaurant, restaurant.get());
        RestaurantTable savedRestaurantTableWithQR = restaurantTableRepository.save(restaurantTableWithQR);
        if (Objects.isNull(savedRestaurantTableWithQR))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.QR_CODE_FAILED);
        return RestaurantTableModel.convertDtoToModel(savedRestaurantTableWithQR);
    }

    @Override
    public RestaurantTableModel getById(int id) {

        return RestaurantTableModel.convertDtoToModel(getTablebyId(id));
    }

    @Override
    public List<RestaurantTableModel> getAllByRestaurantId(int restaurantId,String tableName) {
        checkAuthority(restaurantId);
        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findByRestaurantIdAndNameContainingIgnoreCase(restaurantId,tableName);
        if (CollectionUtils.isEmpty(restaurantTableList))
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_RESTAURANT_ID);
        return restaurantTableList.stream().map(RestaurantTableModel::convertDtoToModel).collect(Collectors.toList());
    }

    @Override
    public List<RestaurantTableModel> getAllTable() {
        return restaurantTableRepository.findAll().stream()
                .map(RestaurantTableModel::convertDtoToModel)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(int id) {
        RestaurantTable restaurantTable = getTablebyId(id);
        if (restaurantTable.getStatus() == FoodStatusEnum.ACTIVE.getId())
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_DELETE_TABLE);
        restaurantTable.setDeleteFlag(1);
        restaurantTable.setDeletionDate(Timestamp.valueOf(LocalDateTime.now()));
        restaurantTableRepository.save(restaurantTable);
        return true;
    }


    @Override
    public RestaurantTableModel updateById(int tableId, RestaurantTableModel restaurantTableModel) {
        checkAuthority(restaurantTableModel.getRestaurantId());
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantTableModel.getRestaurantId());
        if (!restaurant.isPresent())
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_RESTAURANT_ID);
        RestaurantTable restaurantTable = getTablebyId(tableId);
        RestaurantTable fetchedRestaurantTable = restaurantTableRepository.findByNameAndRestaurantId(restaurantTableModel.getName(), restaurantTableModel.getRestaurantId());
        if (Objects.nonNull(fetchedRestaurantTable) && fetchedRestaurantTable.getId() != tableId)
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.INVALID_TABLE_NAME);
        RestaurantTable savedRestaurantTable = restaurantTableRepository.save(RestaurantTable.builder()
                .id(tableId).restaurant(restaurant.get()).name(restaurantTableModel.getName())
                .qrCode(restaurantTable.getQrCode())
                .seats(restaurantTableModel.getSeats())
                .status(FoodStatusEnum.getValue(restaurantTableModel.getStatus()))
                .build());
        if (Objects.isNull(savedRestaurantTable))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.UPDATE_TABLE_FAILED);
        return RestaurantTableModel.convertDtoToModel(savedRestaurantTable);
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
        RestaurantTable restaurantTable = restaurantTableRepository.findByQrCode(qrCode);
        if (Objects.isNull(restaurantTable))
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_QR_CODE);
        Restaurant restaurant = restaurantRepository.findById(restaurantTable.getRestaurant().getId()).get();
        return RestaurantTableDetailsModel.builder()
                .restaurantId(restaurantTable.getRestaurant().getId())
                .restuarantTableId(restaurantTable.getId())
                .restaurantName(restaurant.getRestaurantName())
                .imageUrl(restaurant.getImageUrl())
                .restaurantType(restaurant.getTypes().stream().map(res->{
                  return res.getName();
                }).collect(Collectors.toList()))
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
                .seats(restaurantTable.getSeats())
                .status(restaurantTable.getStatus())
                .build();
    }

    private RestaurantTable getTablebyId(int tableId) {
        RestaurantTable restaurantTable = restaurantTableRepository.findById(tableId);
        if (Objects.isNull(restaurantTable))
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_TABLE_ID);
        checkAuthority(restaurantTable.getRestaurant().getId());
        return restaurantTable;
    }

    private void checkAuthority(int restaurantId) {
        UserAccount userDetails = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        authorityUtil.checkAuthority(userDetails, restaurantId);
    }


}
