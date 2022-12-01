package br.com.project.resouces;

import br.com.project.*;
import br.com.project.domain.Product;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
public class ProductResourceIntegrationTest {
    private final Product p1 = new Product(1L, "Product A", 10.99, 10);
    private final Product p2 = new Product(2L, "Product B", 10.99, 10);

    @GrpcClient("inProcess")
    private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @DisplayName("when valid data is provided a product is created")
    public void createProductSuccessTest() {
        ProductRequest productRequest = ProductRequest.newBuilder().setName("Product C").setPrice(4.99).setQuantityInStock(200).build();

        ProductResponse productResponse = serviceBlockingStub.create(productRequest);

        Assertions.assertNotNull(productResponse);
        Assertions.assertEquals(3L, productResponse.getId());
        Assertions.assertEquals(productRequest.getName(), productResponse.getName());
        Assertions.assertEquals(productRequest.getPrice(), productResponse.getPrice());
        Assertions.assertEquals(productRequest.getQuantityInStock(), productResponse.getQuantityInStock());
    }

    @Test
    @DisplayName("when it calls the create method providing a product with repeated name it throws a StatusRuntimeException")
    public void createProductAlreadyExistsExceptionTest() {
        ProductRequest productRequest = ProductRequest.newBuilder()
                .setName(p1.getName())
                .setPrice(p1.getPrice())
                .setQuantityInStock(p1.getQuantityInStock())
                .build();

        StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class, () -> serviceBlockingStub.create(productRequest));
        Assertions.assertEquals(Status.ALREADY_EXISTS.getCode(), ex.getStatus().getCode());
    }

    @Test
    @DisplayName("when it calls the findById method providing a valid id then returns a product as response")
    public void findByIdSucessTest() {
        RequestById request = RequestById.newBuilder().setId(1L).build();

        ProductResponse response = serviceBlockingStub.findById(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(p1.getId(), response.getId());
        Assertions.assertEquals(p1.getName(), response.getName());
        Assertions.assertEquals(p1.getPrice(), response.getPrice());
        Assertions.assertEquals(p1.getQuantityInStock(), response.getQuantityInStock());
    }


    @Test
    @DisplayName("when it call the findById method providing an invalid id then it throws a ProductNotFoundException")
    public void findByIdProductNotFoundExceptionTest() {
        RequestById request = RequestById.newBuilder().setId(50L).build();

        StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
           serviceBlockingStub.findById(request);
        });
        Assertions.assertEquals(Status.NOT_FOUND.getCode(), ex.getStatus().getCode());
    }


    @Test
    @DisplayName("when it call the delete method providing a valid id then the product is deleted")
    public void deleteProductSuccessTest() {
        RequestById request = RequestById.newBuilder().setId(1L).build();

        Assertions.assertDoesNotThrow(() -> serviceBlockingStub.delete(request));
    }

    @Test
    @DisplayName("when it call the delete method providing an invalid id then it throws a ProductNotFoundException")
    public void deleteProductNotFoundException() {
        RequestById request = RequestById.newBuilder().setId(50L).build();

        StatusRuntimeException ex = Assertions.assertThrows(StatusRuntimeException.class, () -> serviceBlockingStub.delete(request));

        Assertions.assertEquals(Status.NOT_FOUND.getCode(), ex.getStatus().getCode());
    }

    @Test
    @DisplayName("when it call the findAll method then it returns all products")
    public void findAllSuccessTest() {
        List<Product> productsFromDB = List.of(p1, p2);

        ProductResponseList response = serviceBlockingStub.findAll(EmptyRequest.newBuilder().build());
        List<ProductResponse> responseList = response.getProductsList();

        Assertions.assertEquals(2, responseList.size());
        for(int i = 0; i < 2; i++) {
            ProductResponse productResponse = responseList.get(i);
            Product productFromDB = productsFromDB.get(i);

            Assertions.assertEquals(productFromDB.getId(), productResponse.getId());
            Assertions.assertEquals(productFromDB.getName(), productResponse.getName());
            Assertions.assertEquals(productFromDB.getPrice(), productResponse.getPrice());
            Assertions.assertEquals(productFromDB.getQuantityInStock(), productResponse.getQuantityInStock());
        }
    }
}