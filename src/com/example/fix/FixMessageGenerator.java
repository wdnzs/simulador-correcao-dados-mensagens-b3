package com.example.fix;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FixMessageGenerator {

    private static final int TOTAL_MENSAGENS = 5000;
    private static final int EXECUCOES_TOTAIS = 2500;
    private static final String CAMINHO_ARQUIVO_SAIDA = "mensagens.txt";
    private static final String SEPARADOR_FIX = "\u0001";

    private static final String[] CONTAS_DISPONIVEIS = gerarLista("ACC", 10);
    private static final String[] INSTRUMENTOS_DISPONIVEIS = gerarLista("INST", 10);
    private static final String[] TRADERS_DISPONIVEIS = gerarLista("TRADER", 10);

    private static final Random RANDOM = new Random();
    private static final DecimalFormat FORMATO_PRECO = new DecimalFormat("0.00");
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");

    public static void main(String[] args) throws IOException {
        Set<String> orderIdsGerados = new HashSet<>();
        Set<String> clientOrderIdsGerados = new HashSet<>();

        try (FileWriter escritor = new FileWriter(CAMINHO_ARQUIVO_SAIDA)) {
            for (int i = 0; i < TOTAL_MENSAGENS; i++) {
                boolean execucaoTotal = i < EXECUCOES_TOTAIS;
                String mensagem = gerarMensagemFIX(orderIdsGerados, clientOrderIdsGerados, execucaoTotal);
                escritor.write(mensagem + "\n");
            }
        }
    }

    private static String gerarMensagemFIX(Set<String> orderIds, Set<String> clOrdIds, boolean execucaoTotal) {
        StringBuilder mensagem = new StringBuilder();

        String conta = escolherAleatorio(CONTAS_DISPONIVEIS);
        String instrumento = escolherAleatorio(INSTRUMENTOS_DISPONIVEIS);
        String trader = escolherAleatorio(TRADERS_DISPONIVEIS);
        String lado = RANDOM.nextBoolean() ? "1" : "2"; // 1 = Compra, 2 = Venda

        int quantidadeOrdem = gerarInteiro(100, 10000);
        int quantidadeExecutada = execucaoTotal ? quantidadeOrdem : gerarInteiro(1, quantidadeOrdem - 1);
        int quantidadeAcumulada = execucaoTotal ? quantidadeOrdem : quantidadeExecutada;

        double preco = gerarDecimal(5, 50);
        double precoMedio = preco;

        String orderId = gerarIdUnico(orderIds, "OID");
        String clOrdId = gerarIdUnico(clOrdIds, "CID");
        String horarioEnvio = LocalDateTime.now().format(FORMATADOR_DATA);

        // Montagem da mensagem FIX com campos obrigatórios
        mensagem.append("8=FIX.4.4^");
        mensagem.append("35=8^");
        mensagem.append("1=").append(conta).append("^");
        mensagem.append("6=").append(FORMATO_PRECO.format(precoMedio)).append("^");
        mensagem.append("11=").append(clOrdId).append("^");
        mensagem.append("14=").append(quantidadeAcumulada).append("^");
        mensagem.append("17=").append(UUID.randomUUID()).append("^");
        mensagem.append("31=").append(FORMATO_PRECO.format(preco)).append("^");
        mensagem.append("32=").append(quantidadeExecutada).append("^");
        mensagem.append("37=").append(orderId).append("^");
        mensagem.append("38=").append(quantidadeOrdem).append("^");
        mensagem.append("39=").append(execucaoTotal ? "2" : "1").append("^");
        mensagem.append("44=").append(FORMATO_PRECO.format(preco)).append("^");
        mensagem.append("54=").append(lado).append("^");
        mensagem.append("55=").append(instrumento).append("^");
        mensagem.append("60=").append(horarioEnvio).append("^");
        mensagem.append("5149=").append(trader).append("^");

        return mensagem.toString().replace("^", SEPARADOR_FIX);
    }

    private static String gerarIdUnico(Set<String> existentes, String prefixo) {
        String id;
        do {
            id = prefixo + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        } while (!existentes.add(id));
        return id;
    }

    private static int gerarInteiro(int minimo, int maximo) {
        return RANDOM.nextInt(maximo - minimo + 1) + minimo;
    }

    private static double gerarDecimal(double minimo, double maximo) {
        return minimo + (maximo - minimo) * RANDOM.nextDouble();
    }

    private static String[] gerarLista(String prefixo, int quantidade) {
        String[] lista = new String[quantidade];
        for (int i = 0; i < quantidade; i++) {
            lista[i] = prefixo + (i + 1);
        }
        return lista;
    }

    private static String escolherAleatorio(String[] array) {
        return array[RANDOM.nextInt(array.length)];
    }
}