package com.example.fix;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste de integridade do fluxo principal do sistema:
 * - Geração de mensagens FIX
 * - Processamento e criação dos arquivos de saída
 */
public class FixMessageTest {

    @Test
    public void fluxoCompletoDeveGerarArquivosValidos() throws Exception {
        // Executa a geração da massa de mensagens FIX
        FixMessageGenerator.main(null);

        File arquivoMensagens = new File("mensagens.txt");
        assertTrue(arquivoMensagens.exists() && arquivoMensagens.length() > 0,
                "O arquivo mensagens.txt deve ser gerado com conteúdo");

        // Executa o processamento e geração dos arquivos CSV e TXT
        FixMessageHandler.main(null);

        File arquivoCSV = new File("AllMsgs.csv");
        File arquivoExecucoesTotais = new File("FullFill.txt");

        assertTrue(arquivoCSV.exists() && arquivoCSV.length() > 0,
                "O arquivo AllMsgs.csv deve ser gerado com conteúdo");

        assertTrue(arquivoExecucoesTotais.exists(),
                "O arquivo FullFill.txt deve ser gerado mesmo que vazio (mas idealmente com conteúdo)");
    }
}