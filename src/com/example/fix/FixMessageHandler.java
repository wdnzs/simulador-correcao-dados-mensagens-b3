package com.example.fix;

import java.io.*;
import java.util.*;

public class FixMessageHandler {

    private static final String ARQUIVO_ENTRADA = "mensagens.txt";
    private static final String ARQUIVO_CSV = "AllMsgs.csv";
    private static final String ARQUIVO_EXECUCOES_TOTAIS = "FullFill.txt";
    private static final String SEPARADOR_FIX = "\u0001";

    public static void main(String[] args) throws IOException {
        try (BufferedReader leitor = new BufferedReader(new FileReader(ARQUIVO_ENTRADA));
             FileWriter escritorCsv = new FileWriter(ARQUIVO_CSV);
             FileWriter escritorTxt = new FileWriter(ARQUIVO_EXECUCOES_TOTAIS)) {

            // Cabeçalho do CSV com os campos solicitados.
            escritorCsv.write("Horario;Conta;Instrumento;Lado;Qtd Ordem;Qtd Execucao Atual;Qtd Executada Acumulada;Preco Executado;Notional Ordem;Notional Exec Atual;Notional Exec Acumulado;Entering Trader\n");

            String linha;
            while ((linha = leitor.readLine()) != null) {
                Map<String, String> campos = extrairCamposFIX(linha);

                String horario = campos.get("60");
                String conta = campos.get("1");
                String instrumento = campos.get("55");
                String lado = campos.get("54");
                int qtdOrdem = toInt(campos.get("38"));
                int qtdExecucaoAtual = toInt(campos.get("32"));
                int qtdExecutadaAcumulada = toInt(campos.get("14"));
                double precoExecutado = toDouble(campos.get("6"));
                String trader = campos.get("5149");

                double notionalOrdem = qtdOrdem * toDouble(campos.get("44"));
                double notionalExecAtual = qtdExecucaoAtual * precoExecutado;
                double notionalExecAcumulado = qtdExecutadaAcumulada * precoExecutado;

                // Escreve a linha formatada no CSV.
                escritorCsv.write(String.join(";",
                        horario, conta, instrumento, lado,
                        String.valueOf(qtdOrdem),
                        String.valueOf(qtdExecucaoAtual),
                        String.valueOf(qtdExecutadaAcumulada),
                        String.valueOf(precoExecutado),
                        String.format("%.2f", notionalOrdem),
                        String.format("%.2f", notionalExecAtual),
                        String.format("%.2f", notionalExecAcumulado),
                        trader) + "\n");

                // Se for execução total, grava no arquivo específico incluindo tags extras.
                if ("2".equals(campos.get("39")) && "F".equals(campos.get("150"))) {
                    StringBuilder mensagemAlterada = new StringBuilder(linha);
                    mensagemAlterada.append(SEPARADOR_FIX).append("1010=").append(String.format("%.2f", notionalOrdem));
                    mensagemAlterada.append(SEPARADOR_FIX).append("1011=").append(trader);
                    escritorTxt.write(mensagemAlterada.toString() + "\n");
                }
            }
        }
    }

    // Faz o parsing da string FIX em mapa de campos (tag=valor).
    private static Map<String, String> extrairCamposFIX(String mensagem) {
        Map<String, String> mapa = new HashMap<>();
        String[] tokens = mensagem.split(SEPARADOR_FIX);
        for (String token : tokens) {
            int idx = token.indexOf('=');
            if (idx > 0) {
                mapa.put(token.substring(0, idx), token.substring(idx + 1));
            }
        }
        return mapa;
    }

    private static int toInt(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (Exception e) {
            return 0;
        }
    }

    private static double toDouble(String valor) {
        try {
            return Double.parseDouble(valor);
        } catch (Exception e) {
            return 0.0;
        }
    }
}