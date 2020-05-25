package com.food.table.serviceimpl;

import com.food.table.dto.Tiers;
import com.food.table.exception.ApplicationErrors;
import com.food.table.exception.ApplicationException;
import com.food.table.model.TiersModel;
import com.food.table.repo.TiersRepository;
import com.food.table.service.TierApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TierApiServiceImpl implements TierApiService {

    final TiersRepository  tiersRepository;

    @Override
    public List<TiersModel> getAll() {
         return tiersRepository.findAll().stream().map(tier->{
            return TiersModel.convertDtoToModel(tier);
        }).collect(Collectors.toList());
    }

    @Override
    public TiersModel getById(long id) {
        Optional<Tiers> tiers = tiersRepository.findById(id);
        checkExistence(tiers);
        return TiersModel.convertDtoToModel(tiers.get());
    }

    @Override
    public TiersModel insertTier(TiersModel tiersModel) {
        Tiers tiers = tiersRepository.save(Tiers.convertModelToDto(tiersModel));
        if (Objects.isNull(tiers))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.ADD_TIER_FAILED);
        return TiersModel.convertDtoToModel(tiers);
    }

    @Override
    public TiersModel updateTierById(long id, TiersModel tiersModel) {
        Optional<Tiers> tiers = tiersRepository.findById(id);
        checkExistence(tiers);
        Tiers savedTiers = tiersRepository.save(Tiers.convertModelToDto(tiersModel));
        if (Objects.isNull(savedTiers))
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, ApplicationErrors.UPDATE_TIER_FAILED);
        return TiersModel.convertDtoToModel(savedTiers);
    }

    private void checkExistence(Optional<Tiers> tiers) {
        if (!tiers.isPresent()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, ApplicationErrors.INVALID_TIER_ID);
        }
    }
}
