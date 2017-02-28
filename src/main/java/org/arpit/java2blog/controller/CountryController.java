package org.arpit.java2blog.controller;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;

import org.arpit.java2blog.model.Country;
import org.arpit.java2blog.service.CountryService;
import org.arpit.java2blog.service.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class CountryController {
	
	@Autowired
	CountryService countryService;
	
	@RequestMapping(value = "/getAllCountries", method = RequestMethod.GET, headers = "Accept=application/json,application/xml")
	public List<Country> getCountries() {
		
		List<Country> listOfCountries = countryService.getAllCountries();
		return listOfCountries;
	}

	@RequestMapping(value = "/getCountry/{id}", method = RequestMethod.GET, headers = "Accept=application/json,application/xml")
	public Country getCountryById(@PathVariable int id) {
		return countryService.getCountry(id);
	}

	@RequestMapping(value = "/addCountry", method = RequestMethod.POST, headers = "Accept=application/json")
	public void addCountry(@RequestBody Country country) {	
		countryService.addCountry(country);
		
	}

	@RequestMapping(value = "/updateCountry", method = RequestMethod.PUT, headers = "Accept=application/json")
	public void updateCountry(@RequestBody Country country) {
		countryService.updateCountry(country);
	}

	@RequestMapping(value = "/deleteCountry/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public void deleteCountry(@PathVariable("id") int id) {
		countryService.deleteCountry(id);		
	}	
	
	@Autowired
	 ServletContext context;

	 @RequestMapping(value = "/fileupload", headers=("content-type=multipart/*"), method = RequestMethod.POST)
	 public ResponseEntity<FileInfo> upload(@RequestParam("file") MultipartFile inputFile) {
	  FileInfo fileInfo = new FileInfo();
	  HttpHeaders headers = new HttpHeaders();
	  if (!inputFile.isEmpty()) {
	   try {
	    String originalFilename = inputFile.getOriginalFilename();
	    File destinationFile = new File(context.getRealPath("/WEB-INF/uploaded")+  File.separator + originalFilename);
	    inputFile.transferTo(destinationFile);
	    fileInfo.setFileName(destinationFile.getPath());
	    fileInfo.setFileSize(inputFile.getSize());
	    headers.add("File Uploaded Successfully - ", originalFilename);
	    return new ResponseEntity<FileInfo>(fileInfo, headers, HttpStatus.OK);
	   } catch (Exception e) {    
	    return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
	   }
	  }else{
	   return new ResponseEntity<FileInfo>(HttpStatus.BAD_REQUEST);
	  }
	 }
}
