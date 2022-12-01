package br.com.project.resources;

import br.com.project.*;
import br.com.project.dto.ProductInputDTO;
import br.com.project.dto.ProductOutputDTO;
import br.com.project.service.IProductService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class ProductResource extends ProductServiceGrpc.ProductServiceImplBase {

    @Autowired
    private IProductService productService;

    @Override
    public void create(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        ProductInputDTO inputDTO = new ProductInputDTO(request.getName(), request.getPrice(), request.getQuantityInStock());

        ProductOutputDTO outputDTO = productService.create(inputDTO);

        ProductResponse productResponse = ProductResponse.newBuilder()
                .setId(outputDTO.getId())
                .setName(outputDTO.getName())
                .setPrice(outputDTO.getPrice())
                .setQuantityInStock(outputDTO.getQuantityInStock())
                .build();

        responseObserver.onNext(productResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(RequestById request, StreamObserver<ProductResponse> responseObserver) {
        ProductOutputDTO outputDTO = productService.findById(request.getId());

        ProductResponse response = ProductResponse.newBuilder()
                .setId(outputDTO.getId())
                .setName(outputDTO.getName())
                .setPrice(outputDTO.getPrice())
                .setQuantityInStock(outputDTO.getQuantityInStock()).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(RequestById request, StreamObserver<EmptyResponse> responseObserver) {
        productService.delete(request.getId());
        responseObserver.onNext(EmptyResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(EmptyRequest request, StreamObserver<ProductResponseList> responseObserver) {
        List<ProductOutputDTO> outputDTOS = productService.findAll();

        List<ProductResponse> productResponses = outputDTOS.stream().map(outputDTO -> {
            return ProductResponse.newBuilder()
                    .setId(outputDTO.getId())
                    .setName(outputDTO.getName())
                    .setPrice(outputDTO.getPrice())
                    .setQuantityInStock(outputDTO.getQuantityInStock())
                    .build();
        }).collect(Collectors.toList());

        ProductResponseList productResponseList = ProductResponseList.newBuilder()
                .addAllProducts(productResponses)
                .build();

        responseObserver.onNext(productResponseList);
        responseObserver.onCompleted();
    }
}