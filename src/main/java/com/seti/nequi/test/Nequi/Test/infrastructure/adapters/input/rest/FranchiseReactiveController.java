package com.seti.nequi.test.Nequi.Test.infrastructure.adapters.input.rest;

import com.seti.nequi.test.Nequi.Test.application.ports.input.FranchiseReactiveService;
import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.input.rest.dto.*;
import com.seti.nequi.test.Nequi.Test.infrastructure.adapters.input.rest.mapper.MapperDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Validated
@RestController
@RequestMapping(
        value = "/api/v1/reactive/franquicias",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class FranchiseReactiveController {

    private final FranchiseReactiveService franchiseReactiveService;
    private final MapperDTO dtoMapper;

    // -------- Franchise --------

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<FranchiseDTO>> createFranchise(@Valid @RequestBody FranchiseDTO request) {
        log.info("createFranchise start name={}", request.getName());
        return franchiseReactiveService.createFranchise(dtoMapper.toDomain(request))
                .map(dtoMapper::toDTO)
                .map(created -> {
                    // Asume que el DTO tiene getId()
                    URI location = URI.create(String.format("/api/v1/reactive/franquicias/%s", created.getId()));
                    return ResponseEntity.created(location).body(created);
                });
    }

    @GetMapping(consumes = MediaType.ALL_VALUE) // permite GET sin body
    public Flux<FranchiseDTO> getAllFranchises(
            @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(name = "size", defaultValue = "50") @Min(1) int size
    ) {
        log.info("getAllFranchises page={} size={}", page, size);
        return franchiseReactiveService.getAllFranchises()
                .map(dtoMapper::toDTO);
    }

    @GetMapping(value = "/{franchiseId}", consumes = MediaType.ALL_VALUE)
    public Mono<ResponseEntity<FranchiseDTO>> getFranchiseById(@PathVariable @NotBlank String franchiseId) {
        log.info("getFranchiseById id={}", franchiseId);
        return franchiseReactiveService.getFranchiseById(franchiseId)
                .map(dtoMapper::toDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("getFranchiseById not_found id={}", franchiseId);
                    return Mono.error(new ResourceNotFoundException("Franquicia no encontrada"));
                }));
    }

    @PutMapping("/{franchiseId}")
    public Mono<ResponseEntity<FranchiseDTO>> updateFranchise(
            @PathVariable @NotBlank String franchiseId,
            @Valid @RequestBody FranchiseDTO request
    ) {
        log.info("updateFranchise id={} name={}", franchiseId, request.getName());
        return franchiseReactiveService.updateFranchise(franchiseId, dtoMapper.toDomain(request))
                .map(dtoMapper::toDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia no encontrada")));
    }

    @DeleteMapping(value = "/{franchiseId}", consumes = MediaType.ALL_VALUE)
    public Mono<ResponseEntity<Void>> deleteFranchise(@PathVariable @NotBlank String franchiseId) {
        log.info("deleteFranchise id={}", franchiseId);
        return franchiseReactiveService.deleteFranchise(franchiseId)
                .thenReturn(ResponseEntity.noContent().build());
    }

    // -------- Name patches --------

    @PatchMapping("/{franchiseId}/nombre")
    public Mono<ResponseEntity<FranchiseDTO>> updateNameFranchise(
            @PathVariable @NotBlank String franchiseId,
            @Valid @RequestBody NameDTO nameDTO
    ) {
        log.info("updateNameFranchise id={} newName={}", franchiseId, nameDTO.getNewName());
        return franchiseReactiveService.updateNameFranchise(franchiseId, nameDTO.getNewName())
                .map(dtoMapper::toDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia no encontrada")));
    }

    @PatchMapping("/{franchiseId}/branches/{branchId}/nombre")
    public Mono<ResponseEntity<FranchiseDTO>> updateNameBranch(
            @PathVariable @NotBlank String franchiseId,
            @PathVariable @NotBlank String branchId,
            @Valid @RequestBody NameDTO nameDTO
    ) {
        log.info("updateNameBranch franchiseId={} branchId={} newName={}", franchiseId, branchId, nameDTO.getNewName());
        return franchiseReactiveService.updateNameBranch(franchiseId, branchId, nameDTO.getNewName())
                .map(dtoMapper::toDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sucursal no encontrada")));
    }

    @PatchMapping("/{franchiseId}/branches/{branchId}/products/{productId}/nombre")
    public Mono<ResponseEntity<FranchiseDTO>> updateNameProduct(
            @PathVariable @NotBlank String franchiseId,
            @PathVariable @NotBlank String branchId,
            @PathVariable @NotBlank String productId,
            @Valid @RequestBody NameDTO nameDTO
    ) {
        log.info("updateNameProduct franchiseId={} branchId={} productId={} newName={}",
                franchiseId, branchId, productId, nameDTO.getNewName());
        return franchiseReactiveService.updateNameProduct(franchiseId, branchId, productId, nameDTO.getNewName())
                .map(dtoMapper::toDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto no encontrado")));
    }

    // -------- Branch --------

    @PostMapping("/{franchiseId}/branches")
    public Mono<ResponseEntity<FranchiseDTO>> addBranchToFranchise(
            @PathVariable @NotBlank String franchiseId,
            @Valid @RequestBody BranchDTO branchDTO
    ) {
        log.info("addBranchToFranchise franchiseId={} branchName={}", franchiseId, branchDTO.getName());
        return franchiseReactiveService.addBranchToFranchise(franchiseId, dtoMapper.toDomain(branchDTO))
                .map(dtoMapper::toDTO)
                .map(ResponseEntity::ok) // o created si devuelves la sucursal con su ID y Location propia
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Franquicia no encontrada")));
    }

    @DeleteMapping(value = "/{franchiseId}/branches/{branchId}", consumes = MediaType.ALL_VALUE)
    public Mono<ResponseEntity<FranchiseDTO>> deleteBranchOfFranchise(
            @PathVariable @NotBlank String franchiseId,
            @PathVariable @NotBlank String branchId
    ) {
        log.info("deleteBranchOfFranchise franchiseId={} branchId={}", franchiseId, branchId);
        return franchiseReactiveService.deleteBranchToFranchise(franchiseId, branchId)
                .map(dtoMapper::toDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sucursal no encontrada")));
    }

    // -------- Product --------

    @PostMapping("/{franchiseId}/branches/{branchId}/products")
    public Mono<ResponseEntity<FranchiseDTO>> addProductToBranch(
            @PathVariable @NotBlank String franchiseId,
            @PathVariable @NotBlank String branchId,
            @Valid @RequestBody ProductDTO productDTO
    ) {
        log.info("addProductToBranch franchiseId={} branchId={} productName={}",
                franchiseId, branchId, productDTO.getName());
        return franchiseReactiveService.addProductToBranch(franchiseId, branchId, dtoMapper.toDomain(productDTO))
                .map(dtoMapper::toDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Sucursal o producto no encontrado")));
    }

    @DeleteMapping(value = "/{franchiseId}/branches/{branchId}/products/{productId}", consumes = MediaType.ALL_VALUE)
    public Mono<ResponseEntity<FranchiseDTO>> deleteProductOfBranch(
            @PathVariable @NotBlank String franchiseId,
            @PathVariable @NotBlank String branchId,
            @PathVariable @NotBlank String productId
    ) {
        log.info("deleteProductOfBranch franchiseId={} branchId={} productId={}", franchiseId, branchId, productId);
        return franchiseReactiveService.deleteProductToBranch(franchiseId, branchId, productId)
                .map(dtoMapper::toDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto no encontrado")));
    }

    @PatchMapping("/{franchiseId}/branches/{branchId}/products/{productId}/stock")
    public Mono<ResponseEntity<FranchiseDTO>> updateCountProduct(
            @PathVariable @NotBlank String franchiseId,
            @PathVariable @NotBlank String branchId,
            @PathVariable @NotBlank String productId,
            @Valid @RequestBody CountDTO stockDTO
    ) {
        log.info("updateCountProduct franchiseId={} branchId={} productId={} newCount={}",
                franchiseId, branchId, productId, stockDTO.getNewCount());
        return franchiseReactiveService.updateCountProduct(franchiseId, branchId, productId, stockDTO.getNewCount())
                .map(dtoMapper::toDTO)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto no encontrado")));
    }

    // -------- Especial --------

    @GetMapping(value = "/{franchiseId}/products/mas-stock", consumes = MediaType.ALL_VALUE)
    public Flux<BranchDTO> getProductsWithMostStockByBranch(@PathVariable @NotBlank String franchiseId) {
        log.info("getProductsWithMostStockByBranch franchiseId={}", franchiseId);
        return franchiseReactiveService.getProductMostCountByBranch(franchiseId)
                .map(dtoMapper::toDTO);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) { super(message); }
    }
}
