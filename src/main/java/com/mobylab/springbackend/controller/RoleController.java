package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/v1/role")
public class RoleController implements SecuredRestController{

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addRoles(@RequestBody List<String> roleList) {
        logger.info("Request to add roles {}", roleList);
        List<String> addedRoles = roleService.addRoles(roleList);
        logger.info("Successfully added roles {}", addedRoles);
        return new ResponseEntity<>(addedRoles, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<String>> getRoles() {
        List<String> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
