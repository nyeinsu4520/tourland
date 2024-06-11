package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class PackageService {

	 @Autowired
	    private PackageRepository packageRepository;

	    public List<Package> findAll() {
	        return packageRepository.findAll();
	    }

	    public Optional<Package> findById(Integer id) {
	        return packageRepository.findById(id);
	    }

	    public Package save(Package packageObj) {
	        return packageRepository.save(packageObj);
	    }

	    public void deleteById(Integer id) {
	        packageRepository.deleteById(id);
	    }
}
