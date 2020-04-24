package com.food.table.serviceimpl;

import com.food.table.dto.Tiers;
import com.food.table.model.TiersModel;
import com.food.table.repo.TiersRepository;
import com.food.table.service.TierApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return TiersModel.convertDtoToModel(tiersRepository.getOne(id));
    }

    @Override
    public TiersModel insertTier(TiersModel tiersModel) {
        return TiersModel.convertDtoToModel(tiersRepository.save(Tiers.convertModelToDto(tiersModel)));
    }

    @Override
    public TiersModel updateTierById(long id, TiersModel tiersModel) {
        Tiers tiers =tiersRepository.getOne(id);
        tiers.setName(tiersModel.getName());
        return TiersModel.convertDtoToModel(tiersRepository.save(tiers));
    }
}
