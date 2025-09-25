package com.hcasanova.product_inventory.infrastructure.mapper;

import com.hcasanova.product_inventory.domain.model.AppUser;
import com.hcasanova.product_inventory.infrastructure.rest.dto.AppUserDTO;

public class AppUserMapper {

  public static AppUserDTO toDTO(AppUser user) {
    return new AppUserDTO(user.id, user.getUsername(), user.getRole());
  }
}
