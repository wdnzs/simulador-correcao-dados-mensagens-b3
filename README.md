# Simulador de correção de mensagens B3
Projeto para geração e processamento de mensagens no padrão FIX 4.4, conforme especificação da B3.


  ![Visitor Count](https://visitor-badge.laobi.icu/badge?page_id=wdnzs.simulador-correcao-dados-mensagens-b3)

 
# Objetivos
Simular um fluxo real de mensagens de negociação com geração, transformação e exportação de dados, com foco em performance, clareza e boas práticas.

### 📁 Estrutura do Projeto

```📦 Projeto
├── 🏗️ bin/
│   └── 📦 com/
│       └── 📦 example/
│           └── 📦 fix/
│               ├── 📄 FixMessageGenerator.class
│               ├── 📄 FixMessageHandler.class
│               └── 📄 FixMessageTest.class
├── 🧾 src/
│   └── 📦 com/
│       └── 📦 example/
│           └── 📦 fix/
│               ├── 📄 FixMessageGenerator.java
│               └── 📄 FixMessageHandler.java
└── 🧪 test/
    └── 📦 com/
        └── 📦 example/
            └── 📦 fix/
                └── 📄 FixMessageTest.java
├── 📄 AllMsgs.csv
├── 📄 FullFill.txt
├── 📄 mensagens.txt
```

# Como executar
✅ Pré-requisitos

&nbsp; &nbsp; &nbsp; Java 23 instalado (https://jdk.java.net/23/) <br>
&nbsp; &nbsp; &nbsp; Eclipse IDE (https://eclipseide.org/), para executar teste unitário, caso queira.

🔹 1. Compilar
<pre><code>javac -d bin src/com/example/fix/*.java</code></pre>

🔹 2. Executar geração de mensagens (Parte 1)
<pre><code>java -cp bin com.example.fix.FixMessageGenerator</code></pre>

🔹 3. Executar processamento de mensagens (Parte 2)
<pre><code>java -cp bin com.example.fix.FixMessageHandler</code></pre>

🔹 4. Rodar o teste automatizado (JUnit) <br>
&nbsp; &nbsp; &nbsp; Na IDE Eclipse, basta clicar com o botão direito em FixMessageTest.java > Run As > JUnit Test <br>

🧪 O que será gerado? <br>

&nbsp; &nbsp; &nbsp; Um arquivo mensagens.txt contendo 5000 mensagens FIX com campos válidos e únicos.

&nbsp; &nbsp; &nbsp; Um arquivo AllMsgs.csv contendo a versão transformada das mensagens, com cálculo de notional.

&nbsp; &nbsp; &nbsp; Um arquivo FullFill.txt contendo mensagens com execução total, enriquecidas com as tags 1010 e 1011. <br>

Aviso: este projeto foi criado unicamente para fins de teste técnico. Ele simula mensagens com base na estrutura pública da B3.
