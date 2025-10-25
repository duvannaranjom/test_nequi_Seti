package com.seti.nequi.test.config;

import com.seti.nequi.test.domain.model.port.FranchiseRepositoryPort;
import com.seti.nequi.test.domain.usecase.branch.AddBranchUseCase;
import com.seti.nequi.test.domain.usecase.branch.DeleteBranchUseCase;
import com.seti.nequi.test.domain.usecase.branch.UpdateBranchNameUseCase;
import com.seti.nequi.test.domain.usecase.franchise.*;
import com.seti.nequi.test.domain.usecase.product.*;
import com.seti.nequi.test.domain.usecase.report.GetProductsWithMaxStockByBranchUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateFranchiseUseCase createFranchiseUseCase(FranchiseRepositoryPort port) {
        return new CreateFranchiseUseCase(port);
    }

    @Bean
    public GetFranchisesUseCase getFranchisesUseCase(FranchiseRepositoryPort port) {
        return new GetFranchisesUseCase(port);
    }

    @Bean
    public GetFranchiseByIdUseCase getFranchiseByIdUseCase(FranchiseRepositoryPort port) {
        return new GetFranchiseByIdUseCase(port);
    }

    @Bean
    public UpdateFranchiseNameUseCase updateFranchiseNameUseCase(FranchiseRepositoryPort port) {
        return new UpdateFranchiseNameUseCase(port);
    }

    @Bean
    public DeleteFranchiseUseCase deleteFranchiseUseCase(FranchiseRepositoryPort port) {
        return new DeleteFranchiseUseCase(port);
    }

    @Bean
    public AddBranchUseCase addBranchUseCase(FranchiseRepositoryPort port) {
        return new AddBranchUseCase(port);
    }

    @Bean
    public UpdateBranchNameUseCase updateBranchNameUseCase(FranchiseRepositoryPort port) {
        return new UpdateBranchNameUseCase(port);
    }

    @Bean
    public DeleteBranchUseCase deleteBranchUseCase(FranchiseRepositoryPort port) {
        return new DeleteBranchUseCase(port);
    }

    @Bean
    public AddProductUseCase addProductUseCase(FranchiseRepositoryPort port) {
        return new AddProductUseCase(port);
    }

    @Bean
    public UpdateProductNameUseCase updateProductNameUseCase(FranchiseRepositoryPort port) {
        return new UpdateProductNameUseCase(port);
    }

    @Bean
    public UpdateProductCountUseCase updateProductCountUseCase(FranchiseRepositoryPort port) {
        return new UpdateProductCountUseCase(port);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(FranchiseRepositoryPort port) {
        return new DeleteProductUseCase(port);
    }

    @Bean
    public GetProductsWithMaxStockByBranchUseCase getProductsWithMaxStockByBranchUseCase(FranchiseRepositoryPort port) {
        return new GetProductsWithMaxStockByBranchUseCase(port);
    }
}
