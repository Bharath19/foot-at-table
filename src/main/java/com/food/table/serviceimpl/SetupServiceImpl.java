package com.food.table.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.table.constant.ApplicationConstants;
import com.food.table.dto.Setup;
import com.food.table.repo.SetupRepository;
import com.food.table.service.SetupService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SetupServiceImpl implements SetupService{
	
	@Autowired
	SetupRepository setupRepository;
	
	private Map<String, Integer> gstMap = new HashMap<String, Integer>();
	
	@PostConstruct
	public void init() {
		List<Setup> setups = setupRepository.findByCodeIn(List.of(ApplicationConstants.cgstKey, ApplicationConstants.sgstKey));
		try {
//			TODO need to raise exception if setup table doesn't have data
			setups.forEach(setup ->{
				if(setup.getCode().equals(ApplicationConstants.cgstKey) || setup.getCode().equals(ApplicationConstants.sgstKey)) {
					gstMap.put(setup.getCode(), Integer.parseInt(setup.getValue()));
				}
			});
		}catch (Exception e) {
			log.error("unable get GST setup values  "+ e.getStackTrace());
		}
	}
	
	@Override
	public Map<String, Integer> getGstValues() {
		return gstMap;
	}
}
