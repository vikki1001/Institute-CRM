package com.ksv.ktrccrm.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ksv.ktrccrm.constant.ProdConstant;
import com.ksv.ktrccrm.db1.entities.RoleMst;
import com.ksv.ktrccrm.db1.repository.RoleRepository;

@RestController
public class LoginRestController {
	@Autowired
	RoleRepository roleRepository;

	
	@GetMapping("/loginRest")
	public ResponseEntity<List<RoleMst>> loginRestView() {
		try {
			List<RoleMst> list = roleRepository.getRecordList("1", "1");
			return new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PostMapping("/loginRest")
	public ResponseEntity<RoleMst> loginRest(@RequestBody RoleMst roleMst) {
		try {
			return new ResponseEntity<>(roleRepository.save(roleMst), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	


	    @PostMapping("/api/resource")
	    public ResponseEntity<String> createResource(@RequestBody String requestBody) {
	        // Your POST method logic here
	        // Return an appropriate response
	        return ResponseEntity.ok("Resource created successfully");
	    }
	

}
