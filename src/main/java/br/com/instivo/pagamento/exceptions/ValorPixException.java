package br.com.instivo.pagamento.exceptions;

public class ValorPixException extends RuntimeException{

    public ValorPixException(){
        super("Pagamento não autorizado, valor superior a R$ 1000,00 para pagamento via PIX");
    }

}
