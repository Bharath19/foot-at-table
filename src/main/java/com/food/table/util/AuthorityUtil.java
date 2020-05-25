package com.food.table.util;

import com.food.table.dto.Restaurant;
import com.food.table.dto.UserAccount;
import com.food.table.dto.UserRole;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AuthorityUtil {

    public void checkAuthority(UserAccount userAccount, int restaurantId) {

        List<String> roleList = userAccount.getRoles().stream().map(UserRole::getRoleName).collect(Collectors.toList());
        if (!roleList.contains("ADMIN")) {
            List<Integer> restaurantIdList = new ArrayList<>();
            if (Objects.nonNull(userAccount.getRestaurants())) {
                restaurantIdList = userAccount.getRestaurants()
                        .stream().filter(Objects::nonNull)
                        .map(Restaurant::getId)
                        .collect(Collectors.toList());
            }
            if (!restaurantIdList.contains(restaurantId)) {
                throw new ApplicationException(HttpStatus.FORBIDDEN, ApplicationErrors.OPERATION_FORBIDDEN_FOR_USER);
            }
        }
    }
}
