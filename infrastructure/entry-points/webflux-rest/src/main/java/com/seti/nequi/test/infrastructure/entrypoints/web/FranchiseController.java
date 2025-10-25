package com.seti.nequi.test.infrastructure.entrypoints.web;

import com.seti.nequi.test.domain.usecase.branch.AddBranchUseCase;
import com.seti.nequi.test.domain.usecase.branch.DeleteBranchUseCase;
import com.seti.nequi.test.domain.usecase.branch.UpdateBranchNameUseCase;
import com.seti.nequi.test.domain.usecase.franchise.*;
import com.seti.nequi.test.domain.usecase.product.*;
import com.seti.nequi.test.domain.usecase.report.GetProductsWithMaxStockByBranchUseCase;
import com.seti.nequi.test.infrastructure.entrypoints.web.dto.*;
import com.seti.nequi.test.infrastructure.entrypoints.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/franchese")
@RequiredArgsConstructor
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchise;
    private final GetFranchisesUseCase getFranchises;
    private final GetFranchiseByIdUseCase getFranchiseById;
    private final UpdateFranchiseNameUseCase updateFranchiseName;
    private final DeleteFranchiseUseCase deleteFranchise;

    private final AddBranchUseCase addBranch;
    private final UpdateBranchNameUseCase updateBranchName;
    private final DeleteBranchUseCase deleteBranch;

    private final AddProductUseCase addProduct;
    private final UpdateProductNameUseCase updateProductName;
    private final UpdateProductCountUseCase updateProductCount;
    private final DeleteProductUseCase deleteProduct;

    private final GetProductsWithMaxStockByBranchUseCase getMaxStockByBranch;

    // ----- Franchise -----
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseDto> create(@Valid @RequestBody FranchiseDto dto) {
        return createFranchise.execute(DtoMapper.toDomain(dto))
                .map(DtoMapper::toDto);
    }

    @GetMapping
    public Flux<FranchiseDto> list() {
        return getFranchises.execute().map(DtoMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<FranchiseDto> getById(@PathVariable String id) {
        return getFranchiseById.execute(id).map(DtoMapper::toDto);
    }

    @PatchMapping("/{id}/name")
    public Mono<FranchiseDto> rename(@PathVariable String id, @Valid @RequestBody RenameRequest req) {
        return updateFranchiseName.execute(id, req.name()).map(DtoMapper::toDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return deleteFranchise.execute(id);
    }

    // ----- Branch -----
    @PostMapping("/{id}/branches")
    public Mono<FranchiseDto> addBranch(@PathVariable String id, @Valid @RequestBody BranchDto branchDto) {
        return addBranch.execute(id, DtoMapper.toDomain(branchDto))
                .map(DtoMapper::toDto);
    }

    @PatchMapping("/{id}/branches/{branchId}/name")
    public Mono<FranchiseDto> renameBranch(@PathVariable String id, @PathVariable String branchId,
                                           @Valid @RequestBody RenameRequest req) {
        return updateBranchName.execute(id, branchId, req.name()).map(DtoMapper::toDto);
    }

    @DeleteMapping("/{id}/branches/{branchId}")
    public Mono<FranchiseDto> deleteBranch(@PathVariable String id, @PathVariable String branchId) {
        return deleteBranch.execute(id, branchId).map(DtoMapper::toDto);
    }

    // ----- Product -----
    @PostMapping("/{id}/branches/{branchId}/products")
    public Mono<FranchiseDto> addProduct(@PathVariable String id, @PathVariable String branchId,
                                         @Valid @RequestBody ProductDto productDto) {
        return addProduct.execute(id, branchId, DtoMapper.toDomain(productDto))
                .map(DtoMapper::toDto);
    }

    @PatchMapping("/{id}/branches/{branchId}/products/{productId}/name")
    public Mono<FranchiseDto> renameProduct(@PathVariable String id, @PathVariable String branchId,
                                            @PathVariable String productId, @Valid @RequestBody RenameRequest req) {
        return updateProductName.execute(id, branchId, productId, req.name()).map(DtoMapper::toDto);
    }

    @PatchMapping("/{id}/branches/{branchId}/products/{productId}/count")
    public Mono<FranchiseDto> updateCount(@PathVariable String id, @PathVariable String branchId,
                                          @PathVariable String productId, @Valid @RequestBody UpdateCountRequest req) {
        return updateProductCount.execute(id, branchId, productId, req.count()).map(DtoMapper::toDto);
    }

    @DeleteMapping("/{id}/branches/{branchId}/products/{productId}")
    public Mono<FranchiseDto> deleteProduct(@PathVariable String id, @PathVariable String branchId,
                                            @PathVariable String productId) {
        return deleteProduct.execute(id, branchId, productId).map(DtoMapper::toDto);
    }

    // ----- Reporte -----
    @GetMapping("/{id}/products/mas-stock")
    public Flux<BranchDto> maxStockByBranch(@PathVariable String id) {
        return getMaxStockByBranch.execute(id).map(DtoMapper::toDto);
    }
}
