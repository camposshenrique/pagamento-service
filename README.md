# TESTE PRÁTICO - BACKEND
# Desafio: Criação de uma API REST de Pagamento

### Requisitos:

Implementar uma API para inclusão de pagamento de um produto,  aplicando as taxas de acordo com o tipo de pagamento escolhido.

    - Implemente uma operação REST para efetuar um pagamento, aplicando uma taxa de desconto para cada tipo de pagamento escolhido.

    - Após o envio dos dados com sucesso, a API deve retornar todos os dados enviados junto com a taxa aplicada e o valor calculado.
   
    ## Objeto de Entrada da API
        - Nome do produto
        - Tipo de pagamento
        - Valor pago

    ## Regras de negócio das taxas a serem aplicadas como desconto no pagamento do valor enviado, de acordo com cada tipo escolhido no envio dos dados
        - Pix: sem taxa, limitado a uma transação de até R$ 1000,00
        - Cartão de Débito: 2.5% de taxa
        - Cartão de Crédito: VISA: 5% de taxa, MASTERCARD: 4% de taxa, ELO: 3% de taxa
     

    ## Retorno de sucesso da API deve ser o objeto inserido com os seguintes campos:
        - id : Identificador único do pagamento.
        - Produto: Nome do produto.
        - Tipo de pagamento: Tipo de pagamento utilizado (PIX, Cartão de Débito ou Cartão de Crédito).
        - valor pago: valor pago pelo produto.
        - taxa: taxa aplicada ao tipo de pagamento escolhido
        - valor liquido: valor pago incluindo o percentual taxa aplicada (valor + taxa)
    
    ## Retorno da API com erro de validações de negócio
        - Caso o tipo de pagamento não seja informado, retornar o erro: "Tipo de pagamento não informado"
        - Caso o valor pago seja superior a R$ 1000,00 e o tipo de pagamento seja PIX, não aceitar o pagamento,  
            retornando um erro: "Pagamento não autorizado, valor superior a R$ 1000,00 para pagamento via PIX"
    
    ## Retorno da API com erro desconhecido
        - Caso ocorra algum erro desconhecido, retornar o erro: "Erro desconhecido, favor entrar em contato com o suporte"

    ### Entidade "Pagamento" deve conter os seguintes campos: id, produto, tipo de pagamento, valor pago, taxa e valor liquido.

    id: Identificador único do pagamento.
    Produto: Nome do produto.
    Tipo de pagamento: Tipo de pagamento utilizado
        - PIX
        - Cartão de Débito
        - Cartão de Crédito
    valor pago: valor a ser pago pelo produto.
    taxa: taxa aplicada ao tipo de pagamento.
    valor liquido: valor pago com a taxa aplicada.

    - Utilize um banco para persistir os dados (Banco H2 em memória já configurado no projeto).

    - Ao final do desenvolvimento, crie uma branch no repositório atual e enviar um merge-request no para o bitbucket.

    - A avaliação será feira através do push enviado!


### Critérios de Avaliação:

    - Funcionalidade: A API deve ser capaz de realizar a operação para pagamento. (único endpoint)
    - Qualidade do Código: O código deve seguir boas práticas de desenvolvimento e estar bem organizado.
    - Utilização do Spring Boot: Deve-se utilizar o Spring Boot para configurar a aplicação.
    - Validações: O código deve implementar validações adequadas para os campos obrigatórios.
    - Testes: O codigo deve possuir testes unitários
    - Documentação: O código deve estar documentado de forma simples. (Swagger)