# 📱 Diário de Posição e Sensores (Projeto Final - Android Studio)

Aplicativo Android desenvolvido em Java utilizando o Android Studio para monitoramento de sensores de hardware em tempo real e persistência de dados localmente.

---

## 🚀 Sobre o Projeto
O aplicativo principal captura dados do **Sensor de Luz Ambiente** e do **GPS (Localização)** em tempo real, permitindo ao usuário visualizar as leituras na tela inicial e salvá-las em um banco de dados **SQLite** interno. O sistema também conta com uma tela dedicada ao histórico de registros salvos e uma **View Customizada** para representação gráfica.

---

## 👥 Divisão das Partes do Projeto
* **MainActivity & Sensores (Luz e GPS):** Gerenciamento do ciclo de vida, permissões de hardware, eventos de localização e escuta do sensor de luz.
* **IndicadorLuzView:** Componente visual customizado (View Customizada) para exibição gráfica baseada na intensidade da luz.
* **BancoHelper & HistoricoActivity:** Implementação do banco de dados SQLite para persistência local e tela de consulta de registros históricos.

---

## 🛠️ Tecnologias e Recursos Utilizados
* **Linguagem:** Java (Android SDK)
* **IDE:** Android Studio
* **Banco de Dados:** SQLite (`SQLiteOpenHelper`)
* **Hardware:** `SensorManager` (Sensor de Luz) e `LocationManager` (GPS)
* **Componentes visuais:** Activities, Intents Explícitas, Views Customizadas e ScrollView.

---

## 📂 Estrutura das Principais Classes

### 1. `BancoHelper.java` (Persistência SQLite)
Responsável por gerenciar a criação do banco de dados (`diario_sensores.db`), a tabela `registros` (com colunas `id`, `luz` e `gps`) e a inserção de novos dados utilizando `ContentValues` e comandos SQL nativos.

### 2. `HistoricoActivity.java` (Tela de Histórico)
Gerencia a segunda tela do aplicativo. Abre o banco de dados no modo de leitura, executa uma consulta (`SELECT * FROM registros`) utilizando um `Cursor` para varrer os dados, formata as informações e as exibe em um `TextView` com rolagem.

---
