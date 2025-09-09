package com.krainet.auth.controllers;

import com.krainet.auth.core.constants.ApiConstants;
import com.krainet.auth.core.constants.SecurityConstants;
import com.krainet.auth.core.enums.Role;
import com.krainet.auth.core.exceptions.AccessException;
import com.krainet.auth.core.interfaces.services.AccountService;
import com.krainet.auth.core.mappers.AccountMapper;
import com.krainet.auth.core.models.dtos.*;
import com.krainet.auth.core.models.entities.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.API_PREFIX_V1)
@Tag(name = ApiConstants.Account.API_ACCOUNT_CONTROLLER_NAME)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping(ApiConstants.Account.API_ACCOUNT_LIST)
    @Operation(description = "Endpoint to get list of account",
            summary = "This is a summary for account post endpoint")
    @ApiResponse(description = "Success", responseCode = "200",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Page.class))})
    @ApiResponse(description = "Error", responseCode = "400",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))})
    @ResponseBody
    @SecurityRequirement(name = SecurityConstants.AUTH_BEARER_TOKEN)
    public ResponseEntity<Page<AccountDto>> getList(@RequestParam("page") int page,
                                                    @RequestParam("size") int size,
                                                    @RequestParam(name = "direction", required = false) String direction,
                                                    @RequestParam(name = "field", required = false) String field) {
        Pageable pageable = PageRequest.of(page, size);
        if(!(direction == null || field == null)){
            Sort sort = Sort.by(Sort.Direction.fromString(direction), field);
            pageable = PageRequest.of(page, size).withSort(sort);
        }
        return ResponseEntity.ok(accountService.getPages(pageable, (account) -> accountMapper.entityToDto(account)));
    }

    @GetMapping(ApiConstants.Account.API_ACCOUNT_BY_ID)
    @Operation(description = "Endpoint to create new account",
            summary = "This is a summary for account post endpoint")
    @ApiResponse(description = "Success", responseCode = "200",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountDto.class))})
    @ApiResponse(description = "Error", responseCode = "400",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))})
    @ResponseBody
    @SecurityRequirement(name = SecurityConstants.AUTH_BEARER_TOKEN)
    public ResponseEntity<AccountDto> getItem(UsernamePasswordAuthenticationToken token, @RequestParam("id") UUID id) {
        if(!hasPermission(token,id))
            throw new AccessException("You do not have permission to get info about this account");
        return ResponseEntity.ok(accountService.findById(id, (acc) -> accountMapper.entityToDto(acc)));
    }

    @PostMapping(ApiConstants.Account.API_ACCOUNT_CREATE_UPDATE)
    @Operation(description = "Endpoint to create new account",
            summary = "This is a summary for account post endpoint")
    @ApiResponse(description = "Success", responseCode = "200",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountDto.class))})
    @ApiResponse(description = "Error", responseCode = "400",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))})
    @ResponseBody
    public ResponseEntity<AccountDto> create(@RequestBody @Valid AccountCreateDto account) {
        return ResponseEntity.ok(accountService.create(account, (acc) -> accountMapper.entityToDto(acc)));
    }

    @PutMapping(ApiConstants.Account.API_ACCOUNT_CREATE_UPDATE)
    @Operation(description = "Endpoint to update account",
            summary = "This is a summary for account post endpoint")
    @ApiResponse(description = "Success", responseCode = "200",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountDto.class))})
    @ApiResponse(description = "Error", responseCode = "400",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))})
    @ResponseBody
    @SecurityRequirement(name = SecurityConstants.AUTH_BEARER_TOKEN)
    public ResponseEntity<AccountDto> update(UsernamePasswordAuthenticationToken token, @RequestBody @Valid AccountUpdateDto account) {
        if(!hasPermission(token,account.id()))
            throw new AccessException("You do not have permission to update this account");
        return ResponseEntity.ok(accountService.update(account, (acc) -> accountMapper.entityToDto(acc)));
    }

    @PutMapping(ApiConstants.Account.API_ACCOUNT_ROLE_UPDATE)
    @Operation(description = "Endpoint to update account",
            summary = "This is a summary for account post endpoint")
    @ApiResponse(description = "Success", responseCode = "200")
    @ApiResponse(description = "Error", responseCode = "400",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))})
    @ResponseBody
    @SecurityRequirement(name = SecurityConstants.AUTH_BEARER_TOKEN)
    public ResponseEntity<?> updateRole(UsernamePasswordAuthenticationToken token, @RequestBody @Valid RoleUpdateDto account) {
        if(!hasPermission(token,account.accountId()))
            throw new AccessException("You do not have permission to update this account");
        accountService.updateRole(account);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping (ApiConstants.Account.API_ACCOUNT_BY_ID)
    @Operation(description = "Endpoint to delete account",
            summary = "This is a summary for account post endpoint")
    @ApiResponse(description = "Success", responseCode = "200")
    @ApiResponse(description = "Error", responseCode = "400",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class))})
    @ResponseBody
    @SecurityRequirement(name = SecurityConstants.AUTH_BEARER_TOKEN)
    public ResponseEntity<?> delete(UsernamePasswordAuthenticationToken token, @RequestParam("id") UUID id) {
        if(!hasPermission(token,id))
            throw new AccessException("You do not have permission to delete this account");

        accountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

private boolean hasPermission(UsernamePasswordAuthenticationToken token, UUID accountId){
        if(token.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(Role.ADMIN.getRoleWithPrefix())))
        {
            return true;
        }
        var account = (Account)token.getPrincipal();
        return accountId.equals(account.getId());
}

}