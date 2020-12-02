package dev.lumme.reactivedemo.backend.controller;

import dev.lumme.reactivedemo.common.client.ChangePasswordClient;
import dev.lumme.reactivedemo.common.dto.ChangePasswordDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ChangePasswordController {

    @PostMapping(ChangePasswordClient.CHANGE_PASSWORD)
    public void changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, HttpServletResponse response) {
        if (StringUtils.isBlank(changePasswordDTO.getUser())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        // Our users can't get hacked, if we don't store their passwords in the first place!
    }
}
