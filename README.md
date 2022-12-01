# grpc-spring-project

## Resumo
Este é um projeto desenvolvido durante a realização do curso "Spring Boot e gRPC - Crie uma aplicação com gRPC e Java" da Udemy.  
O certificado de conclusão do curso pode ser visualizado em https://ude.my/UC-2d402351-576e-44d4-8117-9bffdfe611e8.

## Sobre o projeto

### Apresentação

O projeto implementa uma aplicação desenvolvida com Spring Boot que realiza um CRUD de produto. A API segue o padrão gRPC e  
utiliza Protocol Buffers para troca de dados.  
  
O banco de dados utilizado foi o PostgreSQL e a parte de persistência e ORM foi implementada com o Spring Data JPA / Hibernate.  
  
Foram implementados testes unitários e de integração para todos os métodos. As asserções são feitas com JUnit, os mocks com  
Mockito e os chamados dos testes de integração com o client do gRPC. O banco de dados de teste é o H2 e, antes de cada teste de  
integração, o banco de dados é populado usando Flyway.
  
A aplicação foi totalmente "containerizada" com Docker e Docker Compose. Foi aplicado o multi-stage building, onde, inicialmente,  
é utilizada a imagem maven:3-amazoncorretto-11 para buildar o projeto e, posteriormente, o jar é transferido para uma imagem  
construída com base na imagem amazoncorretto:11-alpine, deixando o container final da aplicação mais leve. Para evitar que a  
aplicação Java tente se conectar com o banco de dados antes dele estar pronto, foi utilizado o dockerize, uma ferramenta que  
faz com que o container que tem a aplicação Java suba apenas após o container que tem o banco de dados subir totalmente.

### Ferramentas e padrões utilizados
* Java 11 (imagens maven:3-amazoncorretto-11 e amazoncorretto:11-alpine)
* Spring Boot 2.6.0
* Spring Data JPA / Hibernate
* Maven 3 (imagem maven:3-amazoncorretto-11)
* PostgreSQL 14 (imagem postgres:14-alpine)
* H2
* Flyway
* gRPC
* Protocol Buffers
* JUnit 5
* Mockito
* Docker

## Como rodar o projeto
1. Instale o Docker e o Docker Compose em sua máquina.
2. Clone do projeto.
3. Crie uma nova Request do tipo gRPC em sua ferramente de API client e importe o arquivo product-service.proto, que pode ser  
encontrado no diretório grpc-spring-course/src/main/proto/ ou pode ser acessado diretamente pelo link:  
https://github.com/dehonpaulo/grpc-spring-project/blob/main/grpc-spring-course/src/main/proto/product-service.proto  
4. No diretório que tem o arquivo docker-compose.yml, execute o comando "docker-compose up -d --build" e aguarde a conclusão.  
OBS: Deixei, deliberadamente, que o processo de build do maven fosse realizado durante o build e execução dos containers para  
ser possível ver que todos os testes são aprovados e que não há nenhum erro de build do projeto. Isso deixou o processo de  
subir os containers um pouco mais demorado, mas, como explicado, houve um propósito.  
5. Agora, basta fazer as requisições que desejar pela ferramenta de API client. Segue um exemplo de message (corpo da requisição)  
para cada um dos métodos:  
* Create  
{  
&emsp;"name": "Nome do produto",  
&emsp;"price": 10.59,  
&emsp;"quantity_in_stock": 200  
}  
* FindById  
{  
&emsp;"id": 1  
}  
* Delete  
{  
&emsp;"id": 1  
}  
* FindAll  
{  
}
